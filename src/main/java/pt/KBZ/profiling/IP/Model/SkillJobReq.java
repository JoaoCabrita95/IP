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

public class SkillJobReq extends SkillRefObject {
	
	private String priorityLevel;
	
	public SkillJobReq() {
		super();
	}
	
	public SkillJobReq(Skill skill, String priorityLevel, String skillLevel) {
		super(skill, skillLevel);
		this.priorityLevel = priorityLevel;
		
	}
	
	public String getPriorityLevel() {
		return priorityLevel;
	}
	
	public void setPriorityLevel(String priorityLevel) {
		this.priorityLevel = priorityLevel;
	}
	
	public void Save() {
		
		Triple triple;
		
		Map<Triple, String> saveData = new HashMap<Triple, String>();
		
		if(priorityLevel != null) {
			triple = new Triple(getURI(), "qc:priorityLevel", priorityLevel);
			saveData.put(triple, "String");
		}
		
		super.Save();
		SparqlEndPoint.insertTriples(saveData);
		
	}
	
	public static SkillJobReq getJobSkillRefObject(String URI) {
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
        SkillJobReq SkillJobReq = ParseResponseToJobSkillRef(properties);
        SkillJobReq.setURI(uri);
        return SkillJobReq;
	}
	
	public static SkillJobReq getJobSkillRefObjectBySkillID(String skillID) {
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
        SkillJobReq SkillJobReq = getJobSkillRefObject(SparqlEndPoint.ParseResponseToURI(SparqlJsonResults));
        
        return SkillJobReq;
	}
	
	private static SkillJobReq ParseResponseToJobSkillRef(String SparqlJsonResults) {
		InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);

//        System.out.println("============================================");
        
        SkillJobReq SkillJobReq = new SkillJobReq();

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
                    SkillJobReq.setLabel(label);
                    break;
                    
                case "comment":
                	String comment = object;  
                	SkillJobReq.setComment(comment);
                	break;
                    
                case "referesToSkill":
                	String URI = object;  
                	if(URI.contains("#"))
                		URI = URI.substring(URI.indexOf("#") + 1);
                	try {
                		Skill skill = Skill.getSkill(URI);
                		SkillJobReq.setSkill(skill);
                		SkillJobReq.setSkillName(skill.getLabel());
                		SkillJobReq.setSkillURI(skill.getURI());
                	}
                	catch(Exception e) {
                		e.printStackTrace();
                	}
                	
                	break;
                	
                case "skillLevel":
                	String skillLevel = object;  
                	SkillJobReq.setSkillLevel(skillLevel);
                	break;
                	
                case "priorityLevel":
                	String priorityLevel = object;
                	SkillJobReq.setPriorityLevel(priorityLevel);
                	break;
                	
                default:
                    break;
            }
        } 
        return SkillJobReq; 
	}
}
