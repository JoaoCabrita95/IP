package IP.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import IP.GsonParser;
import IP.Model.Application;
import IP.Model.CV;
import IP.Model.JobPosting;
import IP.Model.ModelClassToJson;
import IP.Model.Person;
import IP.Model.RDFObject;
import IP.Model.Skill;
import IP.Model.SparqlEndPoint;

@Path("/")
public class PersonService {
	
	rabbitMQService rabbit = new rabbitMQService();
	
	@GET
	@Path("/profiles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetProfiles() {
		List<Person> profiles;
		try {
			profiles = Person.getPersons();
			JsonArray results = new JsonArray();
            for(Person p : profiles) {
                results.add(ModelClassToJson.getProfileJson(p));
            }
			return Response.ok(results.toString()).build();
		}
		catch (NoSuchElementException e1) {
			return Response.status(Response.Status.OK).entity(e1.getMessage()).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/profiles")
	//@Consumes(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response CreatePerson(String data) {
		GsonParser parser;
		try {
			parser = new GsonParser();
			Person person = parser.toPerson(data);
	
			parser.SavetoFile("output-Person.ttl");
	
			////SparqlEndPoint.insertTriple(parser.toString());
			////SparqlEndPoint.insertTriple(parser.toStringData());
			////SparqlEndPoint.insertTriple(parser.toStringData(),parser.toStringHeader());
			//SparqlEndPoint.insertTriple(parser.toStringData());
			person.Save();
			
//			try {
//				rabbit.bindQueue("");
//				byte[] profileData = getPersonInJson(person).toString().getBytes();
//				rabbit.channel.basicPublish(rabbit.exchange, "info", null, profileData);
//			}
//			catch (Exception e) {
//				System.out.println("Could not send the created Profile to the RabbitMQ queue.");
//			}

			String response = parser.toString("TTL");
			return Response.ok(response, MediaType.TEXT_PLAIN).build();
				
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).entity(e.toString()).build();		
		}

	}
	
//	private JsonArray getPersonInJson(Person person) {
//		
//		JsonArray jsonResults = new JsonArray();
//		
//		JsonObject jsonPropValue = new JsonObject();
//		jsonPropValue.addProperty("label",job.getLabel());
//		jsonPropValue.addProperty("comment",job.getComment());
//		jsonPropValue.addProperty("jobDescription", job.getJobDescription());
//		jsonPropValue.addProperty("contractType",job.getContractType());
//		jsonPropValue.addProperty("sector",job.getSector());
//		jsonPropValue.addProperty("country",job.getCountry());
//		jsonPropValue.addProperty("state",job.getState());
//		jsonPropValue.addProperty("hiringOrg",job.getHiringOrg());
//		jsonPropValue.addProperty("city",job.getCity());
//		jsonPropValue.addProperty("creator_id",job.getcreator_id());
//		jsonPropValue.addProperty("occupation",job.getOccupation());
//		jsonPropValue.addProperty("startDate",job.getStartDate());
//		jsonPropValue.addProperty("endDate",job.getEndDate());
//		jsonPropValue.addProperty("seniorityLevel",job.getseniorityLevel());
//		jsonPropValue.addProperty("expectedSalary",job.getExpectedSalary());
//		jsonPropValue.addProperty("salaryCurrency",job.getSalaryCurrency());
//		jsonPropValue.addProperty("uri", job.getURI());
//		jsonPropValue.addProperty("id", RDFObject.uri2id(job.getURI()));
//		
//		
//		for(Skill skill : job.getSkillReq()) {
//			jsonResults.add(GeneralService.getSkillJson(skill));
//		}
//		jsonPropValue.add("skills", jsonResults );
//		
//		jsonResults = new JsonArray();
//		
//		jsonResults.add(jsonPropValue);
//		
//		return jsonResults;
//	}
	
	@GET
	@Path("/profiles/{userid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetPerson(@PathParam("userid")String userid) {
		try {
			Person person = Person.getPerson(userid);
			JsonElement personJson = ModelClassToJson.getProfileJson(person);
			return Response.ok(personJson.toString()).build();
		}
		catch(NoSuchElementException e1) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
		}
		catch(Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		
	}
	
	@PUT
	@Path("/profiles/{userid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response UpdatePerson(@PathParam("userid")String userid) {
		return Response.status(Response.Status.NOT_IMPLEMENTED).build();	
	}

	@GET
	@Path("/profiles/{userid}/applications")
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetJobApplications(@PathParam("userid")String userid) {
		
		Person person;
		try {
			person = Person.getPerson(userid);
			List<Application> apps = Application.getApplicationsByProfile(userid);
//			CV cv = CV.getCV(person.getCVURI());
//			apps = cv.getApplications();	
			if(apps.isEmpty())
				return Response.status(Response.Status.OK).entity("No Applications made by user found").build();
			else
				return Response.ok(apps).build();
		} catch(NoSuchElementException e1) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

		

	}

//	@POST
//	@Path("/profiles/{userid}/applications/{jobid}")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Person ApplyforJob(@PathParam("jobid")String jobid, @PathParam("userid")String userid, @QueryParam("expectedSalary") String expectedSalary,
//			@QueryParam("availableDate") String availableDate, @QueryParam("salaryCurrency") String salaryCurrency) {
//		Person person = Person.getPerson(userid);
//		JobPosting jp = JobPosting.getJobPosting(jobid);
//		Application app = new Application(person.getURI(), jp.getURI(), expectedSalary, availableDate, salaryCurrency);
//		CV cv = person.getCV();
//		jp.apply(app);
//		cv.addJobApplication(app);
//		try {
//			app.Save();
//			jp.Save();
//			cv.Save();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		return person;
//	}
	
	//TODO: Probably need to do some extra cleaning up in this method, do I erase the application after its been accepted, do I remove the Job offer?
	@POST
	@Path("/profiles/{userid}/currentJob/{jobid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response AssignJobToPerson(@PathParam("jobid") String jobid, @PathParam("userid") String userid ) {
		try {
			Person person = Person.getPerson(userid);
			if(person.hasAppliedToJob(jobid)) {
				if(person.getCurrentJobURI() != null) {
					if(person.getCurrentJobURI().equals(jobid) || person.getCurrentJobURI().equals("saro:" + jobid)){
						return Response.status(Response.Status.BAD_REQUEST).entity("Person has already been selected for this job: " + jobid).build();
					}
				}
				
				if(jobid.contains(":"))
					person.setCurrentJobURI(jobid);
				else if(jobid.contains("http"))
					person.setCurrentJobURI(":" + jobid.substring(jobid.indexOf("#") + 1));
				else
					person.setCurrentJobURI(":" + jobid);
				
				try {
					person.Save();
					return Response.ok(person).build();
				} catch (Exception e) {
					return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Could not save profile").build();
				}
			}
			else
				return Response.status(Response.Status.BAD_REQUEST).entity("Person has not applied for the selected Job").build();
		} 
		catch(NoSuchElementException e1) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		
	}

	@DELETE
	@Path("/profiles/{userid}/applications/{jobid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response DeleteApplication(@PathParam("jobid")String jobid, @PathParam("userid")String userid) {
		
		Person person;
		try {
			person = Person.getPerson(userid);
			CV cv = CV.getCV(person.getCVURI());
			Application app = cv.getApplication(jobid);
			
			if(app == null)
				return Response.status(Response.Status.BAD_REQUEST).entity("User " + userid + " does not have an application to job " + jobid).build();
			
			JobPosting jp = JobPosting.getJobPosting(jobid);
			
			cv.removeJobApplication(jobid);
			jp.removeApplications(app.getURI());
			
			
			SparqlEndPoint.deleteObjectByUri(app.getURI());
			SparqlEndPoint.deleteObjectAssociations(app.getURI());
			
		} 
		catch(NoSuchElementException e1) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		
		
//		try {
//			cv.Save();
//			jp.Save();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		return Response.status(Response.Status.OK).entity("Application to job: "+ jobid + " made by user: " + userid + " has been deleted").build();	
	}
	
	@DELETE
	@Path("profiles/{userID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response DeleteProfile(@PathParam("userID") String userID) {
		try {
			Person p = Person.getPerson(userID);
			if(p.getCVURI() != null) {
				try {
					CV cv = CV.getCV(p.getCVURI());
					if(!cv.getApplications().isEmpty()) {
						for(Application app : cv.getApplications()) {
							RDFObject.quickDeleteByURI(app.getURI());
							RDFObject.deleteURIAssociations(app.getURI());
						}
					}
					
					RDFObject.quickDeleteByURI(p.getCVURI());
				}
				catch(Exception e) {
					System.out.println("No CV found, deleting Profile anyways");
				}
				
			}
			RDFObject.quickDeleteByURI(p.getURI());	
			
			return Response.ok(p).build();
		}
		
		catch (NoSuchElementException e1) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
		}
		
		catch(Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		
	}
}
