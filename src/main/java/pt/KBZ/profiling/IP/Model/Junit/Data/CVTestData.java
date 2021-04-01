package IP.Model.Junit.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import IP.Model.Application;
import IP.Model.CV;
import IP.Model.CVSkillRef;
import IP.Model.Course;
import IP.Model.Education;
import IP.Model.JobPosting;
import IP.Model.Skill;
import IP.Model.WorkHistory;

public class CVTestData {

	List<CV> cvs = new ArrayList<CV>();
	
	public CVTestData(){
		
		cvs = new ArrayList<CV>();
		cvs.add(new CV("id1", "Test CV 1", "Comment 1", "CV Title 1", ":1", "Description 1", "Sector 1", "Optional info 1",
				"600", "Euro", null, null, null, null, new Application(":app1", ":1", ":Job1", "1000", "01/01/21", "Euro")));
		cvs.add(new CV("id2", "Test CV 2", "Comment 2", "CV Title 2", ":2", "Description 2", "Sector 1", "Optional info 2",
				"700", "Euro", null, null, null, null, new Application(":app2", ":2", ":Job2", "750", "01/01/21", "Euro")));
		cvs.add(new CV("id3", "Test CV 3", "Comment 3", "CV Title 3", ":3", "Description 3", "Sector 2", "Optional info 3",
				"800", "Euro", null, null, null, null, new Application(":app3", ":3", ":Job3", "600", "01/01/21", "Euro")));
		cvs.add(new CV("id4", "Test CV 4", "Comment 4", "CV Title 4", ":4", "Description 4", "Sector 2", "Optional info 4",
				"900", "Euro", null, null, null, null, new Application(":app4", ":4", ":Job4", "800", "01/01/21", "Euro")));
		cvs.add(new CV("id5", "Test CV 5", "Comment 5", "CV Title 5", ":5", "Description 5", "Sector 2", "Optional info 5",
				"1000", "Euro", null, null, null, null, new Application(":app5", ":5", ":Job5", "1200", "01/01/21", "Euro")));
		cvs.add(new CV("id6", "Test CV 6", "Comment 6", "CV Title 6", ":6", "Description 6", "Sector 2", "Optional info 6",
				"1200", "Euro", null, null, null, null, new Application(":app6", ":6", ":Job6", "1300", "01/01/21", "Euro")));
		cvs.add(new CV("id7", "Test CV 7", "Comment 7", "CV Title 7", ":7", "Description 7", "Sector 3", "Optional info 7",
				"1250", "USD", null, null, null, null, new Application(":app7", ":7", ":Job7", "1050", "01/01/21", "Euro")));
		cvs.add(new CV("id8", "Test CV 8", "Comment 8", "CV Title 8", ":8", "Description 8", "Sector 3", "Optional info 8",
				"1300", "USD", null, null, null, null, new Application(":app8", ":8", ":Job8", "1250", "01/01/21", "Euro")));
		cvs.add(new CV("id9", "Test CV 9", "Comment 9", "CV Title 9", ":9", "Description 9", "Sector 3", "Optional info 9",
				"1000", "Euro", null, null, null, null, new Application(":app9", ":9", ":Job9", "1678", "01/01/21", "Euro")));
		cvs.add(new CV("id10", "Test CV 10", "Comment 10", "CV Title 10", ":10", "Description 10", "Sector 2", "Optional info 10",
				"2000", "USD", null, null, null, null, new Application(":app10", ":10", ":Job10", "1467", "01/01/21", "Euro")));
		cvs.add(new CV("id11", "Test CV 11", "Comment 11", "CV Title 11", ":11", "Description 11", "Sector 4", "Optional info 11",
				"1500", "Euro", null, null, null, null, new Application(":app11", ":11", ":Job11", "1234", "01/01/21", "Euro")));
		cvs.add(new CV("id12", "Test CV 12", "Comment 12", "CV Title 12", ":12", "Description 12", "Sector 5", "Optional info 12",
				"1100", "Euro", null, null, null, null, new Application(":app12", ":12", ":Job12", "879", "01/01/21", "Euro")));
		cvs.add(new CV("id13", "Test CV 13", "Comment 13", "CV Title 13", ":13", "Description 13", "Sector 3", "Optional info 13",
				"1600", "USD", null, null, null, null, new Application(":app13", ":13", ":Job1", "577", "01/01/21", "Euro")));
		cvs.add(new CV("id14", "Test CV 14", "Comment 14", "CV Title 14", ":14", "Description 14", "Sector 5", "Optional info 14",
				"1200", "Euro", null, null, null, null, new Application(":app14", ":14", ":Job2", "3000", "01/01/21", "Euro")));
		cvs.add(new CV("id15", "Example CV x", "No comment", "Albertos CV", ":15", "Things Alberto has done", "Something new", "Is a fast learner",
				"1000", "Euros", null, null, null, null, new Application(":app98173918", ":15", ":Job2", "2508", "01/01/21", "Euro") ));
		cvs.add(new CV("id16", "Example CV y", "No comment", "Joaquims CV", ":16", "Interests and hobbies", "Something fresh", "Has a very good work ethic",
				"780", "Canadian Dollar", null, null, null, null, new Application(":app17623814", ":16", ":Job4", "5000", "01/01/21", "Euro") ));
		cvs.add(new CV("id17", "Example CV z", "Some comment", "Guilherme CV", ":17", "Etc", "Something challenging", "Very proactive",
				"900", "Euro", null, null, null, null, new Application(":app57168721", ":17", "Job1", "4355", "01/01/21", "Euro")));

		cvs.add(new CV("id22", "CV for Data Scientists Application", "Comment 22", "CV Title 22", ":22", "Description 22", "IT", "Optional info 22",
				"1200", "Euro", null, null, null, null, new Application(":app22", ":22", ":Job2", "3000", "01/01/21", "Euro")));

		cvs.add(new CV("id25", "Student CV", "Comment", "CV Title 25", ":25", "Description 25", "IT", "Optional info 25",
				"1200", "Euro", null, null, null, null, new Application(":app25", ":25", ":Job2", "3000", "01/01/21", "Euro")));

		cvs.add(new CV("id29", "Software Developer CV", "Software Developer CV", "Software Developer CV", ":29", "I am an experinced software Developer.", "IT",
				"Optional info 14","1200", "Euro", null, null, null, null, new Application(":app29", ":29", ":Job2", "3000", "01/01/21", "Euro")));

		cvs.add(new CV("id30", "CV V", "Comment", "CV Title 30", ":30", "Description 30", "IT", "Optional info 30",
				"1200", "Euro", null, null, null, null, new Application(":app30", ":30", ":Job2", "3000", "01/01/21", "Euro")));
		
		List<WorkHistory> twh = new LinkedList<WorkHistory>();
		twh.add(new WorkHistory(":wh51", "RoleName51", "20/02/2000", "30/05/2004", "Employer51", "4 years, 3 months", "RoleDescription51", null, null, "false", "jobType51", ":job50"));
		
		List<Education> te = new LinkedList<Education>();
		te.add(new Education(":ed51", "Major51", null, "Bachelors", "01/01/1997", "01/01/2000", "Educational Organization51", "Description of educational course"));
		
		List<Course> tc = new LinkedList<Course>();
		tc.add(new Course());
		
//		List<CVSkillRef> ts = new LinkedList<CVSkillRef>();
//		ts.add(new CVSkillRef());
//		
		List<Application> ta = new LinkedList<Application>();
//		ta.add(new Application());
		
		cvs.add(new CV("id50", "Complex CV test", "Testing a CV with all possible elements",
				"Complex CV test Title", ":50", "A CV with Work History, Education, Courses and Job applications", "HR", "no extra info", "1200", "Euro",
				twh , te, tc, null, ta));

		addCompetencesToCVs();
	}
	
	public List<CV> getCvs(){
		return cvs;
	}
	
	public CV getCvByURI(String URI) {
		for(CV cv: cvs) {
			if(cv.getURI().equals(URI))
				return cv;
		}
		return null;
	}
	
	public CV getCvByLabel(String label) {
		for(CV cv: cvs) {
			if(cv.getLabel().equals(label))
				return cv;
		}
		return null;
	}
	
	public CV getCvByPerson(String PURI) {
		for(CV cv: cvs) {
			if(cv.getPersonURI().equals(PURI))
				return cv;
		}
		return null;
	}
	
	public void addCompetencesToCVs() {
		SkillTestData skillsData = new SkillTestData();
		for(CV cv: cvs) {
			List<Skill> toInsert = skillsData.getSkillsForCVs(cv.getID());
			for(Skill skill : toInsert) {
				cv.addSkillRef(skill, "01/12/99", "basic", "01/09/99", "01/11/99");
			}
			
		}
	}
	
	public void addApplicationsToCVs() {
		ApplicationTestData apps = new ApplicationTestData();
	}
	
	public void addWorkHistoryToCVs() {
		WorkHistoryTestData wh = new WorkHistoryTestData();
	}
	
	public void addEducationToCVs() {
		EducationTestData ed = new EducationTestData();
	}
	
	public void addCoursesToCVs() {
		CoursesTestData course = new CoursesTestData();
	}
	
}
