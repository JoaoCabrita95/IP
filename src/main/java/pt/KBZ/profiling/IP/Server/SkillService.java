package IP.Server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.google.gson.JsonElement;

import IP.GsonParser;
import IP.Model.CV;
import IP.Model.JobPosting;
import IP.Model.Skill;
import IP.Model.SparqlEndPoint;

@Path("/")
public class SkillService {
	
	public SkillService() {
	}

    @GET
	@Path("/skill")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Skill> GetSkills() {
		try {
			return Skill.getSkills();
		} catch (Exception e) {
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
    		return Response.ok(skill).build();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).entity(e.toString()).build();		
    	}
    }
    
    
    @GET
    @Path("/skill/search/{text}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Skill> SearchSkills(@PathParam("text") String text){
		return Skill.getSkills(text);
    }
}
