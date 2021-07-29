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
	public static final String prefix = ":";
	private static final String schemaPrefix = "schema:";
//	private String URI;
//	private String ID;
//	private String Label;
//	//Seniority Level in the comment section of JobPosting
//	private String Comment;
	private String isHiring;
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
	private String specialization;
	//match with courses
	private List<Course> coursesReq;
	//match with work experience
	private List<WorkHistory> workExperienceReq;
	//match with education and courses?
	private List<Education> educationReq;
	private List<Application> jobApplications;
	private List<SkillJobReq> skillReq;
	
	//Might have to change according to QualiChain ontology model
	
	//TODO: Add constructors for different cases if found necessary
	public JobPosting() {
		super(ClassType, prefix);
		coursesReq = new ArrayList<Course>();
		workExperienceReq = new ArrayList<WorkHistory>();
		educationReq = new ArrayList<Education>();
		jobApplications = new ArrayList<Application>();
		skillReq = new ArrayList<SkillJobReq>();
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
			this.skillReq = new ArrayList<SkillJobReq>();
		}	
		else {
			this.skillReq = jobSkillReqs;
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
	
	public String getSpecialization() {
		return specialization;
	}
	
	public void setSpecialization(String spec) {
		specialization = spec;
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
    	return skillReq;
    }
    
    public void addSkillRef(Skill skill, String priorityLevel, String skillLevel) {
    	SkillJobReq skillJobReq = new SkillJobReq(skill, priorityLevel, skillLevel);
    	skillReq.add(skillJobReq);
    }
    
    public void addSkillRef(SkillJobReq skillJobReq) {
    	skillReq.add(skillJobReq);
    }
    
    public SkillJobReq removeSkillRef(SkillJobReq skillJobReq) {
    	for(SkillJobReq ref : skillReq) {
    		if(ref.getURI().equals(skillJobReq.getURI()) || ref.getSkillURI().equals(skillJobReq.getSkillURI())) {
    			skillReq.remove(ref);
    			return ref;
    		}
    			
    	}
    	return null;
    	
    }
    
    public SkillJobReq hasSkillRef(SkillJobReq skillJobReq) {
    	if(skillReq != null) {
    		for(SkillJobReq skillRef : skillReq) {
        		if(skillRef.getURI().equals(skillJobReq.getURI()) || skillRef.getSkillURI().equals(skillJobReq.getSkillURI()))
        			return skillRef;
        	}
    	}
    	
    	return null;
    }
    
    public SkillJobReq hasSkillRef(Skill skill) {
    	if(skillReq != null) {
    		for(SkillJobReq skillRef : skillReq) {
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
    		saveData.put(triple, "String");
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
        	triple = new Triple(getURI(),  "saro:level", seniorityLevel);
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(specialization != null) {
        	triple = new Triple(getURI(), "saro:specialization", specialization);
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(expectedSalary != null) {
			triple = new Triple(getURI(), "qc:hasExpectedSalary", expectedSalary);
			saveData.put(triple, "String");
//			SparqlEndPoint.insertPropertyValue(triple);
		}
        
        if(salaryCurrency != null) {
	        triple = new Triple(getURI(), "qc:expectedSalaryCurrency", salaryCurrency);
	        saveData.put(triple, "String");
//	        SparqlEndPoint.insertPropertyValue(triple);
        }
        
        for(SkillJobReq skillRef : skillReq) {
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
        
        String properties = SparqlEndPoint.getAllPropertiesByType(uri, JobPosting.ClassType);
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
        System.out.println(SparqlJsonResults);
        ResultSet results = ResultSetFactory.fromJSON(in);
        
        while (results.hasNext()) {

            QuerySolution soln = results.nextSolution();
            Resource res= soln.getResource("subject");
            //String ID = String.valueOf(res);            
            String ID =  res.getURI().substring(res.getURI().lastIndexOf("#")+1);
            try {
            	JobPosting jp = getJobPosting(":" + ID);

                jp.setID( ID);   
                //cv.setURI(StringUtils.substringAfter(ID,"http://rdfs.org/resume-rdf/cv.rdfs#"));   

                JPs.add(jp);  
            }
            catch (Exception e) {
            	System.out.println("Tried retrieving Job Posting with ID: " + ID + " but was unsucessful");
            	continue;
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
                    break;
                            	
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
                	
                case "hasExpectedSalary":
                	String expSalary = object;
                	jp.setExpectedSalary(expSalary);
                	break;
                	
                case "expectedSalaryCurrency":
                	String expSalCur = object;
                	jp.setSalaryCurrency(expSalCur);
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
//                	if(creator_id.contains("#"))
//                		creator_id = ":" + creator_id.substring(creator_id.indexOf("#") + 1);
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
                	
                case "specialization":
                	String spec = object;
                	jp.setSpecialization(spec);
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
    
    public boolean equals(JobPosting compareTo) {
    	
    	boolean contains = false;
    	if(!this.getURI().equals(compareTo.getURI()))
    		return false;
    	if(!this.getID().equals(compareTo.getID()))
    		return false;
    	
    	if(this.getCountry() == null && compareTo.getCountry() != null) {
    		return false;
    	}
    	else if(this.getCountry() == null && compareTo.getCountry() == null) {
    		
    	}
    	else if(!this.getCountry().equals(compareTo.getCountry())){
        	return false;
    	}
    	
    	if(this.getState() == null && compareTo.getState() != null) {
    		return false;
    	}
    	else if(this.getState() == null && compareTo.getState() == null) {
    		
    	}
    	else if(!this.getState().equals(compareTo.getState())){
        	return false;
    	}
    	
    	if(this.getCity() == null && compareTo.getCity() != null) {
    		return false;
    	}
    	else if(this.getCity() == null && compareTo.getCity() == null) {
    		
    	}
    	else if(!this.getCity().equals(compareTo.getCity())){
        	return false;
    	}
    	
    	if(this.getcreator_id() == null && compareTo.getcreator_id() != null) {
    		return false;
    	}
    	else if(this.getcreator_id() == null && compareTo.getcreator_id() == null) {
    		
    	}
    	else if(!this.getcreator_id().equals(compareTo.getcreator_id())){
        	return false;
    	}
    	
    	if(this.getJobDescription() == null && compareTo.getJobDescription() != null) {
    		return false;
    	}
    	else if(this.getJobDescription() == null && compareTo.getJobDescription() == null) {
    		
    	}
    	else if(!this.getJobDescription().equals(compareTo.getJobDescription())){
        	return false;
    	}
    	
    	if(this.getContractType() == null && compareTo.getContractType() != null) {
    		return false;
    	}
    	else if(this.getContractType() == null && compareTo.getContractType() == null) {
    		
    	}
    	else if(!this.getContractType().equals(compareTo.getContractType())){
        	return false;
    	}
    	
    	if(this.getSector() == null && compareTo.getSector() != null) {
    		return false;
    	}
    	else if(this.getSector() == null && compareTo.getSector() == null) {
    		
    	}
    	else if(!this.getSector().equals(compareTo.getSector())){
        	return false;
    	}
    	
    	if(this.getOccupation() == null && compareTo.getOccupation() != null) {
    		return false;
    	}
    	else if(this.getOccupation() == null && compareTo.getOccupation() == null) {
    		
    	}
    	else if(!this.getOccupation().equals(compareTo.getOccupation())){
        	return false;
    	}
    	
    	if(this.getListingOrg() == null && compareTo.getListingOrg() != null) {
    		return false;
    	}
    	else if(this.getListingOrg() == null && compareTo.getListingOrg() == null) {
    		
    	}
    	else if(!this.getListingOrg().equals(compareTo.getListingOrg())){
        	return false;
    	}
    	
    	if(this.getHiringOrg() == null && compareTo.getHiringOrg() != null) {
    		return false;
    	}
    	else if(this.getHiringOrg() == null && compareTo.getHiringOrg() == null) {
    		
    	}
    	else if(!this.getHiringOrg().equals(compareTo.getHiringOrg())){
        	return false;
    	}
    	
    	if(this.getjobLocation() == null && compareTo.getjobLocation() != null) {
    		return false;
    	}
    	else if(this.getjobLocation() == null && compareTo.getjobLocation() == null) {
    		
    	}
    	else if(!this.getjobLocation().equals(compareTo.getjobLocation())){
        	return false;
    	}
    	
    	if(this.getStartDate() == null && compareTo.getStartDate() != null) {
    		return false;
    	}
    	else if(this.getStartDate() == null && compareTo.getStartDate() == null) {
    		
    	}
    	else if(!this.getStartDate().equals(compareTo.getStartDate())){
        	return false;
    	}
    	
    	if(this.getEndDate() == null && compareTo.getEndDate() != null) {
    		return false;
    	}
    	else if(this.getEndDate() == null && compareTo.getEndDate() == null) {
    		
    	}
    	else if(!this.getEndDate().equals(compareTo.getEndDate())){
        	return false;
    	}
    	
    	if(this.getseniorityLevel() == null && compareTo.getseniorityLevel() != null) {
    		return false;
    	}
    	else if(this.getseniorityLevel() == null && compareTo.getseniorityLevel() == null) {
    		
    	}
    	else if(!this.getseniorityLevel().equals(compareTo.getseniorityLevel())){
        	return false;
    	}
    	
    	if(this.getExpectedSalary() == null && compareTo.getExpectedSalary() != null) {
    		return false;
    	}
    	else if(this.getExpectedSalary() == null && compareTo.getExpectedSalary() == null) {
    		
    	}
    	else if(!this.getExpectedSalary().equals(compareTo.getExpectedSalary())){
        	return false;
    	}
    	
    	if(this.getSalaryCurrency() == null && compareTo.getSalaryCurrency() != null) {
    		return false;
    	}
    	else if(this.getSalaryCurrency() == null && compareTo.getSalaryCurrency() == null) {
    		
    	}
    	else if(!this.getSalaryCurrency().equals(compareTo.getSalaryCurrency())){
        	return false;
    	}
    	
    	if(this.getSpecialization() == null && compareTo.getSpecialization() != null) {
    		return false;
    	}
    	else if(this.getSpecialization() == null && compareTo.getSpecialization() == null) {
    		
    	}
    	else if(!this.getSpecialization().equals(compareTo.getSpecialization())){
        	return false;
    	}
    	
    	for(SkillJobReq skillReq : this.getJobSkillReqRefs()) {
    		contains = false;
    		for(SkillJobReq skillReq2 : compareTo.getJobSkillReqRefs()) {
    			if(skillReq.equals(skillReq2)) {
    				contains = true;
    				break;
    			}
    				
    		}
    		if(!contains)
    			return false;
    	}
    	
    	for(SkillJobReq skillReq : compareTo.getJobSkillReqRefs()) {
    		contains = false;
    		for(SkillJobReq skillReq2 : this.getJobSkillReqRefs()) {
    			if(skillReq.equals(skillReq2)) {
    				contains = true;
    				break;
    			}
    				
    		}
    		if(!contains)
    			return false;
    	}
    	
    	for(WorkHistory workHistory : this.getworkExperienceReq()) {
    		contains = false;
    		for(WorkHistory workHistory2 : compareTo.getworkExperienceReq()) {
    			if(workHistory.equals(workHistory2)) {
    				contains = true;
    				break;
    			}
    				
    		}
    		if(!contains)
    			return false;
    	}
    	
    	for(WorkHistory workHistory : compareTo.getworkExperienceReq()) {
    		contains = false;
    		for(WorkHistory workHistory2 : this.getworkExperienceReq()) {
    			if(workHistory.equals(workHistory2)) {
    				contains = true;
    				break;
    			}
    				
    		}
    		if(!contains)
    			return false;
    	}
    	
    	for(Course course : this.getCapabilityReq()) {
    		contains = false;
    		for(Course course2 : compareTo.getCapabilityReq()) {
    			if(course.equals(course2)) {
    				contains = true;
    				break;
    			}
    				
    		}
    		if(!contains)
    			return false;
    	}
    	
    	for(Course course : compareTo.getCapabilityReq()) {
    		contains = false;
    		for(Course course2 : this.getCapabilityReq()) {
    			if(course.equals(course2)) {
    				contains = true;
    				break;
    			}
    				
    		}
    		if(!contains)
    			return false;
    	}
    	
    	for(Education ed : this.getEducationReq()) {
    		contains = false;
    		for(Education ed2 : compareTo.getEducationReq()) {
    			if(ed.equals(ed2)) {
    				contains = true;
    				break;
    			}
    		}
    		if(!contains)
    			return false;
    	}
    	
    	for(Education ed : compareTo.getEducationReq()) {
    		contains = false;
    		for(Education ed2 : this.getEducationReq()) {
    			if(ed.equals(ed2)) {
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
