package IP.Server;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import IP.GsonParser;
import IP.Matching;
import IP.Model.Application;
import IP.Model.CV;
import IP.Model.JobPosting;
import IP.Model.ModelClassToJson;
import IP.Model.Person;
import IP.Model.RDFObject;
import IP.Model.Skill;
import IP.Model.SkillJobReq;
import matomo.matomoClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.http.HttpResponse;
import org.apache.jena.base.Sys;
import org.piwik.java.tracking.PiwikRequest;
import org.piwik.java.tracking.PiwikTracker;

@Path("/")
public class MatchingService {
	
	rabbitMQService rabbit = new rabbitMQService();
	matomoClient mc = new matomoClient();
	private static Logger Log = Logger.getLogger(MatchingService.class.getName());
	
	public MatchingService() {
		Log.setLevel( Level.FINER );
	}

	//TODO:Need to double check the case where results == null
	@GET
	@Path("/jobs/{jobid}/candidates")
	// @Path("/jobmatching")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetJobMatching(@PathParam("jobid") String jobid) {

		String results = null;

		JobPosting job;
		try {
			job = JobPosting.getJobPosting(jobid);
			results = matching(job);
			
			if(results != null)
				return Response.ok(results).build();
			else
				return Response.ok("No candidates for job: " + jobid).build();

		}
		catch (NoSuchElementException e1) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		
	}
	
	@GET
	@Path("profiles/{userid}/recomendations")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCVJobScores(@PathParam("userid") String userid) {
		String results = "";
		String cvURI;
		try {
			cvURI = Person.getPerson(userid).getCVURI();
			//For verification that the person has a CV, if not it throws an exception
			@SuppressWarnings("unused")
			CV cv = CV.getCV(cvURI);
			Map<String, Integer> scores = CV.getJobScores(cvURI);
			for (Map.Entry<String, Integer> score : scores.entrySet()) { 
				results += "JobID: " + score.getKey() + "\n Score: " + score.getValue() + "\n\n";
			}
			return Response.ok(results).build();
		}
		catch (NoSuchElementException e1) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("/profiles/{userid}/scores")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPersonApplicationScores(@PathParam("userid") String userid) {
		String results = "Job Application Scores:\n\n";
		
		Map<String, Integer> scores;
		try {
			scores = CV.getJobApplicationScores(userid);
			for (Map.Entry<String, Integer> score : scores.entrySet()) { 
				results += "JobID: " + score.getKey() + "\n Score: " + score.getValue() + "\n\n";
			}
		}
		catch (NoSuchElementException e1) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		
		
		return Response.ok(results).build();
	}

	@POST
	@Path("/jobs/{jobid}/apply/{userid}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response ApplyforJob(@PathParam("jobid") String jobid, @PathParam("userid") String userid, String data) {

		JobPosting job;
		try {
			job = JobPosting.getJobPosting(jobid);
			CV cv = CV.getCVbyPersonURI(userid);
			Person p = Person.getPerson(userid);
			if(p.hasAppliedToJob(job.getURI()))
				return Response.status(Response.Status.BAD_REQUEST).entity("Profile " + p.getURI() + " has already applied for Job " + job.getURI()).build();
			
			GsonParser parser;
			try {

				parser = new GsonParser();
				Application application = parser.toApplication(data);
				application.setJobURI(job.getURI());
				application.setPersonURI(p.getURI());
				application.Save();
				
				mc.send("job_applications", "apply", job.getID(), 1);
				
//				//TODO: Calculate job matching score and check if it's above a certain threshold, if so send another tick to matomo
//				if(false) {
//					mc.send("job_applications", "matching", job.getID(), 1);
//				}
				
								
				try {
//					rabbit.bindQueue(rabbitMQService.ROUTING_KEY);
					JsonObject rabbitObject = new JsonObject();
					rabbitObject.add("job_application", getApplicationForRabbitMQ(application));
					rabbitObject.addProperty("status", "create");
					System.out.println(rabbitObject);
					rabbit.channel.basicPublish(rabbit.exchange, rabbitMQService.ROUTING_KEY, null, rabbitObject.toString().getBytes());
//					System.out.println(rabbit.channel.isOpen());
				}
				catch (Exception e) {
					System.out.println("Could not send the created Job Application to the RabbitMQ queue.");
				}

//				cv.addJobApplication(application);
//				cv.Save();
//
//				job.apply(application);
//				job.Save();
				return Response.ok("Application created", MediaType.APPLICATION_JSON).build();

			}
			
			catch (Exception e) {
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).entity(e.toString()).build();
			}
		} 
		catch (NoSuchElementException e1) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e1.toString()).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		

	}
	
	private JsonObject getApplicationForRabbitMQ(Application application) {
		JsonObject jsonPropValue = new JsonObject();
		
		jsonPropValue.addProperty("label", application.getLabel());
		jsonPropValue.addProperty("comment", application.getComment());
		jsonPropValue.addProperty("personURI", application.getPersonURI());
//		jsonPropValue.addProperty("personID", RDFObject.uri2id(application.getPersonURI()));
		jsonPropValue.addProperty("jobURI", application.getJobURI());
//		jsonPropValue.addProperty("jobID", RDFObject.uri2id(application.getJobURI()));
		jsonPropValue.addProperty("expectedSalary", application.getExpectedSalary());
		jsonPropValue.addProperty("salaryCurrency", application.getSalaryCurrency());
		jsonPropValue.addProperty("availability", application.getAvailability());
		jsonPropValue.addProperty("uri", application.getURI());
		jsonPropValue.addProperty("id", RDFObject.uri2id(application.getURI()));
			
			
		return jsonPropValue;
	}

	@DELETE
	@Path("/jobs/{jobid}/apply/{userid}")
	public Response DeleteApplication(@PathParam("jobid") String jobid, @PathParam("userid") String userid) {

		try {
			CV cv = CV.getCVbyPersonURI(userid);
			
			
			Application app = Application.getApplicationByUserJob(jobid, userid);
			if(app == null)
				return Response.status(Response.Status.BAD_REQUEST).entity("User " + userid + " does not have an application to job " + jobid).build();;
			
			RDFObject.quickDeleteByURI(app.getURI());
			RDFObject.deleteURIAssociations(app.getURI());
			
			try {
//				rabbit.bindQueue(rabbitMQService.ROUTING_KEY);
				JsonObject rabbitObject = new JsonObject();
				JsonObject jobApp = new JsonObject();
				try {
					jobApp.addProperty("job_id", Integer.valueOf(jobid));
				}
				catch(NumberFormatException e) {
					jobApp.addProperty("job_id", jobid);
				}
				try {
					jobApp.addProperty("user_id", Integer.valueOf(userid));
				}
				catch(NumberFormatException e) {
					jobApp.addProperty("user_id", userid);
				}
				rabbitObject.add("job_application", jobApp);
				rabbitObject.addProperty("status", "delete");
				Log.info(rabbitObject.toString() + "\n");
				rabbit.channel.basicPublish(rabbit.exchange, rabbitMQService.ROUTING_KEY, null, rabbitObject.toString().getBytes());
//				System.out.println(rabbit.channel.isOpen());
			}
			catch (Exception e) {
				System.out.println("Could not send the deleted Job Application to the RabbitMQ queue.");
			}
//			cv.removeJobApplication(jobid);
			
			
//			cv.Save();
			
		}
		catch (NoSuchElementException e1) {
			return  Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		
		return Response.ok("Job Application deleted: job:" + jobid + "  user:" + userid).build();
	}
	
	@DELETE
	@Path("/applications/{applicationID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response DeleteApplicationByID(@PathParam("applicationID") String applicationID) {
		
		try {
			Application app = Application.getApplication(applicationID);
			RDFObject.quickDeleteByURI(app.getURI());
			RDFObject.deleteURIAssociations(app.getURI());
			
			try {
//				rabbit.bindQueue(rabbitMQService.ROUTING_KEY);
				String jobid = app.getJobURI();
				if(jobid.startsWith(":"))
					jobid = jobid.substring(1);
				String userid = app.getPersonURI();
				if(userid.startsWith(":"))
					userid = userid.substring(1);
				
				JsonObject rabbitObject = new JsonObject();
				JsonObject jobApp = new JsonObject();
				try {
					jobApp.addProperty("job_id", Integer.valueOf(jobid));
				}
				catch(NumberFormatException e) {
					jobApp.addProperty("job_id", jobid);
				}
				try {
					jobApp.addProperty("user_id", Integer.valueOf(userid));
				}
				catch(NumberFormatException e) {
					jobApp.addProperty("user_id", userid);
				}
				rabbitObject.add("job_application", jobApp);
				rabbitObject.addProperty("status", "delete");
				Log.info(rabbitObject.toString() + "\n");
				rabbit.channel.basicPublish(rabbit.exchange, rabbitMQService.ROUTING_KEY, null, rabbitObject.toString().getBytes());
//				System.out.println(rabbit.channel.isOpen());
			}
			catch (Exception e) {
				System.out.println("Could not send the deleted Job Application to the RabbitMQ queue.");
			}
			
			return Response.ok(ModelClassToJson.getApplicationJson(app).toString()).build();
			
		}
		catch (NoSuchElementException e1) {
			return  Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
		}
		catch(Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
			
	}
/*	
	@GET
	@Path("/jobs/{userid}/jobapplies")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Application> GetApplication(@PathParam("userid") String userid) {

		List<Application> apps = new ArrayList<>();
		System.out.println(userid);

		try {
			CV cv = CV.getCVbyPersonURI(userid);
			if (cv!=null){
				apps = cv.getApplications();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return apps;
		}
		return apps;
	}
*/
	private static String matching(JobPosting job) throws Exception {
        System.out.println("Listing cv scores for the Job Post with URI: " + job.getURI() + " :");
        System.out.println("Job Requirements: ");
        List<SkillJobReq> reqs = job.getJobSkillReqRefs();
        for (SkillJobReq req : reqs) {
            System.out.println("     " + req.getSkillName());
        }
        HashMap<String, Integer> scores = Matching.getAllCvMatches(job,true);
        
        if(scores.isEmpty())
        	return null;
 
        JsonArray jsonResults = new JsonArray();

        Set<String> results = scores.keySet();
        for(String uri : results) {
			int score = scores.get(uri);
			//if (score > 0) {
				System.out.println("Cv with URI:" + uri + " has a score of: " + scores.get(uri) + ".");
				
				CV cv;
				try {
					cv = CV.getCV(uri);
					String available = (cv.getApplication(job.getURI())==null) ? "" :  cv.getApplication(job.getURI()).getAvailability();
					String expsalary = (cv.getApplication(job.getURI())==null) ? "" :  cv.getApplication(job.getURI()).getExpectedSalary();


					//Person user = Person.getPersonByCV(uri);
					//String name = (user==null) ? "Noname" :  user.getName();
					String name = "Noname";

					JsonObject jsonPropValue=new JsonObject();
					jsonPropValue.addProperty("id",RDFObject.uri2id(cv.getPersonURI()));
					jsonPropValue.addProperty("cvid",uri);
					jsonPropValue.addProperty("name",name);
					jsonPropValue.addProperty("role",job.getLabel());
					jsonPropValue.addProperty("available",available);
					jsonPropValue.addProperty("expsalary",expsalary);
					jsonPropValue.addProperty("score",score);
					jsonResults.add(jsonPropValue);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			//}
        }

        return(jsonResults.toString());
    }
	

}
