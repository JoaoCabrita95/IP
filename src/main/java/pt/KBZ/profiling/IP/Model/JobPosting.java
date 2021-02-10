package IP.Model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class JobPosting extends RDFObject {
	
	private static final String ClassType = "saro:JobPosting";
	public static final String prefix = "saro:";
	private static final String schemaPrefix = "schema:";
//	private String URI;
//	private String ID;
//	private String Label;
//	//Seniority Level in the comment section of JobPosting
//	private String Comment;
	private String creator_id;
	private String jobDescription;
	private String contractType;
	private String sector;
	private String occupation;
	private String listingOrganization;
	private String hiringOrganization;
	private String jobLocation;
	private String country;
	private String state;
	private String city;
	private String startDate;
	private String endDate;
	private String seniorityLevel;
	private String expectedSalary;
	private String salaryCurrency;
	//match with courses
	private List<Course> coursesReq;
	//match with work experience
	private List<WorkHistory> workExperienceReq;
	//match with education and courses?
	private List<Education> educationReq;
	private List<Application> jobApplications;
	private List<SkillJobReq> jobSkillReqs;
	
	//Might have to change according to QualiChain ontology model
	
	//TODO: Add constructors for different cases if found necessary
	public JobPosting() {
		super(ClassType, prefix);
		coursesReq = new ArrayList<Course>();
		workExperienceReq = new ArrayList<WorkHistory>();
		educationReq = new ArrayList<Education>();
		jobApplications = new ArrayList<Application>();
		jobSkillReqs = new ArrayList<SkillJobReq>();
	}
	
	public JobPosting(String id, String label,String comment, String jobDescription, String contractType, String sector, String occupation,
			String listingOrg, String hiringOrg, String jobLoc, String startDate, String endDate, String seniorityLevel, String expSalary,
			String salCur, String creator_id, List<SkillJobReq> jobSkillReqs, List<Course> capReq, List<WorkHistory> knowReq, List<Education> exptReq) {
		super(ClassType, prefix, id, label, comment);
//		this.ID = id;
//		this.URI = prefix+id;
//		this.Label = label;
//		this.Comment = comment;
		if(creator_id != null) {
			if(creator_id.startsWith(":"))
				this.creator_id = creator_id;
			else 
				this.creator_id = ":" + creator_id;
		}
		else 
			this.creator_id = creator_id;
		this.jobDescription = jobDescription;
		this.contractType= contractType;
		this.sector = sector;
		this.occupation = occupation;
		this.listingOrganization = listingOrg;
		this.hiringOrganization = hiringOrg;
		this.jobLocation = jobLoc;
		country = null;
		state = null;
		city = null;
		this.startDate = startDate;
		this.endDate = endDate;
		this.seniorityLevel = seniorityLevel;
		this.expectedSalary = expSalary;
		this.salaryCurrency = salCur;
		
		
		if(jobSkillReqs == null) {
			this.jobSkillReqs = new ArrayList<SkillJobReq>();
		}	
		else {
			this.jobSkillReqs = jobSkillReqs;
		}

		
		if(capReq == null) 
			this.coursesReq = new ArrayList<Course>();
		else
			this.coursesReq = capReq;
		
		if(knowReq == null)
			this.workExperienceReq = new ArrayList<WorkHistory>();
		else
			this.workExperienceReq = knowReq;
		
		if(exptReq == null)
			this.educationReq = new ArrayList<Education>();
		else
			this.educationReq = exptReq;
		if(jobApplications == null)
			this.jobApplications = new ArrayList<Application>();
		
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getcreator_id() {
		return creator_id;
	}
	
	public void setcreator_id(String creator_id) {
		this.creator_id = creator_id;
	}
    
    public String getJobDescription() {
    	return jobDescription;
    }
    
    public void setJobDescription(String description) {
    	this.jobDescription = description;
    }
    
    public String getContractType() {
    	return contractType;
    }
    
    public void setContractType(String contractType) {
    	this.contractType = contractType;
    }
     
    public String getSector() {
    	return sector;
    }
    
    public void setSector(String sector) {
    	this.sector = sector;
    }
    
    public String getOccupation() {
    	return occupation;
    }
    
    public void setOccupation(String occupation) {
    	this.occupation = occupation;
    }
    
    public String getListingOrg() {
    	return listingOrganization;
    }
    
    public void setListingOrg(String listingOrg) {
    	this.listingOrganization = listingOrg;
    }
    
    public String getHiringOrg() {
    	return hiringOrganization;
    }
    
    public void setHiringOrg(String hiringOrg) {
    	this.hiringOrganization = hiringOrg;
    }
    
    public String getjobLocation() {
    	return jobLocation;
    }
    
    public void setjobLocation(String jobLoc) {
    	this.jobLocation = jobLoc;
    }
    
    public String getStartDate() {
    	return startDate;
    }
    
    public void setStartDate(String startDate) {
    	this.startDate = startDate;
    }
    
    public String getEndDate() {
    	return endDate;
    }
    
    public void setEndDate(String endDate) {
    	this.endDate = endDate;
    }
    
    public String getseniorityLevel() {
    	return seniorityLevel;
    }
    
    public void setseniorityLevel(String seniorityLevel) {
    	this.seniorityLevel = seniorityLevel;
    }
    
    public String getExpectedSalary() {
		return expectedSalary;
	}
	
	public void setExpectedSalary(String expectedSalary) {
		this.expectedSalary = expectedSalary;
	}
    
    public String getSalaryCurrency() {
    	return salaryCurrency;
    }
    
    public void setSalaryCurrency(String cur) {
    	this.salaryCurrency = cur;
    }
    
    public List<SkillJobReq> getJobSkillReqRefs(){
    	return jobSkillReqs;
    }
    
    public void addSkillRef(Skill skill, String priorityLevel, String skillLevel) {
    	SkillJobReq skillJobReq = new SkillJobReq(skill, priorityLevel, skillLevel);
    	jobSkillReqs.add(skillJobReq);
    }
    
    public void addSkillRef(SkillJobReq skillJobReq) {
    	jobSkillReqs.add(skillJobReq);
    }
    
    public SkillJobReq removeSkillRef(SkillJobReq skillJobReq) {
    	for(SkillJobReq ref : jobSkillReqs) {
    		if(ref.getURI().equals(skillJobReq.getURI()) || ref.getSkillURI().equals(skillJobReq.getSkillURI())) {
    			jobSkillReqs.remove(ref);
    			return ref;
    		}
    			
    	}
    	return null;
    	
    }
    
    public SkillJobReq hasSkillRef(SkillJobReq skillJobReq) {
    	if(jobSkillReqs != null) {
    		for(SkillJobReq skillRef : jobSkillReqs) {
        		if(skillRef.getURI().equals(skillJobReq.getURI()) || skillRef.getSkillURI().equals(skillJobReq.getSkillURI()))
        			return skillRef;
        	}
    	}
    	
    	return null;
    }
    
    public SkillJobReq hasSkillRef(Skill skill) {
    	if(jobSkillReqs != null) {
    		for(SkillJobReq skillRef : jobSkillReqs) {
        		if(skillRef.getSkillURI().equals(skill.getURI()))
        			return skillRef;
        	}
    	}
    	
    	return null;
    }
    
    public List<Course> getCapabilityReq(){
    	return coursesReq;
    }
    
    public void addCapabilityReq(Course capReq) {
    	this.coursesReq.add(capReq);
    }
    
    public List<WorkHistory> getworkExperienceReq(){
    	return workExperienceReq;
    }
    
    public void addworkExperienceReq(WorkHistory KnowledgeReq) {
    	this.workExperienceReq.add(KnowledgeReq);
    }
    
    public List<Education> getEducationReq(){
    	return educationReq;
    }
    
    public void addEducationReq(Education expertiseReq) {
    	this.educationReq.add(expertiseReq);
    }
    
    public List<Application> getApplications() {
		return this.jobApplications;
	}

	public void apply(Application application) {
		this.jobApplications.add(application);
	}
	
	public void removeApplications(String appID) {
		String appURI = getURIFromID(appID, ":");
    	Application toRemove = null;
    	for(Application app : jobApplications) {
    		if(app.getURI().equals(appURI)) {
    			toRemove = app;
    		}	
    	}
    	jobApplications.remove(toRemove);
	}
	
	//Is not very safe, if the delete succeeds but the Save fails, data will be lost
	public void update() throws Exception {
		quickDeleteByURI(getURI());
		Save();
	}

    public void Save() throws Exception {
    	
    	super.rootRDFSave();
    	Triple triple;
    	
		Map<Triple, String> saveData = new HashMap<Triple, String>();
    	
    	if(creator_id != null) {
    		triple = new Triple(getURI(), "saro:createdBy", creator_id);
    		saveData.put(triple, "Object");
//    		SparqlEndPoint.insertPropertyValue(triple);
    	}
        
        if(jobDescription != null) {
            triple = new Triple(getURI(), "saro:describes", jobDescription);
            saveData.put(triple, "String");
//            SparqlEndPoint.insertPropertyValue(triple);	
        }
        
        if(contractType != null) {
            triple = new Triple(getURI(), schemaPrefix +  "employmentType", contractType);
            saveData.put(triple, "String");
//            SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(sector != null) {
        	triple = new Triple(getURI(), "saro:advertisedIn", sector);
        	saveData.put(triple, "String");
//            SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(occupation != null) {
        	triple = new Triple(getURI(), schemaPrefix + "occupationalCategory", occupation);
        	saveData.put(triple, "String");
//    		SparqlEndPoint.insertPropertyValue(triple);
    	}
        
        if(listingOrganization != null) {
        	//Not sure if it should be an object or a simple value
//        	triple = new Triple(getURI(), schemaPrefix + "listingOrganization", schemaPrefix + ListingOrganization);
//    		SparqlEndPoint.insertTriple(triple);
    		triple = new Triple(getURI(), schemaPrefix + "listingOrganization", listingOrganization);
    		saveData.put(triple, "String");
//    		SparqlEndPoint.insertPropertyValue(triple);
    	}
        
        if(hiringOrganization != null) {
        	//Not sure if it should be an object or a simple value
//        	triple = new Triple(getURI(), schemaPrefix + "hiringOrganization", schemaPrefix +  hiringOrganization);
//    		SparqlEndPoint.insertTriple(triple);
    		triple = new Triple(getURI(), schemaPrefix + "hiringOrganization", hiringOrganization);
    		saveData.put(triple, "String");
//    		SparqlEndPoint.insertPropertyValue(triple);
    	}
        
        if(jobLocation != null) {
        	triple = new Triple(getURI(), schemaPrefix + "jobLocation",  jobLocation);
        	saveData.put(triple, "String");
//    		SparqlEndPoint.insertPropertyValue(triple);
    	}    
        
        if(country != null) {
        	triple = new Triple(getURI(), "vcard:country-name", country);
        	saveData.put(triple, "String");
        }
        
        if(state != null) {
        	triple = new Triple(getURI(), "vcard:region", state);
        	saveData.put(triple, "String");
        }
        
        if(city != null) {
        	triple = new Triple(getURI(), "vcard:locality", city);
        	saveData.put(triple, "String");
        }
        
        if(startDate != null) {
        	triple = new Triple(getURI(), "qc:startDate", startDate);
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(endDate != null) {
        	triple = new Triple(getURI(), "qc:endDate", endDate);
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(seniorityLevel != null) {
        	triple = new Triple(getURI(), prefix + "level", seniorityLevel);
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        for(SkillJobReq skillRef : jobSkillReqs) {
        	skillRef.Save();
        	triple = new Triple(getURI(), "qc:hasSkillRef", skillRef.getURI());
        	saveData.put(triple, "Object");
        }
        
        for(Course capability : coursesReq) {
        	capability.Save();
        	triple = new Triple(getURI(), "saro:requiresCapability", capability.getURI());
        	saveData.put(triple, "Object");
//        	SparqlEndPoint.insertTriple(triple);
        }
        
        for(WorkHistory knowledge : workExperienceReq) {
        	knowledge.Save();
        	triple = new Triple(getURI(), "saro:requiresKnowledge", knowledge.getURI());
        	saveData.put(triple, "Object");
//        	SparqlEndPoint.insertTriple(triple);
        }
        
        for(Education expertise : educationReq) {
        	expertise.Save();
        	triple = new Triple(getURI(), "saro:requiresExpertise", expertise.getURI());
        	saveData.put(triple, "Object");
//        	SparqlEndPoint.insertTriple(triple);
        }
        
        for(Application jobApp: jobApplications) {
        	//not sure if necessary because if the application is on the jobposting it should already have been created by the cv first
//        	if(!RDFObject.exists(jobApp.getLabel()))
//        		jobApp.Save();
        	triple = new Triple(getURI(), "qc:hasJobApplication", jobApp.getURI());
        	saveData.put(triple, "Object");
//        	SparqlEndPoint.insertTriple(triple);
        }
        
       SparqlEndPoint.insertTriples(saveData); 

    }
	
    public static List<JobPosting> getJobPostings() throws Exception{
        String SparqlJsonResults = SparqlEndPoint.getInstances(JobPosting.ClassType);
        List<JobPosting> jobs = ParseResponse(SparqlJsonResults);
        if(jobs.isEmpty())
        	throw new NoSuchElementException("There are no Job Postings in the database found");
        return jobs;
    }

    //TODO: Analyze and fix this method, returning odd results
    public static JobPosting getJobPosting(String URI) throws Exception{
    	String uri = URI;
        if (!uri.startsWith(":") && !uri.startsWith("<http")){
        	if(uri.startsWith("http"))
        		uri = "<" + uri + ">";
        	else
        		uri = ":"+URI;
        }
        
        if(!SparqlEndPoint.existURI(uri)) {
			throw new NoSuchElementException("JobPosting with URI: " + uri + " Not found");
		}
        
        String properties = SparqlEndPoint.getAllProperties(uri);
        JobPosting jp = ParseResponseToJobPost(properties);
        jp.setURI(uri);
        return jp;
    }
    

    //Could be a problem if there are multiple job posts with similar labels?
    public static JobPosting getJobPostingByLabel(String label) throws Exception{
    	String SparqlJsonResults = SparqlEndPoint.getInstancesByPartialLabel(ClassType, label);
		String uri = SparqlEndPoint.ParseResponseToURI(SparqlJsonResults);
		return getJobPosting("<" + uri + ">");
    }
    
    public static List<String> getAllHiringOrganizations() throws Exception{
    	String SparqlJsonResults = SparqlEndPoint.getAllPropertiesByType("schema:hiringOrganization");
    	List<String> orgs = ParsePropertyResponse(SparqlJsonResults);
    	if (orgs.isEmpty())
    		throw new NoSuchElementException("No Hiring companies found");
    	return orgs;
    }
    
    public static List<JobPosting> getJobPostingsBySector(String sector) throws Exception{
    	String SparqlJsonResults = SparqlEndPoint.getInstancesByProperty(ClassType, "saro:advertisedIn","\"" + sector + "\"");
    	List<JobPosting> jobs = ParseResponse(SparqlJsonResults);
    	if (jobs.isEmpty())
    		throw new NoSuchElementException("No Job Postings found in sector: " + sector);
    	return jobs;
    }
    
    public static List<JobPosting> getJobPostingsByListingOrg(String listingOrg) throws Exception{
    	String SparqlJsonResults = SparqlEndPoint.getInstancesByProperty(ClassType, "schema:listingOrganization", "\"" + listingOrg + "\"");
    	List<JobPosting> jobs = ParseResponse(SparqlJsonResults);
    	if (jobs.isEmpty())
    		throw new NoSuchElementException("No Job Postings found listed by: " + listingOrg);
    	return jobs;
    }
    
    public static List<JobPosting> getJobPostingsByHiringOrg(String hiringOrg) throws Exception{
    	String SparqlJsonResults = SparqlEndPoint.getInstancesByProperty(ClassType, "schema:hiringOrganization", "\"" +  hiringOrg + "\"");
    	List<JobPosting> jobs = ParseResponse(SparqlJsonResults);
    	if (jobs.isEmpty())
    		throw new NoSuchElementException("No Job Postings found made by: " + hiringOrg);
    	return jobs;
    }
    
    public static List<JobPosting> getJobPostingsByContractType(String contractType) throws Exception{
    	String SparqlJsonResults = SparqlEndPoint.getInstancesByPropertyPartialValue(ClassType, "schema:employmentType", "\"" + contractType + "\"");
    	List<JobPosting> jobs = ParseResponse(SparqlJsonResults);
    	if (jobs.isEmpty())
    		throw new NoSuchElementException("No Job Postings found with contract type: " + contractType);
    	return jobs;
    }
    
    public static JobPosting deleteObject(String URI) throws Exception {
    	JobPosting jp = JobPosting.getJobPosting(URI);
    	SparqlEndPoint.deleteObjectByUri(jp.getURI());
    	return jp;
    }
    
    
    
    private static List<JobPosting> ParseResponse(String SparqlJsonResults) throws Exception{

        List<JobPosting> JPs = new ArrayList<>();  
        InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);
        
        while (results.hasNext()) {

            QuerySolution soln = results.nextSolution();
            Resource res= soln.getResource("subject");

            //String ID = String.valueOf(res);            
            String ID =  res.getLocalName(); 
            try {
            	JobPosting jp = getJobPosting(":" + ID);

                jp.setID( ID);   
                //cv.setURI(StringUtils.substringAfter(ID,"http://rdfs.org/resume-rdf/cv.rdfs#"));   

                JPs.add(jp);  
            }
            catch (Exception e) {
            	System.out.println("Tried retrieving Job Posting with ID: " + ID + " but was unsucessful");
            }
            
        } 
        return JPs; 

    }
    
    private static JobPosting ParseResponseToJobPost(String SparqlJsonResults){

        InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);

//        System.out.println("============================================");
        
        JobPosting jp = new JobPosting();

        while (results.hasNext()) {

            QuerySolution soln = results.nextSolution();
            
            //System.out.println("soln: "+soln.toString());

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

            switch (res.getLocalName()) {

                case "label":
                    String label = object;   
                    jp.setLabel(label);
                    break;

                case "comment":
                	String comment = object;
                	jp.setComment(comment);
                	break;
                	
                case "describes":
                    String description = object;     
                	if(description.contains("#"))
                		description = description.substring(description.indexOf("#") + 1);
                    jp.setJobDescription(description);
                    break;

                case "employmentType":
                    String empType = object;   
                    jp.setContractType(empType);
                    break;

                case "advertisedIn":
                	String sector = object;
                	if(sector.contains("#"))
                		sector = sector.substring(sector.indexOf("#") + 1);
                	jp.setSector(sector);
                	break;
                	
                case "occupationalCategory":
                	String occupation = object;
                	jp.setOccupation(occupation);
                	break;
                	
                case "listingOrganization":
                	String listingOrg = object;
//                	System.out.println("Listed by: " + listingOrg);
                	//contains(prefix associated with the triple in question) is probably the best way to do this without potentially
                	//erasing part of the triple if it contains a "/"
                	if(listingOrg.contains("/"))
                			listingOrg = listingOrg.substring(listingOrg.lastIndexOf("/") + 1);
                	jp.setListingOrg(listingOrg);
                	break;
                	
                case "hiringOrganization":
                	String hiringOrg = object;
                	if(hiringOrg.contains("/"))
                		hiringOrg = hiringOrg.substring(hiringOrg.lastIndexOf("/") + 1);
                	jp.setHiringOrg(hiringOrg);
                	break;

                case "jobLocation":
                	String jobLoc = object;
                	if(jobLoc.contains("/"))
                		jobLoc = jobLoc.substring(jobLoc.lastIndexOf("/") + 1);
                	jp.setjobLocation(jobLoc);
                	break;

                case "requiresCapability":
                	String reqCapability = object;
                	if(reqCapability.contains("#"))
                		reqCapability = reqCapability.substring(reqCapability.indexOf("#") + 1);
                	jp.addCapabilityReq(Course.getCourse(reqCapability));
                	break;

                case "requiresKnowledge":
                	String reqKnowledge = object;
                	if(reqKnowledge.contains("#"))
                		reqKnowledge = reqKnowledge.substring(reqKnowledge.indexOf("#") + 1);
                	jp.addworkExperienceReq(WorkHistory.getWorkHistory(reqKnowledge));
                	break;

                case "requiresExpertise":
                	String reqExpertise = object;
                	if(reqExpertise.contains("#"))
                		reqExpertise = reqExpertise.substring(reqExpertise.indexOf("#") + 1);
                	jp.addEducationReq(Education.getEducation(reqExpertise));
                	break;

                case "requiresSkill":
                    String reqSkill = object;
                    if(reqSkill.contains("#"))
                        reqSkill = reqSkill.substring(reqSkill.indexOf("#") + 1);
                    Skill skillReq = Skill.getSkill(reqSkill);
                    if(jp.hasSkillRef(skillReq) == null)
                        jp.addSkillRef(skillReq, null, null);
                            	
                case "hasSkillRef":
                	String skillRef = object;
                	if(skillRef.contains("#"))
                		skillRef = skillRef.substring(skillRef.indexOf("#") + 1);
                	jp.addSkillRef(SkillJobReq.getJobSkillRefObject(skillRef));
                	break;
                	
                case "startDate":
                	String startDate = object;
                	jp.setStartDate(startDate);
                	break;
                
                case "endDate":
                	String endDate = object;
                	jp.setEndDate(endDate);
                	break;
                	
                case "level": 
                	String level = object;
                	jp.setseniorityLevel(level);
                	break;
                	
                case "hasJobApplication":
                	String jobAppURI = object;
                	if(jobAppURI.contains("#"))
                		jobAppURI = ":" + jobAppURI.substring(jobAppURI.indexOf("#") + 1);
				try {
					jp.apply(Application.getApplication(jobAppURI));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                	break;
                	
                case "createdBy":
                	String creator_id = object;
                	if(creator_id.contains("#"))
                		creator_id = ":" + creator_id.substring(creator_id.indexOf("#") + 1);
                	jp.setcreator_id(creator_id);
                	break;
                	
                case "country-name":
                	String countryName = object;
                	jp.setCountry(countryName);
                	break;
                	
                case "region":
                	String state = object;
                	jp.setState(state);
                	break;
                	
                case "locality":
                	String city = object;
                	jp.setCity(city);
                	break;
                	
                default:
                    break;
            }
            
            //String ID = String.valueOf(soln.getResource("predicate"));
            //System.out.println(ID);   
            //cv.setId(ID);   
            //cv.setId(StringUtils.substringAfter(ID,"http://rdfs.org/resume-rdf/cv.rdfs#"));   

        } 
        return jp; 

    }
    
    private static List<String> ParsePropertyResponse(String SparqlJsonResults){
    	
    	List<String> properties = new ArrayList<String>();
    	
    	InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);
        while (results.hasNext()) {
        	QuerySolution soln = results.nextSolution();
//            Resource res= soln.getResource("object");
            
            RDFNode Onode = soln.get("object");
            String object="";
            if (Onode.isResource()) {
                object = String.valueOf(soln.getResource("object"));
            }
            else{
                object = String.valueOf(soln.getLiteral("object"));   
            }
            
            if(!properties.contains(object))
            	properties.add(object);
        }
        
    	return properties;
    }
}
