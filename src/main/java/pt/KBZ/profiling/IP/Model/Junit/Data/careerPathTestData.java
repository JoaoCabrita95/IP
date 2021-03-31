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
import IP.Model.Person;
import IP.Model.Skill;
import IP.Model.SkillJobReq;
import IP.Model.WorkHistory;

public class careerPathTestData {
	
	List<CV> cvs = new ArrayList<CV>();
	List<JobPosting> jps = new ArrayList<JobPosting>();
	List<Person> ps = new ArrayList<Person>();
	
	public careerPathTestData() {
		
		List<WorkHistory> twh = new LinkedList<WorkHistory>();
		twh.add(new WorkHistory(":wh50", "RoleName50", "20/02/2000", "30/05/2004", "Employer50", "4 years, 3 months", "RoleDescription50", null, null, "false", "jobType50", "job50"));
		
		List<Education> te = new LinkedList<Education>();
		te.add(new Education(":ed50", "Major50", null, "Bachelors", "01/01/1997", "01/01/2000", "Educational Organization50", "Description of educational course"));
		
		List<Course> tc = new LinkedList<Course>();
//		tc.add(new Course());
		
		List<CVSkillRef> ts = new LinkedList<CVSkillRef>();
//		ts.add(new Skill());
		
		List<SkillJobReq> tjr = new LinkedList<SkillJobReq>();
		
		List<Application> ta = new LinkedList<Application>();
//		ta.add(new Application());
		
		//Create first job
		JobPosting currentJob = new JobPosting("Job60", "first Job", "Starting node of the career path graph", "Intern programmer", "6 months temp", "IT", "Programmer",
				"Business inc", "Business inc", "Business inc address", "01/10/2016", "ongoing", null, "1200", "Euro", null,
				tjr, tc, new ArrayList<WorkHistory>(), new ArrayList<Education>());
		
		//Create first job work history for cv60 (first user test)
		twh.add(new WorkHistory("wh98", currentJob.getOccupation(), currentJob.getStartDate(), currentJob.getEndDate(),
				currentJob.getHiringOrg(), null, currentJob.getJobDescription(), null, null, "true", null, currentJob.getURI()));
		
		//Create firs user test, cv60
		cvs.add(new CV("id60", "CV career path advisor test", "Testing a CV career path",
				"Complex CV test Title", "60", "A CV with Work History, Education, Courses and Job applications", "IT", "no extra info", "1200", "Euro",
				twh , te, tc, ts, ta));
		
		//Create profile for the first user cv
		ps.add(new Person("60", "Career path Person name", "Career path Person surname", "Career path Test Person", "male",
				"918375711", "CareerTest@KnowledgeBiz.pt", "CareerPath1.org", "Portuguese", null, true, ":id60", "IT", null,
				"Intern programmer", null, null, null, null, null));
		
		//Create first job work history for cv61, no longer the current job for this cv
		twh = new LinkedList<WorkHistory>();
		twh.add(new WorkHistory("wh99", currentJob.getOccupation(),	"01/10/2014", "01/01/2017",
				currentJob.getHiringOrg(), "2 years, 3 months", currentJob.getJobDescription(), null, null, "false", null, currentJob.getURI()));
		
		//Create second cv, cv61, more advanced career
		cvs.add(new CV("id61", "CV career path advisor test 2", "Testing a CV career path 2",
				"Complex CV test Title 2", "61", "A CV with Work History, Education, Courses and Job applications 2", "IT", "no extra info", "1400", "Euro",
				twh , te, tc, ts, ta));
		
		//Create profile for second user with cv61
		ps.add(new Person("61", "Career path Person name 2", "Career path Person surname 2", "Career path Test Person 2", "female",
				"918375521", "CareerTest2@KnowledgeBiz.pt", "CareerPath2.org", "Portuguese", null, true, ":id61", "IT", null,
				"Junior programmer", null, null, null, null, null));
		
		//Set the current job of user 60 as the first job
		cvs.get(0).setCurrentJob(currentJob.getURI());
		ps.get(0).setCurrentJobURI(currentJob.getURI());
		
		//current job added to the list of all job postings in the test
		jps.add(currentJob);
		
		//first fork
		twh = new LinkedList<WorkHistory>();
		twh.add(new WorkHistory("wh100", currentJob.getOccupation(), null, null,
				currentJob.getHiringOrg(), null, currentJob.getJobDescription(), null, null, "false", null, currentJob.getURI()));
		JobPosting temp = new JobPosting("Job61", "Job fork 1-1", "Career path advisor test job 1","Junior programmer" ,null , "IT", "Programmer", "Business inc", "Business inc",
				"Business inc address", null, null, null, "1400", "Euro", null, null, null, twh, null);
		jps.add(temp);
		
		cvs.get(1).setCurrentJob(temp.getURI());
		ps.get(1).setCurrentJobURI(temp.getURI());
		
		//first fork
		twh = new LinkedList<WorkHistory>();
		twh.add(new WorkHistory("wh101", currentJob.getOccupation(), null, null,
				currentJob.getHiringOrg(), null, currentJob.getJobDescription(), null, null, "false", null, currentJob.getURI()));
		temp = new JobPosting("Job62", "Job fork 1-2", "Career path advisor test job 2", "Back-End Developer", null, "IT", "Programmer", "Business inc", "Business inc",
				"Business inc address", null, null, null, "1400", "Euro", null, null, null, twh, null);
		jps.add(temp);
		
		//second fork-1
		twh = new LinkedList<WorkHistory>();
		twh.add(new WorkHistory("wh102", jps.get(1).getOccupation(), null, null,
				jps.get(1).getHiringOrg(), null, jps.get(1).getJobDescription(), null, null, "false", null, jps.get(1).getURI()));
		temp = new JobPosting("Job63", "Job fork 2-1-1", "Career path advisor test job 3", "Senior Developer", null, "IT", "Programmer", "Business inc", "Business inc",
				"Business inc address", null, null, null, "1400", "Euro", null, null, null, twh, null);
		jps.add(temp);
		
		cvs.get(1).addWorkHistory(twh.get(0));
		
		//second fork-1
		twh = new LinkedList<WorkHistory>();
		twh.add(new WorkHistory("wh103", jps.get(1).getOccupation(), null, null,
				jps.get(1).getHiringOrg(), null, jps.get(1).getJobDescription(), null, null, "false", null, jps.get(1).getURI()));
		temp = new JobPosting("Job64", "Job fork 2-1-2", "Career path advisor test job 4", "Front-End Developer", null, "IT", "Programmer", "Business inc", "Business inc",
				"Business inc address", null, null, null, "1400", "Euro", null, null, null, twh, null);
		jps.add(temp);
		
		//second fork-2
		twh = new LinkedList<WorkHistory>();
		twh.add(new WorkHistory("wh104", jps.get(2).getOccupation(), null, null,
				jps.get(2).getHiringOrg(), null, jps.get(2).getJobDescription(), null, null, "false", null, jps.get(2).getURI()));
		temp = new JobPosting("Job65", "Job fork 2-2-1", "Career path advisor test job 5", "Server Service Expert", null, "IT", "Programmer", "Business inc", "Business inc",
				"Business inc address", null, null, null, "1400", "Euro", null, null, null, twh, null);
		jps.add(temp);
		
		//third fork-1
		twh = new LinkedList<WorkHistory>();
		twh.add(new WorkHistory("wh105", jps.get(3).getOccupation(), null, null,
				jps.get(3).getHiringOrg(), null, jps.get(3).getJobDescription(), null, null, "false", null, jps.get(3).getURI()));
		temp = new JobPosting("Job66", "Job fork 3-1-1", "Career path advisor test job 6", "Lead Developer", null, "IT", "Programmer", "Business inc", "Business inc",
				"Business inc address", null, null, null, "1400", "Euro", null, null, null, twh, null);
		jps.add(temp);
		
		//third fork-1/2
		twh = new LinkedList<WorkHistory>();
		twh.add(new WorkHistory("wh106", jps.get(4).getOccupation(), null, null,
				jps.get(4).getHiringOrg(), null, jps.get(4).getJobDescription(), null, null, "false", null, jps.get(4).getURI()));
		temp = new JobPosting("Job67", "Job fork 3-1-2", "Career path advisor test job 7", "Full-Stack Developer", null, "IT", "Programmer", "Business inc", "Business inc",
				"Business inc address", null, null, null, "1400", "Euro", null, null, null, twh, null);
		jps.add(temp);
				
		//third fork-2
		twh = new LinkedList<WorkHistory>();
		twh.add(new WorkHistory("wh107", jps.get(5).getOccupation(), null, null,
				jps.get(5).getHiringOrg(), null, jps.get(5).getJobDescription(), null, null, "false", null, jps.get(5).getURI()));
		temp = new JobPosting("Job68", "Job fork 3-2-1", "Career path advisor test job 8", "Software Architect", null, "IT", "Programmer", "Business inc", "Business inc",
				"Business inc address", null, null, null, "1400", "Euro", null, null, null, twh, null);
		jps.add(temp);
		
		//third fork-2
		twh = new LinkedList<WorkHistory>();
		twh.add(new WorkHistory("wh109", jps.get(7).getOccupation(), null, null,
				jps.get(7).getHiringOrg(), null, jps.get(7).getJobDescription(), null, null, "false", null, jps.get(7).getURI()));
		twh.add(new WorkHistory("wh110", jps.get(8).getOccupation(), null, null,
				jps.get(8).getHiringOrg(), null, jps.get(8).getJobDescription(), null, null, "false", null, jps.get(8).getURI()));
		temp = new JobPosting("Job69", "Job fork 3-2-2", "Career path advisor test job 9", "Product Manager", null, "IT", "Manager", "Business inc", "Business inc",
				"Business inc address", null, null, null, "1400", "Euro", null, null, null, twh, null);
		jps.add(temp);
				
		//fourth fork-1/2
		twh = new LinkedList<WorkHistory>();
		twh.add(new WorkHistory("wh111", temp.getOccupation(), null, null,
				temp.getHiringOrg(), null, temp.getJobDescription(), null, null, "false", null, temp.getURI()));
		temp = new JobPosting("Job70", "Job fork 4-2-1", "Career path advisor test job 10", "Senior advisory", null, "IT", "Programmer", "Business inc", "Business inc",
				"Business inc address", null, null, null, "1400", "Euro", null, null, null, twh, null);
		jps.add(temp);
		
		Skill skill = new Skill("skill_60", "Final skill for end of career", null, null, null, "cv:Skill");
		SkillJobReq skillRef = new SkillJobReq(skill, "High", "advanced");
		Skill skill2 = new Skill("skill_61", "Second skill for end of career", null, null, null, "cv:Skill");
		SkillJobReq skillRef2 = new SkillJobReq(skill2, "High", "advanced");
		
		//fourth fork-2
		twh = new LinkedList<WorkHistory>();
		twh.add(new WorkHistory("wh112", temp.getOccupation(), null, null,
				temp.getHiringOrg(), null, temp.getJobDescription(), null, null, "false", null, temp.getURI()));
		temp = new JobPosting("Job71", "Job fork 4-2-2", "Career path advisor test job 11", "End of career", null, "IT", "Programmer", "Business inc", "Business inc",
				"Business inc address", null, null, null, "1400", "Euro", null, null, null, twh, null);
		temp.addSkillRef(skillRef);
		temp.addSkillRef(skillRef2);
		jps.add(temp);
				
	}
		
	public List<CV> getCvs(){
		return cvs;
	}
	
	public List<Person> getProfiles(){
		return ps;
	}
	
	public List<JobPosting> getJobPostings(){
		return jps;
	}
	
}
