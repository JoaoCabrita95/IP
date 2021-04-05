package IP.Model.Junit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import IP.Model.CV;
import IP.Model.Skill;
import IP.Model.SparqlEndPoint;
import IP.Model.Junit.Data.CVTestData;
import IP.Model.Junit.Data.SkillTestData;
import IP.config.Configuration;

import org.junit.jupiter.api.Order;

@TestMethodOrder(value = OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
class CVModelTest {
	
	Configuration cnf = new Configuration();
	CVTestData testDatabase = new CVTestData();
	CV ServerCv = null;
	CV TestDataCV = null;
	List<CV> baseTestCvs = testDatabase.getCvs();
	List<CV> fromServerCvs;
	
	
	@BeforeAll
	void init() {
		cnf.LoadConfiguration(true);
		//Reset database
		SparqlEndPoint.deleteTriple("?s ?o ?p", "?s ?o ?p");
		//Make sure database has prefixes
		SparqlEndPoint.insertTTLFile("G:\\Work\\KnowledgeBiz\\profiling_model\\src\\prefixes.ttl");
	}

	@Test
	@Order(1)
	@DisplayName("Saving CVs to Server from Test CV data")
	void testSave() throws Exception {
		for(CV cv: baseTestCvs) {
			cv.Save();
		}
	}
	
	@Test
	@Order(2)
	@DisplayName("Setting CV Competences and saving to server")
	void testSetCompetences() {
		try {
			fromServerCvs = CV.getCVs();
			List<Skill> skills = new SkillTestData().getSkills();
			for(CV serverCV: fromServerCvs) {
				Skill skill = skills.get(0);
				serverCV.addSkillRef(skill, null, null, null, null);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	@Order(3)
	@DisplayName("Recovering individual CV data by URI from Server Accurately")
	void testGetCV() {
		for(CV toCompare: baseTestCvs) {
			try {
				ServerCv = CV.getCV(toCompare.getURI());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			areCvsEqual(toCompare, ServerCv);
		}
		
	}

	@Test
	@Order(4)
	@DisplayName("Recovering all CVs from Server Accurately")
	void testGetCVs() {
		try {
			fromServerCvs = CV.getCVs();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Might not necessarily be true depending on the Limit of results on the CV query to the server
		assertEquals(fromServerCvs.size(), baseTestCvs.size());
		for(CV toCompare : fromServerCvs) {
			TestDataCV = testDatabase.getCvByURI(toCompare.getURI());
			assert(TestDataCV != null);
			areCvsEqual(toCompare, TestDataCV);
		}
	}

	@Test
	@Order(5)
	@Disabled("Not yet implemented")
	@DisplayName("Recovering all CVs by Sector from Server")
	void testGetCVsBySector() {
		//TODO:Develop the CV get by sector
	}

	@Test
	@Order(6)
	@Disabled("Not yet implemented")
	@DisplayName("Recovering all Cvs that contain specific Skills from Server")
	void testGetCVsBySkill() {
		
	}

	@Test
	@Order(7)
	@Disabled("Not yet implemented")
	@DisplayName("Recovering Cvs by Person from Server")
	void testGetCVbyPerson() {
		
	}
	
	@Test
	@Order(8)
	@DisplayName("Deleting CVs and verifying through gets")
	void deleteCVs() {
		for(CV toDelete: baseTestCvs) {
			try {
				CV.deleteObject(toDelete.getURI());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				TestDataCV = CV.getCV(toDelete.getURI());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			assertEquals(TestDataCV.getLabel(), null);
			
		}
	}
	
	void areCvsEqual(CV cv1, CV cv2) {
		//Can add more asserts to verify if every variable is being saved properly and matches the base test CVs
		assertAll("cv", 
				() -> assertEquals(cv1.getID(), cv2.getID()),
				() -> assertEquals(cv1.getPersonURI(), cv2.getPersonURI()),
				() -> assertEquals(cv1.getLabel(), cv2.getLabel()),
				() -> assertEquals(cv1.getComment(), cv2.getComment()),
				() -> assertEquals(cv1.getDescription(), cv2.getDescription()),
				() -> assertEquals(cv1.getOtherInfo(), cv2.getOtherInfo()),
				() -> assertEquals(cv1.getTargetSector(), cv2.getTargetSector()),
				() -> assertEquals(cv1.getTitle(), cv2.getTitle()),
				() -> assertEquals(cv1.getCourses(), cv2.getCourses()),
				() -> assertEquals(cv1.getEducation(), cv2.getEducation()),
				() -> assertEquals(cv1.getSkills(), cv2.getSkills()),
				() -> assertEquals(cv1.getWorkHistory(), cv2.getWorkHistory())
		);
	}

}
