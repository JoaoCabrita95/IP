package IP.Model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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

public class Application extends RDFObject {
	
	private static final String ClassType ="qc:JobApp";  
    private static final String prefix = ":";
	private String personURI;
	private String jobURI;
	private String expectedSalary;
	private String salaryCurrency;
	private String availableAt;
	
	public Application() {
		super(ClassType, prefix);
	}
	
	/**
	 * Application Constructor
	 * @param URI Specified URI to use as reference to this Application Object
	 * @param PersonURI URI of the Person/user that made the Application
	 * @param JobURI URI of the Job the Application is being made to
	 * @param expectedSalary Expected Salary for the Job being Applied to
	 * @param availableAt Date the Job can be Started at
	 * @param salaryCurrency Currency that the Job will be payed out in
	 */
	public Application(String ID, String PersonURI, String JobURI, String expectedSalary, String availableAt, String salaryCurrency) {
		super(ClassType, prefix, ID, null, null);
		if(PersonURI.startsWith(":"))
			this.personURI = PersonURI;
		else
			this.personURI = ":" + PersonURI;
		if(JobURI.startsWith(":"))
			this.jobURI = JobURI;
		else
			this.jobURI = ":" + JobURI;
		this.expectedSalary = expectedSalary;
		this.salaryCurrency = salaryCurrency;
		this.availableAt = availableAt;
	}
	
	public Application(String PersonURI, String JobURI, String expectedSalary, String availableAt, String salaryCurrency) {
		super(ClassType, prefix);
		if(PersonURI.startsWith(":"))
			this.personURI = PersonURI;
		else
			this.personURI = ":" + PersonURI;
		if(JobURI.startsWith(":"))
			this.jobURI = JobURI;
		else
			this.jobURI = ":" + JobURI;
		this.expectedSalary = expectedSalary;
		this.salaryCurrency = salaryCurrency;
		this.availableAt = availableAt;
	}
	
	/**
	 * Get method for the URI of the Applicant/Person
	 * @return URI of the Person who made this Application
	 */
	public String getPersonURI() {
		return this.personURI;
	}
	
	/**
	 * Set method to define the Person URI
	 * @param PersonURI URI of the Object representing the Person who made the Application
	 */
	public void setPersonURI(String PersonURI) {
		this.personURI = PersonURI;
		if(PersonURI.contains("#"))
			this.personURI = ":" + PersonURI.substring(PersonURI.indexOf("#")+1);
	}
	
	/**
	 * Get method for the URI of the Job being Applied to in this Application
	 * @return URI of the Job being Applied to
	 */
	public String getJobURI() {
		return jobURI;
	}
	
	/**
	 * Set method for the URI of the Job being Applied to
	 * @param JobURI URI of the Job RDF Object being Applied to
	 */
	public void setJobURI(String JobURI) {
		this.jobURI = JobURI;
		if(JobURI.contains("#"))
			this.jobURI = ":" + JobURI.substring(JobURI.indexOf("#")+1);
	}
	
	/**
	 * Get method for the expected salary of the Application
	 * @return Expected salary to be received by the Job Applicant
	 */
	public String getExpectedSalary() {
		return expectedSalary;
	}
	
	/**
	 * Set method to define the Expected Salary in the Application
	 * @param expectedSalary Expected Salary to be defined for the Application
	 */
	public void setExpectedSalary(String expectedSalary) {
		this.expectedSalary = expectedSalary;
	}
    
	/**
	 * Get method for the currency used for the payment of the Job in the Application
	 * @return Currency used in the payment of the Application
	 */
    public String getSalaryCurrency() {
    	return salaryCurrency;
    }
    
    /**
     * Set method for the currency used in the payments of the Job in the Application
     * @param cur Currency used for the payments of the Job of the Application
     */
    public void setSalaryCurrency(String cur) {
    	this.salaryCurrency = cur;
    }
	
    /**
     * Get method for the date at which the Job can be started at
     * @return Date at which the Job can be started at
     */
	public String getAvailability() {
		return availableAt;
	}
	
	/**
	 * Set method to define the date at which the Job in the Application can be started
	 * @param date Date at which the Job in the Application can be started
	 */
	public void setAvailability(String date) {
		availableAt = date;
	}
	
	/**
	 * Gets the Application object from the Database with the URI given at input 
	 * @param URI URI of the Application object being retrieved
	 * @return Application Object identified by param URI
	 */
	public static Application getApplication(String URI) throws Exception {
		String uri = URI;
        if (!uri.startsWith(":") && !uri.startsWith("<http")){
        	if(uri.startsWith("http"))
        		uri = ":"+ uri.substring(uri.indexOf("#")+1);
        	else
        		uri = ":"+URI;
        }
        
        if(!SparqlEndPoint.existURI(uri)) {
			throw new NoSuchElementException("Application with URI: " + uri + " Not found");
		}
        
        String properties = SparqlEndPoint.getAllProperties(uri);
        Application app = ParseResponseToApplication(properties);
        app.setURI(uri);
        return app;
	}
	
	public static Application getApplicationByUserJob(String jobID,String profileID) throws Exception {
		String jobURI = jobID;
		if(!jobURI.contains(":"))
			jobURI = ":" + jobURI;
		String profileURI = profileID;
		if(!profileURI.contains(":"))
			profileURI = ":" + profileURI;
		
		Application result = null;
		
		for(Application app : getApplicationsByJob(jobURI)) {
			if(app.getPersonURI().equals(profileURI))
				result = app;
		}
		
		if(result == null) {
			throw new NoSuchElementException("No application made to job: " + jobID + " by profile: " + profileID);
		}
		
		return result;
	}
	
	public static List<Application> getApplicationsByJob(String jobID) throws Exception{
		String jobURI = jobID;
		if(!jobURI.contains(":"))
			jobURI = ":" + jobURI;
		String properties = SparqlEndPoint.getInstancesByProperty("qc:JobApp", "qc:appliedFor", jobURI);
		List<Application> apps = ParseResponseToApplications(properties);
		return apps;
	}
	
	public static List<Application> getApplicationsByProfile(String profileID) throws Exception{
		String profileURI = profileID;
		if(!profileURI.contains(":"))
			profileURI = ":" + profileURI;
		String properties = SparqlEndPoint.getInstancesByProperty("qc:JobApp", "qc:appliedBy", profileURI);
		List<Application> apps = ParseResponseToApplications(properties);
		return apps;
	}
	
	public static List<Application> ParseResponseToApplications(String properties) throws Exception{
		InputStream in = new ByteArrayInputStream(properties.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);
        List<Application> apps = new LinkedList<Application>();
        
        while (results.hasNext()) {
        	QuerySolution soln = results.nextSolution();

            String object = String.valueOf(soln.getResource("subject"));
            
            apps.add(getApplication(object));
        }
        
        return apps;
	}
	
	/**
	 * Auxiliary method to Parse the response from the server Database data to the Application Java object
	 * @param properties Json data representation of the Application Object from the server Database
	 * @return Application Object parsed from the Json data given at input
	 */
	private static Application ParseResponseToApplication(String properties) {
		InputStream in = new ByteArrayInputStream(properties.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);
        
        Application app = new Application();

        while (results.hasNext()) {

            QuerySolution soln = results.nextSolution();

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
                    app.setLabel(label);
                    break;

                case "comment":
                	String comment = object; 
                	app.setComment(comment);
                	break;
                	
                case "appliedBy":
                	String personURI = object;
                	if(personURI.contains("#"))
                		personURI = ":" + personURI.substring(personURI.indexOf("#") + 1);
                	app.setPersonURI(personURI);
                	break;
                	
                case "appliedFor":
                	String jobURI = object;
                	if(jobURI.contains("#"))
                		jobURI = ":" + jobURI.substring(jobURI.indexOf("#") + 1);
                	app.setJobURI(jobURI);
                	break;
                	
                case "hasExpectedSalary":
                	String expSalary = object;
                	app.setExpectedSalary(expSalary);
                	break;
                	
                case "isAvailableAt":
                	String availableAt = object;
                	app.setAvailability(availableAt);
                	break;
                	
                case "expectedSalaryCurrency":
                	String expSalCur = object;
                	app.setSalaryCurrency(expSalCur);
                	break;
                	
                default:
                    break;
            }

        } 
        return app; 
	}

	/**
	 * Uploads the RDF triples for the Application Object to the server database
	 * @throws Exception Usually Network exceptions in case of communication errors with server
	 */
	public void Save() throws Exception {
		Triple triple;
		super.rootRDFSave();
		
		Map<Triple, String> saveData = new HashMap<Triple, String>();
		
		if(personURI != null) {
			triple = new Triple(getURI(), "qc:appliedBy", personURI);
			saveData.put(triple, "Object");
//			SparqlEndPoint.insertTriple(triple);
		}
		
		if(jobURI != null) {
			triple = new Triple(getURI(), "qc:appliedFor", jobURI);
			saveData.put(triple, "Object");
//			SparqlEndPoint.insertTriple(triple);
			JobPosting jp = JobPosting.getJobPosting(jobURI);
			jp.apply(this);
			jp.Save();
		}
		
		if(expectedSalary != null) {
			triple = new Triple(getURI(), "qc:hasExpectedSalary", expectedSalary);
			saveData.put(triple, "String");
//			SparqlEndPoint.insertPropertyValue(triple);
		}
		
		if(availableAt != null) {
			triple = new Triple(getURI(), "qc:isAvailableAt", availableAt);
			saveData.put(triple, "String");
//			SparqlEndPoint.insertPropertyValue(triple);
		}
		
		if(salaryCurrency != null) {
	        triple = new Triple(getURI(), "qc:expectedSalaryCurrency", salaryCurrency);
	        saveData.put(triple, "String");
//	        SparqlEndPoint.insertPropertyValue(triple);
        }
		
		SparqlEndPoint.insertTriples(saveData);
	}
}
