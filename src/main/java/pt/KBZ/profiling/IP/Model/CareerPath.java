package IP.Model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import IP.Matching;

public class CareerPath {
	
	public static String getJobCareerPathStringOutput(String personURI, boolean internal) throws Exception {
		String results = "";
		
		Person person = Person.getPerson(personURI);
    	CV cv = CV.getCV(person.getCVURI());
    	JobPosting currentJob;
    	if(person.getCurrentJobURI() != null)
    		currentJob = JobPosting.getJobPosting(person.getCurrentJobURI());
    	else if(cv.getCurrentJob() != null)
    		currentJob = JobPosting.getJobPosting(cv.getCurrentJob());
    	else throw new NoCurrentJobException("Selected Person: " + person.getURI() + " does not have a current Job");
    	HashMap<String, Integer> JobScores;
    	if(internal) {
    		String company = currentJob.getHiringOrg();
    	    JobScores = Matching.getCompanyScores(cv, company);
    	}
    	else {
    		JobScores = Matching.getAllJobScoresByCV(cv);
    	}
    	
    	results = processData(JobScores, currentJob, personURI);
		
		return results;
	}
	
	private static String processData(HashMap<String, Integer> jobScores, JobPosting currentJob, String personURI) throws Exception {
		String results = "";
		List<Skill> missingSkills = null;
		for(Map.Entry<String, Integer> jobScore : jobScores.entrySet()) {
//			if(jobScore.getKey().equals(currentJob.getURI())) {
//				continue;
//			}
			JobPosting curJob = JobPosting.getJobPosting(jobScore.getKey());
			
			
			List<JobPosting> precedentJobs = getPrecedentJobs(jobScores, curJob);
			//Need to change output to maybe calculate the difference between the two jobs and not the score of the latter job
			for(JobPosting jobPrecedent : precedentJobs) {
				missingSkills = CV.getSkillRecomendations(personURI, curJob.getURI());
	
	           	results += "{'from': '" + jobPrecedent.getJobDescription() +
	           			"', 'to': '" + curJob.getJobDescription() + "', 'value': " + jobScores.get(curJob.getURI()) + ", 'missing_skills': [";
	           	for(Skill skill : missingSkills) {
	           		results += "{'" + skill.getLabel() + "'}";
	           	}
	           	results +=  "]},\n";
	        }
	    }
		results = results.strip();
		if(results.endsWith(",")) {
			results = results.substring(0, results.length()-1);
		}
		return results;
	}

	public static HashMap<String, LinkedList<JobPosting>> getJobCareerPath(String personURI, boolean internal) throws Exception{
    	
    	//get CV, then get JobPosting with curJob, then get hiring org from JobPosting so the company name doesn't have to be an input string
    	Person person = Person.getPerson(personURI);
    	CV cv = CV.getCV(person.getCVURI());
    	JobPosting currentJob;
    	if(person.getCurrentJobURI() != null)
    		currentJob = JobPosting.getJobPosting(person.getCurrentJobURI());
    	else if(cv.getCurrentJob() != null)
    		currentJob = JobPosting.getJobPosting(cv.getCurrentJob());
    	else throw new NoCurrentJobException("Selected Person: " + person.getURI() + " does not have a current Job");
    	HashMap<String, Integer> JobScores;
    	if(internal) {
    		String company = currentJob.getHiringOrg();
    	    JobScores = Matching.getCompanyScores(cv, company);
    	}
    	else {
    		JobScores = Matching.getAllJobScoresByCV(cv);
    	}
 
	    return processDataForGraph(JobScores, currentJob);
	}

	
	private static HashMap<String, LinkedList<JobPosting>> processDataForGraph(HashMap<String, Integer> jobScores, JobPosting startingJob) throws Exception{
    	HashMap<String, LinkedList<JobPosting>> careerPaths = new HashMap<String, LinkedList<JobPosting>>();
    	
		for(Map.Entry<String, Integer> jobScore : jobScores.entrySet()) {
	    	careerPaths.put(jobScore.getKey(), new LinkedList<JobPosting>());
	    }
	    for (Map.Entry<String, Integer> jobScore : jobScores.entrySet()) { 
//	        System.out.println("Key = " + jobScore.getKey() +  
//	                      ", Value = " + jobScore.getValue()); 
	    	
			JobPosting curJob = JobPosting.getJobPosting(jobScore.getKey());
			//Need to check if the job is in the workhistory of the next one in line for the progression
	          List<JobPosting> precedentJobs = getPrecedentJobs(jobScores, curJob);
	            
	            //for each job precendent get the job list and add this job(jobScore associated job) to that list to create an adjacency
	        for(JobPosting jobPrecedent : precedentJobs) {
	           	LinkedList<JobPosting> tmp = careerPaths.get(jobPrecedent.getURI());
	           	tmp.add(curJob);
	           	careerPaths.put(jobPrecedent.getURI(), tmp);
	        }
			
           
        } 
		return careerPaths;
	}
	
	private static LinkedList<JobPosting> getPrecedentJobs(HashMap<String, Integer> toCompare, JobPosting job) {
    	LinkedList<JobPosting> precedences = new LinkedList<JobPosting>();
    	
    	//For each job from the same company (in the toCompare structure)
    	for(Map.Entry<String, Integer> nextEntry : toCompare.entrySet()) {
    		JobPosting nextJob;
			try {
				nextJob = JobPosting.getJobPosting(nextEntry.getKey());
				//Check if the JobPosting job requires the job being checked from the company as a past work experience, if so add it to the output list
	    		//so it can be added to the adjacency list
	    		for(WorkHistory wh : job.getworkExperienceReq()) {
	    			if(wh.getJobReference().equals(nextJob.getURI())) {
	    				precedences.add(nextJob);
	    			}
	    		}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
    		
    		
    		
    		
    	}
    	return precedences;
    }
}
