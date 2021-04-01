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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import IP.GsonParser;
import IP.Model.CV;
import IP.Model.JobPosting;
import IP.Model.ModelClassToJson;
import IP.Model.Skill;
import IP.Model.SparqlEndPoint;
import matomo.matomoClient;

@Path("/")
public class SkillService {
	
	
	public SkillService() {
	}

    @GET
	@Path("/skill")
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetSkills() {
		try {
			List<Skill> skills = Skill.getSkills();
			JsonArray results = new JsonArray();
            for(Skill skill : skills) {
                results.add(ModelClassToJson.getSkillJsonForInput(skill));
            }
			return Response.ok(results.toString()).build();
		}
		catch(NoSuchElementException e1) {
			return Response.status(Response.Status.NO_CONTENT).build();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return null;
	}
    
    @POST
    @Path("/skill")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSkill(String data) {
    	try {
    		GsonParser parser = new GsonParser();
    		Skill skill = parser.toSkill(data);
    		skill.Save();
    		
    		return Response.ok(ModelClassToJson.getSkillJsonForInput(skill).toString()).build();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).entity(e.toString()).build();		
    	}
    }
    
    @DELETE
    @Path("/skill/{skillID}")
    public Response deleteSkill(@PathParam("skillID") String skillID) {
    	
    	try {
    		Skill skill = Skill.getSkill(skillID);
    		Skill.quickDeleteByURI(skill.getURI());
    		Skill.deleteURIAssociations(skill.getURI());
    		
    		return Response.ok("Skill with id: \"" + skill.getID() + "\" and label: \"" + skill.getLabel() + "\" has been deleted").build();
    	}
    	catch (NoSuchElementException e1){
    		return Response.status(Response.Status.BAD_REQUEST).entity(e1.toString()).build();
    	}
    	catch (Exception e){
    		return Response.status(Response.Status.BAD_REQUEST).entity(e.toString()).build();
    	}
    }
    
    
    @GET
    @Path("/skill/search/{text}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response SearchSkills(@PathParam("text") String text){
    	List<Skill> skills = Skill.getSkills(text);
    	JsonArray results = new JsonArray();
        for(Skill skill : skills) {
            results.add(ModelClassToJson.getSkillJsonForInput(skill));
        }
		return Response.ok(results.toString()).build();
    }
}
