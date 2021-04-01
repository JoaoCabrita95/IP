package IP.Model.Junit.Data;

import java.util.LinkedList;
import java.util.List;

import IP.Model.JobPosting;

public class CareerPathTestData {

	public static List<JobPosting> getMedicalDoctorJobs(){

		List<JobPosting> jobs = new LinkedList<JobPosting>();
		
		JobPosting job = new JobPosting();
		job.setLabel("Medical Residency");
		job.setCity("Almada");
		job.setState("Setubal");
		job.setCountry("Portugal");
		job.setSector("Health Care");
		job.setHiringOrg("Hospital Garcia de Orta");
		job.setListingOrg("Hospital Garcia de Orta");
		job.setjobLocation("Av. Torrado da Silva, 2805-267 Almada");
		job.setcreator_id("1");
		job.setStartDate("");
		job.setEndDate("");
		job.setOccupation("Medical Residency");
		job.setExpectedSalary("1000");
		job.setseniorityLevel("Starter Job");
		job.setURI(":2000");
		
		jobs.add(job);
		
		job = new JobPosting();
		
		
		jobs.add(job);
		
		job = new JobPosting();
		
		
		jobs.add(job);
		
		job = new JobPosting();
		
		
		jobs.add(job);
		
		job = new JobPosting();
		
		
		jobs.add(job);
		
		job = new JobPosting();
		
		
		jobs.add(job);
		
		job = new JobPosting();
		
		
		jobs.add(job);
		
		return jobs;
	}
	
	public static List<JobPosting> getNurseJobs(){
		List<JobPosting> jobs = new LinkedList<JobPosting>();
		
		return null;
	}
	
	public static List<JobPosting> getPharmaceuticalJobs(){
		List<JobPosting> jobs = new LinkedList<JobPosting>();
		
		return null;
	}
	
}
