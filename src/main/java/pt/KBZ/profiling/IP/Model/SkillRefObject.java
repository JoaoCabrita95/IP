package IP.Model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class SkillRefObject extends RDFObject {
	
	public static final String ClassType ="qc:skillRef";     
    private static final String prefix ="qc:";     
	private Skill skill;
	private String skillURI;
	private String skillID;
	private String skillName;
	private String skillLevel;
	
	public SkillRefObject() {
		super(ClassType, prefix);
	}
	
	public SkillRefObject(Skill skill, String skillLevel) {
		super(ClassType, prefix);
		this.skill = skill;
		this.skillURI = skill.getURI();
		this.skillID = skill.getID();
		this.skillName = skill.getLabel();
		this.skillLevel = skillLevel;
		
	}
	
	
	public Skill getSkill() {
		return skill;
	}
	
	public void setSkill(Skill skill) {
		this.skill = skill;
		this.skillURI = skill.getURI();
		this.skillName = skill.getLabel();
	}
	
	public String getSkillURI() {
		if(skillURI == null && skillID != null) {
			skillURI = ":" + skillID;
		}
		return skillURI;
	}
	
	public void setSkillURI(String skillURI) {
		this.skillURI = skillURI;
	}
	
	public String getSkillName() {
		return skillName;
	}
	
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	
	public String getSkillLevel() {
		return skillLevel;
	}
	
	public void setSkillLevel(String skillLevel) {
		this.skillLevel = skillLevel;
	}
	
	public void Save() {
		
		Triple triple;
		super.rootRDFSave();
		
		Map<Triple, String> saveData = new HashMap<Triple, String>();
		
		if(skillURI == null && skillID != null) {
			skillURI = ":" + skillID;
		}
		
		if(skill == null && skillURI != null) {
			skill = Skill.getSkill(skillURI);
		}
		
		if(skill != null) {
			if(!SparqlEndPoint.existURI(skill.getURI())) {
				try {
					skill.Save();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	        triple = new Triple(getURI(), "qc:referesToSkill", skill.getURI());
	        saveData.put(triple, "Object");

        } 
		
		if(skillLevel == null) 
			skillLevel = "basic";
		triple = new Triple(getURI(), "qc:skillLevel", skillLevel);
	    saveData.put(triple, "String");
		
		
		SparqlEndPoint.insertTriples(saveData);
	}
	
	public static SkillRefObject getSkillRefObject(String URI) {
		String uri = URI;
        if (!uri.startsWith(":") && !uri.startsWith("<http")){
        	if(uri.contains("#"))
        		uri = ":" + uri.substring(uri.indexOf("#") + 1);
        	else
        		uri = ":"+URI;
        }
        if(!SparqlEndPoint.existURI(uri)) {
			throw new NoSuchElementException("Skill reference with URI: " + uri + " Not found");
		}
        String properties = SparqlEndPoint.getAllProperties(uri);
        SkillRefObject skillRef = ParseResponseToSkillRef(properties);
        skillRef.setURI(uri);
        return skillRef;
	}
	
	public static SkillRefObject getSkillRefObjectBySkillID(String skillID) {
		String uri = skillID;
        if (!uri.startsWith(":") && !uri.startsWith("<http")){
        	if(uri.contains("#"))
        		uri = ":" + uri.substring(uri.indexOf("#") + 1);
        	else
        		uri = ":"+skillID;
        }
        if(!SparqlEndPoint.existURI(uri)) {
			throw new NoSuchElementException("Skill with URI: " + uri + " Not found");
		}
        
        String SparqlJsonResults = SparqlEndPoint.getInstancesByProperty(ClassType, "qc:referesToSkill", uri);
        SkillRefObject skillRef = getSkillRefObject(SparqlEndPoint.ParseResponseToURI(SparqlJsonResults));
        
        return skillRef;
	}

	private static SkillRefObject ParseResponseToSkillRef(String SparqlJsonResults) {
		InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);

//        System.out.println("============================================");
        
        SkillRefObject skillRef = new SkillRefObject();

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
//                    System.out.println("type: "+type);
                    break;

                case "label":
                    String label = object;   
//                    System.out.println("label: " + label);
                    skillRef.setLabel(label);
                    break;
                    
                case "comment":
                	String comment = object;  
//                	System.out.println("comment : " + comment);
                	skillRef.setComment(comment);
                	break;
                    
                case "referesToSkill":
                	String URI = object;  
                	if(URI.contains("#"))
                		URI = URI.substring(URI.indexOf("#") + 1);
                	try {
                		Skill skill = Skill.getSkill(URI);
                    	skillRef.setSkill(skill);
                    	skillRef.setSkillName(skill.getLabel());
                    	skillRef.setSkillURI(skill.getURI());
                	}
                	catch(Exception e) {
                		e.printStackTrace();
                	}
                	
                	break;
            
                default:
                    break;
            }
        } 
        return skillRef; 
	}
	
}
