package IP.Model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
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
import org.cyberborean.rdfbeans.annotations.RDF;
import org.cyberborean.rdfbeans.annotations.RDFBean;
import org.cyberborean.rdfbeans.annotations.RDFNamespaces;
import org.cyberborean.rdfbeans.annotations.RDFSubject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/*
//for <classpathentry kind="lib" path="lib/RDFBeans-2.1/RDFBeans-2.1.jar"/>
import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFNamespaces;
import com.viceversatech.rdfbeans.annotations.RDFSubject;
*/

@RDFNamespaces(
{      
    "rdfs = http://www.w3.org/2000/01/rdf-schema",
    "qc = http://www.qualichain.eu/ontology/",
    "cv = http://rdfs.org/resume-rdf/cv.rdfs/" 
})

/**
 * Java object representing a Skill, either for job requirements, skills a user has knowledge in 
 * or for recomendations of further exploration of certain skills that might be usefull for certain users
 */
@RDFBean("saro:Skill")
public class Skill extends RDFObject //implements Serializable 
{ 	
    private static String ClassType = "saro:Skill";
    private static String prefix = "saro:";  
    private List<String> superClasses;
//    //Right now the accepted values for skill proficiency are as such: Basic, Junior, Senior, Expert
//    private String proficiencyLevel;
//    //Right now the accepted values for skill priority are as such: Low, Medium, High
//    private String priorityLevel;
    private String coreTo;
    private String isFrom;
    private String skillType;
    private String reuseLevel;
    private List<String> synonyms;
    private List<String> subClasses;
    
    private enum translationLanguages {pt, en, el};
    
    
    
    //TODO: Add constructors for different cases if found necessary
    
    /**
     * Constructor for a Skill object with no set variables
     */
    public Skill() {
    	super(ClassType, prefix);
    	synonyms = new ArrayList<String>();
    	subClasses = new ArrayList<String>();
    	superClasses = new ArrayList<String>();
    }
    
    /**
     * Constructor for a Skill object with set id, label, proficiency level, priority level and comment
     * @param id Skill unique identifier
     * @param label Name of the Skill
     * @param profLevel Level of proficiency of the Skill 
     * @param priority Level of priority of the Skill 
     * @param comment Description of the Skill
     */
    public Skill(String id, String label, String profLevel, String priority, String comment) {
    	super(ClassType, prefix, id, label, comment);
//    	this.proficiencyLevel = profLevel;
//    	this.priorityLevel = priority;
    	synonyms = new ArrayList<String>();
    	subClasses = new ArrayList<String>();
    	superClasses = new ArrayList<String>();
    }
    
    /**
     * Constructor for a Skill object with several set variables
     * @param id Skill unique identifier
     * @param label Name of the Skill
     * @param profLevel Proficiency level of the Skill
     * @param priority Priority level of the Skill
     * @param comment Description of the Skill
     * @param coreTo Area of expertise the Skill belongs to
     * @param synonyms Synonims of the Skill
     * @param subClasses Skills that are less specific but hold some overlap in what skillset they describe
     * @param superClasses Skills that use some of the properties of this Skill but are more advanced
     */
    public Skill(String id, String label, String profLevel, String priority, String comment, String coreTo,
    		List<String> synonyms, List<String> subClasses, List<String> superClasses) {
    	super(ClassType, prefix, id, label, comment);
//    	this.proficiencyLevel = profLevel;
//    	this.priorityLevel = priority;
    	this.synonyms = synonyms;
    	this.subClasses = subClasses;
    	this.superClasses = superClasses;
    	this.coreTo = coreTo;
    }

    /**
     * Get method for Super classes of the Skill
     * @return List of super classes
     */
    public List<String> getSuperClasses() {
    	return superClasses;
    }
    
    /**
     * Add method to make relationships between this Skill and super class Skills
     * @param superClass URI of a Skill that will be set as a super class of this Skill
     */
    public void addSuperClass(String superClass) {
    	String uri = superClass;
//    	if(uri.startsWith("http"))
//    		uri = prefix + uri.substring(uri.indexOf("#")+1, uri.indexOf(">"));
    	this.superClasses.add(uri);
    }
    
    /**
     * Remove method of the list of super classes of this Skill
     * @param superClass URI of a Skill that will be removed as a super class of this Skill
     */
    public void removeSuperClass(String superClass) {
    	superClasses.remove(superClass);
    }
    
//    /**
//     * Returns the proficiency level at which this Skill is known
//     * @return proficiencyLevel variable, a String that represents the level of proficiency at which this Skill is known
//     */
//    public String getProficiencyLevel() {
//    	return this.proficiencyLevel;
//    }
//    
//    /**
//     * Sets the proficiency level at which this Skill is known
//     * @param profLevel String representation of the level of proficiency at which this Skill is known, that is used
//     * to set the proficiencyLevel variable of the Skill
//     */
//    public void setProficiencyLevel(String profLevel) {
//    	this.proficiencyLevel = profLevel;
//    }
//    
//    /**
//     * Return the priorityLevel variable of the Skill, which is a String representation of how important this Skill is for a JobPosting
//     * @return priorityLevel variable, which represents how important this Skill is for a JobPosting
//     */
//    public String getPriorityLevel() {
//    	return this.priorityLevel;
//    }
//    
//    /**
//     * Sets the priority level of the Skill for a JobPosting
//     * @param level String representation of the priority level that will be set to the priorityLevel variable of the SKill
//     */
//    public void setPriorityLevel(String level) {
//    	priorityLevel = level;
//    }
    
    /**
     * Get method that returns what area of expertise this Skill belongs to
     * @return coreTo variable describing the area of expertise of this Skill
     */
    public String getCoreTo() {
    	return coreTo;
    }
    
    /**
     * Set method to define what area of expertise this Skill belongs to
     * @param coreTo variable describing the area of expertise of this Skill
     */
    public void setCoreTo(String coreTo) {
    	this.coreTo = coreTo;
    }
    
    /**
     * Get method that returns where this skill was defined/who defined it
     * @return Who defined/where this skill was defined
     */
    public String getIsFrom() {
    	return isFrom;
    }
    
    /**
     * Set who defined this Skill or where this skill was defined, for example, which database it was pulled from,
     *  or if it's a custom Skill that was user defined, what user defined it
     * @param isFrom where this skill was defined or who defined it
     */
    public void setIsFrom(String isFrom) {
    	this.isFrom = isFrom;
    }
    
    /**
     * Get method that returns what type of Skill this is, if it's a Skill, knowledge or any other type of Skill descriptors
     * @return Skill Type
     */
    public String getSkillType() {
    	return skillType;
    }
    
    /**
     * Sets the type of Skill this object represents
     * @param skillType type of skill this Skill is to be defined as
     */
    public void setSkillType(String skillType) {
    	this.skillType = skillType;
    }
    
    /**
     * Get method that returns a description of how this skill can be reused
     * @return Reuse level variable describing how this Skill can be used in different occupations or areas of work
     */
    public String getReuseLevel() {
    	return reuseLevel;
    }
    
    /**
     * Sets the description of how this Skill can be used
     * @param reuseLevel Description of how the Skill can be used in different occupations or areas of work
     */
    public void setReuseLevel(String reuseLevel) {
    	this.reuseLevel = reuseLevel;
    }
    
    /**
     * Get method that returns a list of synonyms of this skill,
     * mostly for Matching purposes and to verify if different named skills can be used for simillar purposes
     * @return List of Synomyms of this SKill
     */
    public List<String> getSynonyms(){
    	return synonyms;
    }
    
    /**
     * Define a new synonym for this Skill
     * @param synonym the name of a Skill that is synonym to this Skill
     */
    public void addSynonym(String synonym) {
    	synonyms.add(synonym);
    }
    
    /**
     * Remove a synonym for this Skill
     * @param synonym the name of a Skill that is no longer desired to be a synonym to this Skill
     */
    public void removeSynonym(String synonym) {
    	synonyms.remove(synonym);
    }
    
    /**
     * Get method for the list of sub classes of this SKill
     * @return List of sub classes of this Skill
     */
    public List<String> getsubClasses(){
    	return subClasses;
    }
    
    /**
     * Define a new sub class for this Skill
     * @param subClass URI of a Skill that is considered to be a sub class of this Skill
     */
    public void addsubClass(String subClass) {
    	String uri = subClass;
    	if(uri.startsWith("http"))
    		uri = "<" + uri + ">";
    	subClasses.add(uri);
    }
    
    /**
     * Remove a sub class from this Skill
     * @param subClass URI of the sub class that is no longer considered a sub class of this SKill
     */
    public void removesubClasses(String subClass) {
    	subClasses.remove(subClass);
    }
    
    /**
     * Transforms all the variables of the Skill into Triple class objects for insertion in the Database handled by the SparqlEndPoint class
     * @throws Exception 
     */
	public void Save() throws Exception {
            
            super.rootRDFSave();
            
            Triple triple;
            
    		Map<Triple, String> saveData = new HashMap<Triple, String>();
    		
//            if(proficiencyLevel != null) {
//            	triple = new Triple(getURI(), "saro:atProficiencyLevel", proficiencyLevel);
//            	saveData.put(triple, "String");
////            	SparqlEndPoint.insertPropertyValue(triple);
//            }
//            
//            if(priorityLevel != null) {
//            	triple = new Triple(getURI(), "qc:hasPriority", priorityLevel);
//            	saveData.put(triple, "String");
////                SparqlEndPoint.insertPropertyValue(triple);
//            }
            
            if(coreTo != null) {
            	triple = new Triple(getURI(), "saro:isCoreTo", coreTo);
            	saveData.put(triple, "Object");
            }
            
            if(isFrom != null) {
            	triple = new Triple(getURI(), "saro:isFrom", isFrom);
            	saveData.put(triple, "String");
            }
            
            if(skillType != null) {
            	triple = new Triple(getURI(), "saro:hasSkillType", skillType);
            	saveData.put(triple, "String");
            }
            
            if(reuseLevel != null) {
            	triple = new Triple(getURI(), "saro:hasReuseLevel", reuseLevel);
            	saveData.put(triple, "String");
            }
            
            for(String superClass : superClasses) {
            	triple = new Triple(getURI(), "saro:broaderSkill" , superClass);
            	saveData.put(triple, "Object");
            }
            
            for(String synonym : synonyms) {
            	triple = new Triple(getURI(), "saro:synonymOf" , synonym);
            	saveData.put(triple, "String");
            }
            
            for(String subClass : subClasses) {
            	triple = new Triple(getURI(), "saro:narrowerSkill" , subClass);
            	saveData.put(triple, "Object");
            }
            
            SparqlEndPoint.insertTriples(saveData); 
            
	}
	
	/**
	 * Returns the Skill object representation of the Skill with the URI from the parameter uri by performing a GET request to the Database
	 * @param uri The String representation of the URI of the desired Skill
	 * @return Skill object representation of a Skill in the Database with the same URI as the parameter uri
	 */
	public static Skill getSkill(String URI) {
        String uri = URI;
    	if(!uri.startsWith(prefix) && !uri.startsWith("<http")) {
    		if(uri.startsWith(":"))
    			uri = "saro" + uri;
        	if(uri.startsWith("http"))
        		uri ="<"+ uri + ">";
        	else if(!uri.startsWith(prefix))
        		uri = prefix + uri;
        }
    	if(!SparqlEndPoint.existURI(uri)) {
			throw new NoSuchElementException("Skill with URI: " + uri + " Not found");
		}
    	
		String properties = SparqlEndPoint.getAllProperties(uri);
	    //System.out.println(properties);
        Skill skill = ParseResponseToSkill(properties);
	    skill.setURI(uri);
	    
	    return skill;
	}

	/**
	 * Returns a Skill object that has the same Label as the parameter label
	 * @param label String representation of the name of the Skill
	 * @return Skill object stored in the Database with the same Label as the parameter label
	 */
    public static Skill getSkillByLabel(String label) {
		String properties = SparqlEndPoint.getInstancesByLabel(ClassType,label);
        String uri = SparqlEndPoint.ParseResponseToURI(properties);
        if (uri !=null){
            Skill skill = Skill.getSkill(uri);
            return skill;
        }
        else
            return null;
	}

    /**
     * Returns a list of all the Skills stored in the Database as Skill objects
     * @return List of all Skills  stored in the Database accessible by the SparqlEndPoint class as Skill objects
     */
    public static List<Skill> getSkills() throws Exception{
        String SparqlJsonResults = SparqlEndPoint.getInstances(ClassType);
        List<Skill> allSkills = ParseResponse(SparqlJsonResults);
        if(allSkills.isEmpty())
        	throw new NoSuchElementException("There are no Skills found in the Database");
        return allSkills;
    }
    
    public static List<Skill> getSkills(String searchText){
        String SparqlJsonResults = SparqlEndPoint.getInstancesByPartialLabel(ClassType,searchText);
        return ParseResponse(SparqlJsonResults);
    }

    public static Skill deleteObject(String URI) {
    	Skill skill = Skill.getSkill(URI);
    	SparqlEndPoint.deleteObjectByUri(skill.getURI());
    	return skill;
    }
    
    /**
     * Gets a list of skills related to the skill given at input, at the head of the list will be the superClass of the Skill given at input if it has any
     * then fills the rest of the list with synonym skills
     * @param skillURI URI of the skill which will be operated on
     * @return Map of relationships between the original skill and other skills, superClass skills, Synonym skills and subClass skills, in the format of 
     * "type of relationship" to list of skills with that relationship in relation to the input skill
     */
    public static Map<String, List<Skill>> getRelatedSkills(String skillURI){
    	Map<String, List<Skill>> skillRelationships = new HashMap<String, List<Skill>>();
    	List<Skill> relatedSkills = new ArrayList<Skill>();
    	
    	Skill skill = Skill.getSkill(skillURI);
    	relatedSkills.add(skill);
    	skillRelationships.put("Original skill", relatedSkills);
    	
    	relatedSkills = new ArrayList<Skill>();
    	for(String superClass : skill.getSuperClasses()) {
    		relatedSkills.add(Skill.getSkill(superClass));
    	}
    	skillRelationships.put("Skill SuperClasses", relatedSkills);
    	
    	relatedSkills = new ArrayList<Skill>();
    	for(String synonymSkill : skill.getSynonyms()) {
    		relatedSkills.add(Skill.getSkill(synonymSkill));
    	}
    	skillRelationships.put("Synonym Skills", relatedSkills);
    	
    	relatedSkills = new ArrayList<Skill>();
    	for(String subClass : skill.getsubClasses()) {
    		relatedSkills.add(Skill.getSkill(subClass));
    	}
    	//ClassType might have to change to saro:Skill for the whole class
//    	String SparqlResults = SparqlEndPoint.getInstancesByProperty(ClassType, "saro:subClassOf", skillURI);
//    	relatedSkills = ParseResponse(SparqlResults);
    	skillRelationships.put("SubClass Skills", relatedSkills);
    	
    	
    	return skillRelationships;
    }
    
    public static List<String> getSkillFields() {
		// TODO Auto-generated method stub
    	
    	List<String> fields = new LinkedList<String>();
    	
    	String SparqlJsonResults = SparqlEndPoint.getAllPropertiesByType("saro:isCoreTo");
    	
    	InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);
        String object = "";
        while (results.hasNext()) {
        	QuerySolution soln = results.nextSolution();
            Resource res= soln.getResource("object");
            
            RDFNode Onode = soln.get("object");
            if (Onode.isResource()) {
                object = String.valueOf(soln.getResource("object"));
            }
            else{
                object = String.valueOf(soln.getLiteral("object"));   
            }
                  
            if(object.contains("#"))
            	object = object.substring(object.indexOf("#") +1 );
            
            if(!fields.contains(object))
            	fields.add(object);
        }
        
		return fields;
	}
    
    public static JsonArray getSkillLabelsByField(String field){
    	
    	JsonArray skillsLabelsByURI = new JsonArray();
    	
    	if(!field.contains(":"))
    		field = "saro:" + field;
    	
    	String SparqlJsonResults = SparqlEndPoint.getLabelByPropertyValueFromClass(ClassType, "saro:isCoreTo", field);
    	
    	JsonObject translations;
    	InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);
        String subject = "";
        String object = "";
        
        JsonObject tmpSkill;
        
        String translationSparqlJsonResults;
        String translation;
        String[] splitTranslation = new String[2];
        
        while (results.hasNext()) {
        	
        	tmpSkill = new JsonObject();
        	
        	QuerySolution soln = results.nextSolution();
            
            RDFNode Onode = soln.get("object");
            if (Onode.isResource()) {
                object = String.valueOf(soln.getResource("object"));
            }
            else{
                object = String.valueOf(soln.getLiteral("object"));   
            }
            
            if(object.contains("#"))
            	object = object.substring(object.indexOf("#") +1 );
            
            Onode = soln.get("subject");
            if (Onode.isResource()) {
            	subject = String.valueOf(soln.getResource("subject"));
            }
            else{
            	subject = String.valueOf(soln.getLiteral("subject"));   
            }
            
            if(subject.contains("#"))
            	subject = subject.substring(subject.indexOf("#") +1 );
            
            if(!subject.contains(":"))
            	subject = "saro:" + subject;
            
            
            translations = new JsonObject();
            
            translationSparqlJsonResults = SparqlEndPoint.getObjectByUriProperty(subject, "skos:prefLabel");
            
            InputStream translationIn = new ByteArrayInputStream(translationSparqlJsonResults.getBytes(StandardCharsets.UTF_8));
            ResultSet translationResults = ResultSetFactory.fromJSON(translationIn);
            
            while (translationResults.hasNext()) {
            	QuerySolution transSoln = translationResults.nextSolution();
                
                RDFNode transOnode = transSoln.get("object");
                if (transOnode.isResource()) {
                	translation = String.valueOf(transSoln.getResource("object"));
                }
                else{
                	translation = String.valueOf(transSoln.getLiteral("object"));   
                }
                splitTranslation = translation.split("@");
                
                for(translationLanguages l : translationLanguages.values()) {
                	if(splitTranslation[1].equals(l.toString()))
                		translations.addProperty(splitTranslation[1], splitTranslation[0]);
                }
                
            }
            
            
            tmpSkill.addProperty("uri", subject);
            tmpSkill.addProperty("label", object);
            tmpSkill.add("translations", translations);
            
            skillsLabelsByURI.add(tmpSkill);
        }
        
    	return skillsLabelsByURI;
    }
    
    /**
     * Parses a Json Response from the Database into Skill objects and creates a List of all the Skills gathered from the Response
     * @param SparqlJsonResults Database query response in Json format with RDF data structure 
     * @return A list of Skill objects derived from the Json SparqlJsonResults
     */
    private static List<Skill> ParseResponse(String SparqlJsonResults){
    	
        List<Skill> Skills = new ArrayList<>();  
        InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);
        
        while (results.hasNext()) {

            QuerySolution soln = results.nextSolution();
            Resource res= soln.getResource("subject");

            //String ID = String.valueOf(res);            
            String ID =  res.getURI().substring(res.getURI().lastIndexOf("#")+1);   
            try {
            	Skill skill = getSkill(prefix + ID);
                skill.setID( ID);   
                //cv.setURI(StringUtils.substringAfter(ID,"http://rdfs.org/resume-rdf/cv.rdfs#"));   

                Skills.add(skill);  
            }
            catch (Exception e) {
            	System.out.println("Tried to retrieve skill with ID: " + ID + " but was not sucessful");
            }
            
        } 
        return Skills; 
    }

    /**
     * Returns a single Skill object from a String in Json format
     * @param SparqlJsonResults Database query response in Json format of a single Skill with RDF data structure 
     * @return Skill object derived from parsed Json SparqlJsonResults
     */
    private static Skill ParseResponseToSkill(String SparqlJsonResults) {

        InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);

//        System.out.println("============================================");
        
        Skill skill = new Skill();

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
                case "type":
                    String type = object;   
                    break;

                case "label":
                    String label = object;   
                    skill.setLabel(label);
                    break;
                 
                case "comment":
                	String comment = object;  
                	skill.setComment(comment);
                	break;
                	
                case "broaderSkill":
                	String hasSuperClass = object; 
                	if(hasSuperClass.contains("#"))
                		hasSuperClass = "saro:" + hasSuperClass.substring(hasSuperClass.indexOf("#") + 1);
                	skill.addSuperClass(hasSuperClass);
                	break;
                	
//                case "atProficiencyLevel":
//                	String proficiency = object;  
//                	skill.setProficiencyLevel(proficiency);
//                	break;
//                	
//                case "hasPriority":
//                	String priority = object;  
//                	skill.setPriorityLevel(priority);
//                	break;
                	
                case "coreTo":
                	String coreTo = object;
                	if(coreTo.contains("#"))
                		coreTo = ":" + coreTo.substring(coreTo.indexOf("#") + 1);
                	skill.setCoreTo(coreTo);
                	break;
                	
                case "isFrom":
                	String isFrom = object;
                	skill.setIsFrom(isFrom);
                	break;
                	
                case "hasSkillType":
                	String hasSkillType = object;
                	skill.setSkillType(hasSkillType);
                	break;
                	
                case "hasReuseLevel":
                	String hasReuseLevel = object;
                	skill.setReuseLevel(hasReuseLevel);
                	break;
                	
                case "synonymOf":
                case "prefLabel":
                case "altLabel":
                	String synonym = object;  
                	skill.addSynonym(synonym);
                	break;
                	
                case "narrowerSkill":
                	String hasSubClass = object;
                	if(hasSubClass.contains("#"))
                		hasSubClass = "saro:" + hasSubClass.substring(hasSubClass.indexOf("#") + 1);
                	skill.addsubClass(hasSubClass);
                	break;
                	
                default:
                    break;
            }
        }
        return skill;
    }
    
    
    public boolean equals(Skill compareTo) {
    	if(!this.getURI().equals(compareTo.getURI()))
    		return false;
    	if(!this.getID().equals(compareTo.getID()))
    		return false;
    	
    	if(this.getCoreTo() == null && compareTo.getCoreTo() != null) {
    		return false;
    	}
    	else if(this.getCoreTo() == null && compareTo.getCoreTo() == null) {
    		
    	}
    	else if(!this.getCoreTo().equals(compareTo.getCoreTo())){
        	return false;
    	}
    	
    	if(this.getIsFrom() == null && compareTo.getIsFrom() != null) {
    		return false;
    	}
    	else if(this.getIsFrom() == null && compareTo.getIsFrom() == null) {
    		
    	}
    	else if(!this.getIsFrom().equals(compareTo.getIsFrom()))
    		return false;
    	
    	if(this.getSkillType() == null && compareTo.getSkillType() != null) {
    		return false;
    	}
    	else if(this.getSkillType() == null && compareTo.getSkillType() == null) {
    		
    	}
    	else if(!this.getSkillType().equals(compareTo.getSkillType()))
    		return false;
    	
    	if(this.getReuseLevel() == null && compareTo.getReuseLevel() != null) {
    		return false;
    	}
    	else if(this.getReuseLevel() == null && compareTo.getReuseLevel() == null) {
    		
    	}
    	else if(!this.getReuseLevel().equals(compareTo.getReuseLevel()))
    		return false;
    	
    	if(this.getLabel() == null && compareTo.getLabel() != null) {
    		return false;
    	}
    	else if(this.getLabel() == null && compareTo.getLabel() == null) {
    		
    	}
    	else if(!this.getLabel().equals(compareTo.getLabel()))
    		return false;
    	
    	if(this.getComment() == null && compareTo.getComment() != null) {
    		return false;
    	}
    	else if(this.getComment() == null && compareTo.getComment() == null) {
    		
    	}
    	else if(!this.getComment().equals(compareTo.getComment()))
    		return false;
    	
    	for(String subClass : this.getsubClasses()) {
    		if(!compareTo.getsubClasses().contains(subClass))
    			return false;
    	}
    	
    	for(String subClass : compareTo.getsubClasses()) {
    		if(!this.getsubClasses().contains(subClass))
    			return false;
    	}
    	
    	for(String superClass : this.getSuperClasses()) {
    		if(!compareTo.getSuperClasses().contains(superClass))
    			return false;
    	}
    	
    	for(String superClass : compareTo.getSuperClasses()) {
    		if(!this.getSuperClasses().contains(superClass))
    			return false;
    	}
    	
    	for(String synonim : this.getSynonyms()) {
    		if(!compareTo.getSynonyms().contains(synonim))
    			return false;
    	}
    	
    	for(String synonim : compareTo.getSynonyms()) {
    		if(!this.getSynonyms().contains(synonim))
    			return false;
    	}
    	
    	return true;
    }

	
}