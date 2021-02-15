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

public class CVSkillRef extends SkillRefObject {

	private String evalDate;
	private String acquiredDate;
	private String skillLastUsedDate;
	private String progress;
	
	public CVSkillRef() {
		super();
	}
	
	public CVSkillRef(Skill skill, String evalDate, String acquiredDate, String skillLevel, String skillLastUsedDate) {
		super(skill, skillLevel);
		this.evalDate = evalDate;
		this.acquiredDate = acquiredDate;
		this.skillLastUsedDate = skillLastUsedDate;
		progress = "0";
	}
	
	public String getEvalDate() {
		return evalDate;
	}
	
	public void setEvalDate(String evalDate) {
		this.evalDate = evalDate;
	}
	
	public String getAcquiredDate() {
		return acquiredDate;
	}
	
	public void setAcquiredDate(String acquiredDate) {
		this.acquiredDate = acquiredDate;
	}
	
	public String getSkillLastUsedDate() {
		return skillLastUsedDate;
	}
	
	public void setSkillLastUsedDate(String skillLastUsedDate) {
		this.skillLastUsedDate = skillLastUsedDate;
	}
	
	public String getProgress() {
		return progress;
	}
	
	public void setProgress(String progress) {
		this.progress = progress;
	}
	
	public void Save() {
		
		super.Save();
		
		Triple triple;
		
		Map<Triple, String> saveData = new HashMap<Triple, String>();
		
		if(evalDate != null) {
			triple = new Triple(getURI(), "qc:hasEvaluationDate", evalDate);
	        saveData.put(triple, "String");
		}
		
		if(acquiredDate != null) {
			triple = new Triple(getURI(), "qc:acquiredOn", acquiredDate);
	        saveData.put(triple, "String");
		}
		
		if(skillLastUsedDate != null) {
			triple = new Triple(getURI(), "qc:skillLastUsedDate", skillLastUsedDate);
	        saveData.put(triple, "String");
		}
		
		if(progress != null) {
			triple = new Triple(getURI(), "qc:skillProgress", progress);
	        saveData.put(triple, "String");
		}
		
		super.Save();
		SparqlEndPoint.insertTriples(saveData);
	}
	
	public static CVSkillRef getCVSkillRefObject(String URI) {
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
        CVSkillRef CVSkillRef = ParseResponseToCVSkillRef(properties);
        CVSkillRef.setURI(uri);
        return CVSkillRef;
	}
	
	public static CVSkillRef getCVSkillRefObjectBySkillID(String skillID) {
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
        CVSkillRef CVSkillRef = getCVSkillRefObject(SparqlEndPoint.ParseResponseToURI(SparqlJsonResults));
        
        return CVSkillRef;
	}
	
	private static CVSkillRef ParseResponseToCVSkillRef(String SparqlJsonResults) {
		InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);

//        System.out.println("============================================");
        
        CVSkillRef CVskillRef = new CVSkillRef();

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
                    CVskillRef.setLabel(label);
                    break;
                    
                case "comment":
                	String comment = object;  
                	CVskillRef.setComment(comment);
                	break;
                    
                case "referesToSkill":
                	String URI = object;  
                	if(URI.contains("#"))
                		URI = URI.substring(URI.indexOf("#") + 1);
                	try {
                		Skill skill = Skill.getSkill(URI);
                		CVskillRef.setSkill(skill);
                		CVskillRef.setSkillName(skill.getLabel());
                		CVskillRef.setSkillURI(skill.getURI());
                	}
                	catch(Exception e) {
                		e.printStackTrace();
                	}
                	
                	break;
            
                case "hasEvaluationDate":
                	String evalDate = object;  
                	CVskillRef.setEvalDate(evalDate);
                	break;
                	
                case "acquiredOn":
                	String adquiredDate = object;  
                	CVskillRef.setAcquiredDate(adquiredDate);
                	break;
                	
                case "skillLevel":
                	String skillLevel = object;  
                	CVskillRef.setSkillLevel(skillLevel);
                	break;
                	
                case "skillLastUsedDate":
                	String lastUsedDate = object;
                	CVskillRef.setSkillLastUsedDate(lastUsedDate);
                	break;
                	
                case "skillProgress":
                	String progress = object;
                	CVskillRef.setProgress(progress);
                	
                default:
                    break;
            }
        } 
        return CVskillRef; 
	}
}
