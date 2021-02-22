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
import org.cyberborean.rdfbeans.annotations.RDFBean;
import org.cyberborean.rdfbeans.annotations.RDFNamespaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


@RDFNamespaces(
{
    "rdfs = http://www.w3.org/2000/01/rdf-schema",
    "qc = http://www.qualichain.eu/ontology/",
    "cv = http://rdfs.org/resume-rdf/cv.rdfs/" 
})

@RDFBean("qc:Person")
/**
 * Person model class, data structure that stores, saves, updates and recovers Personal information about a person in an rdf based database
 */
public class Person extends RDFObject {
    private static final String ClassType ="qc:Person";  
    public static final String prefix = "qc:";
    private String name;
    private String surname;
    private String gender;
    @SuppressWarnings("unused")
	private String phoneNumber;
    @SuppressWarnings("unused")
	private String email;
    //Like a linkedin page or a blog for reference work for example
    @SuppressWarnings("unused")
	private String personalPage;
    private String nationality;
    //Not sure what ontology to use for the address
    private String address;
    private boolean driversLicense;
    private String cvURI;
    //Describe what the area of interest is or the area of expertise in a field?
    private String competenceArea;
    private String competenceAreaDescription;
    private String role;
    private String currentJobURI;
    private CV personalCV;
    private List<String> qualifications;
    private List<String> experiences;
    private List<String> memberships;
    private List<String> publications;
    private List<String> accomplishments;
    private List<Application> jobApplications;
    
    
  //TODO: Add constructors for different cases if found necessary
    
    /**
     * Constructor for Person class without set details, creates a default structure to add information on with a auto-generated uri/id
     */
    public Person() {
    	super(ClassType, prefix);
    	qualifications = new ArrayList<>();
    	experiences = new ArrayList<>();
    	memberships = new ArrayList<>();
    	publications = new ArrayList<>();
    	accomplishments = new ArrayList<>();
    	jobApplications = new ArrayList<>();
    }
    
    /**
     * Constructor for Person class with set input variables
     * @param id Identifier for the Person
     * @param name First name of the Person
     * @param surname Last name of the Person
     * @param comment Comment left by the person about the profile
     * @param gender Gender of the Person
     * @param phoneNum Phone Number/contact information of the Person
     * @param email Email contact for the Person
     * @param personalPage Personal website given by the Person
     * @param nationality Nationality of the Person
     * @param address Address of residence of the Person
     * @param driversLicense true if the Person has a valid drivers license, false if not
     * @param cvUri The URI of the CV of the person in the database
     * @param competenceArea Area of work the Person is competent on
     * @param competenceAreaDesc Further description of the area/areas of competence of the Person
     * @param role Role in the Persons current work situation
     * @param qualif List of qualifications the Person has
     * @param exp List of past work experiences the Person has
     * @param member List of memberships the Person has
     * @param pubs List of publications made by the Person, for example scientific journals
     * @param accomp List of accomplishments of the Person 
     */
    public Person(String id, String name, String surname, String comment, String gender, String phoneNum,
    		String email, String personalPage, String nationality, String address, boolean driversLicense,
    		String cvUri, String competenceArea, String competenceAreaDesc, String role, List<String> qualif,
    		List<String> exp, List<String> member, List<String> pubs, List<String> accomp) {
    	super(ClassType, prefix, id, name + " " + surname, comment);
    	this.name = name;
    	this.surname = surname;
    	this.gender = gender;
    	this.phoneNumber = phoneNum;
    	this.email = email;
    	this.personalPage = personalPage;
    	this.nationality = nationality;
    	this.address = address;
    	this.driversLicense = driversLicense;
    	if(!cvUri.startsWith(":"))
    		this.cvURI = ":" + cvUri;
    	else
    		this.cvURI = cvUri;
    	this.competenceArea = competenceArea;
    	this.competenceAreaDescription = competenceAreaDesc;
    	this.role = role;
    	this.qualifications = qualif;
    	if(qualifications == null)
    		qualifications = new ArrayList<>();
    	
    	this.experiences = exp;
    	if(experiences == null)
    		experiences = new ArrayList<>();
    	
    	this.memberships = member;
    	if(memberships == null)
    		memberships = new ArrayList<>();
    	
    	this.publications = pubs;
    	if(publications == null)
    		publications = new ArrayList<>();
    	
    	this.accomplishments = accomp;
    	if(accomplishments == null)
    		accomplishments = new ArrayList<>();
    	
    	if(jobApplications == null) {
    		jobApplications = new ArrayList<>();
    	}
    	
    	currentJobURI = null;
    }
    
    /**
     * Get method, returns the Person's name
     * @return Name of the Person
     */
    public String getName() {
    	return name;
    }
    
    /**
     * Set method, sets the Name of the Person
     * @param name Name to be set as First name of the Person
     */
    public void setName(String name) {
    	this.name = name;
    }
    
    /**
     * Get method, returns Person's last name
     * @return Person's last name
     */
    public String getsurname() {
    	return surname;
    }
    
    /**
     * Set method, sets the last name of the Person
     * @param surname Name to be set as the Person's last name
     */
    public void setsurname(String surname) {
    	this.surname = surname;
    }
    
    /**
     * Get method, returns the Gender of the Person
     * @return Gender of the Person
     */
    public String getGender() {
    	return this.gender;
    }
    
    /**
     * Set method, defines the Gender of the Person
     * @param gender Gender the person is to be set as
     */
    public void setGender(String gender) {
    	this.gender = gender;
    }
    
    public String getNationality() {
    	return this.nationality;
    }
    
    public void setNationality(String nationality) {
    	this.nationality = nationality;
    }
    
    /**
     * Get method, returns the residence address of the Person
     * @return Person's residence address
     */
    public String getAddress() {
    	return this.address;
    }
    
    /**
     * Set method, sets the residence address of the Person
     * @param address Address to set as residence address of the Person
     */
    public void setAddress(String address) {
    	this.address = address;
    }
    
    /**
     * Get method, returns if the Person has a driver's license or not
     * @return true if the Person has a driver's license, false if the Person doesn't have a driver's license
     */
    public boolean getDriversLicense() {
    	return this.driversLicense;
    }
    
    //Should be a boolean value, not string, maybe restricting the String to true or false is easier though
    /**
     * Set method, sets if the Person has a driver's license or not
     * @param driversLicense True if the Person has a driver's license, false if the person doesn't have a driver's license
     */
    public void setDriversLicense(boolean driversLicense) {
    	this.driversLicense = driversLicense;
    }
    
    /**
     * Get method, returns the URI of the CV associated with the Person in the database
     * @return URI of a CV in the database associated with this Person
     */
    public String getCVURI() {
    	return cvURI;
    }
    
    /**
     * Set method, sets the URI of the CV this Person has associated
     * @param CVURI URI of a CV or the ID of a CV
     */
    public void setCVURI(String CVURI) {
    	if(CVURI.contains(":"))
    		this.cvURI = CVURI;
    	else
    		this.cvURI = ":" + CVURI;
    }
    
    /**
     * Get method, returns the area this Person works in/has competences in
     * @return Area the Person works in/has competences in
     */
    public String getCompetenceArea() {
    	return competenceArea;
    }
    
    /**
     * Set method, sets the area this Person works in/has competences in
     * @param competenceArea Area the Person works in/has competences in
     */
    public void setCompetenceArea(String competenceArea) {
    	this.competenceArea = competenceArea;
    }
    
    /**
     * Get method, returns a description of the area this person has defined as their competence area
     * @return description of the area this person has defined as their competence area
     */
    public String getCompetenceAreaDescription() {
    	return this.competenceAreaDescription;
    }
    
    /**
     * Set method, sets the description of the area this person has defined as their competence area
     * @param competenceAreaDescription description of the area this person has defined as their competence area
     */
    public void setCompetenceAreaDescription(String competenceAreaDescription) {
    	this.competenceAreaDescription = competenceAreaDescription;
    }
    
    /**
     * Get method, returns the role the person has in their current job
     * @return Role the person has in their current job
     */
    public String getRole() {
    	return this.role;
    }
    
    /**
     * Set method, sets the Role the person has in their current job
     * @param role Role the person has in their current job
     */
    public void setRole(String role) {
    	this.role = role;
    }
    
    /**
     * Get method, returns the URI of the job the Person is currently employed in
     * @return URI of the job the Person is currently employed in
     */
    public String getCurrentJobURI() {
    	return this.currentJobURI;
    }
    
    /**
     * Set method, sets the URI of the job the Person is currently employed in
     * @param jobURI URI of the job the Person is currently employed in
     */
    public void setCurrentJobURI(String jobURI) {
    	this.currentJobURI = jobURI;
    }
    
    /**
     * Get method, returns the CV model object of the cv associated with this Person
     * @return cv associated with this Person
     */
    public CV getCV() {
    	return personalCV;
    }
    
    /**
     * Set method, sets the input CV personalCV as the CV associated with this Person, as well as the URI in the cvURI variable from the CV
     * @param personalCV CV to be associated with this Person
     */
    public void setCV(CV personalCV) {
    	this.personalCV = personalCV;
    	setCVURI(personalCV.getURI());
    }
    
    /**
     * Get method, returns the Qualifications this Person has
     * @return Qualifications of this Person
     */
    public List<String> getQualifications(){
    	return qualifications;
    }
    
    /**
     * Add method, adds a qualification to the List of the Qualifications this Person has
     * @param qualification Qualification to be added to this Person
     */
    public void addQualification(String qualification) {
    	this.qualifications.add(qualification);
    }
    
    /**
     * Get method, Returns list of Work Experiences this Person had
     * @return List of work experiences this person had
     */
    public List<String> getExperiences(){
    	return experiences;
    }
    
    /**
     * Add method, adds a work experience to the list of Experiences this Person has
     * @param experience Work Experience to be added to this Person's list of Work Experiences
     */
    public void addExperience(String experience) {
    	this.experiences.add(experience);
    }
    
    /**
     * Get method, returns List of memberships this Person has
     * @return List of Memberships
     */
    public List<String> getMembership(){
    	return memberships;
    }
    
    /**
     * Add method, adds a new membership to this Person's Membership list
     * @param membership Membership to an association or group
     */
    public void addMembership(String membership) {
    	this.memberships.add(membership);
    }
    
    /**
     * Get method, returns published works of this Person
     * @return List of publication titles of his Person
     */
    public List<String> getPublications(){
    	return publications;
    }
    
    /**
     * Add method, adds a publication made by this Person
     * @param publication Title of a publication 
     */
    public void addPublication(String publication) {
    	this.publications.add(publication);
    }
    
    /**
     * Get method, returns this Person's accomplishments
     * @return List of Accomplishments
     */
    public List<String> getAccomplishments(){
    	return accomplishments;
    }
    
    /**
     * Add method, adds an accomplishment to this Person's list of accomplishments
     * @param accomplishment Accomplishment this Person achieved
     */
    public void addAccomplishment(String accomplishment) {
    	this.accomplishments.add(accomplishment);
    }
    
    public String getPersonJsonStringInput() {
    	JsonArray jsonResults = new JsonArray();
		
		JsonObject jsonPropValue = new JsonObject();
		jsonPropValue.addProperty("label",this.getLabel());
		jsonPropValue.addProperty("comment",this.getComment());
		jsonPropValue.addProperty("name", this.getName());
		jsonPropValue.addProperty("surname",this.getsurname());
		jsonPropValue.addProperty("gender",this.getGender());
		jsonPropValue.addProperty("nationality",this.getNationality());
		jsonPropValue.addProperty("driversLicense",this.getDriversLicense());
		jsonPropValue.addProperty("cvURI",this.getCVURI());
		jsonPropValue.addProperty("competenceArea",this.getCompetenceArea());
		jsonPropValue.addProperty("competenceAreaDescription",this.getCompetenceAreaDescription());
		jsonPropValue.addProperty("role",this.getRole());
		jsonPropValue.addProperty("currentJobURI",this.getCurrentJobURI());
		jsonPropValue.addProperty("URI", this.getURI());
		jsonPropValue.addProperty("ID", RDFObject.uri2id(this.getURI()));
		
		jsonResults.add(jsonPropValue);
		
		return jsonResults.toString();
    }
    
    
    /**
     * Save method, takes all the info present in the different variables and uploads them to the database in RDF triple format
     * @throws Exception
     */
	public void Save() throws Exception {
		Triple triple;
		super.rootRDFSave();

		Map<Triple, String> saveData = new HashMap<Triple, String>();
        
		//TODO: Double check foaf ontology for first and last name
		//foaf:name is a junction of name surname and title
		//could add title such as MD PHD and so on
		if(name != null) {
			triple = new Triple(getURI(), "foaf:firstName", name);
			saveData.put(triple, "String");
//			SparqlEndPoint.insertPropertyValue(triple);
		}
		
		if(surname != null) {
			triple = new Triple(getURI(), "foaf:lastName", surname);
			saveData.put(triple, "String");
//			SparqlEndPoint.insertPropertyValue(triple);
		}
		
		if(role != null) {
			triple = new Triple(getURI(), "foaf:title", role);
			saveData.put(triple, "String");
//			SparqlEndPoint.insertPropertyValue(triple);
		}
		
        if(gender != null) {
            triple = new Triple(getURI(), "cv:gender", gender);
            saveData.put(triple, "String");
//            SparqlEndPoint.insertPropertyValue(triple);	
        }
        
        if(driversLicense) {
            triple = new Triple(getURI(), "cv:hasDriversLicense", "true");
            saveData.put(triple, "String");
//            SparqlEndPoint.insertPropertyValue(triple);
        }
        else {
            triple = new Triple(getURI(), "cv:hasDriversLicense", "false");
            saveData.put(triple, "String");
//            SparqlEndPoint.insertPropertyValue(triple);	
        }
        
        if(cvURI != null) {
        	triple = new Triple(getURI(), "qc:hasResume", cvURI);
        	saveData.put(triple, "Object");
//            SparqlEndPoint.insertTriple(triple);	
        }
        
        if(competenceArea != null) {
        	triple = new Triple(":" + competenceArea, "rdf:type", "qc:CompetenceArea");
        	saveData.put(triple, "Object");
//    		SparqlEndPoint.insertTriple(triple);
    		
    		triple = new Triple(getURI(), "qc:field", ":" + competenceArea);
    		saveData.put(triple, "Object");
//            SparqlEndPoint.insertTriple(triple);
        }
        
        if(competenceArea != null && competenceAreaDescription != null) {
    		triple = new Triple(":" + competenceArea, "rdfs:label", competenceAreaDescription);
    		saveData.put(triple, "String");
//    		SparqlEndPoint.insertPropertyValue(triple);	
        }
        
        if(nationality != null) {
        	triple = new Triple(getURI(), "cv:hasNationality", nationality);
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        if(currentJobURI != null) {
        	triple = new Triple(getURI(), "qc:currentJob", currentJobURI);
        	saveData.put(triple, "Object");
        }
        
        for(String qualification : qualifications) {
        	triple = new Triple(getURI(), "qc:hasAccomplishment", ":" + qualification);
        	saveData.put(triple, "Object");
//        	SparqlEndPoint.insertTriple(triple);
        	
        	triple = new Triple(prefix + qualification, "rdf:type", "qc:Qualification");
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        for(String experience : experiences) {
        	triple = new Triple(getURI(), "qc:hasAccomplishment", ":" + experience);
        	saveData.put(triple, "Object");
//        	SparqlEndPoint.insertTriple(triple);
        	
        	triple = new Triple(prefix +experience, "rdf:type", "qc:Experience");
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        for(String membership : memberships) {
        	triple = new Triple(getURI(), "qc:hasAccomplishment", ":" + membership);
        	saveData.put(triple, "Object");
//        	SparqlEndPoint.insertTriple(triple);
        	
        	triple = new Triple(prefix + membership, "rdf:type", "qc:Membership");
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        for(String publication : publications) {
        	triple = new Triple(getURI(), "qc:hasAccomplishment", ":" + publication);
        	saveData.put(triple, "Object");
//        	SparqlEndPoint.insertTriple(triple);
        	
        	triple = new Triple(prefix + publication, "rdf:type", "qc:Publication");
        	saveData.put(triple, "String");
//        	SparqlEndPoint.insertPropertyValue(triple);
        }
        
        SparqlEndPoint.insertTriples(saveData);
        
        
}
	
	/**
	 * Gets a list of all Persons in the database
	 * @return List of Person objects
	 * @throws Exception 
	 */
    public static List<Person> getPersons() throws Exception{
        String SparqlJsonResults = SparqlEndPoint.getInstances(Person.ClassType);
        List<Person> persons = ParseResponse(SparqlJsonResults);
        if(persons.isEmpty())
        	throw new NoSuchElementException("No Profiles found in Database");
        return persons;
    }

    /**
     * Get a Person object from the database from a specific URI input
     * @param URI URI of the Person in the database
     * @return Person class object with the input URI
     * @throws Exception 
     */
	public static Person getPerson(String URI) throws Exception{
        String uri = URI;
		if (!uri.startsWith(":") && !uri.startsWith("<http")){
			if(uri.startsWith("http"))
				uri = "<" + uri + ">";
			else
				uri = ":"+uri;
        }
		if(!SparqlEndPoint.existURI(uri)) {
			throw new NoSuchElementException("Person with URI: " + uri + " Not found");
		}
        String properties = SparqlEndPoint.getAllProperties(uri);
        Person person = ParseResponseToPerson(properties);
        person.setURI(uri);
        return person;
    }
	
	//TODO:getPersonsByField should get the persons with the field through the fields label instead or partial match
	//GET THE PERSON JSON RESULTS IN A SINGLE JSON
	/**
	 * Get list of Persons working in a specific field of work
	 * @param field Area of competence
	 * @return List of Persons related to a certain field of work
	 * @throws Exception 
	 */
	public static List<Person> getPersonsByField(String field) throws Exception{
		List<Person> persons = new ArrayList<Person>();
		String SparqlJsonFieldResults = SparqlEndPoint.getInstancesByPartialLabel("qc:CompetenceArea", field);
		List<String> properties = ParseResponseToProperty(SparqlJsonFieldResults);
		List<String> SparqlJsonResults = new ArrayList<String>();
		
		for(String property: properties) {
			SparqlJsonResults.add(SparqlEndPoint.getInstancesByProperty(ClassType, "qc:field","<" + property + ">" ));		
			
		}
		
		InputStream in = null;
		ResultSet results = null;
		for(String person: SparqlJsonResults) {
			in = new ByteArrayInputStream(person.getBytes(StandardCharsets.UTF_8));
	        results = ResultSetFactory.fromJSON(in);
	        while(results.hasNext()) {
	        	QuerySolution soln = results.nextSolution();
	            persons.add(getPerson(String.valueOf(soln.getResource("subject"))));
	        }
		}
//		return ParseResponse(SparqlJsonResults);
		return persons;
	}
	
	/**
	 * Get a Person with a specific name or partial name
	 * @param name Name or partial name of the Person being retrieved
	 * @return Person class object with a matching name
	 * @throws Exception 
	 */
	public static Person getPersonByName(String name) throws Exception {
		String SparqlJsonResults = SparqlEndPoint.getInstancesByPartialLabel(ClassType, name);
		String uri = SparqlEndPoint.ParseResponseToURI(SparqlJsonResults);
		return getPerson("<" + uri + ">");
	}
	
	/**
	 * Get a person with a specific associated CV
	 * @param cvURI The URI of the CV associated with the Person being retrieved
	 * @return Person class object associated with a CV
	 * @throws Exception 
	 */
	public static Person getPersonByCV(String cvURI) throws Exception {
		String SparqlJsonResults = SparqlEndPoint.getInstancesByProperty(ClassType, "qc:hasResume", cvURI);
		String uri = SparqlEndPoint.ParseResponseToURI(SparqlJsonResults);
		if (uri !=null)
            return getPerson("<" + uri + ">");
        else
            return null;
	}
	
	/**
	 * Delete method, deletes the returned Person from the database
	 * @param URI URI of the Person being deleted from the database
	 * @return Person class object with the URI input
	 * @throws Exception 
	 */
	public static Person deleteObject(String URI) throws Exception {
		Person p = Person.getPerson(URI);
    	SparqlEndPoint.deleteObjectByUri(p.getURI());
    	return p;
    }
	
	/**
	 * Parses the Json data input to a list of properties
	 * @param SparqlJsonResults Input string of data in a Json format from a Sparql query
	 * @return List of properties from the input
	 */
	private static List<String> ParseResponseToProperty(String SparqlJsonResults) {
        InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);
        List<String> properties = new ArrayList<String>();
        while(results.hasNext()) {
        	
        	QuerySolution soln = results.nextSolution();
        	
        	properties.add(String.valueOf(soln.getResource("subject")));
        }
        
		return properties;
	}
	
	/**
	 * Parser for the input Json data, turns the data into a Person class object
	 * @param SparqlJsonResults Json data containing information about a Person
	 * @return Person class object parsed from the input Json format data
	 */
	private static Person ParseResponseToPerson(String SparqlJsonResults){

        InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);

//        System.out.println("============================================");
        
        Person person = new Person();

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

            switch (res.getLocalName()) {

                case "label":
                    person.setLabel(object);
                    break;
                    
                case "firstName":
                	person.setName(object);
                	break;

                case "lastName":
                	person.setsurname(object);
                	break;
                	
                case "title":
                	person.setRole(object);
                	break;
                	
                case "gender":
                	person.setGender(object);
                	break;
            
                case "hasDriversLicense": 
                	if(object.equals("true"))
                		person.setDriversLicense(true);
                	else if(object.equals("false"))
                		person.setDriversLicense(false);	
                	break;
                	
                case "hasResume":
                	if(object.contains("#"))
                		object = object.substring(object.indexOf("#") + 1);
                	person.setCVURI(object);
                	break;
                	
                case "field":
                	if(object.contains("#"))
                		object = object.substring(object.indexOf("#") + 1);
                	person.setCompetenceArea(object);
                	break;
                	

                case "hasAccomplishment":
                	person.addAccomplishment(object);
                	break;

                case "comment":
                	person.setComment(object);
                	break;
                	
                case "currentJob":
                	if(object.contains("#"))
                		object = object.substring(object.indexOf("#") + 1);
                	person.setCurrentJobURI(":" + object);
                	break;
                          	
                default:
                    break;
            }
            
            //String ID = String.valueOf(soln.getResource("predicate"));
            //System.out.println(ID);   
            //cv.setId(ID);   
            //cv.setId(StringUtils.substringAfter(ID,"http://rdfs.org/resume-rdf/cv.rdfs#"));   

        } 
        return person; 

    }

	/**
	 * Parses the input data to a list of Person class objects
	 * @param SparqlJsonResults Json format data containing information about multiple Person class objects
	 * @return List of Persons parsed from the input data
	 * @throws Exception 
	 */
	private static List<Person> ParseResponse(String SparqlJsonResults) throws Exception {

        List<Person> Persons = new ArrayList<>();  
        InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);
        while (results.hasNext()) {
        	QuerySolution soln = results.nextSolution();
            Resource res= soln.getResource("subject");
            //String ID = String.valueOf(res);      
            //Extra steps to parse the URIs that don't contain letters(only numbers)
            String ID =  res.getURI().substring(res.getURI().lastIndexOf("#")+1);
            try {
            	Person person = getPerson(":" + ID);
                person.setID( ID);   
                //cv.setURI(StringUtils.substringAfter(ID,"http://rdfs.org/resume-rdf/cv.rdfs#"));   

                Persons.add(person); 
            }
            catch (Exception e) {
            	System.out.println("Tried to retrieve profile with ID: " + ID + " but was unsucessful");
            }
            
        } 
        return Persons; 
	}
	
	//Maybe add a String defining the type of accomplishment and use it to further define the accomplishment and its details
	/**
	 * Saves a single accomplishment to the server database in RDF triple format
	 * @param accomplishment accomplishment made by this Person
	 */
	@SuppressWarnings("unused")
	private void saveAccomplishment(String accomplishment) {
    	Triple triple = new Triple(getURI(),"qc:hasAccomplishment", accomplishment);
    	SparqlEndPoint.insertPropertyValue(triple);
		
	}

	public boolean hasAppliedToJob(String jobID) throws Exception {
		String jobURI = jobID;
		if(!jobURI.contains(":"))
			jobURI = ":" + jobURI;
		List<Application> apps = Application.getApplicationsByProfile(this.getURI());
//		System.out.println(apps);
		for(Application app : apps) {
			if(app.getJobURI() != null && app.getJobURI().equals(jobURI))
				return true;
		}
		return false;
	}

}
