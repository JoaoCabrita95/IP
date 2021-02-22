package IP.Model;

//import org.apache.jena.graph.NodeFactory;
//import org.apache.jena.graph.Triple;

public class Triple {

    private String subject;
    private String predicate; 
    private String object;

    /**
     * Creates a Triple object with the parameters subject, predicate and object as it's values.
     * @param subject
     * @param predicate
     * @param object
     */
    public Triple(String subject, String predicate, String object){
        /*
        Triple triple = new Triple(
			NodeFactory.createURI(subject), 
			NodeFactory.createURI(predicate), 
			NodeFactory.createURI(object)
			);
        */
        this.object = object;
        this.subject = subject;
        this.predicate = predicate;
    }

    /**
     * Returns a string representation of the Triple for non-Literal object values in RDF format
     * @return Triple in String form for non-Literal object values
     */
    public String toString(){
        return spaceRemoval(this.subject) + " " + spaceRemoval(this.predicate) + " " + spaceRemoval(this.object) + " .\n"; 
    }
    
    /**
     * Returns a string representation of the Triple for Literal object values in RDF format
     * @return Triple in String form for Literal object values
     */
    public String toPropertyValueString(){
        return spaceRemoval(this.subject) + " " + spaceRemoval(this.predicate) + " \"" + this.object + "\" .\n"; 
    }

    /**
     * Returns a string that has no value with a space,
     * if there is a space found in the original String
     * it replaces it with an underscore ( _ ).
     * @param target The String that will be replaced by the same String with spaces replaced by underscores
     * @return String with replaced space values
     */
    private String spaceRemoval(String target) {
		return target.trim().replaceAll(" ", "_");
	}

}