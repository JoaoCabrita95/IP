package IP.Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;
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

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.jena.base.Sys;

import IP.GsonParser;
import IP.Model.Application;
import IP.Model.CV;
import IP.Model.JobPosting;
import IP.Model.ModelClassToJson;
import IP.Model.RDFObject;
import IP.Model.Skill;
import IP.Model.SkillJobReq;
import IP.Model.SparqlEndPoint;
import IP.Model.WorkHistory;
import matomo.matomoClient;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * 
 *
 */
@Path("/")
public class JobpostingService {
	rabbitMQService rabbit = new rabbitMQService();
	matomoClient mc = new matomoClient();
	
	private static Logger Log = Logger.getLogger(JobpostingService.class.getName());
	public JobpostingService() {
		Log.setLevel( Level.FINER );
	}
	
	/**
	 * 
	 * @return
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response root() {
		return Response.ok().entity("Root").build();	
	}
	
	/**
	 * 
	 * @return
	 */
	@GET
	@Path("/jobs")
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetJobs() {
		try {
			List<JobPosting> jobs = JobPosting.getJobPostings();
            JsonArray results = new JsonArray();
            int size = jobs.size() - 1;
            for(int i = size; i >= 0; i--) {
            	results.add(ModelClassToJson.getJobJson(jobs.get(i)));
            }
//            for(JobPosting job : jobs) {
//                results.add(ModelClassToJson.getJobJson(job));
//            }
            return Response.status(Response.Status.OK).entity(results.toString()).build();
		} 
		catch (NoSuchElementException e1) {
			return Response.status(Response.Status.OK).entity(e1.getMessage()).build();
		}
		catch (Exception e) {
			Log.info(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}	
	}
	
	/**
	 * 
	 * @param hiringOrg
	 * @return
	 */
	@GET
	@Path("/jobs/companies/{hiringOrg}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetCompanyJobs(@PathParam("hiringOrg") String hiringOrg){
		try {
			List<JobPosting> jobs = JobPosting.getJobPostingsByHiringOrg(hiringOrg);
			JsonArray jobsArray = new JsonArray();
			for(JobPosting job : jobs) {
				jobsArray.add(ModelClassToJson.getJobJson(job));
			}
			return Response.status(Response.Status.OK).entity(jobsArray.toString()).build();
		}
		catch (NoSuchElementException e1) {
			return Response.status(Response.Status.OK).entity(e1.getMessage()).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	@GET
	@Path("/jobs/companies")
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetHiringOrganizations(){
		try {
			List<String> hiringOrgs = JobPosting.getAllHiringOrganizations();
			JsonArray orgs = new JsonArray();
			for(String org : hiringOrgs)
				orgs.add(org);
			return Response.status(Response.Status.OK).entity(orgs.toString()).build();
		} 
		catch (NoSuchElementException e1) {
			return Response.status(Response.Status.OK).entity(e1.getMessage()).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	@POST
	@Path("/jobs")
	//@Consumes(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	//@Produces(MediaType.APPLICATION_JSON)
	//@JsonRawValue
	public Response CreateJobposting(String data) {
		Log.info(String.format("Post Request @ %s\n",  data));
		GsonParser parser;
		try {
			parser = new GsonParser();
			JobPosting job = parser.toJobPosting(data);
			parser.SavetoFile("output-jobpost.ttl");
			String response = parser.toString("TTL");
			
			
			job.setURI(":" + job.getID());
			
			if(SparqlEndPoint.existURI(job.getURI()))
				return Response.status(Response.Status.BAD_REQUEST).
						entity("Job Posting already exists, either delete Job with Job URI: " + job.getURI() + " or use update service").build();

			
			job.Save();
			
			try {
				JsonObject rabbitObject = new JsonObject();
				rabbitObject.add("job", getJobPostinginJson(job));
				rabbitObject.addProperty("status", "create");
//				Log.info(rabbitObject.toString() + "\n");
				System.out.println(rabbitObject);
				rabbit.channel.basicPublish(rabbit.exchange, rabbitMQService.ROUTING_KEY, null, rabbitObject.toString().getBytes());
//				System.out.println(rabbit.channel.isOpen());
			}
			catch (Exception e) {
				System.out.println("Could not send the created JobPosting to the RabbitMQ queue.");
			}
			
			
			return Response.ok(getJobPostinginJson(job).toString(), MediaType.APPLICATION_JSON).build();

			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).entity(e.toString()).build();		
		}

		////SparqlEndPoint.insertTriple(parser.toString());
		////SparqlEndPoint.insertTriple(parser.toStringData());
		////SparqlEndPoint.insertTriple(parser.toStringData(),parser.toStringHeader());
		//SparqlEndPoint.insertTriple(parser.toStringData());

	}
	
	/**
	 * 
	 * @param job
	 * @return
	 */
	private JsonObject getJobPostinginJson(JobPosting job) {
		
		JsonObject jsonPropValue = new JsonObject();
		jsonPropValue.addProperty("label",job.getLabel());
		jsonPropValue.addProperty("comment",job.getComment());
		jsonPropValue.addProperty("jobDescription", job.getJobDescription());
		jsonPropValue.addProperty("contractType",job.getContractType());
		jsonPropValue.addProperty("specialization", job.getSpecialization());
		jsonPropValue.addProperty("sector",job.getSector());
		jsonPropValue.addProperty("country",job.getCountry());
		jsonPropValue.addProperty("state",job.getState());
		jsonPropValue.addProperty("hiringOrg",job.getHiringOrg());
		jsonPropValue.addProperty("city",job.getCity());
		jsonPropValue.addProperty("creator_id",job.getcreator_id());
		jsonPropValue.addProperty("occupation",job.getOccupation());
		jsonPropValue.addProperty("startDate",job.getStartDate());
		jsonPropValue.addProperty("endDate",job.getEndDate());
		jsonPropValue.addProperty("seniorityLevel",job.getseniorityLevel());
		jsonPropValue.addProperty("expectedSalary",job.getExpectedSalary());
		jsonPropValue.addProperty("salaryCurrency",job.getSalaryCurrency());
		jsonPropValue.addProperty("uri", job.getURI());
		jsonPropValue.addProperty("id", RDFObject.uri2id(job.getURI()));
		

		JsonArray jsonTmp = new JsonArray();
		for(SkillJobReq skillReq : job.getJobSkillReqRefs()) {
			jsonTmp.add(ModelClassToJson.getJobSkillRef(skillReq));
		}
		
		jsonPropValue.add("skills", jsonTmp );
		
		
		return jsonPropValue;
	}

	/**
	 * 
	 * @param jobURI
	 * @return
	 */
	@GET
	@Path("/jobs/{jobURI}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetJob(@PathParam("jobURI")String jobURI) {
		JobPosting job;
		try {
			job = JobPosting.getJobPosting(jobURI);
            JsonElement jobData = ModelClassToJson.getJobJson(job);
            return Response.ok(jobData.toString()).build();    
		}
		catch (NoSuchElementException e1) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
			
	}

	/**
	 * 
	 * @param data
	 * @param jobURI
	 * @return
	 */
	@PUT
	@Path("/jobs/{jobURI}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response UpdateJob(String data, @PathParam("jobURI")String jobURI) {
		JobPosting exsitingJob;
		try {
			exsitingJob = JobPosting.getJobPosting(jobURI);
			GsonParser parser = new GsonParser();
			JobPosting newJob = parser.toJobPosting(data);
			String uri = exsitingJob.getURI();
			
			newJob.setURI(uri);
//			for (Application application : exsitingJob.getApplications()) {
//				newJob.apply(application);	
//			}
			try {
				newJob.update();
				try {
					
					JsonObject rabbitObject = new JsonObject();
					rabbitObject.add("job", getJobPostinginJson(newJob));
					rabbitObject.addProperty("status", "update");
					Log.info(rabbitObject.toString() + "\n");
					rabbit.channel.basicPublish(rabbit.exchange, rabbitMQService.ROUTING_KEY, null, rabbitObject.toString().getBytes());
//					System.out.println(rabbit.channel.isOpen());
				}
				catch (Exception e) {
					System.out.println("Could not send the updated JobPosting to the RabbitMQ queue.");
				}
				
				return Response.ok(getJobPostinginJson(newJob).toString(), MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).entity(e.toString()).build();		

			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e1.getMessage()).build();
		}

		
	}
	
	/**
	 * 
	 * @param jobURI
	 * @return
	 */
	@DELETE
	@Path("/jobs/{jobURI}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response DeleteJob(@PathParam("jobURI")String jobURI) {
		try {
			JobPosting job = JobPosting.getJobPosting(jobURI);
			//Code block to remove any applications made to that Job as well as the Job itself
//			if(!job.getApplications().isEmpty()) {
//				for(Application app : job.getApplications()) {
//					RDFObject.quickDeleteByURI(app.getURI());
//					RDFObject.deleteURIAssociations(app.getURI());
//				}
//			}
			
			RDFObject.quickDeleteByURI(job.getURI());
			try {
				
				JsonObject rabbitObject = new JsonObject();
				JsonObject jobID = new JsonObject();
				try {
					jobID.addProperty("job_id", Integer.valueOf(jobURI));
				}
				catch(NumberFormatException e) {
					jobID.addProperty("job_id", jobURI);
				}
				rabbitObject.add("job", jobID);
				rabbitObject.addProperty("status", "delete");
				Log.info(rabbitObject.toString() + "\n");
				rabbit.channel.basicPublish(rabbit.exchange, rabbitMQService.ROUTING_KEY, null, rabbitObject.toString().getBytes());
//				System.out.println(rabbit.channel.isOpen());
			}
			catch (Exception e) {
				System.out.println("Could not send the deleted JobPosting to the RabbitMQ queue.");
			}
			//Not sure if I should erase all associations or not
//			RDFObject.deleteURIAssociations(job.getURI());
			return Response.status(Response.Status.OK).entity("Job Posting with URI: " + jobURI + " has been deleted.").build();
		}
		catch (NoSuchElementException e1) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} 
	}

	/**
	 * 
	 * @param jobURI
	 * @return
	 */
	@GET
	@Path("/jobs/{jobURI}/apply")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetApplicationsforJob(@PathParam("jobURI")String jobURI) {
		JsonArray jsonJobApps = new JsonArray();
		JobPosting job;
		try {
			job = JobPosting.getJobPosting(jobURI);
//			List<Application> apps = job.getApplications();	
			List<Application> apps = Application.getApplicationsByJob(jobURI);	
			for(Application app: apps) {
				jsonJobApps.add(ModelClassToJson.getApplicationJson(app));
			}
			if(apps.isEmpty())
				return Response.status(Response.Status.OK).entity("No applications found for this Job").build();
			else
				return Response.status(Response.Status.OK).entity(jsonJobApps.toString()).build();
		} 
		catch (NoSuchElementException e1) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

}
