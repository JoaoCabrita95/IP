package IP.ServerJunit;

import java.util.List;

import IP.Client.HTTPrequest;
import IP.Model.CV;
import IP.Model.JobPosting;
import IP.Model.ModelClassToJson;
import IP.Model.Person;
import IP.Model.Skill;
import IP.Model.Junit.Data.CVTestData;
import IP.Model.Junit.Data.JobPostingTestData;
import IP.Model.Junit.Data.PersonTestData;
import IP.Model.Junit.Data.SkillTestData;

public class CompleteServiceTest {
	
	public static String REQUEST_PATH = "http://localhost:8000";

	public static void main(String[] args) {
		try {
			HTTPrequest request;
			String body;
			String response;
			
			//---------------------------------------------------------------------------------------------------------------
			//Create skills test
			
			List<Skill> testSkills = new SkillTestData().getSkills();
			
			for(Skill skill : testSkills) {
				request = new HTTPrequest(REQUEST_PATH + "/skill");	
				
				request.addRequestProperty("Content-Type", "text/plain");
				request.addRequestProperty("Accept", "*/*");
				body = ModelClassToJson.getSkillJson(skill).toString();
				System.out.println(body);
				request.POSTrequest(body);
	
				
				//request.addURLParam("query="+query);
				response = request.Response();
				System.out.println(response);
				request.close();
			}
			
			
			//Get all skills test
			
			request = new HTTPrequest(REQUEST_PATH + "/skill");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.GETrequest();

			
			//request.addURLParam("query="+query);
			response = request.Response();
			System.out.println(response);
			request.close();
			
			
			//---------------------------------------------------------------------------------------------------------------
			//Create profiles test
			
			List<Person> testProfiles = new PersonTestData().getPersons();
			
			for(Person profile : testProfiles) {
				request = new HTTPrequest(REQUEST_PATH + "/profiles");	
				
				request.addRequestProperty("Content-Type", "text/plain");
				request.addRequestProperty("Accept", "*/*");
				body = ModelClassToJson.getProfileJson(profile).toString();
				System.out.println(body);
				request.POSTrequest(body);
	
				
				//request.addURLParam("query="+query);
				response = request.Response();
				System.out.println(response);
				request.close();
				break;
			}
			
			
			//Get all profiles test
			
			request = new HTTPrequest(REQUEST_PATH + "/skill");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.GETrequest();

			
			//request.addURLParam("query="+query);
			response = request.Response();
			System.out.println(response);
			request.close();
			
			
			//---------------------------------------------------------------------------------------------------------------
			//Create CVs test
			
			List<CV> testCVs = new CVTestData().getCvs();
			
			for(CV cv : testCVs) {
				request = new HTTPrequest(REQUEST_PATH + "/cv");	
				
				request.addRequestProperty("Content-Type", "text/plain");
				request.addRequestProperty("Accept", "*/*");
				body = ModelClassToJson.getCVJson(cv).toString();
				System.out.println(body);
				request.POSTrequest(body);
	
				
				//request.addURLParam("query="+query);
				response = request.Response();
				System.out.println(response);
				request.close();
				break;
			}
			
			
			//Get all cvs test
			
			request = new HTTPrequest(REQUEST_PATH + "/skill");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.GETrequest();

			
			//request.addURLParam("query="+query);
			response = request.Response();
			System.out.println(response);
			request.close();
			
			
			//---------------------------------------------------------------------------------------------------------------
			//Create jobs test
			
			List<JobPosting> testJobPosts = new JobPostingTestData().getJobPostings();
			
			for(JobPosting job : testJobPosts) {
				request = new HTTPrequest(REQUEST_PATH + "/jobs");	
				
				request.addRequestProperty("Content-Type", "text/plain");
				request.addRequestProperty("Accept", "*/*");
				body = ModelClassToJson.getJobJson(job).toString();
				System.out.println(body);
				request.POSTrequest(body);
	
				
				//request.addURLParam("query="+query);
				response = request.Response();
				System.out.println(response);
				request.close();
				break;
			}
			
			
			//Get all jobs test
			
			request = new HTTPrequest(REQUEST_PATH + "/skill");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.GETrequest();

			
			//request.addURLParam("query="+query);
			response = request.Response();
			System.out.println(response);
			request.close();
			
			
			//---------------------------------------------------------------------------------------------------------------
			//matching test
			request = new HTTPrequest(REQUEST_PATH + "/jobs");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			body = "";
			request.POSTrequest(body);
	
			
			//request.addURLParam("query="+query);
			response = request.Response();
			System.out.println(response);
			request.close();
			
			
			//---------------------------------------------------------------------------------------------------------------
			//application test
			request = new HTTPrequest(REQUEST_PATH + "/jobs");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.POSTrequest(body);
	
			
			//request.addURLParam("query="+query);
			response = request.Response();
			System.out.println(response);
			request.close();
			
			
			//application verification
			request = new HTTPrequest(REQUEST_PATH + "/jobs");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.POSTrequest(body);
	
			
			//request.addURLParam("query="+query);
			response = request.Response();
			System.out.println(response);
			request.close();
			
			
			//---------------------------------------------------------------------------------------------------------------
			//Match application
			request = new HTTPrequest(REQUEST_PATH + "/jobs");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.POSTrequest(body);
	
			
			//request.addURLParam("query="+query);
			response = request.Response();
			System.out.println(response);
			request.close();
			
			
			//---------------------------------------------------------------------------------------------------------------
			//delete cv test
			request = new HTTPrequest(REQUEST_PATH + "/jobs");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.POSTrequest(body);
	
			
			//request.addURLParam("query="+query);
			response = request.Response();
			System.out.println(response);
			request.close();
			
			
			//delete cv verification
			request = new HTTPrequest(REQUEST_PATH + "/jobs");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.POSTrequest(body);
	
			
			//request.addURLParam("query="+query);
			response = request.Response();
			System.out.println(response);
			request.close();
			
			
			//delete jobposting test
			request = new HTTPrequest(REQUEST_PATH + "/jobs");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.POSTrequest(body);
	
			
			//request.addURLParam("query="+query);
			response = request.Response();
			System.out.println(response);
			request.close();
			
			
			//delete jobposting verification
			request = new HTTPrequest(REQUEST_PATH + "/jobs");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.POSTrequest(body);
	
			
			//request.addURLParam("query="+query);
			response = request.Response();
			System.out.println(response);
			request.close();
			
			
			//delete profile test
			request = new HTTPrequest(REQUEST_PATH + "/jobs");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.POSTrequest(body);
	
			
			//request.addURLParam("query="+query);
			response = request.Response();
			System.out.println(response);
			request.close();
			
			
			//delete profile verification
			request = new HTTPrequest(REQUEST_PATH + "/jobs");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.POSTrequest(body);
	
			
			//request.addURLParam("query="+query);
			response = request.Response();
			System.out.println(response);
			request.close();
			
			
			//--------------------------------------------------------------------------------------------------------------
			//Get competence overview test
			request = new HTTPrequest(REQUEST_PATH + "/jobs");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.POSTrequest(body);
	
			
			//request.addURLParam("query="+query);
			response = request.Response();
			System.out.println(response);
			request.close();
			
			
			//--------------------------------------------------------------------------------------------------------------
			//Assign job test
			request = new HTTPrequest(REQUEST_PATH + "/jobs");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.POSTrequest(body);
	
			
			//request.addURLParam("query="+query);
			response = request.Response();
			System.out.println(response);
			request.close();
			
			
			//---------------------------------------------------------------------------------------------------------------
			//CareerPath test
			request = new HTTPrequest(REQUEST_PATH + "/jobs");	
			
			request.addRequestProperty("Content-Type", "text/plain");
			request.addRequestProperty("Accept", "*/*");
			request.POSTrequest(body);
	
			
			//request.addURLParam("query="+query);
			response = request.Response();
			System.out.println(response);
			request.close();
		
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

}
