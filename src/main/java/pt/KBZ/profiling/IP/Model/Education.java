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
 * Java object representation of an Educational path, for example a university course or a workshop
 */
public class Education extends RDFObject{

	private static final String ClassType ="cv:Education";
    private static final String prefix ="cv:"; 
    private String major;
    private String minor;
    private String degreeType;
    private String from;
    private String to;
    private String organization;
    private String description;
	
    /**
     * Constructor for the Education model Class, without any set variables
     */
	public Education() {
		super(ClassType, prefix);
	}
	
	/**
	 * Constructor for the Education model Class, with a defined URI
	 * @param URI Unique id for an Education object
	 */
	public Education(String URI) {
		super(ClassType, prefix, RDFObject.uri2id(URI), null, null);
	}
	
	public Education(String ID, String major, String minor, String degreeType, String from, String to, String organization, String description) {
		super(ClassType, prefix, ID, null, null);
		this.major = major;
		this.minor = minor;
		this.degreeType = degreeType;
		this.from = from;
		this.to = to;
		this.organization = organization;
		this.description = description;
	}
	
	/**
	 * Set method for the Title of the education certification
	 * @param Title Title of the Education certification
	 */
	public void setMajor(String major) {
		this.major = major;
	}
	
	/**
	 * Get method for the Title of the education certification
	 * @return Title of the education certification
	 */
	public String getMajor() {
		return major;
	}
	
	public void setMinor(String minor) {
		this.minor = minor;
	}
	
	public String getMinor() {
		return minor;
	}
	
	public void setDegreeType(String type) {
		this.degreeType = type;
	}
	
	public String getDegreeType() {
		return degreeType;
	}
	
	/**
	 * Sets starting date of the educational path described by this Education object
	 * @param from Starting date
	 */
	public void setFrom(String from) {
		this.from = from;
		
	}
	
	/**
	 * Gets starting date of the educational path described by this Education object
	 * @return Starting date
	 */
	public String getFrom() {
		return from;
	}
	
	/**
	 * Sets end date of the educational path described by this Education object
	 * @param to End date
	 */
	public void setTo(String to) {
		this.to = to;
		
	}
	
	/**
	 * Gets end date of the educational path described by this Education object
	 * @return End date
	 */
	public String getTo() {
		return to;
	}
	
	/**
	 * Sets where the Education certification was awarded from
	 * @param organisation Education entity who awarded the certification
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	
	/**
	 * Gets where the Education certification was awarded from
	 * @return Education entity who awarded the certification
	 */
	public String getOrganization() {
		return organization;
	}
	
	/**
	 * Sets a description of what this Education object entails
	 * @param description Description of the Education 
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Get method for the description of this Education object
	 * @return Description of the Education 
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Get method that returns an Education object with the associated URI
	 * @param URI Unique identifier of the Education object to be returned
	 * @return Education object
	 */
	public static Education getEducation(String URI) {
		String uri = URI;
    	if(!uri.startsWith(prefix) && !uri.startsWith("<http")) {
        	if(uri.startsWith("http"))
        		uri ="<"+ uri + ">";
        	else
        		uri = prefix+uri;
		}
		String properties = SparqlEndPoint.getAllProperties(uri);
	    //System.out.println(properties);
		Education ed = ParseResponseToEducation(properties);
	    ed.setURI(uri);
	    
	    return ed;
	}
	
	/**
	 * Auxiliary method to get all the different data from the database server for an education object described in the Json format
	 * @param SparqlJsonResults Json format data from the database server
	 * @return Education object derived from the input Json data
	 */
	private static Education ParseResponseToEducation(String SparqlJsonResults) {
		InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);

        
        Education ed = new Education();

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
                    ed.setLabel(label);
                    break;
                    
                case "eduMajor":
                	String major = object;  
                	ed.setMajor(major);
                	break;
                	
                case "eduMinor":
                	String minor = object;
                	ed.setMinor(minor);
                	break;
                	
                case "degreeType":
                	String degreeType = object;
                	ed.setDegreeType(degreeType);
                	break;
            
                case "eduStartDate":
                	String startDate = object;  
                	ed.setFrom(startDate);
                	break;
                	
                case "eduGradDate":
                	String endDate = object;  
                	ed.setTo(endDate);
                	break;
                	
                case "studiedIn":
                	String organization = object;  
                	ed.setOrganization(organization);
                	break;
                	
                case "eduDescription":
                	String desc = object;
                	ed.setDescription(desc);
                	
                default:
                    break;
            }
            
            //String ID = String.valueOf(soln.getResource("predicate"));
            //System.out.println(ID);   
            //cv.setId(ID);   
            //cv.setId(StringUtils.substringAfter(ID,"http://rdfs.org/resume-rdf/cv.rdfs#"));   

        } 
        return ed; 
	}

	/**
	 * Save method for the Education class, takes all the data in the different variables and tries to upload it to the designated server
	 */
	public void Save() {
		super.rootRDFSave();
        
        Triple triple;
        
		Map<Triple, String> saveData = new HashMap<Triple, String>();
        
        if(major != null) {
        	triple = new Triple(getURI(), prefix + "eduMajor", major);
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(minor != null) {
        	triple = new Triple(getURI(), prefix + "eduMinor", minor);
        	saveData.put(triple, "String");
        }
        
        if(degreeType != null) {
        	triple = new Triple(getURI(), prefix + "degreeType", degreeType);
        	saveData.put(triple, "String");
        }
        
        if(from != null) {
        	triple = new Triple(getURI(), prefix +  "eduStartDate", from);
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(to != null) {
        	triple = new Triple(getURI(), prefix + "eduGradDate", to);
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(organization != null) {
        	triple = new Triple(getURI(), prefix + "studiedIn", organization);
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(description != null) {
        	triple = new Triple(getURI(), prefix + "eduDescription", description);
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        SparqlEndPoint.insertTriples(saveData);
	}
	
	public boolean equals(Education ed) {
		return true;
	}

}
