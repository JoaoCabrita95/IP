package IP.Model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.cyberborean.rdfbeans.annotations.RDFBean;
import org.cyberborean.rdfbeans.annotations.RDFNamespaces;

import IP.Matching;

@RDFNamespaces(
{      
    "rdfs = http://www.w3.org/2000/01/rdf-schema",
    "qc = http://www.qualichain.eu/ontology/",
    "cv = http://rdfs.org/resume-rdf/cv.rdfs/" 
})

@RDFBean("cv:CV")
public class CV extends RDFObject {
	
    private static final String ClassType ="cv:CV";     
    private static final String prefix =":";     
    private String title = null;
    private String personURI = null;
    private String description = null;
    private String targetSector = null;
    private String otherInfo = null;
    private String currentJob = null;
    private String realocationInfo = null;
    private List<WorkHistory> workHistory;
    private List<Education> education;
    private List<Course> courses;
    private List<CVSkillRef> skills;
    private List<String> accomplishments;
    private List<Application> jobApplications;

    //TODO: Add remove methods for Lists
    
    //TODO: Add constructors for different cases if found necessary
    /**
     * Constructor with empty WorkHistory, Education, Courses, and Skills lists and no values attributed 
     */
    public CV() {
    	super(ClassType, prefix);
    	workHistory = new ArrayList<>();
    	education = new ArrayList<>();
    	courses = new ArrayList<>();
    	jobApplications = new ArrayList<>();
    	skills = new ArrayList<>();
    	accomplishments = new ArrayList<>();
    }
    
    /**
     * Constructor with input for every variable and list for the CV object
     * @param id CV id, used to derive the URI for now, should be changed to a random unique URI generator
     * @param label CV "name", another identifier that also describes the CV loosely
     * @param comment CV comment, any short comment on the CV
     * @param title CV title, can be a short descriptor of the person who's CV this is, or a description of the goal of the CV
     * @param personUri URI of the person to whom this CV belongs to
     * @param desc CV description, another way of describing the person's intention with the CV 
     * @param targetSect Description of the target job the CV is aimed at
     * @param info information about the person that could be useful for the job position the CV is applying for
     * @param expSalary Expected salary amount the Person wants to receive for the Job
     * @param salaryCurrency The currency the Person wants to/expects to be payed in
     * @param workHist History of past job experiences the Person has had
     * @param educ Educational history the Person has
     * @param courses Courses the Person has attended
     * @param skills Skills the Person holds/has certification in
     * @param jobApps Job applications made with this CV
     */
    public CV(String id, String label, String comment, String title, String personUri,
    		String desc, String targetSect, String info, String expSalary, String salaryCurrency,
    		List<WorkHistory> workHist, List<Education> educ, List<Course> courses, List<CVSkillRef> skillRefs,
    		List<Application> jobApps) {
    	super(ClassType, prefix, id, label, comment);
    	this.title = title;
    	this.personURI = personUri;
    	this.description = desc;
    	this.targetSector = targetSect;
    	this.otherInfo = info;
    	if(workHist == null) {
    		this.workHistory = new ArrayList<WorkHistory>();
    	}
    	else
    		this.workHistory = workHist;
    	if(educ == null) {
    		this.education = new ArrayList<Education>();
    	}
    	else
    		this.education = educ;
    	if(courses == null) {
    		this.courses = new ArrayList<Course>();
    	}
    	else
    		this.courses = courses;
    	if(skillRefs == null) {
    		this.skills = new ArrayList<CVSkillRef>();
    	}
    	else {
    		this.skills = skillRefs;
    	}
    	if(jobApps == null)
    		this.jobApplications = new ArrayList<Application>();
    	else 
    		this.jobApplications = jobApps;

    	accomplishments = new ArrayList<>();
    }
    
    /**
     * Constructor with input for every variable and list for the CV object
     * @param id CV id, used to derive the URI for now, should be changed to a random unique URI generator
     * @param label CV "name", another identifier that also describes the CV loosely
     * @param comment CV comment, any short comment on the CV
     * @param title CV title, can be a short descriptor of the person who's CV this is, or a description of the goal of the CV
     * @param personUri URI of the person to whom this CV belongs to
     * @param desc CV description, another way of describing the person's intention with the CV 
     * @param targetSect Description of the target job the CV is aimed at
     * @param info information about the person that could be useful for the job position the CV is applying for
     * @param expSalary Expected salary amount the Person wants to receive for the Job
     * @param salaryCurrency The currency the Person wants to/expects to be payed in
     * @param workHist History of past job experiences the Person has had
     * @param educ Educational history the Person has
     * @param courses Courses the Person has attended
     * @param skills Skills the Person holds/has certification in
     * @param jobApp Job application made with this CV
     */
    public CV(String id, String label, String comment, String title, String personUri,
    		String desc, String targetSect, String info, String expSalary, String salaryCurrency,
    		List<WorkHistory> workHist, List<Education> educ, List<Course> courses, List<CVSkillRef> skillRefs,
    		Application jobApp) {
    	super(ClassType, prefix, id, label, comment);
    	this.title = title;
    	this.personURI = personUri;
    	this.description = desc;
    	this.targetSector = targetSect;
    	this.otherInfo = info;
    	if(workHist == null) {
    		this.workHistory = new ArrayList<WorkHistory>();
    	}
    	else
    		this.workHistory = workHist;
    	if(educ == null) {
    		this.education = new ArrayList<Education>();
    	}
    	else
    		this.education = educ;
    	if(courses == null) {
    		this.courses = new ArrayList<Course>();
    	}
    	else
    		this.courses = courses;
    	if(skillRefs == null) {
    		this.skills = new ArrayList<CVSkillRef>();
    	}
    	else {
    		this.skills = skillRefs;
    	}
    	this.jobApplications = new ArrayList<Application>();
    	jobApplications.add(jobApp);

    	accomplishments = new ArrayList<>();
//    	skillRefs = new ArrayList<>();
    }

    
    /**
     * Get method that returns the Title of this CV
     * @return CV Title
     */
    public String getTitle() {
    	return title;
    }
    
    /**
     * Set method that defines the Title of this CV
     * @param title CV Title
     */
    public void setTitle(String title) {
    	this.title = title;
    }
    
    /**
     * Get method that returns the CV owner's person object URI
     * @return Person object URI
     */
    public String getPersonURI() {
    	return personURI;
    }
    
    /**
     * Set method to define who this CV belongs to
     * @param personURI The URI of the Person RDF object who owns this CV
     */
    public void setPersonURI(String personURI) {
        String URI = personURI;
        if(!URI.startsWith(":") && !URI.startsWith("<http")) {
        	if(URI.startsWith("http"))
        		URI ="<"+ URI + ">";
        	else
        		URI = ":"+URI;
        }
        this.personURI = URI;
    }
    
    /**
     * Get method that returns a description of this CV
     * @return Description of this CV
     */
    public String getDescription() {
    	return description;
    }
    
    /**
     * Set method to define a description for this CV
     * @param description Description of this CV
     */
    public void setDescription(String description) {
    	this.description = description;
    }
    
    /**
     * Get method that returns the Sector this CV is targeted for
     * @return Sector this CV is targeting
     */
    public String getTargetSector() {
    	return targetSector;
    }
    
    /**
     * Set method to define what Sector this CV is Targeting
     * @param sector The Sector this CV is targeting (For example: "IT", "HR", "Management")
     */
    public void setTargetSector(String sector) {
    	this.targetSector = sector;
    }
    
    /**
     * Get method for extra information the CV holder might have added
     * @return Extra information stored in the CV
     */
    public String getOtherInfo() {
    	return otherInfo;
    }
    
    /**
     * Set method to define any extra information the CV holder might find relevant
     * @param info Extra information to be stored in the CV
     */
    public void setOtherInfo(String info) {
    	this.otherInfo = info;
    }
    
    /**
     * Adds more information to the otherinfo field without replacing any already existing information stored
     * previously
     * @param info Extra information to be stored in the CV
     */
    public void addOtherInfo(String info) {
    	this.otherInfo = this.otherInfo + "\n" + info + "\n";
    }
    
    public void setCurrentJob(String jobPostingURI) {
    	currentJob = jobPostingURI;
    }
    
    public String getCurrentJob() {
    	return currentJob;
    }
    
    public void setRealocation(String realocation) {
    	this.realocationInfo = realocation;
    }
    
    public String getRealocation() {
    	return realocationInfo;
    }
    
    /**
     * Retrieves the List of previous work experiences the CV holder wrote on their CV
     * @return List of Work History objects stored in this CV
     */
    public List<WorkHistory> getWorkHistory()	{
    	return this.workHistory;
    }
    
    /**
     * Adds a work experience that the CV holder has done in the past
     * @param workHistory
     */
    public void addWorkHistory(WorkHistory workHistory) {
    	this.workHistory.add(workHistory);
    }
    
    /**
     * 
     * @param workHistoryURI
     */
    public void addWorkHistory(String workHistoryURI) {
    	
    }
    
    /**
     * Removes WorkHistory record from CV
     * @param workHistory workHistory Object
     */
    public void removeWorkHistory(WorkHistory workHistory) {
    	this.workHistory.remove(workHistory);
    }
    
    /**
     * Removes workHistory record from CV
     * @param workHistoryURI String URI of the workHistory to be removed
     */
    public void removeWorkHistory(String workHistoryURI) {
    	this.workHistory.remove(WorkHistory.getWorkHistory(workHistoryURI));
    }
    
    /**
     * Retrieves the Education this CV holder has been through
     * @return List of Education degrees this CV has
     */
    public List<Education> getEducation()	{
    	return this.education;
    }
    
    /**
     * Adds an Education qualification that the Person with this CV has
     * @param education Object that defines an Educational path someone has gone through,
     *  a degree or a certification for example
     */
    public void addEducation(Education education) {
    	this.education.add(education);
    }
    
    /**
     * 
     * @param educationURI
     */
    public void addEducation(String educationURI) {
    	
    }
    
    /**
     * Removes Education record from CV
     * @param education Education object to be removed from CV
     */
    public void removeEducation(Education education) {
    	this.education.remove(education);
    }
    
    /**
     * Removes Education record from CV
     * @param educationURI String URI of the Education object to be removed from the CV
     */
    public void removeEducation(String educationURI) {
    	this.education.remove(Education.getEducation(educationURI));
    }
    
    /**
     * Get all Courses associated with the CV
     * @return List of Courses in association with the CV
     */
    public List<Course> getCourses()	{
    	return this.courses;
    }
    
    /**
     * Adds Course object to CV
     * @param course Java object representation of a Certified Course
     */
    public void addCourse(Course course) {
    	this.courses.add(course);
    }
    
    /**
     * 
     * @param courseURI
     */
    public void addCourse(String courseURI) {
    	//TODO: incomplete method
    }
    
    /**
     * 
     * @param course
     */
    public void removeCourse(Course course) {
    	//TODO: incomplete method
    }
    
    /**
     * 
     * @param courseURI
     */
    public void removeCourse(String courseURI) {
    	//TODO: incomplete method
    }
    
    //TODO: Generalize all other competences to skills and then specify what each skill is?
    /**
     * 
     * @param workExperience
     * @return
     */
    public boolean hasWorkExperience ( WorkHistory workExperience) {
    	for(WorkHistory job : workHistory) {
    		//not sure I should keep the IgnoreCase since it's a URI 
    		if(job.getURI().equals(workExperience.getURI())) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 
     * @param educ
     * @return
     */
    public boolean hasEducation ( Education educ) {
    	for(Education certificate : education) {
    		//not sure I should keep the IgnoreCase since it's a URI 
    		if(certificate.getURI().equalsIgnoreCase(educ.getURI())) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 
     * @param course
     * @return
     */
    public boolean hasCourse ( Course course) {
    	for(Course certificate : courses) {
    		//not sure I should keep the IgnoreCase since it's a URI 
    		if(certificate.getURI().equalsIgnoreCase(course.getURI())) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 
     * @return
     */
    public List<Application> getApplications(){
    	return jobApplications;
    }
    
    /**
     * 
     * @param jobID
     * @return
     */
    public Application getApplication(String jobID) {
    	String jobURI = jobID;
    	if(!jobID.contains(":"))
    		jobURI = getURIFromID(jobID, ":");
    	for(Application app : jobApplications) {
    		if(app.getJobURI().equals(jobURI) || app.getJobURI().equals(jobID))
    			return app;
    	}
    	return null;
    }
    
    /**
     * 
     * @param app
     */
    public void addJobApplication(Application app) {
    	jobApplications.add(app);
    }
    
    public void removeJobApplication(String jobID) {
    	String jobURI = getURIFromID(jobID, ":");
    	Application toRemove = null;
    	for(Application app : jobApplications) {
    		if(app.getJobURI().equals(jobURI)) {
    			toRemove = app;
    		}	
    	}
    	jobApplications.remove(toRemove);
    }
    
    public List<CVSkillRef> getSkillRefs(){
    	return skills;
    }
    
    public void addSkillRef(Skill skill, String evalDate, String skillLevel, String adquiredDate, String skillLastUsedDate) {
    	CVSkillRef skillRef = new CVSkillRef(skill, evalDate, adquiredDate, skillLevel, skillLastUsedDate);
    	skills.add(skillRef);
    }
    
    public void addSkillRef(CVSkillRef skillRef) {
    	skills.add(skillRef);
    }
    
    public void removeSkillRef(CVSkillRef skillRef) {
    	for(SkillRefObject ref : skills) {
    		if(ref.getURI().equals(skillRef.getURI()) || ref.getSkillURI().equals(skillRef.getSkillURI())) {
    			skills.remove(ref);
    			return;
    		}
    			
    	}
    	
    }
    
    public CVSkillRef hasSkillRef(CVSkillRef newSkillRef) {
    	if(newSkillRef.getURI() == null)
    		return null;
    	if(newSkillRef.getSkillURI() == null)
    		return null;
    	
    	if(skills != null) {
    		for(CVSkillRef skillRef : skills) {
        		if(skillRef != null && (skillRef.getURI().equals(newSkillRef.getURI()) || skillRef.getSkillURI().equals(newSkillRef.getSkillURI())))
        			return skillRef;
        	}
    	}
    	
    	return null;
    }
    
    public CVSkillRef hasSkillRef(Skill skill) {
    	if(skills != null) {
    		for(CVSkillRef skillRef : skills) {
        		if(skillRef.getSkillURI().equals(skill.getURI()))
        			return skillRef;
        	}
    	}
    	
    	return null;
    }
    
    
    //Is not very safe, if the delete succeeds but the Save fails, data will be lost
    /**
     * 
     * @throws Exception
     */
    public void update() throws Exception {
//    	CV oldCV = CV.getCV(getURI());
    	
//    	if(this.title == null)
//    		this.title = oldCV.getTitle();
//    	if(this.personURI == null)
//    		this.personURI = oldCV.getPersonURI();
//    	if(this.description == null)
//    		this.description = oldCV.getDescription();
//    	if(this.targetSector == null)
//    		this.targetSector = oldCV.getTargetSector();
//    	if(this.otherInfo == null)
//    		this.otherInfo = oldCV.getOtherInfo();
//    	if(this.currentJob == null)
//    		this.currentJob = oldCV.getCurrentJob();
//    	
//    	for(WorkHistory wh : oldCV.getWorkHistory()) {
//    		if(!this.hasWorkExperience(wh))
//    			this.addWorkHistory(wh);
//    	}
//    	for(Education ed : oldCV.getEducation()) {
//    		if(!this.hasEducation(ed))
//    			this.addEducation(ed);
//    	}
//    	for(Course course : oldCV.getCourses()) {
//    		if(!this.hasCourse(course))
//    			this.addCourse(course);
//    	}
//    	for(CVSkillRef skillRef : oldCV.getSkillRefs()) {
//    		if(this.hasSkillRef(skillRef) == null)
//    			this.addSkillRef(skillRef);
//    	}
//    	for(Application app : oldCV.getApplications()) {
//    		if(this.getApplication(app.getJobURI()) == null)
//    			this.addJobApplication(app);
//    	}

		quickDeleteByURI(getURI());
		Save();
	}
    
    /**
     * 
     * @throws Exception
     */
	public void Save() throws Exception {

		//Insert CV
		Triple triple;
		super.rootRDFSave();
		
		Map<Triple, String> saveData = new HashMap<Triple, String>();
		
        if(title != null) {
	        //Insert CV title
	        triple = new Triple(getURI(), "cv:cvTitle", title);
	        saveData.put(triple, "String");
//	        SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(personURI != null) {
	        //Insert Person CV association
        	String URI = personURI;
            if(!(URI.contains(":") || URI.contains("<http"))) {
                URI = ":"+URI;
            }
            this.setPersonURI(URI);
	        triple = new Triple(getURI(), "cv:aboutPerson", URI);
	        saveData.put(triple, "Object");
//	        SparqlEndPoint.insertTriple(triple);
        }
        
        if(description != null) {
	        //Insert CV description
	        triple = new Triple(getURI(), "cv:hasDescription", description);
	        saveData.put(triple, "String");
//	        SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(targetSector != null) {
	        triple = new Triple(getURI(), "saro:Sector", targetSector);
	        saveData.put(triple, "String");
//	        SparqlEndPoint.insertPropertyValue(triple);
        }
             
        if(otherInfo != null) {
	        triple = new Triple(getURI() , "cv:hasOtherInfo", otherInfo);
	        saveData.put(triple, "String");
//	        SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(currentJob != null) {
        	triple = new Triple(getURI(), "qc:currentJob", currentJob);
        	saveData.put(triple, "Object");
        }
        
        if(realocationInfo != null) {
        	triple = new Triple(getURI() , "qc:realocationInfo", realocationInfo);
	        saveData.put(triple, "String");
        }
        
        //Insert CV work history list
        for(WorkHistory workHistory : workHistory) {
        	workHistory.Save();
        	triple = new Triple(getURI(), "cv:hasWorkHistory", workHistory.getURI());
        	saveData.put(triple, "Object");
//        	SparqlEndPoint.insertTriple(triple);
        }
        
        //Insert CV education list
        for(Education education : education) {
        	education.Save();
        	triple = new Triple(getURI(), "cv:hasEducation", education.getURI());
        	saveData.put(triple, "Object");
//        	SparqlEndPoint.insertTriple(triple);
        	
        }
        
        //Insert CV courses
        for(Course course : courses) {
        	course.Save();
        	triple = new Triple(getURI(), "cv:hasCourse", course.getURI());
        	saveData.put(triple, "Object");
//        	SparqlEndPoint.insertTriple(triple);
        }
        
        for(CVSkillRef skillRef : skills) {
        	skillRef.Save();
        	
        	triple = new Triple(getURI(), "qc:hasSkillRef", skillRef.getURI());
        	saveData.put(triple, "Object");
        }
        
        for(String acc : accomplishments) {
        	triple = new Triple(getURI(), "qc:refersToAccomplishment", acc);
        	saveData.put(triple, "Object");
        }
        
        for(Application app : jobApplications) {
        	app.Save();
        	triple = new Triple(getURI(), "qc:hasAppliedTo", app.getURI());
        	saveData.put(triple, "Object");
//        	SparqlEndPoint.insertTriple(triple);
        }
        SparqlEndPoint.insertTriples(saveData);

    }

	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
    public static List<CV> getCVs() throws Exception {
        String SparqlJsonResults = SparqlEndPoint.getInstances(CV.ClassType);
        List<CV> cvs = ParseResponse(SparqlJsonResults);
        if(cvs.isEmpty())
        	throw new NoSuchElementException("No CVs found in Database");
        return cvs;
    }

    //TODO:not sure what to do with this yet?
    /**
     * 
     * @param SkillURI
     * @return
     * @throws Exception 
     */
    public static List<CV> getCVs(String SkillURI) throws Exception{
        //if (SparqlEndPoint.existURI(SkillURI)){
            
            @SuppressWarnings("unused")
			Skill skill = Skill.getSkill(SkillURI);
            String SparqlJsonResults = SparqlEndPoint.getInstancesByProperty(CV.ClassType,"qc:hasAccomplishment",SkillURI);
            return ParseResponse(SparqlJsonResults);
    
        //}
        //return null;
    }

    /**
     * 
     * @param Skilllabel
     * @return
     * @throws Exception 
     */
    public static List<CV> getCVsBySkill(String Skilllabel) throws Exception{
        Skill skill = Skill.getSkillByLabel(Skilllabel);
        
        String SparqlJsonResults = SparqlEndPoint.getInstancesByProperty(CV.ClassType, "cv:hasSkill", skill.getURI());
        return ParseResponse(SparqlJsonResults);
    }

    /**
     * 
     * @param URI
     * @return
     * @throws Exception 
     */
    public static CV getCV(String URI) throws Exception{
    	String uri = URI;
        if (!uri.startsWith(":") && !uri.startsWith("<http")){
        	if(uri.startsWith("http"))
        		uri = "<" + uri + ">";
        	else
        		uri = ":"+URI;
        }
        if(!SparqlEndPoint.existURI(uri)) {
			throw new NoSuchElementException("CV with URI: " + uri + " Not found");
		}
        String properties = SparqlEndPoint.getAllProperties(uri);
//        System.out.println(properties);
        CV cv = ParseResponseToCV(properties);
        cv.setURI(uri);
        return cv;
    }
    
    public static String getCVJson(String URI) {
    	String uri = URI;
        if (!uri.startsWith(":") && !uri.startsWith("<http")){
        	if(uri.startsWith("http"))
        		uri = "<" + uri + ">";
        	else
        		uri = ":"+URI;
        }
        String properties = SparqlEndPoint.getAllProperties(uri);
        return properties;
    }
    
    /**
     * 
     * @param uri
     * @return
     * @throws Exception 
     */
    public static CV getCVbyPersonURI(String uri) throws Exception {
    	String URI = uri;
    	if(!URI.startsWith(":") && !URI.startsWith("<http")) {
        	if(URI.startsWith("http"))
        		URI ="<"+ URI + ">";
        	else
        		URI = ":"+URI;
		}
    	Person person = Person.getPerson(URI);
    	String cvURI = person.getCVURI();
    	if(cvURI != null && !cvURI.startsWith(":"))
    		cvURI = ":" + cvURI;
    	if(!SparqlEndPoint.existURI(cvURI)) {
    		if(cvURI == null)
    			throw new NoSuchElementException("CV URI is null");
			throw new NoSuchElementException("CV with URI: " + cvURI + " Not found");
		}
    	return CV.getCV(cvURI);
    }

    /**
     * 
     * @param name
     * @return
     * @throws Exception 
     */
    public static CV getCVbyPerson(String name) throws Exception {
    	
    	String properties = SparqlEndPoint.getInstancesByProperty("cv:CV", "cv:aboutPerson",":"+ name);
//    	System.out.println(properties);
    	List<CV> cvTMP = ParseResponse(properties);
    	if(cvTMP.isEmpty())
    		throw new NoSuchElementException("CV from profile " + name + " not found");
    	return cvTMP.get(0);    	
    }
    
    // Return a map with one Job to one Score
    public static HashMap<String, Integer> getJobApplicationScores(String PersonID) throws Exception{
    	CV cv = getCVbyPerson(PersonID);
    	HashMap<String, Integer> jobScores = Matching.getApplicationsScores(cv);
    	if(jobScores.isEmpty())
    		throw new NoSuchElementException("No valid applications made by user: " + PersonID);
    	return jobScores;
    }
    
//    public static HashMap<String, Integer> getJobApplicationScoresByPerson(String PersonID) throws Exception{
//    	Person person = Person.getPerson(PersonID);
//    	return getJobApplicationScores(person.getCVURI());
//    }
    
    public static HashMap<String, Integer> getJobScores(String cvURI) throws Exception{
    	CV cv = getCV(cvURI);
    	HashMap<String, Integer> jobScores = Matching.getAllJobScoresByCV(cv);
    	if(jobScores.isEmpty())
    		throw new NoSuchElementException("No recommendations available");
    	return jobScores;
    }
    
    //Implement with adjacency list, maybe use a graph implementation
    //make sure all jobs are from the current employee
    public static HashMap<String, LinkedList<JobPosting>> getJobCareerPath(String PersonURI) throws Exception{
    	HashMap<String, LinkedList<JobPosting>> careerPaths = new HashMap<String, LinkedList<JobPosting>>();
    	
    	//get CV, then get JobPosting with curJob, then get hiring org from JobPosting so the company name doesn't have to be an input string
    	Person person = Person.getPerson(PersonURI);
    	CV cv = CV.getCV(person.getCVURI());
    	JobPosting currentJob;
    	if(person.getCurrentJobURI() != null)
    		currentJob = JobPosting.getJobPosting(person.getCurrentJobURI());
    	else if(cv.getCurrentJob() != null)
    		currentJob = JobPosting.getJobPosting(cv.getCurrentJob());
    	else throw new NoCurrentJobException("Selected Person: " + person.getURI() + " does not have a current Job");
		String company = currentJob.getHiringOrg();
	    
	    HashMap<String, Integer> JobScores = Matching.getCompanyScores(cv, company);
	    
	    for(Map.Entry<String, Integer> jobScore : JobScores.entrySet()) {
	    	careerPaths.put(jobScore.getKey(), new LinkedList<JobPosting>());
	    }
	    for (Map.Entry<String, Integer> jobScore : JobScores.entrySet()) { 
//	        System.out.println("Key = " + jobScore.getKey() +  
//	                      ", Value = " + jobScore.getValue()); 
	    	JobPosting curJob = JobPosting.getJobPosting(jobScore.getKey());
            
            //Need to check if the job is in the workhistory of the next one in line for the progression
            List<JobPosting> precedentJobs = getPrecedentJobs(JobScores, curJob);
            
            //for each job precendent get the job list and add this job(jobScore associated job) to that list to create an adjacency
            for(JobPosting jobPrecedent : precedentJobs) {
            	LinkedList<JobPosting> tmp = careerPaths.get(jobPrecedent.getURI());
            	tmp.add(curJob);
            	careerPaths.put(jobPrecedent.getURI(), tmp);
            }
    
        } 
	    	
    	//Create last cycle to remove the jobs in the precedents of the current job and other non related jobs
    	
    	return careerPaths;
	
    	
    }
    
    //Returns best JobPosting match to progress from
    //the first element of the linked list in the different lists is chosen to be the best match of the job entry if it's the most similar and is a workhistory requirement of job
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
    
    /**
     * 
     * @param cvURI
     * @return
     * @throws Exception 
     */
    //choose return
    public static List<Skill> getSkillRecomendations(String PersonID, String jobURI) throws Exception{
    	CV cv = CV.getCVbyPerson(PersonID);
    	JobPosting job;
		
		job = JobPosting.getJobPosting(jobURI);
		List<SkillJobReq> jobSkills = job.getJobSkillReqRefs();
	    List<CVSkillRef> cvSkills = cv.getSkillRefs();
	    List<Skill> recomendations = new ArrayList<Skill>();
	    boolean found = false;
	    
	    for(SkillJobReq jobSkillRef : jobSkills) {
	    	for(CVSkillRef skillRef : cvSkills) {
	    		if(skillRef.getSkillURI().equals(jobSkillRef.getSkillURI())){
	    			found = true;
	    		}
	    	}
	    	
	    	if(!found)	{
	    		recomendations.add(Skill.getSkill(jobSkillRef.getSkillURI()));
	    	}
	    	
	    	found = false;
	    }
	    
	    return recomendations;
		
    	
    }
    
    public static List<JobPosting> getJobRecomendations(String PersonID) throws Exception{
    	CV cv = CV.getCVbyPerson(PersonID);
    	
    	List<JobPosting> jobRecomendations = new ArrayList<JobPosting>();
    	
    	HashMap<String, Integer> scores = Matching.getAllJobScoresByCV(cv);
    	
    	for(Map.Entry<String, Integer> nextEntry : scores.entrySet()) {
    		if(nextEntry.getValue() > 50) {
    			try {
					jobRecomendations.add(JobPosting.getJobPosting(nextEntry.getKey()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
    		}
    	}
    		
    	return jobRecomendations;
    }
    
    /**
     * 
     * @param URI
     * @return
     * @throws Exception 
     */
    public static CV deleteObject(String URI) throws Exception {
    	CV cv = CV.getCV(URI);
    	SparqlEndPoint.deleteObjectByUri(cv.getURI());
    	deleteURIAssociations(cv.getURI());
    	return cv;
    } 
    
    /**
     * 
     * @param SparqlJsonResults
     * @return
     * @throws Exception 
     */
    private static List<CV> ParseResponse(String SparqlJsonResults) throws Exception{

        List<CV> CVs = new ArrayList<>();  
        InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);
        
        while (results.hasNext()) {

            QuerySolution soln = results.nextSolution();
            Resource res= soln.getResource("subject");

            //String ID = String.valueOf(res);            
            String ID =  res.getLocalName();            
            try {
            	CV cv = getCV(prefix + ID);
            	cv.setID(ID);   
              //cv.setURI(StringUtils.substringAfter(ID,"http://rdfs.org/resume-rdf/cv.rdfs#"));   

              CVs.add(cv);  
            }
            catch(Exception e) {
            	System.out.println("Tried to get cv with ID: " + ID + " and was unsucessful");
            }
        } 
        return CVs; 

    }
    
    /**
     * 
     * @param SparqlJsonResults
     * @return
     */
    private static CV ParseResponseToCV(String SparqlJsonResults){

        InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);

//        System.out.println("============================================");
        
        CV cv = new CV();

        while (results.hasNext()) {

            QuerySolution soln = results.nextSolution();
//            System.out.println("soln: "+soln.toString());

            //String predicate = String.valueOf(soln.getResource("predicate"));   
            //System.out.println("predicate: "+predicate);

            Resource res= soln.getResource("predicate");

            RDFNode Onode = soln.get("object");
            String object="";
            if (Onode.isResource()) {
                object = String.valueOf(soln.getResource("object"));
            }
            else{
                object = String.valueOf(soln.getLiteral("object"));   
            }

            //System.out.println("field:"+res.getLocalName()+". value:"+object);

            /* to repalce the switch
            try {
                Field field = CV.class.getField(res.getLocalName());
                field.set(cv, object); 
                System.out.println(res.getLocalName()+":"+field.get(cv).toString());

            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            */
            
            switch (res.getLocalName()) {

                case "label":
                    String label = object;   
//                    System.out.println("label: " + label);
                    cv.setLabel(label);
                    break;
                    
                case "cvTitle":
                	String title = object;  
//                	System.out.println("cvTitle: " + title);
                	cv.setTitle(title);
                	break;
            
                case "aboutPerson":
                	String personURI = object;  
                	if(personURI.contains("#"))
                		personURI = personURI.substring(personURI.indexOf("#") + 1);
//                	System.out.println("aboutPerson: " + personURI);
                	cv.setPersonURI(":" + personURI);
                	break;
                	
                case "hasOtherInfo":
                	String otherInfo = object;
                	cv.setOtherInfo(otherInfo);
                	break;
                	                	
                case "hasWorkHistory":
                	String workHistory = object;  
                	if(workHistory.contains("#"))
                		workHistory = workHistory.substring(workHistory.indexOf("#") + 1);
                	cv.addWorkHistory(WorkHistory.getWorkHistory(workHistory));
                	break;
                	

                case "hasDescription":
                	String description = object;  
//                	System.out.println("hasDescription: " + description);
                	cv.setDescription(description);
                	break;

                case "comment":
                	String comment = object;  
//                	System.out.println("comment : " + comment);
                	cv.setComment(comment);
                	break;
                	
                case "realocationInfo":
                	String realocation = object;
                	cv.setRealocation(realocation);
                	break;

                case "hasEducation":
                	String education = object;  
                	if(education.contains("#"))
                		education = education.substring(education.indexOf("#") + 1);
                	cv.addEducation(Education.getEducation(education));
                	break;
                	

                case "hasCourse":
                	String course = object;  
                	if(course.contains("#"))
                		course = course.substring(course.indexOf("#") + 1);
                	cv.addCourse(Course.getCourse(course));
                	break;
                	
                case "hasAppliedTo":
                	String applicationURI = object;
                	if(applicationURI.contains("#"))
                		applicationURI = applicationURI.substring(applicationURI.indexOf("#") + 1);
					try {
						cv.addJobApplication(Application.getApplication(applicationURI));
					} catch (Exception e) {
						e.printStackTrace();
					}
                	break;
                	
                case "Sector":
                	String sector = object;
                	cv.setTargetSector(sector);
                	break;
                	
                case "currentJob":
                	String curJob = object;
                	if(curJob.contains("#"))
                		curJob = curJob.substring(curJob.indexOf("#") + 1);
                	cv.setCurrentJob(":" + curJob);
                	break;
                	
                case "hasSkill":
                    String skillURI = object;
                    if(skillURI.contains("#"))
                        skillURI = skillURI.substring(skillURI.indexOf("#") + 1);
                    Skill skill = Skill.getSkill(skillURI);
                    if(cv.hasSkillRef(skill) == null)
                        cv.addSkillRef(skill, null, null, null, null);
                	break;
                	
                case "hasSkillRef":
                	String skillRef = object;
                	if(skillRef.contains("#"))
                		skillRef = skillRef.substring(skillRef.indexOf("#") + 1);
                	try {
                		cv.addSkillRef(CVSkillRef.getCVSkillRefObject(skillRef));
                	}
                	catch(Exception e) {
                		e.printStackTrace();
                	}
                	break;
                	
                case "refersToAccomplishment":
                	String accomplishment = object;
                	if(accomplishment.contains("#"))
                		accomplishment = accomplishment.substring(accomplishment.indexOf("#") + 1);
                	Skill tmp = Skill.getSkill(accomplishment);
                    if(cv.hasSkillRef(tmp) == null)
                        cv.addSkillRef(tmp, null, null, null, null);
                	cv.addAccomplishment(accomplishment);
                	break;
                	
                	
                default:
                    break;
            }
            
            //String ID = String.valueOf(soln.getResource("predicate"));
            //System.out.println(ID);   
            //cv.setId(ID);   
            //cv.setId(StringUtils.substringAfter(ID,"http://rdfs.org/resume-rdf/cv.rdfs#"));   

        } 
        return cv; 

    }
    
    public void addAccomplishment(String accomplishment) {
		accomplishments.add(accomplishment);
	}
    
    public List<String> getAccomplishments(){
    	return accomplishments;
    }

	/**
     * 
     * @return 
     */
    public String getInfo() {
    	return "URI: " + getURI() + "\n" +
    			"Label: " + getLabel() + "\n" + 
    			"Person: " + personURI;
    }

	public List<Skill> getSkills() {
        List<Skill> skills = new ArrayList<Skill>(this.skills.size());
		
        for(SkillRefObject skillRef : this.skills) {
            if(skillRef.getSkillURI() != null)
                skills.add(Skill.getSkill(skillRef.getSkillURI()));
            else
                skills.add(skillRef.getSkill());
		}
		
		return skills;
	}
    
	//TODO: Complete method that compares all variables in this and compareTo
    public boolean equals(CV compareTo) {
    	
    	boolean contains = false;
    	
    	if(!this.getURI().equals(compareTo.getURI()))
    		return false;
    	if(!this.getID().equals(compareTo.getID()))
    		return false;
    	
    	if(this.getTitle() == null && compareTo.getTitle() != null) {
    		return false;
    	}
    	else if(this.getTitle() == null && compareTo.getTitle() == null) {
    		
    	}
    	else if(!this.getTitle().equals(compareTo.getTitle())){
        	return false;
    	}
    	
    	if(this.getPersonURI() == null && compareTo.getPersonURI() != null) {
    		return false;
    	}
    	else if(this.getPersonURI() == null && compareTo.getPersonURI() == null) {
    		
    	}
    	else if(!this.getPersonURI().equals(compareTo.getPersonURI())){
        	return false;
    	}
    	
    	if(this.getDescription() == null && compareTo.getDescription() != null) {
    		return false;
    	}
    	else if(this.getDescription() == null && compareTo.getDescription() == null) {
    		
    	}
    	else if(!this.getDescription().equals(compareTo.getDescription())){
        	return false;
    	}
    	
    	if(this.getTargetSector() == null && compareTo.getTargetSector() != null) {
    		return false;
    	}
    	else if(this.getTargetSector() == null && compareTo.getTargetSector() == null) {
    		
    	}
    	else if(!this.getTargetSector().equals(compareTo.getTargetSector())){
        	return false;
    	}
    	
    	if(this.getOtherInfo() == null && compareTo.getOtherInfo() != null) {
    		return false;
    	}
    	else if(this.getOtherInfo() == null && compareTo.getOtherInfo() == null) {
    		
    	}
    	else if(!this.getOtherInfo().equals(compareTo.getOtherInfo())){
        	return false;
    	}
    	
    	if(this.getCurrentJob() == null && compareTo.getCurrentJob() != null) {
    		return false;
    	}
    	else if(this.getCurrentJob() == null && compareTo.getCurrentJob() == null) {
    		
    	}
    	else if(!this.getCurrentJob().equals(compareTo.getCurrentJob())){
        	return false;
    	}
    	
    	if(this.getRealocation() == null && compareTo.getRealocation() != null) {
    		return false;
    	}
    	else if(this.getRealocation() == null && compareTo.getRealocation() == null) {
    		
    	}
    	else if(!this.getRealocation().equals(compareTo.getRealocation())){
        	return false;
    	}

    	for(WorkHistory workHistory : this.getWorkHistory()) {
    		contains = false;
    		for(WorkHistory workHistory2 : compareTo.getWorkHistory()) {
    			if(workHistory.equals(workHistory2)) {
    				contains = true;
    				break;
    			}
    				
    		}
    		if(!contains)
    			return false;
    	}

    	for(WorkHistory workHistory : compareTo.getWorkHistory()) {
    		contains = false;
    		for(WorkHistory workHistory2 : this.getWorkHistory()) {
    			if(workHistory.equals(workHistory2)) {
    				contains = true;
    				break;
    			}
    				
    		}
    		if(!contains)
    			return false;
    	}

    	for(Education education : this.getEducation()) {
    		contains = false;
    		for(Education education2 : compareTo.getEducation()) {
    			if(education.equals(education2)) {
    				contains = true;
    				break;
    			}
    				
    		}
    		if(!contains)
    			return false;
    	}
    	
    	for(Education education : compareTo.getEducation()) {
    		contains = false;
    		for(Education education2 : this.getEducation()) {
    			if(education.equals(education2)) {
    				contains = true;
    				break;
    			}
    				
    		}
    		if(!contains)
    			return false;
    	}
    	
    	for(Course course : this.getCourses()) {
    		contains = false;
    		for(Course course2 : compareTo.getCourses()) {
    			if(course.equals(course2)) {
    				contains = true;
    				break;
    			}
    				
    		}
    		if(!contains)
    			return false;
    	}
    	
    	for(Course course : compareTo.getCourses()) {
    		contains = false;
    		for(Course course2 : this.getCourses()) {
    			if(course.equals(course2)) {
    				contains = true;
    				break;
    			}
    				
    		}
    		if(!contains)
    			return false;
    	}
    	
    	for(CVSkillRef skillRef : this.getSkillRefs()) {
    		contains = false;
    		for(CVSkillRef skillRef2 : compareTo.getSkillRefs()) {
    			if(skillRef.equals(skillRef2)) {
    				contains = true;
    				break;
    			}
    				
    		}
    		if(!contains)
    			return false;
    	}

    	for(CVSkillRef skillRef : compareTo.getSkillRefs()) {
    		contains = false;
    		for(CVSkillRef skillRef2 : this.getSkillRefs()) {
    			if(skillRef.equals(skillRef2)) {
    				contains = true;
    				break;
    			}
    		}
    		if(!contains)
    			return false;
    	}

    	for(Skill skill : this.getSkills()) {
    		contains = false;
    		for(Skill skill2 : compareTo.getSkills()) {
    			if(skill.equals(skill2)) {
    				contains = true;
    				break;
    			}
    				
    		}
    		if(!contains)
    			return false;
    	}

    	for(Skill skill : compareTo.getSkills()) {
    		contains = false;
    		for(Skill skill2 : this.getSkills()) {
    			if(skill.equals(skill2)) {
    				contains = true;
    				break;
    			}
    		}
    		if(!contains)
    			return false;
    	}
    	    	    	
    	return true;
    }
    

}
