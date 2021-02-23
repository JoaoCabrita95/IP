package IP.Server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import IP.GsonParser;
import IP.Model.CV;
import IP.Model.CVSkillRef;
import IP.Model.CareerPath;
import IP.Model.JobPosting;
import IP.Model.ModelClassToJson;
import IP.Model.NoCurrentJobException;
import IP.Model.Person;
import IP.Model.RDFObject;
import IP.Model.Skill;
import IP.Model.SkillRefObject;
import IP.Model.SparqlEndPoint;
import IP.Model.WorkHistory;

/**
 * CVService class is a service for CV related endpoint path handling for the Qualichain back-end server
 */
@Path("/")
public class CVService {
	rabbitMQService rabbit = new rabbitMQService();
	
	public CVService() {
	}

	/**
	 * Sends a request for a list of all the CVs stored in the server database
	 * @return Json format data of all CVs in the database
	 */
    @GET
	@Path("/cv")
	@Produces(MediaType.APPLICATION_JSON)
    public Response GetCVs() {
		try {
			List<CV> cvs = CV.getCVs();
            JsonArray results = new JsonArray();
            for(CV cv : cvs) {
                results.add(ModelClassToJson.getCVJson(cv));
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
    
    /**
     * 
     * @param PersonURI
     * @return
     */
    @GET
    @Path("/cv/{PersonURI}/percentage")
    public Response getCVCompleteness(@PathParam("PersonURI") String PersonURI) {
    	try{
    		CV cv = CV.getCVbyPersonURI(PersonURI);
    		int completenessPercentage = 0;
    		if(!cv.getWorkHistory().isEmpty())
    			completenessPercentage += 25;
    		if(!cv.getEducation().isEmpty())
    			completenessPercentage += 25;
    		if(!cv.getCourses().isEmpty())
    			completenessPercentage += 25;
    		if(!cv.getSkillRefs().isEmpty())
    			completenessPercentage += 25;
    		
    		return Response.status(Response.Status.OK).entity("{\"uid\":" + PersonURI + ",\"completeness\":" + completenessPercentage + "}").build();
    	}
    	catch (NoSuchElementException e1) {
    		return Response.status(Response.Status.OK).entity("{\"uid\":" + PersonURI + ",\"completeness\":" + 0 + "}").build();
    	}
    	catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}	
    }
    
    /**
     * 
     * @param PersonURI
     * @return
     */
    @GET
    @Path("/cv/{PersonURI}/careerPath")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCareerPath(@PathParam("PersonURI") String PersonURI){
    	try {
//    		HashMap<String, LinkedList<JobPosting>> careerPath = CV.getJobCareerPath(PersonURI);
    		String careerPath = CareerPath.getJobCareerPathStringOutput(PersonURI, true);
    		return Response.ok(careerPath).build();
    	} 
    	catch (NoSuchElementException e1) {
    		return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
    	}
    	catch (NoCurrentJobException e2) {
    		return Response.status(Response.Status.BAD_REQUEST).entity(e2.getMessage()).build();
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}
    	
    }
    
    /**
     * 
     * @param PersonURI
     * @param jobID
     * @return
     */
    @GET
    @Path("/cv/{PersonURI}/recommendations/{jobID}/skills")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSkillRecommendations(@PathParam("PersonURI") String PersonURI, @PathParam("jobID") String jobID){
    	try {
    		List<Skill> skillRecommendations = CV.getSkillRecomendations(PersonURI, jobID);
    		return Response.ok(skillRecommendations).build();
    	}
    	catch (NoSuchElementException e1) {
    		return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	}
    }
    
    //TODO:Replaced by Matching Service method getCVJobScores
    /**
     * 
     * @param PersonURI
     * @return
     */
    @GET
    @Path("/cv/{PersonURI}/recommendations/jobs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJobRecommendations(@PathParam("PersonURI") String PersonURI){
    	try {
    		List<JobPosting> jobRecommendations = CV.getJobRecomendations(PersonURI);
    		return Response.ok(jobRecommendations).build();
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
     * @param PersonURI
     * @return
     */
    @GET
    @Path("/cv/{PersonURI}/applicationScores")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApplicationScores(@PathParam("PersonURI") String PersonURI){
    	try {
    		HashMap<String, Integer> scores = CV.getJobApplicationScores(PersonURI);
    		if(scores.isEmpty())
    			return Response.ok("No scores were able to be calculated").build();
			return Response.ok(scores).build();
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
     * @return
     */
	@POST
	@Path("/cv")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response CreateCV(String data) {
		GsonParser parser;
		try {
			parser = new GsonParser();
			CV cv = parser.toCV(data);
						
			try {
				Person p = Person.getPerson(cv.getPersonURI());
				p.setCVURI(cv.getURI());
				p.Save();
			}
			catch(NoSuchElementException e1) {
				System.out.println("profile not found, creating cv anyway");
				Person p = new Person();
				p.setURI(cv.getPersonURI());
				p.setCVURI(cv.getURI());
				p.Save();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			if(SparqlEndPoint.existURI(cv.getURI()))
				throw new IllegalArgumentException("CV already exists, either delete cv with person URI: " + cv.getPersonURI() + " or use update service");

			parser.SavetoFile("output-cv.ttl");
			String response = parser.toString("TTL");

			////SparqlEndPoint.insertTriple(parser.toString());
			////SparqlEndPoint.insertTriple(parser.toStringData());
			////SparqlEndPoint.insertTriple(parser.toStringData(),parser.toStringHeader());
			//SparqlEndPoint.insertTriple(parser.toStringData());
//			if(SparqlEndPoint.existURI(cv.getURI()))
//				return Response.status(Response.Status.BAD_REQUEST).entity("CV ID:" + cv.getID() +" already exists").build();
			cv.Save();
			
			
			//TODO:Add to publishing queue RabbitMQ
			try {
				JsonObject rabbitObject = new JsonObject();
				rabbitObject.add("cv", getCVinJson(cv));
				
				System.out.println(rabbitObject);
				rabbit.channel.basicPublish(rabbit.exchange, rabbitMQService.ROUTING_KEY, null, rabbitObject.toString().getBytes());
				System.out.println(rabbit.channel.isOpen());
			}
			catch (Exception e) {
				System.out.println("Could not send the created CV to the RabbitMQ queue.");
				e.printStackTrace();
			}
			
			
			return Response.ok(response, MediaType.APPLICATION_JSON).build();
			
			
		} 
		catch(NoSuchElementException e1) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
		}
		catch(IllegalArgumentException e2) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e2.toString()).build();	
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).entity(e.toString()).build();		
		}
	}
	
	/**
	 * 
	 * @param cv
	 * @return
	 */
	private JsonObject getCVinJson(CV cv) {
		
		JsonArray jsonTmp = new JsonArray();
		
		JsonObject jsonPropValue = new JsonObject();
		jsonPropValue.addProperty("label",cv.getLabel());
		jsonPropValue.addProperty("uri",cv.getURI());
		jsonPropValue.addProperty("id",RDFObject.uri2id(cv.getURI()));
		jsonPropValue.addProperty("comment",cv.getComment());
		jsonPropValue.addProperty("title",cv.getTitle());
		jsonPropValue.addProperty("personURI",cv.getPersonURI());
		jsonPropValue.addProperty("targetSector",cv.getTargetSector());
		jsonPropValue.addProperty("description",cv.getDescription());
		
		
		for(CVSkillRef skillRef : cv.getSkillRefs()) {
			jsonTmp.add(ModelClassToJson.getCVSkillRefJson(skillRef));
		}
		jsonPropValue.add("skills", jsonTmp);
		
		jsonTmp = new JsonArray();
		for(WorkHistory wh : cv.getWorkHistory()) {
			jsonTmp.add(ModelClassToJson.getWorkHistoryJson(wh));
		}
		jsonPropValue.add("workHistory",jsonTmp);
		
		return jsonPropValue;
	}

	/**
	 * 
	 * @param personID
	 * @return
	 */
	@GET
	@Path("/cv/{personID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetCV(@PathParam("personID")String personID) {
		
		try {
			CV cv = CV.getCVbyPersonURI(personID);
            JsonElement jsonResults = ModelClassToJson.getCVJson(cv);
//			System.out.println(jsonResults.toString());
			
            return Response.ok(jsonResults.toString()).build();
		} 
		catch (NoSuchElementException e1) {
    		return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
    	}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}		
	}
	
	
	/*
	@GET
	@Path("/cv/{cvURI}")
	@Produces(MediaType.APPLICATION_JSON)
	public CV GetCV(@PathParam("cvURI")String cvURI) {
		CV cv = CV.getCV(cvURI);	
		return cv;		
	}
	*/
	
	/**
	 * 
	 * @param personURI
	 * @param data
	 * @return
	 */
	@PUT
	@Path("/cv/{personURI}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response UpdateCV(@PathParam("personURI")String personURI, String data) {
		try {
			CV cv = CV.getCVbyPersonURI(personURI);
			GsonParser parser = new GsonParser();
			
			CV updatedCV = parser.toCV(data);
			
//			if(updatedCV.getURI() == null){
//				updatedCV.setURI(cv.getURI());
//			}
			
			if(cv.getURI().equals(updatedCV.getURI())) {
				updatedCV.update();
				
				try {
					JsonObject rabbitObject = new JsonObject();
					rabbitObject.add("cv", getCVinJson(cv));
					
					System.out.println(rabbitObject);
					rabbit.channel.basicPublish(rabbit.exchange, rabbitMQService.ROUTING_KEY, null, rabbitObject.toString().getBytes());
					System.out.println(rabbit.channel.isOpen());
				}
				catch (Exception e) {
					System.out.println("Could not send the created CV to the RabbitMQ queue.");
				}
				
				JsonElement newCV = ModelClassToJson.getCVJson(updatedCV);
				
				return Response.ok(newCV.toString()).build();
			}
			else
				return Response.status(Response.Status.CONFLICT).entity("CV URI from new data does not match previous CV associated with the profile").build();
	
		}
		catch(NoSuchElementException e1) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
		}
		catch(Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}	
	}
	
	/**
	 * 
	 * @param profileID
	 * @return
	 */
	@DELETE
	@Path("/cv/{profileID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCV(@PathParam("profileID")String profileID) {
		try {
			CV cv = CV.getCVbyPersonURI(profileID);
			RDFObject.quickDeleteByURI(cv.getURI());
			RDFObject.deleteURIAssociations(cv.getURI());
			JsonElement cvJson = getCVinJson(cv);
			return Response.ok(cvJson.toString()).build();
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
	 * @param profileID
	 * @return
	 */
	@GET
	@Path("/cv/{profileID}/skills")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCVSkills(@PathParam("profileID") String profileID) {
		try {
			CV cv = CV.getCVbyPersonURI(profileID);
			
			return Response.ok(cv.getSkills()).build();
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
	@Path("/cv/{profileID}/skillsRefs")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCVSkillRefs(@PathParam("profileID") String profileID) {
		try {
			CV cv = CV.getCVbyPersonURI(profileID);
			
			List<CVSkillRef> refs = cv.getSkillRefs();
			
			JsonArray results = new JsonArray(refs.size());

			for(CVSkillRef ref : refs) {
				results.add(ModelClassToJson.getCVSkillJsonFromRef(ref));
			}
			
			return Response.ok(results.toString()).build();
		}
		catch (NoSuchElementException e1) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/cv/{profileID}/skills")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response makeSkillRef(@PathParam("profileID") String profileID, String data) {
		try {
			GsonParser parser = new GsonParser();
			CV cv = CV.getCVbyPersonURI(profileID);
			CVSkillRef newRef = parser.toCVSkillRef(data);
			
			
			if(cv.hasSkillRef(newRef) == null) {
				cv.addSkillRef(newRef);
				cv.Save();
				try {
					JsonObject rabbitObject = new JsonObject();
					rabbitObject.add("cv", getCVinJson(cv));
					
					System.out.println(rabbitObject);
					rabbit.channel.basicPublish(rabbit.exchange, rabbitMQService.ROUTING_KEY, null, rabbitObject.toString().getBytes());
					System.out.println(rabbit.channel.isOpen());
				}
				catch (Exception e) {
					System.out.println("Could not send the created CV to the RabbitMQ queue.");
				}
				
			}
			else
				return Response.status(Response.Status.BAD_REQUEST).entity("CV already contains this skill, to update the reference please use PUT method").build();
			
			JsonElement result = ModelClassToJson.getCVSkillJsonFromRef(newRef);
			
			return Response.ok(result.toString()).build();
			
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
	@Path("/cv/{profileID}/skills")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response skillRefUpdate(@PathParam("profileID") String profileID, String data) {
		
		//Have to remove old reference from cv and add the new one
		try {
			GsonParser parser = new GsonParser();
			CV cv = CV.getCVbyPersonURI(profileID);
			CVSkillRef newRef = parser.toCVSkillRef(data);
			String skillURI = newRef.getSkillURI();
			
//			CVSkillRef ref = CVSkillRef.getCVSkillRefObjectBySkillID(skillURI);
			CVSkillRef ref = cv.hasSkillRef(newRef);
			JsonElement result = null;
			
			if(cv.hasSkillRef(ref) != null) {
				cv.removeSkillRef(ref);
				RDFObject.quickDeleteByURI(ref.getURI());
				RDFObject.deleteURIAssociations(ref.getURI());
				newRef.setURI(ref.getURI());
				newRef.setSkill(Skill.getSkill(skillURI));
				cv.addSkillRef(newRef);
				cv.update();
				result = ModelClassToJson.getCVSkillJsonFromRef(newRef);
//				try {
//					JsonObject rabbitObject = new JsonObject();
//					rabbitObject.add("cv", getCVinJson(cv));
//					
//					System.out.println(rabbitObject);
//					rabbit.channel.basicPublish(rabbit.exchange, rabbitMQService.ROUTING_KEY, null, rabbitObject.toString().getBytes());
//					System.out.println(rabbit.channel.isOpen());
//				}
//				catch (Exception e) {
//					System.out.println("Could not send the created CV to the RabbitMQ queue.");
//				}
			}
			else
				return Response.status(Response.Status.BAD_REQUEST).entity("The selected skill reference "+ ref.getURI()
						+ " does not belong to the selected profile " + profileID).build();
			
			
			return Response.ok(result.toString()).build();
			
		}
		catch(NoSuchElementException e1) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
		}
		catch(Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
//	/**
//	 * 
//	 * @param profileID
//	 * @param data
//	 * @return
//	 */
//	@POST
//	@Path("/cv/{profileID}/skills")
//	@Consumes(MediaType.TEXT_PLAIN)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response postCVSkill(@PathParam("profileID") String profileID, String data) {
//		try {
//			GsonParser parser = new GsonParser();
//			CV cv = CV.getCVbyPersonURI(profileID);
//			Skill newSkill = parser.toSkill(data);
//			cv.addSkill(newSkill);
//			cv.addSkillRef(newSkill, null, null, null, null);
//			cv.Save();
//			
//			return Response.ok(newSkill).build();
//			
//		}
//		catch(NoSuchElementException e1) {
//			return Response.status(Response.Status.BAD_REQUEST).entity(e1.getMessage()).build();
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
//		}
//	}
	
	/**
	 * 
	 * @return
	 */
	@GET
	@Path("/skills")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSkills(){
		Map<String, String> skillsMap = new HashMap<String, String>();
		try {
			List<Skill> skills = Skill.getSkills();
			for(Skill skill : skills) {
				skillsMap.put(skill.getURI(), skill.getLabel());
			}
			return Response.ok(skillsMap).build();
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
