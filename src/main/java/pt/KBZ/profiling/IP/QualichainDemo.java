//package IP;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//import java.util.logging.FileHandler;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.util.logging.StreamHandler;
//
//import org.apache.jena.atlas.logging.java.TextFormatter;
//
//import IP.Model.*;
//import IP.RML.Mapping;
//import IP.config.Configuration;
//
//
//public class QualichainDemo {
//	
//	private static Logger Log = Logger.getLogger(QualichainDemo.class.getName());
//	private static FileHandler FileLogOutput; 
//	
//	private static Level logLevel;
//	
//	private static boolean reset = false; 
//	private static boolean addPrefixes = true; 
//	private static boolean cvTest = false; 
//	private static boolean personTest = false; 
//	private static boolean skillTest = false; 
//	private static boolean jobPostingTest = false; 
//	private static boolean resetAndPostAll = false; 
//	private static boolean generalGetTest = false;
//	private static boolean individualGetTests = false;
//	private static boolean specificGetTest = false; 
//	private static boolean matchingTest = false; 
//	private static boolean fileTest = false; 	
//	private static boolean GsonTest = false; 
//	private static boolean deleteTests = false;
//	private static boolean updateTests = false;
//	private static boolean rmlMapperTest = false;
//	
//	//args[0] for level of detail in log, can use args[1] to specify tests to make
//	public static void main(String[] args) throws Exception {
//		
//		//Create new Log handler for the console output
//        StreamHandler sh = new StreamHandler(System.err, new TextFormatter());
//        //Set the file for logger output
//        
//      	FileLogOutput = new FileHandler("output.txt");
//      	//Set the file format for the handler, might have to create a custom formatter to have the desired output
//      	FileLogOutput.setFormatter(new TextFormatter());
//        //Prevent Logger to use default handler
//        Log.setUseParentHandlers(false);
//        //Set new handlers for Logger
//		Log.addHandler(sh);
//		Log.addHandler(FileLogOutput);
//		
//		logLevel = null;
//		
//		//If there is log level specified in args[0], choose it, else default to CONFIG
//		if(args.length > 0) {
//			setLogLevel(args[0]);
//		}
//		else {
//			logLevel = Level.CONFIG;
//			Log.info("To specify a log level, pass as the first argument one of the following:"
//					+ " SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST\n\t Starting tests with default log level CONFIG\n");
//		}
//		
//		Log.info("Log level set as: " + logLevel + "\n");
//		
//		//Close System.err handler and re-attach a new one for System.out after instruction on how to use the system
//		sh.close();
//		sh = new StreamHandler(System.out, new TextFormatter());
//		Log.addHandler(sh);
//		
//		//Set the appropriate level for the handlers
//		sh.setLevel(logLevel);
//		FileLogOutput.setLevel(logLevel);
//		//Set the appropriate level for the Logger
//		Log.setLevel(logLevel);
//		//maybe log all the tests that are about to be executed beforehand
//		Log.info("Executing chosen tests\n");	
//		
//		
//		Configuration cnf = new Configuration();
//		cnf.LoadConfiguration();
//		
//		if(addPrefixes) {
//			Log.config("ADDING PREFIXES TO FUSEKI TEST");
//			addPrefixes();
//		}
//		
//		if(fileTest) {
//			Log.config("INSERTING A FILE TEST\n");
//			SparqlEndPoint.insertTTLFile("C:/Users/user/Desktop/qualichain project/custom_made_v2.ttl");
//		}
//			
//		
//		if(resetAndPostAll) {
//			Log.config("RESETING FUSEKI DATA AND REUPLOADING TEST DATA\n");
//			resetDatabase();
//			skillClassTripleInsertTest();
//			cvClassTripleInsertTest();
//			personClassTripleInsertTest();
//			jobPostingClassTripleInsertTest();
//		}
//		if(reset) {
//			Log.config("RESET FUSEKI DATA TEST:");
//			resetDatabase();
//		}
//			
//			
//		if(cvTest) {
//			Log.config("CV INSERT FUSEKI TEST");
//			cvClassTripleInsertTest();
//		}
//			
//		
//		if(personTest) {
//			Log.config("PERSON INSERT FUSEKI TEST");
//			personClassTripleInsertTest();
//		}
//			
//		
//		if(skillTest) {
//			Log.config("SKILL INSERT FUSEKI TEST");
//			skillClassTripleInsertTest();
//		}
//			
//		
//		if(jobPostingTest) {
//			Log.config("JOBPOSTING INSERT FUSEKI TEST\n");
//			jobPostingClassTripleInsertTest();
//		}
//			
//		
//		if(generalGetTest) {
//			Log.config("GET FUSEKI DATA TEST\n");
//			queryTests();
//		}
//		
//		if(individualGetTests) {
//			Log.config("SINGLE GET METHOD TEST");
//			indQueryTests();
//		}
//			
//		
//		if(specificGetTest) {
//			Log.config("DETAILED GET FUSEKI DATA TEST\n");
//			specGetTests();
//		}
//			
//			
//		if(matchingTest) {
//			Log.config("JOBPOSTING/CV MATCHING TEST\n");
//			matchingTests();
//		}
//			
//		
//		if(GsonTest) {
//			Log.config("GSON CONVERSION TEST\n");
//			GsonTest();
//		}
//		
//		if(deleteTests) {
//			Log.config("DELETE URI TEST\n");
//			deleteTest();
//		}
//		
//		if(updateTests){
//			//TODO 
//			Log.config("UPDATE TEST \t==================NOT IMPLEMENTED==================\n");
//		}
//		
//		if(rmlMapperTest) {
//			Log.config("RML MAPPER TEST\n");
//			rmlMapperTest();
//		}
//			
//		Log.info("======================================END_OF_TEST=============================================");
//		
//	}
//	
//	
//	private static void setLogLevel(String level) {
//		switch(level) {
//		case "SEVERE":
//			logLevel = Level.SEVERE;
//			break;
//		case "WARNING":
//			logLevel = Level.WARNING;
//			break;
//		case "INFO":
//			logLevel = Level.INFO;
//			break;
//		case "CONFIG":
//			logLevel = Level.CONFIG;
//			break;
//		case "FINE":
//			logLevel = Level.FINE;
//			break;
//		case "FINER":
//			logLevel = Level.FINER;
//			break;
//		case "FINEST":
//			logLevel = Level.FINEST;
//			break;
//		default:
//			Log.warning("Input Log Level not in the correct format\n\t Input was: " + level);
//			logLevel = Level.CONFIG;
//		}
//		
//	}
//	
//	private static void rmlMapperTest() {
//		Mapping mapping = new Mapping();
//		mapping.map("RMLMapperCV.ttl", "turtle");		
//	}
//	
//	private static void deleteTest() {
//		Log.info("===============================DELETE TEST=====================================\\n");
//		Log.info("Deleting cv with URI: ...\n");
//		try {
//			CV.deleteObject("cv:id1");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Log.info("Deleting skill with URI: ...");
//		Skill.deleteObject("cv:skill_1");
//		
//		Log.info("Deleting Person with URI: ...");
//		try {
//			Person.deleteObject("qc:Alberto");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Log.info("Deleting JobPosting with URI: ...");
//		try {
//			JobPosting.deleteObject("saro:Job1");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	private static void GsonTest() {
//		Log.warning("NO TEST AVAILABLE YET");
//	}
//	
//	private static void matchingTests() throws Exception {
//		Log.info("===============================MATCHING_TEST=====================================\n");
//		
//		List<JobPosting> jobs = JobPosting.getJobPostings();
//		for(JobPosting job : jobs) {
//			Log.info("Listing cv scores for the Job Post with URI: " + job.getURI());
//			Log.info("=============================================================================");
//			Log.info("Job Requirements: ");
//			List<Skill> reqs = job.getSkillReq();
//			for(Skill req: reqs) {
//				Log.info("\t\t" + req.getLabel());
//			}
//			HashMap<String, Integer> scores = Matching.getAllCvMatches(job, false);
//			Log.info("=============================================================================");
//			Set<String> results = scores.keySet();
//			for(String uri : results) {
//				Log.info("Cv with URI: " + uri + " has a score of: " + scores.get(uri) + ".");
//			}
//			Log.info("=============================================================================\n");
//		}
//		
//		Log.info("Listing scores by Application made by each person");
//		Log.info("=============================================================================\n");
//		for(Person person : Person.getPersons()) {
//			Log.info("Person: " + person.getURI());
//			Log.info("=============================================================================");
//			Map<String, Integer> scores = CV.getJobApplicationScores(person.getURI());
//			for (Map.Entry<String, Integer> score : scores.entrySet()) { 
//				Log.info("Job = " + score.getKey() +  
//	                          ",\n\t\t\t\t\t Score = " + score.getValue()); 
//				Log.info("=============================================================================");
//	        } 
//			Log.info("=============================================================================\n");
//		}
//		
//		Log.info("Listing scores by Application made by each CV");
//		Log.info("=============================================================================\n");
//		for(CV cv : CV.getCVs()) {
//			Log.info("CV: " + cv.getLabel());
//			Log.info("=============================================================================");
//			Map<String, Integer> scores = CV.getJobApplicationScores(cv.getURI());
//			for (Map.Entry<String, Integer> score : scores.entrySet()) { 
//				Log.info("Job = " + score.getKey() +  
//	                          ",\n\t\t\t\t\t Score = " + score.getValue()); 
//				Log.info("=============================================================================");
//	        } 
//			Log.info("=============================================================================\n");
//		}
//	}
//	
//	private static void specGetTests() throws Exception {
//
//		Log.info("===============================FILTERED_GET_TESTS=====================================\n");
//		
//		List<Skill> skills = Skill.getSkills();
//		//GET CVS BY SKILL TEST
//		Log.info("Get CVs by Skill");
//		Log.info("=============================================================================\n");
//		for(Skill skill: skills) {
//			Log.info("CVs with Skill: " + skill.getLabel());
//			Log.info("==============================================================================================");
//			List<CV> cvs = CV.getCVsBySkill(skill.getLabel());
//			for(CV cv: cvs) {
//				Log.info(Person.getPerson(cv.getPersonURI()).getLabel() + " has the skill with the Label " + skill.getLabel() + " as described in " + cv.getLabel());
//			}
//			Log.info("==============================================================================================\n");
//		}
//		
//		//GET PERSON BY NAME TEST
//		Log.info("Get Person by name");
//		Log.info("==============================================================================================\n");
//		List<Person> testPersons = Person.getPersons();
//		for(Person person: testPersons) {
//			Person testSubject = Person.getPersonByName(person.getLabel().substring(0, 2));
//			if(testSubject.getLabel() != null) {
//				Log.info("Person with name: " + testSubject.getLabel() + " has the following accomplishments");
//				Log.info("==============================================================================================");	
//			}
//			List<String> accomplishments = testSubject.getAccomplishments();
//			Log.info("Accomplishments:");
//			for(String accomplishment: accomplishments) {
//				Log.info("         " + accomplishment);
//			}
//			Log.info("==============================================================================================\n");
//		}
//		
//		
//		//GET PERSON BY FIELD OF EXPERTISE TEST
//		//TODO:Weird results here, test again
//		Log.info("Get Person by Field description: etc" );
//		testPersons = Person.getPersonsByField("etc");
//		for(Person person: testPersons) {
//			Log.info("Person named: " + person.getLabel() + " with CV " + person.getCVURI() + " is specialized in field: " 
//		+ person.getCompetenceArea());
//			Log.info("========================================================================================\n");
//		}
//		
//		//GET PERSON BY CV URI TEST
//		Log.info("Get Person by CV URI");
//		Log.info("========================================================================================\n");
//		Log.info("Getting Person with CV URI: cv:id1");
//		Person testSubject = Person.getPersonByCV("cv:id1");
//		Log.info("Person named: " + testSubject.getLabel() + " has CV with URI: " + testSubject.getCVURI());
//		
//		Log.info("========================================================================================");
//		
//		Log.info("Getting Person with CV URI: cv:id2");
//		testSubject = Person.getPersonByCV("cv:id2");
//		Log.info("Person named: " + testSubject.getLabel() + " has CV with URI: " + testSubject.getCVURI());
//		
//		Log.info("========================================================================================");
//		
//		Log.info("Getting Person with CV URI: cv:id3");
//		testSubject = Person.getPersonByCV("cv:id3");
//		Log.info("Person named: " + testSubject.getLabel() + " has CV with URI: " + testSubject.getCVURI());
//		
//		Log.info("========================================================================================\n");
//		
//		
//		//GET CV BY PERSON TEST
//		Log.info("Get CV by partial Person name");
//		Log.info("========================================================================================\n");
//		
//		Log.info("Getting CV through partial person name 'Albert'");
//		CV testCvByPerson = CV.getCVbyPerson("Albert");
//		Log.info("CV with URI: " + testCvByPerson.getURI() + " is about Person named: "
//		+ Person.getPersonByCV(testCvByPerson.getURI()).getLabel());
//		
//		Log.info("========================================================================================");
//		Log.info("Getting CV through partial person name 'Joaq'");
//		testCvByPerson = CV.getCVbyPerson("Joaq");
//		Log.info("CV with URI: " + testCvByPerson.getURI() + " is about Person named: "
//		+ Person.getPersonByCV(testCvByPerson.getURI()).getLabel());
//
//		Log.info("========================================================================================");
//		Log.info("Getting CV through partial person name 'Belo'");
//		testCvByPerson = CV.getCVbyPerson("Belo");
//		Log.info("CV with URI: " + testCvByPerson.getURI() + " is about Person named: "
//		+ Person.getPersonByCV(testCvByPerson.getURI()).getLabel());
//
//
//		//JobPosting Class needs rework, has weird behavior
//		Log.info("========================================================================================\n");
//		//TODO:GET JOB POSTING BY LABEL TEST, needs testing, broken
//		Log.info("Get Job Posts by label");
//		JobPosting jp = JobPosting.getJobPostingByLabel("scientist");
//		Log.info("JobPosting URI: " + jp.getURI() + " with Label: " + jp.getLabel());
//		Log.info("========================================================================================\n");
//		
//		//GET JOB POSTING BY SECTOR TEST
//		Log.info("Get Job Posts by Sector");
//		List<JobPosting> jps = JobPosting.getJobPostingsBySector("Data_Science");
//		Log.info("Available JobPostings with Sector: Data_Science");
//		for(JobPosting job: jps) {
//			Log.info(job.getLabel());
//		}
//		Log.info("========================================================================================\n");
//		
//		//GET JOB POSTING BY LISTING ORG TEST
//		jps = JobPosting.getJobPostingsByListingOrg("Merquer_inc");
//		Log.info("Available JobPostings by Listing Organization: Merquer_inc");
//		for(JobPosting job: jps) {
//			Log.info(job.getLabel());
//		}
//		Log.info("========================================================================================\n");
//		
//		//GET JOB POSTING BY HIRING ORG TEST
//		jps = JobPosting.getJobPostingsByHiringOrg("USCL");
//		Log.info("Available JobPostings by Hiring Organization: USCL");
//		for(JobPosting job: jps) {
//			Log.info(job.getLabel());
//		}
//		Log.info("========================================================================================\n");
//		
//		//GET JOB POSTING BY CONTRACT TYPE TEST
//		jps = JobPosting.getJobPostingsByContractType("month");
//		Log.info("Available JobPostings by Contract type");
//		for(JobPosting job: jps) {
//			Log.info(job.getLabel());
//		}
//		Log.info("========================================================================================\n");
//		
//	}
//	
//	
//	private static void jobPostingClassTripleInsertTest() throws Exception {
////		String id, String label,String comment, String jobDescription, String contractType, String sector, String occupation,
////		String listingOrg, String hiringOrg, String jobLoc, List<Skill> skillReq, List<String> capReq, List<String> knowReq, List<String> exptReq
//		JobPosting testPost = null;
//		
//		Log.info("==============================================================================================\n");
//		
//		Log.info("Atempting to insert example JobPosting data");
//		Log.fine("==============================================================================================");
//
//		Log.fine("JobPosting id: Job1");
//		
//		testPost = new JobPosting("Job1", "Data scientist Required", "Job Post for a university in Colorado", "Need someone to do xyz", "6 month period", "Data Science",
//				"Science Team Leader", "Merquer inc", "USCL", "City Z in Colorado", "01/01/20", "31/05/20", null, null, null, null, new ArrayList<Skill>(),
//				new ArrayList<Course>(), new ArrayList<WorkHistory>(), new ArrayList<Education>());
//		testPost.addSkillReq("cv:skill_1");
//		testPost.addSkillReq("cv:skill_3");
//		testPost.Save();
//		
//		Log.fine("JobPosting id: Job2");
//		
//		testPost = new JobPosting("Job2", "AVG Construction Team Coordinator", "Manage a team of workers building AVG automatons for factory work",
//				"Manage schedules, material orders and work posts for the different workers working in your section", "One year contract with possibility of renovation",
//				"Automation","Team Coordinator/Manager", "Merquer inc", "Imgursa", "Guia", "01/02/20", "01/02/21", null, null, null, null,
//				new ArrayList<Skill>(), new ArrayList<Course>(),
//				new ArrayList<WorkHistory>(), new ArrayList<Education>());
//		
//		testPost.addSkillReq("cv:skill_2");
//		testPost.addSkillReq("cv:skill_3");
//		testPost.addSkillReq("cv:skill_4");
//		testPost.Save();
//		
//		Log.fine("JobPosting id: Job3");
//		
//		
//		testPost = new JobPosting("Job3", "Salary manager for Company X", "Company X requires a salary manager urgently",
//				"The job requires that the candidate be able to manage employees salaries, making sure there is a fair distribuition of company resources with enough overhead for expansion",
//				"Indefinite time contract", "Management", "Senior Accounting Expert", "Jason co", "Imgursa", "Guia", "01/09/20", "01/03/21", null, null, null, null,
//				new ArrayList<Skill>(), new ArrayList<Course>(),
//				new ArrayList<WorkHistory>(), new ArrayList<Education>());
//		
//		testPost.addSkillReq("cv:skill_1");
//		testPost.addSkillReq("cv:skill_3");
//		testPost.addSkillReq("cv:skill_5");
//		testPost.Save();
//		
//		Log.fine("==============================================================================================\n");
//		
//	}
//	
//	
//	private static void skillClassTripleInsertTest() throws Exception {
//		
//		Skill testSkill = null;
//		Log.info("==============================================================================================\n");
//		
//		Log.info("Atempting to insert example Skill data");
//		Log.info("==============================================================================================");
//		Log.fine("Skill id:skill_1, Skill label: Excel, Skill comment: Knowledge in Excel, Skill priority: null, Skill proficiency: null");
//		
//		testSkill = new Skill("skill_1", "Excel", null, null, "knowledge in Excel", "cv:Skill");
//		testSkill.Save();
//		
//		Log.fine("Skill id:skill_2, Skill label: Soldering, Skill comment: Experience in Soldering, Skill priority: null, Skill proficiency: null");
//		
//		testSkill = new Skill("skill_2", "Soldering", "","", "Experience in Soldering", "cv:Skill");
//		testSkill.Save();
//		
//		Log.fine("Skill id:skill_3, Skill label: Personnel Management comment: Basic training in personnel Management, Skill priority: null, Skill proficiency: null");
//		
//		testSkill = new Skill("skill_3", "Personnel Management", "","", "Basic training in personnel Management", "cv:Skill");
//		testSkill.Save();
//		
//		Log.fine("Skill id:skill_4, Skill label: AGV Maintenence, Skill comment: Knows how to use diagnotics tools for AGV automatons, re-program sensors, \n\t\t\t\t\t"
//				+ " stress tests etc..., Skill priority: null, Skill proficiency: null");
//		
//		testSkill = new Skill("skill_4", "AGV Maintenence", "","", "Knows how to use diagnotics tools for AGV automatons, re-program sensors, stress tests etc...", "cv:Skill");
//		testSkill.Save();
//		
//		Log.fine("Skill id:skill_5, Skill label: Financial Advisory, Skill comment: Will help you grow your company or manage your personal finances, \n\t\t\t\t\t"
//				+ " Skill priority: null, Skill proficiency: null");
//		
//		testSkill = new Skill("skill_5", "Financial Advisory", "","", "Will help you grow your company or manage your personal finances", "cv:Skill");
//		testSkill.Save();
//		
//		Log.fine("==============================================================================================\n");
//		
//	}
//	
//	private static void personClassTripleInsertTest() throws Exception {
//
//		Log.info("==============================================================================================\n");
//		Person testInsertPerson;
//		Log.info("Atempting to insert example Person data");
//		Log.fine("==============================================================================================");
//		Log.fine("Person id:Alberto, Person label: Alberto Cabrita, Person comment: Alberto personal info, Person Gender: Male,\n\t\t\t\t\t"
//				+ "Person has driving license: true, Person CV id:id1");
//		testInsertPerson = new Person("Alberto", "Alberto", "Cabrita", "Alberto personal info", "Male", "922283384", "asc.99@jimail.com", "albertoS.com", "Portuguese", null, true,
//				"cv:id1", null, null, "employee", null, null, null, null, null);
//		testInsertPerson.Save();
//		
//		Log.fine("Person id:Joaquim, Person label: Joaquim Cabral, Person comment: Alberto personal info, , Person Gender: Male,\n\t\t\t\t\t"
//				+ "Person has driving license: false, Person CV id:id2");
//		testInsertPerson = new Person("Joaquim", "Joaquim", "Cabral", "Joaquim personal info", "Male", "922245684", "jc.92@jimail.com", "JoJosCentral.com", "Portuguese", null, false,
//				"cv:id2", null, null, "employee", null, null, null, null, null);
//		testInsertPerson.Save();
//		
//		Log.fine("Person id:Guilherme, Person label: Guilherme Belo, Person comment: Guilherme personal info, Person Gender: Male,\n\t\t\t\t\t"
//				+ "Person has driving license: true, Person CV id:id3");
//		testInsertPerson = new Person("Guilherme", "Guilherme", "Belo", "Guilherme personal info", "Male", "939445684", "ggb.88@jimail.com", "PressFreeN.com", "Portuguese", null, true,
//				"cv:id3", null, null, "employee", null, null, null, null, null);
//		testInsertPerson.Save();
//		
//		Log.fine("==============================================================================================\n");
//
//		Log.info("Adding Competences to example Persons");
//		Log.fine("==============================================================================================");
//		Log.fine("Adding Competence: Economics with Label: Focus on accounting...etc to Person with Id Alberto");
//		testInsertPerson = Person.getPerson("qc:Alberto");
//		testInsertPerson.setCompetenceArea("Economics");
//		testInsertPerson.setCompetenceAreaDescription("Focus on accounting...etc");
//		testInsertPerson.Save();
//		
//		Log.fine("Adding Competence: Metallurgy with Label: Expertise in soldering...etc to Person with Id Joaquim");
//		testInsertPerson = Person.getPerson("qc:Joaquim");
//		testInsertPerson.setCompetenceArea("Metallurgy");
//		testInsertPerson.setCompetenceAreaDescription("Expertise in soldering...etc");
//		testInsertPerson.Save();
//		Log.fine("==============================================================================================\n");
//		
//		Log.info("Adding Memberships to example Persons");
//		Log.fine("==============================================================================================");
//		Log.fine("Adding Membership: Stock Exchange Membership to Person with Id Alberto");
//		testInsertPerson = Person.getPerson("qc:Alberto");
//		testInsertPerson.addMembership("Stock Exchange Membership");
//		testInsertPerson.Save();
//		Log.fine("==============================================================================================\n");
//		
//		Log.info("Adding Experience history to example Persons");
//		Log.fine("==============================================================================================");
//		Log.fine("Adding Experience: 10 years working at X company to Person with Id Joaquim");
//		testInsertPerson = Person.getPerson("qc:Joaquim");
//		testInsertPerson.addExperience("10 years working at X company");
//		testInsertPerson.Save();
//		Log.fine("==============================================================================================\n");
//		
//	}
//	
//	private static void cvClassTripleInsertTest() throws Exception {
//		
//		CV testCV = null;
//		Log.info("==============================================================================================\n");
//		
//		Log.info("Atempting to insert example CV data");
//		Log.info("==============================================================================================");
//		Log.fine("CV id: id1, CV label: Example CV 1, CV comment: No comment, About person: Alberto, CVTitle: Albertos CV, CV otherInfo: Things Alberto has done,\n\t\t\t\t\t"
//				+ "Applying for job: Something new, Currency of salary: Euro, Expected salary: 1000, Other info: Is a fast learner");
//		
//		testCV = new CV("id1", "Example CV 1", "No comment", "Albertos CV", "qc:Alberto", "Things Alberto has done", "Something new", "Is a fast learner",
//				"1000", "Euros", new ArrayList<WorkHistory>(), new ArrayList<Education>(), new ArrayList<Course>(), new ArrayList<Skill>(), new ArrayList<Application>() );
//		testCV.Save();
//		
//		Log.fine("CV id: id2, CV label: Example CV 2, CV comment: No comment, About person: Joaquim, CVTitle: Joaquims CV, CV otherInfo: Interests and hobbies,\n\t\t\t\t\t"
//				+ "Applying for job: Something fresh, Currency of salary: Canadian Dollar, Expected salary: 780, Other info: Has a very good work ethic");
//		
//		testCV = new CV("id2", "Example CV 2", "No comment", "Joaquims CV", "qc:Joaquim", "Interests and hobbies", "Something fresh", "Has a very good work ethic",
//				"780", "Canadian Dollar", new ArrayList<WorkHistory>(), new ArrayList<Education>(), new ArrayList<Course>(), new ArrayList<Skill>(), new ArrayList<Application>() );
//		testCV.Save();
//		
//		Log.fine("CV id: id3, CV label: Example CV 3, CV comment: Some Comment, About person: Guilherme, CVTitle: Guilherme CV, CV otherInfo: Etc,\n\t\t\t\t\t"
//				+ "Applying for job: Something challenging, Currency of salary: Euro, Expected salary: 900, Other info: Very proactive");
//		
//		testCV = new CV("id3", "Example CV 3", "Some comment", "Guilherme CV", "qc:Guilherme", "Etc", "Something challenging", "Very proactive",
//				"900", "Euro", new ArrayList<WorkHistory>(), new ArrayList<Education>(), new ArrayList<Course>(), new ArrayList<Skill>(), new ArrayList<Application>() );
//		testCV.Save();
//		
//		Log.info("CV id: id5, NO OTHER DATA");
//		testCV = new CV();
//		testCV.setURI("cv:id5");
//		testCV.Save();
//		
//		Log.fine("==============================================================================================\n");
//		
//		Log.info("Adding work history to example CVs");
//		Log.fine("==============================================================================================");
//		
//		Log.fine("Adding work history: Bank teller to CV with Id id1");
//		testCV = CV.getCV("cv:id1");
//		WorkHistory test = new WorkHistory();
//		test.setLabel("Bank teller");
//		test.Save();
//		testCV.addWorkHistory(test);
//		testCV.Save();
//		
//		Log.fine("Adding work history: Imgursa to CV with Id id2");
//		testCV = CV.getCV("cv:id2");
//		test = new WorkHistory();
//		test.setLabel("Imgursa");
//		test.Save();
//		testCV.addWorkHistory(test);
//		testCV.Save();
//		
//		Log.fine("==============================================================================================\n");
//		
//		Log.info("Adding education history to example CVs");
//		Log.fine("==============================================================================================");
//		Log.fine("Adding education: BS in Econ to CV with Id id1");
//		testCV = CV.getCV("cv:id1");
//		Education testEd = new Education();
//		testEd.setLabel("BS in Econ");
//		testEd.Save();
//		testCV.addEducation(testEd);
//		testCV.Save();
//		
//		Log.fine("Adding education: 12th grade to CV with Id id2");
//		testCV = CV.getCV("cv:id2");
//		testEd = new Education();
//		testEd.setLabel("12th grade");
//		testEd.Save();
//		testCV.addEducation(testEd);
//		testCV.Save();
//		
//		Log.fine("Adding education: BS in Automation to CV with Id id3");
//		testCV = CV.getCV("cv:id3");
//		testEd = new Education();
//		testEd.setLabel("BS in Automation");
//		testEd.Save();
//		testCV.addEducation(testEd);
//		testCV.Save();
//		
//		Log.fine("==============================================================================================\n");
//		
//		Log.info("Adding skills to example CVs");
//		Log.fine("==============================================================================================");
//		
//		Log.fine("Adding skill: skill_1 to CV with Id id1");
//		testCV = CV.getCV("cv:id1");
//		testCV.addSkill("skill_1");
//		testCV.Save();
//		
//		Log.fine("Adding skill: skill_3 to CV with Id id1");
//		testCV = CV.getCV("cv:id1");
//		testCV.addSkill("skill_3");
//		testCV.Save();
//		
//		Log.fine("Adding skill: skill_5 to CV with Id id1");
//		testCV = CV.getCV("cv:id1");
//		testCV.addSkill("skill_5");
//		testCV.Save();
//		
//		Log.fine("Adding skill: skill_2 to CV with Id id2");
//		testCV = CV.getCV("cv:id2");
//		testCV.addSkill("skill_2");
//		testCV.Save();
//		
//		Log.fine("Adding skill: skill_2 to CV with Id id3");
//		testCV = CV.getCV("cv:id3");
//		testCV.addSkill("skill_2");
//		testCV.Save();
//		
//		Log.fine("Adding skill: skill_3 to CV with Id id3");
//		testCV = CV.getCV("cv:id3");
//		testCV.addSkill("skill_3");
//		testCV.Save();
//		
//		Log.fine("Adding skill: skill_4 to CV with Id id3");
//		testCV = CV.getCV("cv:id3");
//		testCV.addSkill("skill_4");
//		testCV.Save();
//		
//		Log.fine("==============================================================================================\n");
//		
//	}
//	
//	private static void queryTests() throws Exception {
//		List<CV> cvs = CV.getCVs();
//		List<Skill> skills = Skill.getSkills();
//		List<Person> persons = Person.getPersons();
//		List<JobPosting> jobPosts = JobPosting.getJobPostings();
//		Log.info("=============================QUERY_TEST==========================================");
//		Log.info("===========================LISTING_ALL_CVS=======================================\n");
//		for(CV cv : cvs) {
//			List<Application> apps = cv.getApplications();
//			Log.info("Cv ID: " + cv.getID());
//			Log.info("=========================================");
//			Log.info("Cv Title: " + cv.getTitle());
//			Log.info("=========================================");
//			Log.info("Cv Label: " + cv.getLabel());
//			Log.info("=========================================");
//			Log.info("Cv About Person: " + cv.getPersonURI());
//			Log.info("=========================================");
//			Log.info("Cv Description: " + cv.getDescription());
//			Log.info("=========================================");
//			Log.info("Cv Comment: " + cv.getComment());
//			Log.info("=========================================");
//			
//			for(Application app : apps) {
//				Log.info("=========================================");
//				Log.info("Applied to Job: " + app.getJobURI());
//				Log.info("Available to start at: " + app.getAvailability());
//				Log.info("Expected Salary for Job application: " + app.getExpectedSalary());
//				Log.info("=========================================");
//			}
//			
//			Log.info("Cv Skills: ");
//			List<Skill> lists = cv.getSkills();
//			for(Skill skill: lists) {
//				Log.info("	" + skill.getLabel());
//			}
//			Log.info("=========================================");
//			Log.info("Cv Courses: ");
//			List<Course> courselists = cv.getCourses();
//			for(Course course: courselists) {
//				Log.info("	" + course.getLabel());
//			}
//			Log.info("=========================================");
//			Log.info("Cv Education: ");
//			List<Education> Edulists = cv.getEducation();
//			for(Education education: Edulists) {
//				Log.info("	" + education.getLabel() );
//			}
//			Log.info("=========================================");
//			Log.info("Cv Work History: ");
//			List<WorkHistory> WHlists = cv.getWorkHistory();
//			for(WorkHistory work: WHlists) {
//				Log.info("	" + work.getLabel());
//			}
//			
//			
//			
//			Log.info("=========================================\n");
//		}
//		Log.info("=========================LISTING_ALL_SKILLS==============================\n");
//		
//		for(Skill skill: skills) {
//			Log.info("Skill ID: " + skill.getID());
//			Log.info("=========================================");
//			Log.info("Skill Label: " + skill.getLabel());
//			Log.info("=========================================");
//			Log.info("Skill Comment " + skill.getComment());
//			Log.info("=========================================\n");
//		}
//
//		Log.info("=========================LISTING_ALL_PERSONS==============================\n");
//		
//		for(Person person: persons) {
//			Log.info("Person ID: " + person.getID());
//			Log.info("=========================================");
//			Log.info("Person Label: " + person.getLabel());
//			Log.info("=========================================");
//			Log.info("Person Name: " + person.getName());
//			Log.info("=========================================");
//			Log.info("Person Surname: " + person.getsurname());
//			Log.info("=========================================");
//			Log.info("Person Role or Title: " + person.getRole());
//			Log.info("=========================================");
//			Log.info("Person Comment: " + person.getComment());
//			Log.info("=========================================");
//			Log.info("Person Gender: " + person.getGender());
//			Log.info("=========================================");
//			Log.info("Person Drivers License: " + person.getDriversLicense());
//			Log.info("=========================================");
//			Log.info("Person CV URI: " + person.getCVURI());
//			Log.info("=========================================");
//			Log.info("Person Competence Area: " + person.getCompetenceArea());
//			Log.info("=========================================");
//			Log.info("Person Accomplishments: ");
//			List<String> accomplishments = person.getAccomplishments();
//			for(String accomplishment: accomplishments) {
//				Log.info("	" + accomplishment);
//			}
//			Log.info("=========================================\n");
//			
//		}
//
//		Log.info("======================LISTING_ALL_JOB_POSTS===========================\n");
//		
//		for(JobPosting jp : jobPosts) {
//			Log.info("Job Post ID: " + jp.getID());
//			Log.info("=========================================");
//			Log.info("Job Post Label: " + jp.getLabel());
//			Log.info("=========================================");
//			Log.info("Job Post Comment: " + jp.getComment());
//			Log.info("=========================================");
//			Log.info("Job Post Description: " + jp.getJobDescription());
//			Log.info("=========================================");
//			Log.info("Job Post Contract Type: " + jp.getContractType());
//			Log.info("=========================================");
//			Log.info("Job Post Sector: " + jp.getSector());
//			Log.info("=========================================");
//			Log.info("Job Post Position/Occupation: " + jp.getOccupation());
//			Log.info("=========================================");
//			Log.info("Job Post Listed By: " + jp.getListingOrg());
//			Log.info("=========================================");
//			Log.info("Job Post Hiring to: " + jp.getHiringOrg());
//			Log.info("=========================================");
//			Log.info("Job Post Work Location: " + jp.getjobLocation());
//			Log.info("=========================================");
//			Log.info("Job Post Start Date: " + jp.getStartDate());
//			Log.info("=========================================");
//			Log.info("Job Post End Date: " + jp.getEndDate());
//			Log.info("=========================================");
//			Log.info("Job Post Skill Requirements:");
//			List<Skill> SkillReq = jp.getSkillReq();
//			for(Skill req : SkillReq) {
//				Log.info("	" + req.getLabel());
//			}
//			Log.info("=========================================");
//			Log.info("Job Post Required Capabilities:");
//			
//			List<Course> capabilityReq = jp.getCapabilityReq();
//			for(Course req : capabilityReq) {
//				Log.info("	" + req.getLabel());
//			}
//			Log.info("=========================================");
//			
//			Log.info("Job Post Required Knowledge:");
//			
//			List<WorkHistory> workExpReq = jp.getworkExperienceReq();
//			for(WorkHistory req : workExpReq) {
//				Log.info("	" + req.getLabel());
//			}
//			Log.info("=========================================");
//			
//			Log.info("Job Post Required Expertise:");
//			
//			List<Education> qualifications = jp.getEducationReq();
//			for(Education req : qualifications) {
//				Log.info("	" + req.getLabel());
//			}
//			Log.info("=========================================\n");	
//			
//			
//		}
//	}
//	
//	private static void indQueryTests() throws Exception {
//		List<CV> cvs = CV.getCVs();
//		List<Skill> skills = Skill.getSkills();
//		List<Person> persons = Person.getPersons();
//		List<JobPosting> jobPosts = JobPosting.getJobPostings();
//		CV testCV = null;
//		Skill testSkill = null;
//		Person testPerson = null;
//		JobPosting testJobPosting = null;
//		
//		for(CV cv : cvs) {
//			testCV = CV.getCV(cv.getURI());
//			Log.info("CV ID TEST: " + cv.getID() + " should be the same as: " + testCV.getID());
//			for(Application app : cv.getApplications()) {
//				Log.info(app.getJobURI());
//			}
//		}
//		for(JobPosting jp : jobPosts) {
//			testJobPosting = JobPosting.getJobPosting(jp.getURI());
//			Log.info("JobPosting ID TEST: " + jp.getID() + " should be the same as: " + testJobPosting.getID());
//		}
//		for(Person ps : persons) {
//			testPerson = Person.getPerson(ps.getURI());
//			Log.info("Person ID TEST: " + ps.getID() + " should be the same as: " + testPerson.getID());
//		}
//		for(Skill skl : skills) {
//			testSkill = Skill.getSkill(skl.getURI());
//			Log.info("Skill ID TEST: " + skl.getID() + " should be the same as: " + testSkill.getID());
//		}
//		
//		
//	}
//	
//	private static void addPrefixes() {
//		Log.info("Atempting to insert prefix list to database\n");
//		SparqlEndPoint.insertTTLFile("prefixes.ttl");
//	}
//	
//	private static void resetDatabase() {
//		Log.info("Atempting to delete all triples from database\n");
//		SparqlEndPoint.deleteTriple("?s ?o ?p", "?s ?o ?p");
//		
//	}
//	
//}