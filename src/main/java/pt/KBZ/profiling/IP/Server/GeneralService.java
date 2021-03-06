package IP.Server;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.jena.atlas.logging.java.TextFormatter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import IP.Client.HTTPrequest;
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
import IP.RML.Mapping;

@Path("/")
public class GeneralService {

	private static final String NOTIFICATION_PATH = "http://qualichain.epu.ntua.gr:5004/notifications HTTP/1.1";
	rabbitMQService rabbit = new rabbitMQService();
//	rabbitMQConsumer rabbitConsumer = new rabbitMQConsumer();

	private static Logger Log = Logger.getLogger(GeneralService.class.getName());
	
	public ConnectionFactory factory;
	public Connection conn;
	public Channel channel;
	public String exchange;
	public static final String ROUTING_KEY = "produce_user_id";
	private final static String QUEUE_NAME = "produce_user_id";
	public static String REQUEST_PATH = "http://localhost:8000";
	public GeneralService() {
		Log.setLevel( Level.FINER );
		startRabbitMQConsumer();
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
			input = input.replace("saro:Topic", "saro:Skill");
			input = input.replace("saro:Product", "saro:Skill");
			input = input.replace("saro:Tool", "saro:Skill");
//			System.out.println(input);
			Log.info(input);
			String response = SparqlEndPoint.insertTriple(input);
			if(response.equals("IO ERROR"))
				return Response.status(Response.Status.BAD_REQUEST).entity("Data input incorrect format, use turtle(TTL) format").build();
			return Response.ok().entity("Data inserted successfully").build();
		}
		catch (Exception e) {
			
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
						Log.info(e.getMessage());
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
						Log.info(e.getMessage());
					}
				}
				
				for(Skill skill: skills) {
					try {
						skill.Save();
					} catch (Exception e) {
						Log.info(e.getMessage());
					}
				}
				for(JobPosting jp: jps) {
					try {
						jp.Save();
					} catch (Exception e) {
						Log.info(e.getMessage());
					}
				}
				for(Person p : ps) {
					try {
						p.Save();
					} catch (Exception e) {
						Log.info(e.getMessage());
					}
				}
				
				for(CV cv: cvs) {
					try {
						cv.Save();
					} catch (Exception e) {
						Log.info(e.getMessage());
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
						Log.info(e.getMessage());
					}
				}
				for(Person p : ps) {
					try {
						RDFObject.quickDeleteByURI(p.getURI());
						RDFObject.deleteURIAssociations(p.getURI());
					} catch(Exception e) {
						Log.info(e.getMessage());
					}
				}
				for(CV cv : cvs) {
					try {
						RDFObject.quickDeleteByURI(cv.getURI());
						RDFObject.deleteURIAssociations(cv.getURI());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.info(e.getMessage());
					}
				}
			}
			
			for(JobPosting job : jps) {
				try {
					job.Save();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.info(e.getMessage());
				}
			}
			for(Person p : ps) {
				try {
					p.Save();
				} catch(Exception e) {
					Log.info(e.getMessage());
				}
			}
			for(CV cv : cvs) {
				try {
					cv.Save();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.info(e.getMessage());
				}
			}
			
			return Response.ok("Test data has been inserted").build();
		}
		
		@POST
		@Path("/test/insertPresetTestData/{extraData}")
		public String rabbitMQTest() {
			
			String test = "test";
			
			try {
				rabbit.channel.basicPublish(rabbit.exchange, rabbitMQService.ROUTING_KEY, null, test.getBytes());
			} catch (IOException e) {
				Log.info(e.getMessage());
			}
			
			
			return null;
		}
		@POST
		@Path("/test/notification/{profileID}")
		public Response notificationTest(@PathParam("profileID") String profileID) {
			System.out.println("sending out test notification");
			HTTPrequest request;
			String response;
			
			JsonObject notification = new JsonObject();
			notification.addProperty("user_id", profileID);
			notification.addProperty("message", "Test Notification for +" + profileID);
			
			request = new HTTPrequest(NOTIFICATION_PATH);	
			request.addRequestProperty("Content-Type", "application/json");
			response = request.POSTrequest(notification.toString());
			
			return Response.ok(response).build();
		}
		
//		@DELETE
//		@Path("/rabbit/deleteUser/{userID}")
		public Response deleteUserAndRelationships( String userID) {
			Log.info("Trying to delete user with ID: " + userID + "\n");
			
			//Write delete function
			
			try {
				List<Application> apps = Application.getApplicationsByProfile(userID);
				if(apps.isEmpty())
					Log.info("No applications made by user found\n");
				for(Application app : apps) {
					System.out.println(app.getJobURI());
					Application.deleteURIAssociations(app.getURI());
					Application.quickDeleteByURI(app.getURI());
				}
					
			} catch (Exception e) {
				Log.info("Application deletion process error: " + e.getMessage() + "\n");
			}
			try {
				CV cv = CV.getCVbyPerson(userID);
				System.out.println(cv.getURI());
				CV.deleteObject(cv.getURI());
			} catch (Exception e) {
				Log.info("CV deletion process error: " + e.getMessage() + "\n");
			}

			try {
				Person.deleteObject(userID);
			} catch (Exception e) {
				Log.info("Profile deletion process error: " + e.getMessage() + "\n");
			}
			
			
			return Response.status(Response.Status.OK).entity("User with ID: "+ userID + " has been deleted.").build();
		}
		
		public void startRabbitMQConsumer() {
			factory = new ConnectionFactory();
			factory.setUsername("rabbitmq");
			factory.setPassword("rabbitmq");
			factory.setVirtualHost("/");
			factory.setHost("qualichain.epu.ntua.gr");
			factory.setPort(5672);
			exchange = "";
			try {
				conn = factory.newConnection();
				System.out.println(conn.getServerProperties());
				channel = conn.createChannel();

				channel.queueDeclarePassive(ROUTING_KEY);
//			    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

			    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//			    	HTTPrequest request;
//					String body;
//					String response;
			        String message = new String(delivery.getBody(), "UTF-8");
//			        System.out.println(" [x] Received '" + message + "'");
			        
			        String userID = message.substring(message.indexOf(":") +1, message.lastIndexOf("}")).trim();
//			        System.out.println(userID);
			        
			        deleteUserAndRelationships(userID);
			        
//			        request = new HTTPrequest(REQUEST_PATH + "/rabbit/deleteUser/" + userID);	
//					
//					request.addRequestProperty("Content-Type", "text/plain");
//					request.addRequestProperty("Accept", "*/*");
//					request.DELETErequest();
//					
//					response = request.Response();
			    };
			    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
				
			} catch (IOException e) {
				Log.info(e.getMessage());
			} catch (TimeoutException e) {
				Log.info(e.getMessage());
			}
		}
		
		
}
