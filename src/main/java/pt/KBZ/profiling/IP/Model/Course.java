package IP.Model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class Course extends RDFObject {
	
	private static final String ClassType ="cv:Course";
    private static final String prefix ="cv:"; 
    private String qualification;
    private String organizedBy;
    private boolean isCertification;
    private String courseTitle;
    private String courseDescription;
    private String courseURL;
    private String startDate;
    private String finishDate;
    private List<String> relatedTo = null;

    /**
     * Creates a new Course with a randomly generated ID and URI
     */
	public Course() {
		super(ClassType, prefix);
		relatedTo = new ArrayList<String>();
	}
	
	/**
	 * Creates a new course with the input URI
	 * @param URI URI for Course Identification
	 */
	public Course(String URI) {
		super(ClassType, prefix, RDFObject.uri2id(URI), null, null);
		relatedTo = new ArrayList<String>();
	}
	
	public Course(String ID, String qualification, String organizedBy, boolean isCertification, String courseTitle,
			String courseDescription, String courseURL, String startDate, String finishDate) {
		super(ClassType, prefix, ID, null, null);
		this.qualification = qualification;
		this.organizedBy = organizedBy;
		this.isCertification = isCertification;
		this.courseTitle = courseTitle;
		this.courseDescription = courseDescription;
		this.courseURL = courseURL;
		this.startDate = startDate;
		this.finishDate = finishDate;
		relatedTo = new ArrayList<String>();
	}
	
	public void setIsCertification(boolean certification) {
		isCertification = certification;
	}
	
	public boolean isCertification() {
		return isCertification;
	}
	
	public void setCourseTitle(String title) {
		courseTitle = title;
	}
	
	public String getCourseTitle() {
		return courseTitle;
	}
	
	public void setCourseDescription(String desc) {
		courseDescription = desc;
	}
	
	public String getCourseDescription() {
		return courseDescription;
	}
	
	public void setCourseURL(String URL) {
		courseURL = URL;
	}
	
	public String getCourseURL() {
		return courseURL;
	}
	
	public void setStartDate(String date) {
		startDate = date;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setFinishDate(String date) {
		finishDate = date;
	}
	
	public String getFinishDate() {
		return finishDate;
	}
	
	/**
	 * Set method for the qualification this Course provides
	 * @param qual Qualification the Course provides
	 */
	public void setQualification(String qual) {
		this.qualification = qual;
	}
	
	/**
	 * Get method that returns the qualification this Course provides
	 * @return Qualification this Course provides
	 */
	public String getQualification() {
		return qualification;
	}
	
	/**
	 * Set method to define who developed this Course
	 * @param dev Developer of this Course
	 */
	public void setOrganizedBy(String org) {
		this.organizedBy = org;
	}
	
	/**
	 * Get method that returns who this Course was developed by
	 * @return Course Developer
	 */
	public String getOrganizedBy() {
		return organizedBy;
	}
	
	/*
	 * Use URIs for the relations with the course
	 */
	/**
	 * Related Skills or Qualifications to this Course
	 * @param related Skills or Qualifications related to this Course
	 */
	public void addRelatedTo(String related) {
		this.relatedTo.add(related);
	}
	
	/**
	 * Get method for the list of Skills or Qualifications related to this Course
	 * @return Skills or Qualifications related to this Course
	 */
	public List<String> getRelatedTo() {
		return relatedTo;
	}
	
	/**
	 * Static method to retrieve the Course object from server Database
	 * @param URI URI identifier of the Course 
	 * @return Course Object stored in the server Database
	 */
	public static Course getCourse(String URI) {
		String uri = URI;
    	if(!uri.startsWith(":") && !uri.startsWith("<http")) {
        	if(uri.startsWith("http"))
        		uri ="<"+ uri + ">";
        	else
        		uri = ":"+uri;
		}
		String properties = SparqlEndPoint.getAllProperties(uri);
	    //System.out.println(properties);
		Course crs = ParseResponseToCourse(properties);
        crs.setURI(uri);
	    
	    return crs;
	}
	
	
	/**
	 * Receives Json data from server Database and parses it to Course Java Objects
	 * @param SparqlJsonResults Json format data about a Course 
	 * @return Parsed Course java object from the Json data input
	 */
	private static Course ParseResponseToCourse(String SparqlJsonResults) {
		InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);

        
        Course crs = new Course();

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
                    crs.setLabel(label);
                    break;
                    
                case "leadsToQualification":
                	String certificate = object;  
                	crs.setQualification(certificate);
                	break;
                	
                case "organizedBy":
                	String org = object;
                	crs.setOrganizedBy(org);
                	break;
                	
                case "relatedTo":
                	String relatedTo = object;
                	if(relatedTo.contains("#"))
                		relatedTo = relatedTo.substring(relatedTo.indexOf("#") + 1);
                	crs.addRelatedTo(relatedTo);
                	break;
                	
                case "isCertification":
                	String certification = object;
                	if(certification.equals("true"))
						crs.setIsCertification(true);
					else
						crs.setIsCertification(false);
                	break;
                	
                case "courseTitle":
                	String courseTitle = object;
                	crs.setCourseTitle(courseTitle);
                	break;
                	
                case "courseDescription":
                	String desc = object;
                	crs.setCourseDescription(desc);
                	break;
                	
                case "courseURL":
                	String courseURL = object;
                	crs.setCourseURL(courseURL);
                	break;
                	
                case "courseStartDate":
                	String start = object;
                	crs.setStartDate(start);
                	break;
                	
                case "courseFinishDate":
                	String finish = object;
                	crs.setFinishDate(finish);
                	break;
							                	
                default:
                    break;
            }
            
            //String ID = String.valueOf(soln.getResource("predicate"));
            //System.out.println(ID);   
            //cv.setId(ID);   
            //cv.setId(StringUtils.substringAfter(ID,"http://rdfs.org/resume-rdf/cv.rdfs#"));   

        } 
        return crs; 
	}

	/**
	 * Saves the Java Object data to the server Database as RDF triples
	 */
	public void Save() {
		
		super.rootRDFSave();
        
        Triple triple;

		Map<Triple, String> saveData = new HashMap<Triple, String>();
        
        if(qualification != null) {
        	triple = new Triple(getURI(), "saro:leadsToQualification", qualification);
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(organizedBy != null) {
        	triple = new Triple(getURI(), prefix + "organizedBy", organizedBy);
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(relatedTo != null && relatedTo.size() != 0) {
        	for(String relation : relatedTo) {
            	triple = new Triple(getURI(), "saro:relatedTo", relation);
            	saveData.put(triple, "Object");
//            	SparqlEndPoint.insertTriple(triple);
            }
        }
        
        if(isCertification)
        	triple = new Triple(getURI(), prefix + "isCertification", "true");
        else
        	triple = new Triple(getURI(), prefix + "isCertification", "false");
        //Might have to be String, should test
        saveData.put(triple, "String");
        
        if(courseTitle != null) {
        	triple = new Triple(getURI(), prefix + "courseTitle", courseTitle);
        	saveData.put(triple, "String");
        }
        
        if(courseDescription != null) {
        	triple = new Triple(getURI(), prefix + "courseDescription", courseDescription);
        	saveData.put(triple, "String");
        }
        
        if(courseURL != null) {
        	triple = new Triple(getURI(), prefix + "courseURL", courseURL);
        	saveData.put(triple, "String");
        }
        
        if(startDate != null) {
        	triple = new Triple(getURI(), prefix + "courseStartDate", startDate);
        	saveData.put(triple, "String");
        }
        
        if(finishDate != null) {
        	triple = new Triple(getURI(), prefix + "courseFinishDate", finishDate);
        	saveData.put(triple, "String");
        }

        SparqlEndPoint.insertTriples(saveData);
        
        
	}
	
	public boolean equals(Course course) {
		return true;
	}

}
