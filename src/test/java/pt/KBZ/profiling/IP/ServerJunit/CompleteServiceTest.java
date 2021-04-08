package IP.ServerJunit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.apache.jena.atlas.logging.java.TextFormatter;
import org.apache.jena.fuseki.FusekiLogging;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import IP.GsonParser;
import IP.Client.HTTPrequest;
import IP.Model.CV;
import IP.Model.JobPosting;
import IP.Model.ModelClassToJson;
import IP.Model.Person;
import IP.Model.Skill;
import IP.Model.SparqlEndPoint;
import IP.Model.Junit.Data.CVTestData;
import IP.Model.Junit.Data.JobPostingTestData;
import IP.Model.Junit.Data.PersonTestData;
import IP.Model.Junit.Data.SkillTestData;
import IP.Server.RestServer;

public class CompleteServiceTest {
	
	public static String REQUEST_PATH = "http://localhost:7529";
	private static String REST_PORT = "7529";
	private static Logger Log = Logger.getLogger(CompleteServiceTest.class.getName());
	private static FileHandler FileLogOutput; 
	private static Level logLevel;
	
	@Test
//	@Disabled
	/**
	 * Tests the calls to the server for creating the main Model Class Objects, Skill, JobPosting, Person and CV
	 * and also the calls to the server for retrieving them, both to test those services and to verify if the data
	 * stored in the server is correctly stored
	 */
	void CorrectInputCreateModelClasses() {
		try {
			
			FileLogOutput = new FileHandler("CorrectInputCreateModelClasses.txt");
			FileLogOutput.setFormatter(new TextFormatter());
			StreamHandler sh = new StreamHandler(System.out, new TextFormatter());
			Log.setUseParentHandlers(false);
			Log.addHandler(FileLogOutput);
			Log.addHandler(sh);
			logLevel = Level.CONFIG;
			FileLogOutput.setLevel(logLevel);
			Log.setLevel(logLevel);
			
			Log.info("=====================================START OF TEST============================================\n");
			
			initializeServices();
			
			Thread.sleep(4000L);
			
			
			
			//Variables for parsing server responses
			Gson gson = new Gson();
	        
	        //Variables for sending and receiving requests to and from the server
			HTTPrequest request;
			String body;
			String response;
			
			//Clear test database
			request = new HTTPrequest(REQUEST_PATH + "/ClearDatabase");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.DELETErequest();
	
			
			//request.addURLParam("query="+query);
			response = request.Response();
			Log.info("");
			Log.info("=============================================");
			Log.info("SERVER RESPONSE:");
			Log.info(response);
			Log.info("=============================================");
			Log.info("");
			request.close();
			
			Log.info("");
			Log.info("=============================================");
			Log.info("Connection test... Expecting \"Root\" response");
			
			request = new HTTPrequest(REQUEST_PATH + "/");	

			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.GETrequest();
			
			response = request.Response();
			
			Log.info(response);

			assertTrue(response.equals("Root"));
			

//			Thread.sleep(8000L);
			
			Log.info("=============================================\n");
			Log.info("");
			Log.info("SERVER RESPONSE TEST SUCCESSFUL\n");
			Log.info("");
			Log.info("=============================================\n");
			Log.info("");
			Thread.sleep(1000L);
			
			
			//---------------------------------------------------------------------------------------------------------------
			//Create skills test
			
			Log.info("=============================================");
			Log.info("");
			Log.info("CREATE SKILLS TEST:");
			Log.info("");
			Log.info("=============================================");
			Log.info("");
			
			List<Skill> testSkills = new SkillTestData().getSkills();
			
			for(Skill skill : testSkills) {
				request = new HTTPrequest(REQUEST_PATH + "/skill");	
				
				request.addRequestProperty("Content-Type", "text/plain");
				request.addRequestProperty("Accept", "*/*");
				body = ModelClassToJson.getSkillJsonForInput(skill).toString();
				Log.info("=============================================");
				Log.info("SENDING CREATE SKILL REQUEST:");
				Log.info(body);
				Log.info("=============================================");
				Log.info("");
				request.POSTrequest(body);
				//Tests if the equals method defined in skill class works as intended
				assertTrue(skill.equals(skill));
				response = request.Response();
				Skill skillGet = Skill.getSkill(skill.getURI());
				
				Log.info("DIRECT CALL TO FUSEKI RESPONSE");
				Log.info(ModelClassToJson.getSkillJsonForInput(skillGet).toString());
				assertTrue(skill.equals(skillGet));
				
				Log.info("");
				Log.info("=============================================");
				Log.info("SERVER RESPONSE:");
				Log.info(response);
				Log.info("=============================================");
				Log.info("");
				request.close();
				Thread.sleep(100L);
			}
			Log.info("CREATE SKILLS API TEST SUCCESSFUL");
			
			//Get all skills test
			Log.info("");
			Log.info("=============================================");
			Log.info("SENDING GET ALL SKILLS REQUEST...");
			request = new HTTPrequest(REQUEST_PATH + "/skill");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.GETrequest();
			
			
			response = request.Response();
			Skill[] skillArray = gson.fromJson(response, Skill[].class);
			assertTrue(testSkills.size() == skillArray.length);
			boolean isContained = false;
			for(Skill skill : skillArray) {
				for(Skill compareTo : testSkills) {
					if(skill.equals(compareTo)) {
						isContained = true;
						break;
					}
				}
				assertTrue(isContained);
				isContained = false;
			}
			
			Log.info("RESULTS:");
			Log.info(response);
			request.close();
			
			Log.info("=============================================");
			Log.info("GET ALL SKILLS API TEST SUCCESSFUL");

			Thread.sleep(1000L);
				
			
			//---------------------------------------------------------------------------------------------------------------
			//Create profiles test
			
			Log.info("=============================================");
			Log.info("");
			Log.info("CREATE PROFILE TEST:");
			Log.info("");
			Log.info("=============================================");
			Log.info("");
			
			List<Person> testProfiles = new PersonTestData().getPersons();
			
			for(Person profile : testProfiles) {
				request = new HTTPrequest(REQUEST_PATH + "/profiles");	
				
				request.addRequestProperty("Content-Type", "text/plain");
				request.addRequestProperty("Accept", "*/*");
				body = ModelClassToJson.getProfileJson(profile).toString();
				Log.info("=============================================");
				Log.info("SENDING CREATE PROFILE REQUEST:");
				Log.info(body);
				Log.info("=============================================");
				Log.info("");
				
				request.POSTrequest(body);
				
				assertTrue(profile.equals(profile));
				
				response = request.Response();
				Person personGet = Person.getPerson(profile.getURI());
				
				Log.info("DIRECT CALL TO FUSEKI RESPONSE");
				Log.info(ModelClassToJson.getProfileJson(personGet).toString());
				assertTrue(profile.equals(personGet));
				
				Log.info("");
				Log.info("=============================================");
				Log.info("SERVER RESPONSE:");
				Log.info(response);
				Log.info("=============================================");
				Log.info("");
				request.close();
				Thread.sleep(100L);
			}
			
			
			//Get all profiles test
			Log.info("");
			Log.info("=============================================");
			Log.info("SENDING GET ALL PROFILES REQUEST...");
			
			request = new HTTPrequest(REQUEST_PATH + "/profiles");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.GETrequest();

			
			response = request.Response();
			Person[] profileArray = gson.fromJson(response, Person[].class);
			assertTrue(testProfiles.size() == profileArray.length);
			isContained = false;
			for(Person profile : profileArray) {
				for(Person compareTo : testProfiles) {
					if(profile.equals(compareTo)) {
						isContained = true;
						break;
					}
				}
				assertTrue(isContained);
				isContained = false;
			}
			
			Log.info("RESULTS:");
			Log.info(response);
			request.close();
			
			Log.info("=============================================");
			Log.info("GET ALL PROFILES API TEST SUCCESSFUL");
			
			Thread.sleep(1000L);
			
			//---------------------------------------------------------------------------------------------------------------
			//Create CVs test
			
			Log.info("=============================================");
			Log.info("");
			Log.info("CREATE CV TEST:");
			Log.info("");
			Log.info("=============================================");
			Log.info("");
			
			List<CV> testCVs = new CVTestData().getCvs();
			
			for(CV cv : testCVs) {
				request = new HTTPrequest(REQUEST_PATH + "/cv");	
				
				request.addRequestProperty("Content-Type", "text/plain");
				request.addRequestProperty("Accept", "*/*");
				body = ModelClassToJson.getCVJson(cv).toString();
				Log.info("=============================================");
				Log.info("SENDING CREATE CV REQUEST:");
				Log.info(body);
				Log.info("=============================================");
				Log.info("");
				
				request.POSTrequest(body);
	
				
				//request.addURLParam("query="+query);
				response = request.Response();
				CV cvGet = CV.getCV(cv.getURI());
				
				Log.info("DIRECT CALL TO FUSEKI RESPONSE");
				Log.info(ModelClassToJson.getCVJson(cvGet).toString());
				assertTrue(cv.equals(cvGet));
				
				Log.info("");
				Log.info("=============================================");
				Log.info("SERVER RESPONSE:");
				Log.info(response);
				Log.info("=============================================");
				Log.info("");
				request.close();
			}
			
			
			//Get all cvs test
			
			Log.info("");
			Log.info("=============================================");
			Log.info("SENDING GET ALL CVs REQUEST...");
			
			request = new HTTPrequest(REQUEST_PATH + "/cv");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.GETrequest();
			
			response = request.Response();
			CV[] cvArray = gson.fromJson(response, CV[].class);
			assertTrue(testCVs.size() == cvArray.length);
			isContained = false;
			for(CV cv : cvArray) {
				for(CV compareTo : testCVs) {
					if(cv.equals(compareTo)) {
						isContained = true;
						break;
					}
				}
				assertTrue(isContained);
				isContained = false;
			}
			
			
			
			Log.info("RESULTS:");
			Log.info(response);
			request.close();
			
			Log.info("=============================================");
			Log.info("GET ALL CVs API TEST SUCCESSFUL");
			
			Thread.sleep(1000L);
			
			//---------------------------------------------------------------------------------------------------------------
			//Create jobs test
			
			Log.info("=============================================");
			Log.info("");
			Log.info("CREATE JOBPOSTING TEST:");
			Log.info("");
			Log.info("=============================================");
			Log.info("");
			
			List<JobPosting> testJobPosts = new JobPostingTestData().getJobPostings();
			
			for(JobPosting job : testJobPosts) {
				request = new HTTPrequest(REQUEST_PATH + "/jobs");	
				
				request.addRequestProperty("Content-Type", "text/plain");
				request.addRequestProperty("Accept", "*/*");
				body = ModelClassToJson.getJobJson(job).toString();
				
				Log.info("=============================================");
				Log.info("SENDING CREATE JOBPOSTING REQUEST:");
				Log.info(body);
				Log.info("=============================================");
				Log.info("");
				
				request.POSTrequest(body);
				
				//request.addURLParam("query="+query);
				response = request.Response();
				
				JobPosting jobGet = JobPosting.getJobPosting(job.getURI());
				
				Log.info("DIRECT CALL TO FUSEKI RESPONSE");
				Log.info(ModelClassToJson.getJobJson(jobGet).toString());
				assertTrue(job.equals(jobGet));
				
				Log.info("");
				Log.info("=============================================");
				Log.info("SERVER RESPONSE:");
				Log.info(response);
				Log.info("=============================================");
				Log.info("");
				
				request.close();
			}
			
			
			//Get all jobs test
			
			Log.info("");
			Log.info("=============================================");
			Log.info("SENDING GET ALL JOBPOSTINGS REQUEST...");
			
			request = new HTTPrequest(REQUEST_PATH + "/jobs");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.GETrequest();

			
			//request.addURLParam("query="+query);
			response = request.Response();
			
			JobPosting[] jobArray = gson.fromJson(response, JobPosting[].class);
			assertTrue(testJobPosts.size() == jobArray.length);
			isContained = false;
			for(JobPosting job : jobArray) {
				for(JobPosting compareTo : testJobPosts) {
					if(job.equals(compareTo)) {
						isContained = true;
						break;
					}
				}
				assertTrue(isContained);
				isContained = false;
			}
			
			
			
			Log.info("RESULTS:");
			Log.info(response);
			
			request.close();
			
			Log.info("=============================================");
			Log.info("GET ALL JOBPOSTINGS API TEST SUCCESSFUL");
			
			Thread.sleep(1000L);
			

			
			//---------------------------------------------------------------------------------------------------------------
			//delete cv test

			Log.info("");
			Log.info("=============================================");
			Log.info("DELETE CV TEST");
			Log.info("=============================================");
			
			request = new HTTPrequest(REQUEST_PATH + "/cv/1");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.DELETErequest();
	
			
			//request.addURLParam("query="+query);
			response = request.Response();
			Log.info("");
			Log.info("=============================================");
			Log.info("SERVER RESPONSE:");
			Log.info(response);
			Log.info("=============================================");
			Log.info("");
			request.close();
			
			
			//delete cv verification
			
			Log.info("");
			Log.info("=============================================");
			Log.info("DELETE CV VERFICATION");
			Log.info("=============================================");
			
			assertFalse(SparqlEndPoint.existURI(":id1"));
			
			request = new HTTPrequest(REQUEST_PATH + "/cv/1");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			response = request.GETrequest();
	
			//request.addURLParam("query="+query);
//			response = request.Response();
			assertTrue(response.equals("Bad Request"));
			Log.info("");
			Log.info("=============================================");
			Log.info("SERVER RESPONSE:");
			Log.info(response);
			Log.info("=============================================");
			Log.info("");
			request.close();
			
			
			//delete jobposting test
			
			Log.info("");
			Log.info("=============================================");
			Log.info("DELETE JOBPOSTING TEST");
			Log.info("=============================================");
			
			request = new HTTPrequest(REQUEST_PATH + "/jobs/Job1");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.DELETErequest();
	
			
			//request.addURLParam("query="+query);
			response = request.Response();
			Log.info("");
			Log.info("=============================================");
			Log.info("SERVER RESPONSE:");
			Log.info(response);
			Log.info("=============================================");
			Log.info("");
			request.close();
			
			
			//delete jobposting verification
			
			Log.info("");
			Log.info("=============================================");
			Log.info("DELETE JOBPOSTING VERFICATION");
			Log.info("=============================================");
			
			assertFalse(SparqlEndPoint.existURI(":Job1"));
			
			request = new HTTPrequest(REQUEST_PATH + "/jobs/Job1");	
			
//			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			response = request.GETrequest();
	
			
			//request.addURLParam("query="+query);
//			response = request.Response();
			assertTrue(response.equals("Bad Request"));
			Log.info("");
			Log.info("=============================================");
			Log.info("SERVER RESPONSE:");
			Log.info(response);
			Log.info("=============================================");
			Log.info("");
			request.close();
			
			
			//delete profile test
			
			Log.info("");
			Log.info("=============================================");
			Log.info("DELETE PROFILE TEST");
			Log.info("=============================================");
			
			request = new HTTPrequest(REQUEST_PATH + "/profiles/1");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.DELETErequest();
	
			
			//request.addURLParam("query="+query);
			response = request.Response();
			Log.info("");
			Log.info("=============================================");
			Log.info("SERVER RESPONSE:");
			Log.info(response);
			Log.info("=============================================");
			Log.info("");
			request.close();
			
			
			//delete profile verification
			
			Log.info("");
			Log.info("=============================================");
			Log.info("DELETE PROFILE VERFICATION");
			Log.info("=============================================");
			
			assertFalse(SparqlEndPoint.existURI(":1"));
			
			request = new HTTPrequest(REQUEST_PATH + "/profiles/1");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			response = request.GETrequest();
	
			
			//request.addURLParam("query="+query);
//			response = request.Response();
			assertTrue(response.equals("Bad Request"));
			Log.info("");
			Log.info("=============================================");
			Log.info("SERVER RESPONSE:");
			Log.info(response);
			Log.info("=============================================");
			Log.info("");
			request.close();
			
			
			
			
	
			Log.info("======================================END OF TEST=============================================");
						
		}
		catch (Exception e) {
			e.printStackTrace();
			Log.severe(e.getMessage());
			fail(e.getMessage());
		}
	}
	
	@Test
	/**
	 * Tests the Exception handling for the create and get services for Model classes when input is incorrect
	 */
	void IncorrectInputCreateModelClasses() {
		
	}
	
	@Test
	/**
	 * Tests if the create and get application services works if the input for the creation of applications is the expected input
	 */
	void CorrectInputCreateApplications() {
		
		
//		//---------------------------------------------------------------------------------------------------------------
//		//matching test
//		request = new HTTPrequest(REQUEST_PATH + "/jobs");	
//		
//		request.addRequestProperty("Content-Type", "text/plain");
//		request.addRequestProperty("Accept", "*/*");
//		body = "";
//		request.POSTrequest(body);
//
//		
//		//request.addURLParam("query="+query);
//		response = request.Response();
//		System.out.println(response);
//		request.close();
//		
//		
//		//---------------------------------------------------------------------------------------------------------------
//		//application test
//		request = new HTTPrequest(REQUEST_PATH + "/jobs");	
//		
//		request.addRequestProperty("Content-Type", "text/plain");
//		request.addRequestProperty("Accept", "*/*");
//		request.POSTrequest(body);
//
//		
//		//request.addURLParam("query="+query);
//		response = request.Response();
//		System.out.println(response);
//		request.close();
//		
//		
//		//application verification
//		request = new HTTPrequest(REQUEST_PATH + "/jobs");	
//		
//		request.addRequestProperty("Content-Type", "text/plain");
//		request.addRequestProperty("Accept", "*/*");
//		request.POSTrequest(body);
//
//		
//		//request.addURLParam("query="+query);
//		response = request.Response();
//		System.out.println(response);
//		request.close();
//		
//		
//		//---------------------------------------------------------------------------------------------------------------
//		//Match application
//		request = new HTTPrequest(REQUEST_PATH + "/jobs");	
//		
//		request.addRequestProperty("Content-Type", "text/plain");
//		request.addRequestProperty("Accept", "*/*");
//		request.POSTrequest(body);
//
//		
//		//request.addURLParam("query="+query);
//		response = request.Response();
//		System.out.println(response);
//		request.close();
//		
//		//--------------------------------------------------------------------------------------------------------------
//		//Assign job test
//		request = new HTTPrequest(REQUEST_PATH + "/jobs");	
//		
//		request.addRequestProperty("Content-Type", "text/plain");
//		request.addRequestProperty("Accept", "*/*");
//		request.POSTrequest(body);
//
//		
//		//request.addURLParam("query="+query);
//		response = request.Response();
//		System.out.println(response);
//		request.close();
		
	}
	
	@Test
	/**
	 * Tests the Exception handling for creating and getting applications when input is incorrect
	 */
	void IncorrectInputCreateApplications() {
		
	}
	
	@Test
	void CorrectInputCareerPath() {
		
//		//---------------------------------------------------------------------------------------------------------------
//		//CareerPath test
//		request = new HTTPrequest(REQUEST_PATH + "/jobs");	
//		
//		request.addRequestProperty("Content-Type", "text/plain");
//		request.addRequestProperty("Accept", "*/*");
//		request.POSTrequest(body);
//
//		
//		//request.addURLParam("query="+query);
//		response = request.Response();
//		System.out.println(response);
//		request.close();
		
	}
	
	@Test
	void IncorrectInputCareerPath() {
		
	}
	
	/**
	 * Starts the temporary servers for the duration of each test 
	 */
	private void initializeServices() {
//		new Thread() {
//			public void run() {
//				try {
//					FusekiLogging.setLogging();
//					Dataset ds = DatasetFactory.create();
//					FusekiServer server = FusekiServer.create()
//					  .port(4343)
//					  .add("/QualiChainTest", ds, true)
//					  .build();
//					server.start();
//					Log.info("Fuseki Server Inicialized");
//				}
//				catch(Exception e) {
//					Log.info("FUSEKI SERVER NOT FOUND ON PORT: 3030, DATASET /QualiChainTest");
//				}
//			}
//		}.start();
		new Thread() {
			public void run() {
				try {
					String[] args = new  String[2];
//					args[0] = "http://localhost:4343/QualiChain/";
					args[0] = "test";
//					args[0] = "internal";
					args[1] = REST_PORT;
					RestServer.main(args);
					Log.info("RestServer inicialized");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}
