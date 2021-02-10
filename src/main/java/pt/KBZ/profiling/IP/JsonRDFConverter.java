package IP;

import IP.Model.Triple;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.Model;

public class JsonRDFConverter {

	private String jsonInput;
	private String RDFOutput;
	private Model curModel;
	
	/**
	 * JsonRDFConverter uses the json parameter to create a Model to have available for parsing
	 * @param json String with Json format containing RDF Triples 
	 */
	public JsonRDFConverter(String json) {
		curModel = createModel(json);
		jsonInput = json;
		RDFOutput = "";
	}
	
	/**
	 * Uses the parameter json to create a Model with the data it contains
	 * @param json String with Json format containing RDF Triples
	 * @return A Model containing the data contents of the json parameter
	 */
	public Model createModel(String json) {
		
		InputStream in = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);
        
        curModel = results.getResourceModel();
		
		return curModel;
	}
	
	/**
	 * Recovers a Json file and places its data in the String jsonInput for later parsing
	 * @param path Path in the file system of where the Json file is located
	 * @return String representation of the data in the Json file located in the parameter path
	 */
	public String getJsonFromFile(String path) {
		return null;
	}
	
	/**
	 * Get RDF triples contained in parameter json
	 * @param json Json formated String containing Triples to be transformed into RDF format in TTL convention
	 * @return String representation of RDF triples in the TTL convention
	 */
	public String getRDFFromJson(String json) {
		
		
		InputStream in = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);
        
        
        while (results.hasNext()) {
        	
            QuerySolution soln = results.nextSolution();
            
            RDFOutput += getTripleFromSegment(soln);
                    
        }
		
		return RDFOutput;
	}
	
	/**
	 * Returns a String with RDF triples derived from the jsonInput instance variable
	 * @return RDF Triple String derived from the jsonInput instance variable
	 */
	public String getRDFFromJson() {
		return getRDFFromJson(jsonInput);
	}
	
	/**
	 * Returns a String with RDF triples derived from parameter model
	 * @param model A Model containing RDF triple data
	 * @return String representation of RDF triples derived from the parameter model
	 */
	public String getRDFFromModel(Model model) {
		return null;
	}
	
	/**
	 * Returns a String with RDF triples derived from the instance variable curModel
	 * @return String representation of RDF triples derived from instance variable curModel
	 */
	public String getRDFFromModel() {
		return getRDFFromModel(curModel);
	}
	
	/**
	 * Returns a String containing one or multiple triples that are derived from the parameter soln 
	 * @param soln QuerySolution object containing triples in Json query format
	 * @return String containing one or multiple triples in RDF format
	 */
	//Get triple/s from a segment given from the json that is being worked on, to be used in getRDF classes
	private String getTripleFromSegment(QuerySolution soln) {
		
        Triple triple = null;
		String subject = null;
		String predicate = null;
		String object = null;
		String type = null;
		String output = "";
		

        //Since the Json probably won't be received with a uri, we can generate one semi-randomly
        subject = URIGenerator("saro:JobPosting");

		//if the Json is in SPARQL RDF format
		boolean formated = false;
		
		if(formated) {
        	//It will have to be done differently since I will only receive the subject and the objects related to it and derive the properties from them
            subject = String.valueOf(soln.getResource("?subject"));
            predicate = String.valueOf(soln.getResource("?predicate"));
            if(soln.get("?object").isLiteral())
            	object = String.valueOf(soln.getLiteral("?object"));
            else
            	object = String.valueOf(soln.getResource("?object"));
            
            triple = new Triple(subject, predicate, object);
            output += triple.toString();	
        }
        
        else {
        	if(soln.get("?object").isLiteral()) {
        		object = String.valueOf(soln.getLiteral("?object"));
        		type = String.valueOf(soln.getResource("?predicate"));
        		
        		switch(type) {
        		case "Occupation" : 
        			predicate = "";
        			break;
        		case "ContractType" :
        			predicate = "schema:employmentType";
        			break;
        		case "seniorityLevel" : 
        			predicate = "rdfs:comment";
        			break;
        		case "Skill" : 
        			predicate = "saro:needsSkill";
        			break;
        		case "Description" :
        			predicate = "saro:describes";
        			break;
        		case "SkillLevel" :
        			predicate = "saro:atProficiencyLevel";
        			subject = String.valueOf(soln.getResource("?subject"));
        			break;
        		case "SkillPriority" :
        			predicate = "qc:hasPriority";
        			subject = String.valueOf(soln.getResource("?subject"));
        			break;
        		default:
        			break;
        		}
        		System.out.println(subject);
        		System.out.println(predicate);
        		System.out.println(object);
        		triple = new Triple(subject, predicate, object);
        		output += triple.toString();	
        	}
        	else {
        		
        	}
        }

		return output;
	}
	
	/**
	 * Random String generator for Strings with 10 characters with numerals and letters and a prefix from parameter type
	 * @param type Prefix for the URI String
	 * @return A String representing a URI with a prefix and an ID
	 */
	private String URIGenerator(String type) {
		String URI = type;
		
		int leftLimit = 48; // numeral '0'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();
	 
	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();
	    
	    URI += "_" + generatedString;
		
		return URI;
	}
}
