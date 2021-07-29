package IP;

import IP.Model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Matching {

	private static HashMap<String, Integer> scores;
//	private static int weight;
	private static final int SKILL_COEFICIENT = 10;
	private static final int COURSE_COEFICIENT = 10;
	private static final int EDUCATION_COEFICIENT = 10;
	private static final int WORKHISTORY_COEFICIENT = 10;
	
	/**
	 * Uses a JobPosting object (job) to analyze how good of a match each CV object in the available Database, or that has applied for the job
	 * is compared to it.
	 * the calculation of each score depends on the JobPosting requirements and the CV qualification
	 * @param job  JobPosting type object that will be analyzed against every CV available in the Database
	 * @param applied if applied is true, the method will return CV scores for CVs that applied for the Job, if false, the method
	 * will return all database CVs scores related to the job
	 * @return A HashMap with the CV URIs as keys and the corresponding score as value 
	 * @throws Exception 
	 */
	public static HashMap<String, Integer> getAllCvMatches(JobPosting job, boolean applied) throws Exception{
		scores = new HashMap<String ,Integer>();
		int score = 0;
//		System.out.println("CALCULATING JOB MAX SCORE");
		int highScore = getJobMaxScore(job);
//		System.out.println("JOBPOSTING SCORE");
//		System.out.println(highScore);
		List<CV> cvs;
		List<Application> apps;
		//If we are looking at applied cvs only we get the cvs that applied for the job and use that list
		if(applied) {
			cvs = new ArrayList<CV>();
			apps = Application.getApplicationsByJob(job.getURI());
			for(Application app: apps) {
				cvs.add(CV.getCVbyPersonURI(app.getPersonURI()));
			}
		}
		//Otherwise we get all cvs and fill the list to use for the matching with all of them
		else {
			cvs = CV.getCVs();
		}
		
		
		for(CV cv : cvs) {
//			System.out.println(cv.getInfo());
			score = getScore(job, cv);
			if (score>highScore) 
				score = highScore;
//			System.out.println("CV SCORE");
//			System.out.println(score);
			if(score > 0) {
				if(highScore == 0)
					scores.put(cv.getURI(), 100);
				else
					scores.put(cv.getURI(), (score*100)/highScore);
			}
			else
				scores.put(cv.getURI(), 0);
				
		}
		return scores;
	}
	
	public static HashMap<String, Integer> getApplicationsScores(CV cv) throws Exception{
		scores = new HashMap<String ,Integer>();
		int score = 0;
		int highScore;
		
		JobPosting job = null;
		for(Application app : Application.getApplicationsByProfile(cv.getPersonURI())) {
			
			if(app.getJobURI() == null)
				continue;
			
			job = JobPosting.getJobPosting(app.getJobURI());
			
			highScore = getJobMaxScore(job);
			score = getScore(job, cv);
			if (score>highScore) 
				score = highScore;
			
			if(highScore == 0)
				scores.put(app.getJobURI(), 100);
			else
				scores.put(app.getJobURI(), (score*100)/highScore);
		}
		return scores;
	}
	
	public static HashMap<String, Integer> getAllJobScoresByCV(CV cv){
		scores = new HashMap<String ,Integer>();
		int score = 0;
		int highScore;
		
		try {
			for(JobPosting job : JobPosting.getJobPostings()) {
				highScore = getJobMaxScore(job);
				score = getScore(job, cv);
				if (score>highScore) 
					score = highScore;
				if(highScore == 0)
					scores.put(job.getURI(), 100);
				else
					scores.put(job.getURI(), (score*100)/highScore);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return scores;
	}
	
	public static HashMap<String, Integer> getCompanyScores(CV cv, String company){
		scores = new HashMap<String ,Integer>();
		int score = 0;
		int highScore;
		
		List<JobPosting> companyJobs;
		try {
			companyJobs = JobPosting.getJobPostingsByHiringOrg(company);
			for(JobPosting job : companyJobs) {
				highScore = getJobMaxScore(job);
				score = getScore(job, cv);
				if (score>highScore) 
					score = highScore;
				if(score == 0 && highScore == 0) {
					score = 100;
				}
				else if(score == 0 && highScore != 0) {
					//TEMP VALUE FOR SANKEY DIAGRAM TO BE VISIBLE
					score = 10;
				}
				else
					score = (score*100)/highScore;
				scores.put(job.getURI(), score);
			}
			
			return scores;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	@SuppressWarnings("unused")
	private static int getJobMaxScore(JobPosting job) {
		int score = 0;
//		for(Course cs: job.getCapabilityReq()) {
//			score += 1 * COURSE_COEFICIENT;
//		}
		for(Education ed: job.getEducationReq()) {
			score += 1 * EDUCATION_COEFICIENT;
		}
		for(WorkHistory wh: job.getworkExperienceReq()) {
			score += 1 * WORKHISTORY_COEFICIENT;
		}
		for(SkillJobReq skl: job.getJobSkillReqRefs()) {
			int tmpScore = 1;
			if(skl.getSkillLevel() != null) {
				switch(skl.getSkillLevel()) {
					case "junior":
						tmpScore += 2;
						break;
					case "advanced":
						tmpScore += 3;
						break;
					default : 
						break;
				}
			}
			
			
			score += tmpScore * getSkillPriorityModifier(skl) * SKILL_COEFICIENT;
		}
		
		return score;
	}

	//Generalize to all requirements besides skills, may have to add different more specific classes from a generalized skill class
	/**
	 * Calculates a score to determine how well the CV cv fits the requirement for a JobPosting job
	 * If a requirement is a skill, score will be affected by further analysis of the skill's priority and proficiency
	 * @param job JobPosting class object that will be analyzed for the score calculation
	 * @param cv CV class object that will be scored according to the job requirements
	 * @return Integer representation of how well the CV qualifications matches the JobPosting requirements 
	 */
	@SuppressWarnings("unused")
	private static int getScore(JobPosting job, CV cv) {
		
		int score = 0;
		
//		if(cv.getTargetSector().equals(job.getSector()) || cv.getTargetSector().contains(job.getSector()) || job.getSector().contains(cv.getTargetSector())) {
//			score ++;
//		}
		
		
		List<SkillJobReq> requirements = job.getJobSkillReqRefs();
		CVSkillRef cvSkillRef = null;
		int priorityModifier = 1;
		if(requirements.size() > 0) {
			for(SkillJobReq req: requirements) {
				priorityModifier = getSkillPriorityModifier(req);
				cvSkillRef = cv.hasSkillRef(req.getSkill());
				if(cvSkillRef != null) {
					score += getSkillReqScore(req, cvSkillRef) * priorityModifier * SKILL_COEFICIENT;
				}
			}
		}
//		System.out.println("SKILL SCORE");
//		System.out.println(score);
		
		boolean hasReq = false;
		List<Course> Caprequirements = job.getCapabilityReq();
		if(Caprequirements.size() != 0) {
			for(Course req :Caprequirements) {
				List<Course> courses = cv.getCourses();
				for(Course i : courses) {
					if(i.equals(req)) {
						score += 1 * COURSE_COEFICIENT;
						hasReq = true;
					}
				}
//				if(!hasReq) {
//					score -= 1;
//				}
				hasReq = false;
			}
		}
		
		
		List<Education> Exprequirements = job.getEducationReq();
		
		if(Exprequirements.size() != 0) {
			for(Education req : Exprequirements) {
				List<Education> education = cv.getEducation();
				for(Education i : education) {
					if(i.equals(req)) {
						score += 1 * EDUCATION_COEFICIENT;
						hasReq = true;
					}
				}
//				if(!hasReq) {
//					score -= 1;
//				}
				hasReq = false;
			}
		}
		
		
		List<WorkHistory> Knwrequirements = job.getworkExperienceReq();
		if(Knwrequirements.size() != 0) {
			for(WorkHistory req : Knwrequirements) {
				List<WorkHistory> workHistory = cv.getWorkHistory();
				for(WorkHistory i : workHistory) {
					if(i.getLabel() != null && req.getLabel() != null && i.getLabel().equals(req.getLabel()))
						score += 1 * WORKHISTORY_COEFICIENT;
											
				}
//				if(!hasReq) {
//					score -= 1;
//				}
				hasReq = false;
			}
		}
		
		
		return score;
	}
	
	private static int getSkillPriorityModifier(SkillJobReq ref) {
		int mod = 1;
		String priorityLevel = ref.getPriorityLevel();
		if(priorityLevel != null) {
			switch(priorityLevel) {
				case "medium":
					mod += 1;
					break;
				case "high":
					mod += 2;
					break;
			}
		}
		
		return mod;
	}
	
	private static int getSkillReqScore(SkillJobReq jobSkillRef, CVSkillRef cvSkillRef) {
		int score = 0;
		String cvSkillLevel = cvSkillRef.getSkillLevel();
		String jobSkillLevel = jobSkillRef.getSkillLevel();
		
		if(cvSkillLevel != null && jobSkillLevel != null) {
			switch(cvSkillLevel) {
			
			case "basic":
				score += 1;
				break;
				
			case "junior":
				if(!jobSkillLevel.equalsIgnoreCase("basic"))
					score += 2;
				else
					score += 1;
				break;
				
			case "advanced":
				if(jobSkillLevel.equalsIgnoreCase("advanced"))
					score += 3;
				else
					score += 2;
				break;
			}
		}
		else
			score = 1;
		
		
		
		return score;
	}
	
	/**
	 * Calculates and Integer representation of how much a Skill toCompare is worth compared to
	 * the required Skill jobReq
	 * @param jobReq A skill required by a JobPosting
	 * @param toCompare A skill from a CV
	 * @return An integer representation of how valuable the Skill toCompare is in comparison to
	 * the Skill jobReq
	 */
	@SuppressWarnings("unused")
	private static int getSkillWeight(Skill jobReq, Skill toCompare) {
		int score = 0;
		boolean isSubClass = false;
		boolean isRelated = false;

		if (jobReq==null)
			return 0;
		else if (jobReq.getLabel()==null)
			return 0; 

		if (toCompare==null)
			return 0;
		else if (toCompare.getLabel()==null)
			return 0; 
		
		for(String skill : jobReq.getsubClasses()) {
			if(toCompare.getLabel().equals(Skill.getSkill(skill).getLabel())) {
				isSubClass = true;
				isRelated = true;
			}
		}
		
		for(String skill : jobReq.getSuperClasses()) {
			if(toCompare.getLabel().equals(Skill.getSkill(skill).getLabel()))
				isRelated = true;
		}
		

		
		//Need a function to understand if they are at least related so it doesn't compare two completely unrelated skills like a
		//Java skill with a Surgery skill, then if they are related continue the method, if not return 0
		//Tries to assess if the two skills are related or not, if they are not related returns 0 immediately
		//Thought process is the following, if they are the same object it automatically says they are related
		//And if the skill labels are the same or one of the labels contains the other, then they are related in some way and the method continues
		if(jobReq != toCompare && !(jobReq.getLabel().equalsIgnoreCase(toCompare.getLabel()) ||
									jobReq.getLabel().toLowerCase().contains(toCompare.getLabel().toLowerCase()) ||
									toCompare.getLabel().toLowerCase().contains(jobReq.getLabel().toLowerCase()))) {
			
			if(!isRelated) {
				for(String skill : jobReq.getSynonyms()) {
					if(skill.equals(toCompare.getLabel())) {
						isRelated = true;
						break;
					}
				}
				if(!isRelated)
					return score;
			}
		}
		
		
		String reqSkillProficiency = null;
		String reqSkillPriority = null;
		String cvSkillProficiency = null;
//		if(jobReq.getProficiencyLevel() != null) {
//			reqSkillProficiency = jobReq.getProficiencyLevel().toLowerCase();
//		}
//		if(jobReq.getPriorityLevel() != null) {
//			reqSkillPriority = jobReq.getPriorityLevel().toLowerCase();
//		}
//		if(toCompare.getProficiencyLevel() != null) {
//			cvSkillProficiency = toCompare.getProficiencyLevel().toLowerCase();
//		}
		
		
		
		if(reqSkillPriority != null) {
			//Switch with score association for each case, greater, equals and lower than
			switch(reqSkillPriority) {
				case "high" :
					score += 3;
					break;
				case "medium" :
					score += 2;
					break;
				case "low" :
					score += 1;
				default :
					break;
			}
		}
		
		if(reqSkillProficiency != null && cvSkillProficiency != null) {
			if(reqSkillProficiency.equalsIgnoreCase(cvSkillProficiency) ) {
				score += 1;
			}
			else {
				switch (reqSkillProficiency) {
					//Need the possibilities of proficiencies to compare
					case "basic":
						score += 1;
						break;
					case "junior":
						if(!cvSkillProficiency.equalsIgnoreCase("basic"))
							score += 1;
//						else
//							score -=1;
						break;
					case "advanced":
						if(cvSkillProficiency.equalsIgnoreCase("expert"))
							score += 1;
//						else
//							score -=1;
						break;
//					case "expert":
//						score -=1;
//						break;
					default:
						break;
				}
			}
		}
	
		if(isSubClass)
			return score/2;
		else
			return score;
	}
	
	
	
}
