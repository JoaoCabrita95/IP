package IP.Model.Junit.Data;

import java.util.ArrayList;
import java.util.List;

import IP.Model.CV;
import IP.Model.Course;
import IP.Model.Education;
import IP.Model.JobPosting;
import IP.Model.Skill;
import IP.Model.WorkHistory;

public class JobPostingTestData {

	List<JobPosting> JPs = new ArrayList<JobPosting>();	
	
	
	public JobPostingTestData(){
		JPs.add(new JobPosting("Job1", "Solutions Architect", "no comment", "Solutions Architect Description", "full_time",
				"Sector 1", "Job Position Name 1", "Listing Organization 1", "Hiring Organization 1", "Location 1", "2020-03-11", "2022-03-11",
				"senior_level", null, null, "27", null, null, null, null));
		
		JobPosting jp = JPs.get(0);
		jp.setCity("city 1");
		jp.setState("State 1");
		jp.setCountry("Country 1");
		jp.setSpecialization("Project Manager");
		JPs.set(0, jp);
		
		JPs.add(new JobPosting("Job2", "Software Engineer", "JobPosting Comment 2", "Development of Software products", "contract",
				"IT", "Job Position Name 2", "Listing Organization 1", "Hiring Organization 2", "Location 2", "2020-02-01", "2021-02-01",
				"senior_level", null, null, "27", null, null, null, null));
		JPs.add(new JobPosting("Job3", "Data Scientist", "JobPosting Comment 3", "Job Description 3", "contract",
				"Sector 1", "Job Position Name 3", "Listing Organization 2", "Hiring Organization 2", "Location 3", "2020-01-01", "2022-01-01",
				"entry_level", null, null, "27", null, null, null, null));
		JPs.add(new JobPosting("Job4", "Frontend Developer", "JobPosting Comment 4", "Job Description 4", "temporary",
				"Sector 2", "Job Position Name 4", "Listing Organization 2", "Hiring Organization 1", "Location 1", "2020-01-01", "2022-01-01",
				"associate", null, null, "27", null, null, null, null));
		JPs.add(new JobPosting("Job5", "Data Scientist", "JobPosting Comment 5", "Job Description 5", "volunteer",
				"Sector 2", "Blockchain Engineer", "Listing Organization 2", "Hiring Organization 2", "Location 2", "2020-01-01", "2022-01-01",
				"senior_level", null, null, "27", null, null, null, null));
		JPs.add(new JobPosting("Job6", "Programmer", "JobPosting Comment 6", "Job Description 6", "internship",
				"Sector 2", "Job Position Name 6", "Listing Organization 1", "Hiring Organization 2", "Location 3", "2020-01-01", "2022-01-01",
				"director", null, null, "27", null, null, null, null));
		JPs.add(new JobPosting("Job7", "Developer", "JobPosting Comment 7", "Job Description 7", "contract",
				"Sector 2", "Job Position Name 7", "Listing Organization 3", "Hiring Organization 3", "Location 4", "2020-01-01", "2022-01-01",
				"executive", null, null, "27", null, null, null, null));
		JPs.add(new JobPosting("Job8", "PHP Senior Developer", "JobPosting Comment 8", "Job Description 8", "part_time",
				"Sector 3", "Job Position Name 8", "Listing Organization 4", "Hiring Organization 3", "Location 4", "2020-01-01", "2022-01-01",
				"expert", null, null, "27", null, null, null, null));
		JPs.add(new JobPosting("Job9", "Backend developer", "JobPosting Comment 9", "Job Description 9", "contract",
				"Sector 4", "Job Position Name 9", "Listing Organization 3", "Hiring Organization 3", "Location 4", "2020-01-01", "2022-01-01",
				"senior_level", null, null, "27", null, null, null, null));
		JPs.add(new JobPosting("Job10", "Test JobPosting 10", "JobPosting Comment 10", "Job Description 10", "contract",
				"Sector 4", "Job Position Name 10", "Listing Organization 4", "Hiring Organization 3", "Location 5", "2020-01-01", "2022-01-01",
				"internership", null, null, "27", null, null, null, null));
		JPs.add(new JobPosting("Job11", "Test JobPosting 11", "JobPosting Comment 11", "Job Description 11", "contract",
				"Sector 5", "Job Position Name 11", "Listing Organization 4", "Hiring Organization 4", "Location 6", "2020-01-01", "2022-01-01",
				"senior_level", null, null, "27", null, null, null, null));
		JPs.add(new JobPosting("Job12", "Test JobPosting 12", "JobPosting Comment 12", "Job Description 12", "contract",
				"Sector 5", "Job Position Name 12", "Listing Organization 1", "Hiring Organization 4", "Location 6", "2020-01-01", "2022-01-01",
				"senior_level", null, null, "27", null, null, null, null));
		JPs.add(new JobPosting("Job18763218", "Data scientist", "Job Post for a university in Colorado", "Need someone to do xyz", "contract", "Data Science",
				"Science Team Leader", "Merquer inc", "USCL", "City Z in Colorado", "01-01-20", "31-05-20", "senior_level", null, null, "27",null,null,null, null));
		JPs.add(new JobPosting("Job12987681", "AVG Construction Team Coordinator", "Manage a team of workers building AVG automatons for factory work",
				"Manage schedules, material orders and work posts for the different workers working in your section", "contract",
				"Automation","Team Coordinator/Manager", "Merquer inc", "Imgursa", "Guia", "01-02-20", "01-02-21", "senior_level", null, null,"27",null,null,null, null));
		JPs.add(new JobPosting("Job198273817", "Salary manager for Company X", "Company X requires a salary manager urgently",
				"The job requires that the candidate be able to manage employees salaries, making sure there is a fair distribuition of company resources with enough overhead for expansion",
				"contract", "Management", "Senior Accounting Expert", "Jason co", "Imgursa", "Guia", "01-09-20", "01-03-21", "senior_level", null, null, "27",null,null,null, null));
		
		//Null values test
		ArrayList<Skill> skills = new ArrayList<>();
		skills.add(new Skill(null, "null", "null", null, null, "saro:Skill"));
//		JPs.add(new JobPosting(null, "sdfsdfsdf", null, "sdfsdfsdfsdf", "4", null, null, null, null, "sdfsdfsdfs", null, null,
//				null, null, null, null, null, null, null, null));
		
		addCompetenceReq();
	}
	
	public List<JobPosting> getJobPostings(){
		return JPs;
	}
	
	public JobPosting getJobPostingByURI(String URI) {
		for(JobPosting jp: JPs) {
			if(jp.getURI().equals(URI))
				return jp;
		}
		return null;
	}
	
	public JobPosting getJobPostingByLabel(String label) {
		for(JobPosting jp: JPs) {
			if(jp.getLabel().equals(label))
				return jp;
		}
		return null;
	}
	
	public void addCompetenceReq() {
		SkillTestData skillsData = new SkillTestData();
		for(JobPosting jp: JPs) {
			for(Skill skill : skillsData.getSkillsForJobPostings(jp.getID()))
			jp.addSkillRef(skill, null, null);
		}
	}
	
}
