package IP.Model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class RDFObject {

	private String CLASSTYPE = null;
	private String PREFIX = null;
	private String uri = null;
	private String id = null;
	private String label = null;
	private String comment = null;
	
	/**
	 * RDFObject simple constructor with rdf type and prefix standard
	 * @param classType String representing the type of the RDFObject, format is "prefix:name" for example "cv:CV", "qc:Person"
	 * @param prefix String representing the prefix used for saving triples related to the RDFObject
	 */
	public RDFObject(String classType, String prefix) {
		CLASSTYPE = classType;
		PREFIX = prefix;
		autoGenerateIDURI();
	}
	
	/**
	 * RDFObject constructor that creates an object with classType, prefix, id, label and comment to define a simple RDFObject
	 * @param classType String representing the type of the RDFObject, format is "prefix:name" for example "cv:CV", "qc:Person"
	 * @param prefix String representing the prefix used for saving triples related to the RDFObject
	 * @param id Unique id to identify different RDFObjects
	 * @param label Name of the RDFObject
	 * @param comment Description of the RDFObject
	 */
	public RDFObject(String classType, String prefix, String id, String label, String comment) {
		CLASSTYPE = classType;
		PREFIX = prefix;
		if(id != null) {
			if(id.startsWith(PREFIX)) {
				uri = id;
				this.id = id.substring(1);
			}
			else
				uri = PREFIX + id;
		}
		else
			uri = PREFIX + id;
		setURI(uri);
		this.id = id;
		this.label = label;
		this.comment = comment;
	}
	
	/**
	 * Gets RDFObject URI
	 * @return RDFObject URI
	 */
	public String getURI() {
		return uri;
	}
	
	/**
	 * Gets RDFObject ID
	 * @return RDFObject ID
	 */
	public String getID() {
		return id;
	}
	
	/**
	 * Gets RDFObject label
	 * @return RDFObject label
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Gets RDFObject comment
	 * @return RDFObject comment
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * Sets the URI for the RDFObject
	 * @param URI String format of the URI to be set as the RDFObject URI
	 */
	public void setURI(String URI) {
		this.uri = URI;
		if(URI.contains("#"))
			this.uri = PREFIX + URI.substring(URI.indexOf("#")+1, URI.lastIndexOf(">"));
		if(URI.contains(PREFIX))
			id = URI.substring(URI.indexOf(":")+1);
		else if(URI.contains("#"))
			id = URI.substring(URI.indexOf("#")+1, URI.lastIndexOf(">"));
		else if(URI.contains("http"))
			id = URI.substring(URI.indexOf("http")); //TODO: temp, figure out a way to construct the ID out of this URI format
		
			
	}
	
	/**
	 * Sets the ID for the RDFObject
	 * @param ID String format of the ID to be set as the RDFObject ID
	 */
	public void setID(String ID) {
		this.id = ID;
	}
	
	/**
	 * Sets the label for the RDFObject
	 * @param label String format of the label to be set as the RDFObject label
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * 
	 * @param Description String format of the comment to be set as the RDFObject comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
	/**
	 * The method saves the data stored in the RDFObject to the server being accessed by the SparqlEndPoint class as RDF triples
	 * @throws Exception 
	 */
	public void rootRDFSave() {
		
		Map<Triple, String> saveData = new HashMap<Triple, String>();
		
		if(id == null || uri == null || (!uri.startsWith(PREFIX) && !uri.startsWith("<http")) || !uri.startsWith(PREFIX)) {
			autoGenerateIDURI();
		}
		
		if(!uri.startsWith(PREFIX)){
			uri = PREFIX + uri;
		}
		
		Triple triple = new Triple(uri, "rdf:type", CLASSTYPE);
		saveData.put(triple, "Object");
//        SparqlEndPoint.insertTriple(triple);
        
        if(label != null) {
        	triple = new Triple(uri, "rdfs:label", label);
        	saveData.put(triple, "String");
//            SparqlEndPoint.insertPropertyValue(triple);
        }
		

        if(comment != null) {
        	triple = new Triple(uri, "rdfs:comment", comment);
        	saveData.put(triple, "String");
//            SparqlEndPoint.insertTriple(triple.toPropertyValueString());   
		}
        
        SparqlEndPoint.insertTriples(saveData);
		
		
	}
	
	/**
	 * Deletes every propriety related to the URI, no return for confirmation of what was deleted
	 * Requires the URI to have the correct format, not guaranteed to work otherwise
	 * @param URI URI of the object meant to be deleted
	 */
	public static void quickDeleteByURI(String URI) {
		SparqlEndPoint.deleteObjectByUri(URI);
	}
	
	/**
	 * Deletes every RDF triple in storage that is related to given URI
	 * @param URI URI of the object to be deleted
	 */
	public static void deleteURIAssociations(String URI) {
		SparqlEndPoint.deleteObjectAssociations(URI);
	}
	
	/**
	 * Generates a URI and ID for a new RDF Object
	 */
	private void autoGenerateIDURI() {
		
		do {
			id ="id" + UUID.randomUUID().toString();
			uri = PREFIX + id;
		}
		while(SparqlEndPoint.existURI(uri));
	}
	
	/**
	 * Verifies if a label is already in Use in an RDF object
	 * @param label label usually related to a rdf:label property in an RDF triple that
	 * represents a name or description of an object
	 * @return True if the label exists in storage, False if it doesn't
	 */
    public static boolean exists(String label){
        String properties = SparqlEndPoint.getInstancesByLabel(label);
        String uri = SparqlEndPoint.ParseResponseToURI(properties);
        if (uri !=null)
            return true;
        else
            return false;
    }

    /**
     * Takes the input URI and returns the fraction of the URI without the Ontology associated
     * Usually represents the ID of the Object referenced by the URI
     * @param uri URI of an RDF Object
     * @return ID of the Object given by the URI
     */
    public static String uri2id(String uri){
        
        String id = uri;
        if(id.contains(":")) {
            id = id.split(":")[1];
        }
        return id;
    }
	
	public static String getURIFromID(String id, String prefix) {
    	String uri = id;
        if (!uri.startsWith(prefix) && !uri.startsWith("<http")){
        	if(uri.startsWith("http"))
        		uri = "<" + uri + ">";
        	else
        		uri = prefix+id;
        }
        return uri;
    }

}
