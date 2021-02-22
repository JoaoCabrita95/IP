package IP.Server;


import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import IP.Model.Application;
import IP.Model.CV;
import IP.Model.Course;
import IP.Model.Education;
import IP.Model.JobPosting;
import IP.Model.Person;
import IP.Model.RDFObject;
import IP.Model.Skill;
import IP.Model.SparqlEndPoint;
import IP.Model.WorkHistory;
import IP.Model.Junit.Data.CVTestData;
import IP.Model.Junit.Data.JobPostingTestData;
import IP.Model.Junit.Data.PersonTestData;
import IP.Model.Junit.Data.SkillTestData;
import IP.Model.Junit.Data.careerPathTestData;

@Path("/")
public class GeneralService {
	public GeneralService() {
	}
	
	@POST
	@Path("/insertTextTriples")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response insertTextTriples(String data) {
		try {
			String input = data;
			if(data.startsWith("@prefix")) {
				input = data.substring(data.lastIndexOf("@prefix"));
				input = input.substring(input.indexOf("\n"));
			}
			String response = SparqlEndPoint.insertTriple(input);
			if(response.equals("IO ERROR"))
				return Response.status(Response.Status.BAD_REQUEST).entity("Data input incorrect format, use turtle(TTL) format").build();
			return Response.ok().entity("Data inserted successfully").build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getMessage()).build();
		}
	}
	
	@DELETE
	@Path("/ClearDatabase")
	public Response deleteAllTriples() {
		SparqlEndPoint.deleteTriple("?s ?o ?p", "?s ?o ?p");
		return Response.ok("Please be carefull with this method, all triples have been erased from storage").build();
	}
	
	//Inserts test data for Career path method with pre-defined job postings with chained work histories for graph
		@POST
		@Path("/test/insertPresetTestData/{extraData}")
		@Consumes(MediaType.TEXT_PLAIN)
		public Response insertTestData(@PathParam("extraData") String extraData, String reset) {
			if(extraData.equals("true")) {
				SkillTestData skd = new SkillTestData();
				CVTestData cvd = new CVTestData();
				PersonTestData pd = new PersonTestData();
				JobPostingTestData jpd = new JobPostingTestData();
				
				List<Skill> skills = skd.getSkills();
				List<CV> cvs = cvd.getCvs();
				List<Person> ps = pd.getPersons();
				List<JobPosting> jps = jpd.getJobPostings();
			
				for(Skill skill: skills) {
					try {
						RDFObject.quickDeleteByURI(skill.getURI());
						RDFObject.deleteURIAssociations(skill.getURI());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				for(JobPosting jp: jps) {
					try {
						RDFObject.quickDeleteByURI(jp.getURI());
						RDFObject.deleteURIAssociations(jp.getURI());
					} catch (Exception e) {
						System.err.println(e.getMessage());
					}
				}
				for(Person p : ps) {
					try {
						RDFObject.quickDeleteByURI(p.getURI());
						RDFObject.deleteURIAssociations(p.getURI());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				for(Skill skill: skills) {
					try {
						skill.Save();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				for(JobPosting jp: jps) {
					try {
						jp.Save();
					} catch (Exception e) {
						System.err.println(e.getMessage());
					}
				}
				for(Person p : ps) {
					try {
						p.Save();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				for(CV cv: cvs) {
					try {
						cv.Save();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
			careerPathTestData data = new careerPathTestData();
			List<JobPosting> jps = data.getJobPostings();
			List<CV> cvs = data.getCvs();
			List<Person> ps = data.getProfiles();
			if(reset.equals("true")) {
				for(JobPosting job : jps) {
					try {
						RDFObject.quickDeleteByURI(job.getURI());
						RDFObject.deleteURIAssociations(job.getURI());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for(Person p : ps) {
					try {
						RDFObject.quickDeleteByURI(p.getURI());
						RDFObject.deleteURIAssociations(p.getURI());
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				for(CV cv : cvs) {
					try {
						RDFObject.quickDeleteByURI(cv.getURI());
						RDFObject.deleteURIAssociations(cv.getURI());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			for(JobPosting job : jps) {
				try {
					job.Save();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for(Person p : ps) {
				try {
					p.Save();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			for(CV cv : cvs) {
				try {
					cv.Save();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return Response.ok("Test data has been inserted").build();
		}
}
