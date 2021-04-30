package IP.Model.Junit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import IP.Matching;
import IP.Client.HTTPrequest;
import IP.Model.Accomplishment;
import IP.Model.Application;
import IP.Model.CV;
import IP.Model.CareerPath;
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
import IP.Server.CVService;
import IP.ServerJunit.CompleteServiceTest;
import IP.Server.JobpostingService;
import IP.Server.MatchingService;
import IP.Server.PersonService;
import IP.Server.RestServer;
import IP.config.Configuration;

@TestMethodOrder(value = OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
class ModelGlobalTest {

	Configuration cnf = new Configuration();
	SkillTestData skd;
	CVTestData cvd;
	PersonTestData pd;
	JobPostingTestData jpd;
	
	@BeforeAll
	void init() {
		
		cnf.LoadConfiguration(true);
		//Reset database
		SparqlEndPoint.deleteTriple("?s ?o ?p", "?s ?o ?p");
		skd = new SkillTestData();
		cvd = new CVTestData();
		pd = new PersonTestData();
		jpd = new JobPostingTestData();
		//Make sure database has prefixes, Wrong content-encoding????
//		SparqlEndPoint.insertTTLFile("prefixes.ttl");
	}
	
	@Test
	@Order(1)
	@DisplayName("Attempting to get data that doesn't exist from server")
	@Disabled("Disabled")
	void failGetData() {
		try {
			assertTrue(CV.getCVs().size() == 0);
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
		try {
			assertTrue(Person.getPersons().size() == 0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			assertTrue(Skill.getSkills().size() == 0);
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
		try {
			assertTrue(JobPosting.getJobPostings().size() == 0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	@Order(2)
	@DisplayName("Attempting to insert data from all model classes to server")
//	@Disabled("Disabled")
	void insertAllData() {
		List<Skill> skills = skd.getSkills();
		List<CV> cvs = cvd.getCvs();
		List<Person> ps = pd.getPersons();
		List<JobPosting> jps = jpd.getJobPostings();
	
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
		for(CV cv: cvs) {
			try {
				cv.Save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for(Person p : ps) {
			try {
				p.Save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Test
	@Order(3)
	@DisplayName("Verifying if its possible to recover Personal data from CV, and CVs from Personal data")
	@Disabled("Disabled")
	void verifyCVtoPersonAssociation() throws Exception {
		List<Person> ps = new ArrayList<Person>();
		List<CV> cvs = cvd.getCvs();
		Person tempPerson = null;
		CV tempCV = null;
		for(CV cv: cvs) {
			tempPerson = Person.getPerson(cv.getPersonURI());
			ps.add(tempPerson);
//			assertTrue(tempPerson.getLabel() != null);
		}
		assertTrue(ps.size() == pd.getPersons().size() + 4);
		
		ps = pd.getPersons();
		cvs = new ArrayList<CV>();
		for(Person p: ps) {
			tempCV = CV.getCV(p.getCVURI());
			cvs.add(tempCV);
//			assertTrue(tempCV.getLabel() != null);
		}
		assertTrue(cvs.size() == cvd.getCvs().size() - 4);
		
	}
	
	@Test
	@Order(4)
	@DisplayName("Recovering JobPostings by Skill URI")
	@Disabled("Not yet Implemented")
	void getJobPostingsBySkills() {
		
	}
	
	@Test
	@Order(5)
	@DisplayName("Recovering CVs by Skill URI")
	@Disabled("Not yet Implemented")
	void getCVsBySkills() {
		
	}
	
	@Test
	@Order(6)
	@DisplayName("Testing if matching function returns desired results")
	@Disabled("Skipped because of output")
	void matchingTest() {
		List<JobPosting> jps;
		try {
			jps = JobPosting.getJobPostings();
			HashMap<String, Integer> matches;
			for(JobPosting jp: jps) {
				matches = Matching.getAllCvMatches(jp, true);
				assertFalse(matches.containsValue(0));
				assertFalse(matches.containsValue(101));
				assertTrue(matches.size() <= cvd.getCvs().size());
//				System.out.println("Matches for job " + jp.getLabel());
//				System.out.println(matches);
//				System.out.println();
				for(Application app: jp.getApplications()) {
					System.out.println(app.getPersonURI());
					System.out.println(app.getJobURI());
					System.out.println(app);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	@Order(7)
	@DisplayName("Career path test")
	@Disabled("Disabled")
	void careerPathTest() throws Exception {
		careerPathTestData data = new careerPathTestData();
		List<JobPosting> jps = data.getJobPostings();
		List<CV> cvs = data.getCvs();
		List<Person> ps = data.getProfiles();
		System.out.println();
		System.out.println("====================================Career path test====================================");
		for(JobPosting job : jps) {
			try {
				job.Save();
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
		for(Person p : ps) {
			try {
				p.Save();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(CareerPath.getJobCareerPathStringOutput("60", true));
//		System.out.println("\n\n");
//		HashMap<String, LinkedList<JobPosting>> results = null;
//		for(CV cv : cvs) {
//			results = CV.getJobCareerPath(cv.getURI());
//			
//			System.out.println(results);
//			graphOutputCareerPath.graphOutput(results);
//			
//			System.out.println("==================================");
//		}
		
		
		
		
		
		
		
	}
	
	@Test
	@Order(8)
	@DisplayName("Testing Delete Methods")
	@Disabled("Disabled")
	void deleteTest() {
//		CV.deleteObject("cv:id1");
//		JobPosting.deleteObject("saro:Job1");
	}
	
	@Test
	@Order(9)
	@DisplayName("Testing get hiring orgs")
	@Disabled("Disabled")
	void getHiringOrgs() {
		System.out.println("======================================================================================");
		List<String> hiringOrgs;
		try {
			hiringOrgs = JobPosting.getAllHiringOrganizations();
			for(String hiringOrg : hiringOrgs) {
				System.out.println(hiringOrg);
			}
			System.out.println();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	@Order(10)
	@DisplayName("Getting different data from database")
	@Disabled("Disabled")
	void getTests() {
		System.out.println("Person Get tests");
		List<Person> profiles;
		try {
			profiles = Person.getPersons();
			for(Person p : profiles) {
				System.out.println(p.getLabel());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	@Order(11)
	@DisplayName("Accomplishment test")
	@Disabled("")
	void accomplishmentTest() {
		
	}
	
}
