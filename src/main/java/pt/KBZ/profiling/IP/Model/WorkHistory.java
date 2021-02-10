package IP.Model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

/**
 * Java object representation of work experiences, either past jobs users have been through or job requirements for new job offers
 */
public class WorkHistory extends RDFObject {

	private static final String ClassType ="cv:WorkHistory";
    private static final String prefix ="cv:"; 
    private String position;
    private String from;// used for CV
    private String to;// Used for CV
	private String employer;
	private String duration;//used for Job Requirements
	private String description;
	private String careerLevel;
	private String numSubordinates;
	private String jobType;
	private String isCurrent;
	private String jobReference;
	
	/**
	 * WorkHistory constructor, specifies the ClassType and the prefix used for class specific triples
	 */
	public WorkHistory() {
		super(ClassType, prefix);
	}
	
	/**
	 * WorkHistory constructor, specifies the ClassType, the prefix and a URI for a particular workHistory object
	 * @param URI
	 */
	public WorkHistory(String URI) {
		super(ClassType, prefix, RDFObject.uri2id(URI), null, null);
	}
	
	public WorkHistory(String ID, String position, String from, String to, String employer,
			String duration, String jobDescription, String careerLevel, String numSubordinates,
			String isCurrent, String jobType, String jobURI) {
		super(ClassType, prefix, ID, null, null);
		this.position = position;
		this.from = from;
		this.to = to;
		this.employer = employer;
		this.duration = duration;
		this.description = jobDescription;
		this.careerLevel = careerLevel;
		this.numSubordinates = numSubordinates;
		this.jobType = jobType;
		this.isCurrent = isCurrent;
		if(jobURI.startsWith(":"))
			jobReference = jobURI;
		else if(jobURI.startsWith("http"))
			jobReference = ":" + jobURI.substring(jobURI.indexOf("#") + 1);
			
	}
	
	public void setDescription(String desc) {
		this.description = desc;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setCareerLevel(String level) {
		this.careerLevel = level;
	}
	
	public String getCareerLevel() {
		return careerLevel;
	}
	
	public void setNumSubordinates(String num) {
		this.numSubordinates = num;
	}
	
	public String getNumSubordinates() {
		return numSubordinates;
	}
	
	public void setJobType(String type) {
		this.jobType = type;
	}
	
	public String getJobType() {
		return jobType;
	}
	
	public void setIsCurrent(boolean isCurrent) {
		if(isCurrent)
			this.isCurrent = "true";
		else
			this.isCurrent = "false";
	}
	
	public String getIsCurrent() {
		return isCurrent;
	}
	
	/**
	 * Set method to define the position the workHistory is described by
	 * @param position Description of the position or role of the job defined by this workHistory instance
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	
	/**
	 * Get method for the position of the job the workHistory describes
	 * @return position the workHistory instance describes
	 */
	public String getPosition() {
		return position;
	}
	
	/**
	 * Set method to define when the entity who has the workHistory started the job associated with this instance
	 * @param from Starting date of the workHistory instance
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	
	/**
	 * Get method that recovers the info about when the workHistory job was started
	 * @return Starting date of the WorkHistory instance
	 */
	public String getFrom() {
		return from;
	}
	
	/**
	 * Set method to define when the entity who has the workHistory stopped working the job associated with this instance
	 * @param to End date of the workHistory instance
	 */
	public void setTo(String to) {
		this.to = to;
	}
	
	/**
	 * Get method that recovers the info about when the workHistory job was ended
	 * @return Ending date of the workHistory instance
	 */
	public String getTo() {
		return to;
	}
	
	/**
	 * Set method to define the Organization that hired the entity associated with this workHistory instance
	 * @param emp Name of the employer/company 
	 */
	public void setEmployer(String emp) {
		this.employer = emp;
	}
	
	/**
	 * Get method that recovers the name of the employer/company associated with this workHistory instance
	 * @return Name of the employer/company
	 */
	public String getEmployer() {
		return employer;
	}
	
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public String getDuration() {
		return duration;
	}
	
	public void setJobReference(String jobURI) {
		if(jobURI.startsWith(":"))
			jobReference = jobURI;
		else if(jobURI.startsWith("http:"))
			jobReference = ":" + jobURI.substring(jobURI.indexOf("#") + 1);
		else
			jobReference = jobURI;
	}
	
	public String getJobReference() {
		return jobReference;
	}
	
	/**
	 * Get method for a specific instance of a workHistory object from the server associated with the service with a specific object URI
	 * @param URI Unique identifier of a workHistory object
	 * @return WorkHistory instance with a specific URI
	 */
	public static WorkHistory getWorkHistory(String URI) {
		String uri = URI;
		
    	if(!uri.startsWith(":") && !uri.startsWith("<http")) {
        	if(uri.startsWith("http"))
        		uri ="<"+ uri + ">";
        	else
        		uri = ":"+uri;
		}
    	
		String properties = SparqlEndPoint.getAllProperties(uri);
	    //System.out.println(properties);
        WorkHistory wh = ParseResponseToWorkHistory(properties);
	    wh.setURI(uri);
	    
	    return wh;
	}

	/**
	 * Auxiliary method that parses the results from the associated server from Json to a workHistory Java object
	 * @param SparqlJsonResults Json format description of a WorkHistory java object
	 * @return WorkHistory object from server database
	 */
	private static WorkHistory ParseResponseToWorkHistory(String SparqlJsonResults) {
			InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
	        ResultSet results = ResultSetFactory.fromJSON(in);

	        
	        WorkHistory wh = new WorkHistory();

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
	                case "type":
	                    String type = object;   
	                    break;

	                case "label":
	                    String label = object;   
	                    wh.setLabel(label);
	                    break;
	                    
	                case "comment":
	                	String comment = object; 
	                	wh.setComment(comment);
	                	break;
	                	
	                case "startDate":
	                	String from = object;  
	                	wh.setFrom(from);
	                	break;
	                	
	                case "endDate":
	                	String to = object;  
	                	wh.setTo(to);
	                	break;
	                	
	                case "duration":
	                	String duration = object;
	                	wh.setDuration(duration);
	                	break;
	                	
	                case "employedIn":
	                	String employer = object;
	                	wh.setEmployer(employer);
	                	break;
	                	
	                case "jobTitle":
	                	String position = object;
	                	wh.setPosition(position);
	                	break;
	                	
	                case "jobDescription":
	                	String desc = object;
	                	wh.setDescription(desc);
	                	break;
	                	
	                case "careerLevel":
	                	String level = object;
	                	wh.setCareerLevel(level);
	                	break;
	                	
	                case "numSubordinates":
	                	String num = object;
	                	wh.setNumSubordinates(num);
	                	break;
	                	
	                case "jobType":
	                	String jobType = object;
	                	wh.setJobType(jobType);
	                	break;
	                	
	                case "jobRefURI":
	                	String URI = object;
	                	wh.setJobReference(URI);
	                	
	                default:
	                    break;
	            }
	        }
	        return wh;
	}

	/**
	 * Save method that takes the data in this workHistory object and uploads it to the server database
	 */
	public void Save() {
		super.rootRDFSave();
        
        Triple triple;

		Map<Triple, String> saveData = new HashMap<Triple, String>();
        
        if(position != null) {
        	triple = new Triple(getURI(), prefix + "jobTitle", position);
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(from != null) {
        	triple = new Triple(getURI(), prefix + "startDate", from);
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(to != null) {
        	triple = new Triple(getURI(), prefix + "endDate", to);
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(duration != null) {
        	triple = new Triple(getURI(), "qc:duration", duration);
        	saveData.put(triple, "String");
        }
        
        if(employer != null) {
        	triple = new Triple(getURI(), prefix + "employedIn", employer);
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(description != null) {
        	triple = new Triple(getURI(), prefix + "jobDescription", description);
        	saveData.put(triple, "String");
        }
        
        if(careerLevel != null) {
        	triple = new Triple(getURI(), prefix + "careerLevel", careerLevel);
        	saveData.put(triple, "String");
        }
        
        if(numSubordinates != null) {
        	triple = new Triple(getURI(), prefix + "numSubordinates", numSubordinates);
        	saveData.put(triple, "String");
        }
        
        if(jobType != null) {
        	triple = new Triple(getURI(), prefix + "jobType", jobType);
        	saveData.put(triple, "String");
        }
        
        if(isCurrent != null) {
        	triple = new Triple(getURI(), prefix + "isCurrent", isCurrent);
        	saveData.put(triple, "String");
        }
        
        if(jobReference != null) {
        	triple = new Triple(getURI(), "qc:jobRefURI", jobReference);
        	saveData.put(triple, "Object");
        }
        
        SparqlEndPoint.insertTriples(saveData);
	}
}
