package IP.Model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import IP.Client.HTTPrequest;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.rdfconnection.RDFConnectionRemote;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
//import Model.RDFClass;

import org.apache.jena.sparql.graph.GraphFactory;

/*
* The SPARQL Protocol describes a means for conveying SPARQL queries and updates
* to a SPARQL processing service and returning the results via HTTP.
* Ref: https://www.w3.org/TR/sparql11-protocol
*/

public class SparqlEndPoint {
	
	public static String REQUEST_PATH;
	
	private static final String QUERYHEADER = "prefix : <http://localhost:3030/QualiChain/#> \r\n" +
			"prefix vcard: <http://www.w3.org/2006/vcard/ns#> \r\n" +
			"prefix owl: <http://www.w3.org/2002/07/owl#> \r\n" + 
			"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \r\n" + 
			"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \r\n" + 
			"prefix dc: <http://purl.org/dc/elements/1.1/> \r\n" + 
			"prefix saro: <http://w3id.org/saro#> \r\n" + 
			"prefix badge: <https://w3id.org/openbadges/v2#> \r\n" + 
			"prefix skos: <http://www.w3.org/2004/02/skos/core#> \r\n" + 
			"prefix ethon: <http://ethon.consensys.net/> \r\n" + 
			"prefix qc: <http://w3id.org/qualichain#> \r\n" + 
			"prefix schema: <http://schema.org/> \r\n" + 
			"prefix xml: <http://www.w3.org/XML/1998/namespace> \r\n" + 
			"prefix xsd: <http://www.w3.org/2001/XMLSchema#> \r\n" + 
			"prefix cv: <http://rdfs.org/resume-rdf/cv.rdfs#> \r\n" + 
			"prefix foaf: <http://xmlns.com/foaf/spec/> \r\n " + 
			"prefix esco: <http://data.europa.eu/esco/model#> \r\n ";
	
	
	
	
	//Base Protocol operations/////////////////////////////////
	
	//Query method of SPARQL protocol
	//The query operation is used to send a SPARQL query to a service and receive the results of the query
	public static String query(String query) {
//		System.out.println("Query:");
//		System.out.println(query);
		return sendQuery("query", query);
	}
	
	//Update method of SPARQL protocol
	//The update operation is used to send a SPARQL update request to a service
	//SPARQL/Update provides the following facilities:
		//Insert new triples to an RDF graph.
		//Delete triples from an RDF graph.
		//Perform a group of update operations as a single action.
		//Create a new RDF Graph to a Graph Store.
		//Delete an RDF graph from a Graph Store.
	public static String update(String query) {
//		System.out.println("Update Query");
//		System.out.println(query);
		return sendQuery("update", query);

		//HTTPrequest request = new HTTPrequest(REQUEST_PATH + "update");		
		
		//request.addRequestProperty("Content-Type", "application/sparql-update; charset=UTF-8");
		//request.addRequestProperty("Accept", "application/sparql-results+json, */*; q=0.9");
		
		//request.POSTrequest(query);
		
		//String response = request.Response();
		//request.close();
		//return response;
	}
	
	//Triple CRUD queries////////////////////////////////////
	public static String insertTriple(String triple, String header) {
		String query = header + " INSERT DATA { " + triple + "\n }";
		return update(query);
	}

	public static String insertTriple(String triple) {
		String query = QUERYHEADER + " INSERT DATA { " + triple + "\n }";
		return update(query);
	}

	public static String insertTriple(Triple t) {
		String query = QUERYHEADER + " INSERT DATA { " + t.toString() + " }";
//		System.out.println(t.toString());
		return update(query);
	}
	
	public static String insertTriples(Map<Triple, String> saveData) {
		String query = QUERYHEADER + " INSERT DATA { " ;
		for (Map.Entry<Triple, String> entry : saveData.entrySet()) {
            if(entry.getValue().contentEquals("String"))
            	query += " " + entry.getKey().toPropertyValueString() + " ";
            else
            	query += " " + entry.getKey().toString() + " ";
		}
		query += " }";
		
		String results = update(query);
		if(results.equals("IO ERROR"))
			System.out.println(query.toString());
		return results;
	}
	
	public static String deleteTriple(String triple, String filter) {
		String query = QUERYHEADER + " DELETE { " + triple + "\n } WHERE {" + filter + "} ";
		return update(query);
	}
	
	public static String insertPropertyValue(Triple triple) {
		String query = QUERYHEADER + " INSERT DATA { " + triple.toPropertyValueString() + "\n }";
//		System.out.println(triple.toPropertyValueString());
		return update(query);
	}
	
	public static String insertTTLFile(String path) {
				
		try (RDFConnection conn = RDFConnectionFactory.connect(REQUEST_PATH)) {
//		    conn.put(path);
		    conn.load(path);
		    conn.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return path;
	}

	public static boolean existURI(String URI) {
		
		if(URI == null)
			return false;
//		System.out.println(URI);
		String query = QUERYHEADER  
		+ " SELECT ?subject ?predicate ?object "
		+ "WHERE {"
		+ "	" + URI + " ?predicate ?object" 
		+ " } LIMIT 1";

//		if((ParseResponseToURI(query(query)) == null))
//			return false;
//		else
//			return true;
		String serverResponse = query(query);
		InputStream in = new ByteArrayInputStream(serverResponse.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);
        if(results != null && results.hasNext()) {
            return true;
        }	
        else
        	return false;
	}
	
	public static String getAllPropertiesByType(String type) {
		String query = QUERYHEADER 
				+ " SELECT ?subject ?predicate ?object"
				+ " WHERE {"
				+ "	?subject " + type + " ?object ."  
				+ " } ";
		
		return query(query);
	}

	// Find instances of a class
	public static String getInstances(String ClassType) {
	
		String query = QUERYHEADER  
		+ " SELECT ?subject ?predicate ?object"
		+ " WHERE {"
		+ "	?subject rdf:type " + ClassType +" ."  
		+ " } ";

		return query(query);
	}

	// Find instances by class with a label
	public static String getInstancesByLabel(String Label) {
	
		String query = QUERYHEADER  
		+ " SELECT ?subject ?predicate ?object "
		+ "WHERE {"
		+ " ?subject rdfs:label \"" +Label+ "\"  " 
		+ " } LIMIT 1";

		return query(query);
	}


	// Find instances by class with a label
	public static String getInstancesByLabel(String ClassType, String Label) {
	
		String query = QUERYHEADER  
		+ " SELECT ?subject ?predicate ?object "
		+ "WHERE {"
		+ "	?subject a " + ClassType +" ."  
		+ " ?subject rdfs:label \"" +Label+ "\"  " 
		+ " } LIMIT 1";

		return query(query);
	}
	
	public static String getInstancesByPartialLabel(String ClassType, String Label) {
		String query = QUERYHEADER  
		+ " SELECT ?subject ?predicate ?object "
		+ "WHERE {"
		+ "	?subject a " + ClassType +" ."  
		+ " ?subject rdfs:label ?label  " 
		+ " filter contains(?label,\"" +Label+ "\" )"
		+ " } ";

		return query(query);
	}
	
	// Find instances by class that have a property
	public static String getInstancesByProperty(String ClassType, String property, String value) {

		String query = QUERYHEADER  
		+ " SELECT ?subject "
		+ "WHERE {"
		+ "	?subject a " + ClassType +" ."  
		+ " ?subject "+ property +" " +value  
		+ " } LIMIT 100";

		return query(query);
	}
	
	public static String getInstancesByPropertyPartialValue(String ClassType, String property, String value) {
		
		String query = QUERYHEADER  
		+ " SELECT ?subject "
		+ "WHERE {"
		+ "	?subject a " + ClassType +" ."  
		+ " ?subject "+ property +" ?value "
		+ " filter contains (?value, \"" + value + "\" ) "
		+ " } LIMIT 100";

		return query(query);
	}
	
	//gets all the properties of an entity by URI
	public static String getAllProperties(String SubjectURI) {
		
		String query = QUERYHEADER  
		+ " SELECT ?subject ?predicate ?object "
		+ "WHERE {"
		+ "	" + SubjectURI + " ?predicate ?object ." 
		+ " } ";

		return query(query);
	}

		// Find all properties of an instance
		public static String getProperties(String uri) {

			String query = QUERYHEADER  
			+ " SELECT distinct ?subject ?property ?value"
			+ " WHERE { "
			+ uri + " ?property ?value  "  
			+ " filter ( ?property not in ( rdf:type ) ) "
			+ " } ";
	
			return query(query);
		}
		
		// Find all properties of an instance
		public static String getProperties(String uri, String label) {
	
			String query = QUERYHEADER  
			+ " select ?property ?value "
			+ " WHERE { "
			+ " ?class rdfs:label \"  " + label+ " \" ; "
			+ " rdfs:subClassOf [ owl:hasValue ?value ; "  
			+ " owl:onProperty [ rdfs:label ?property ] ] . "
			+ " } ";
	
			return query(query);
		}
		
		public static String getObjectByUriProperty(String URI, String property) {
			String query = QUERYHEADER  
					+ " SELECT ?subject ?predicate ?object "
					+ "WHERE {"
					+ "	" + URI + " " + property + " ?object ." 
					+ " } ";
			return query(query);
		}
		
		public static String deleteObjectByUri(String URI) {
//			System.out.println(URI);
			String query = QUERYHEADER
					+ " DELETE "
					+ " WHERE {"
					+ " " + URI  + "?predicate ?object ."
					+ "}";
			return update(query);
		}
		
		public static String deleteObjectAssociations(String URI) {
			String query = QUERYHEADER
					+ " DELETE "
					+ " WHERE {"
					+ " ?subject ?predicate " + URI + " ."
					+ "}";
			return update(query);
		}
		
	///CRUD from RDF file???//////////////////////////////////////////
	/*
	public static String insert(String path) {
		try {
			String fileContent = Files.readString(new File(path).toPath());
			//In case of file containing the prefixes
			if(fileContent.startsWith("@")) {
				int startingIndex = fileContent.indexOf(".", fileContent.indexOf(">", fileContent.lastIndexOf("@prefix")));
				fileContent = fileContent.substring(startingIndex+1).trim();
			}
			
			String finalQuery = QUERYHEADER + " INSERT DATA { " + fileContent + "\n }";
			return update(finalQuery);
		} catch (IOException e) {
			System.err.println("Err");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String delete(String path) {
		try {
			String fileContent = Files.readString(new File(path).toPath());
			//In case of file containing the prefixes
			if(fileContent.startsWith("@")) {
				int startingIndex = fileContent.indexOf(".", fileContent.indexOf(">", fileContent.lastIndexOf("@prefix")));
				fileContent = fileContent.substring(startingIndex+1).trim();
			}
			
			String finalQuery = QUERYHEADER + " DELETE DATA { " + fileContent + "\n }";
			return update(finalQuery);
		} catch (IOException e) {
			System.err.println("Err");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String select(String path) {
		try {
			String fileContent = Files.readString(new File(path).toPath());
			//In case of file containing the prefixes
			if(fileContent.startsWith("@")) {
				int startingIndex = fileContent.indexOf(".", fileContent.indexOf(">", fileContent.lastIndexOf("@prefix")));
				fileContent = fileContent.substring(startingIndex+1).trim();
			}
			String finalQuery = QUERYHEADER + " SELECT * WHERE { " + fileContent + "\n }";
			return query(finalQuery);
		} catch (IOException e) {
			System.err.println("Err");
			e.printStackTrace();
		}
		return null;
	}
	
	*/
	//Recursive loading sub-classes////////////////////////
	//ref:https://tutorial-academy.com/ontology-traversal-jena-sparql/
	private static List<String> getRoots()
	{
		
		// find all owl:Class entities and filter these which do not have a parent
		String getRootsQuery = 
				  "SELECT DISTINCT ?s WHERE " 
				+ "{"
				+ "  ?s <http://www.w3.org/2000/01/rdf-schema#subClassOf> <http://www.w3.org/2002/07/owl#Thing> . " 
				+ "  FILTER ( ?s != <http://www.w3.org/2002/07/owl#Thing> && ?s != <http://www.w3.org/2002/07/owl#Nothing> ) . " 
				+ "  OPTIONAL { ?s <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?super . " 
				+ "  FILTER ( ?super != <http://www.w3.org/2002/07/owl#Thing> && ?super != ?s ) } . " 
				+ "}";
		
		String roots = query(getRootsQuery);
		return parseResults(roots);
	}
	/*
	public static void traverse( OntModel model, String entity, List<String> occurs, int depth )
	{
		if( entity == null ) return;
		
		String queryString 	= "SELECT ?s WHERE { "
						   	+ "?s <http://www.w3.org/2000/01/rdf-schema#subClassOf> <" + entity + "> . }" ;
		
		Query query = QueryFactory.create( queryString  );
		
		if ( !occurs.contains( entity ) ) 
		{
			// print depth times "\t" to retrieve an explorer tree like output
			for( int i = 0; i < depth; i++ ) { System.out.print("\t"); }
			// print out the URI
			System.out.println( entity );
			
			try ( QueryExecution qexec = QueryExecutionFactory.create( query, model ) ) 
			{
				ResultSet results = qexec.execSelect();
				while( results.hasNext() )
				{
					QuerySolution soln = results.nextSolution();
					RDFNode sub = soln.get("s"); 
					
					if( !sub.isURIResource() ) continue;
					
					String str = sub.toString();
					
	                // push this expression on the occurs list before we recurse to avoid loops
	                occurs.add( entity );
	                // traverse down and increase depth (used for logging tabs)
					traverse( model, str, occurs, depth + 1 );
	                // after traversing the path, remove from occurs list
	                occurs.remove( entity );
				}
			}
		}
		
	}
	public static void traverseStart(String entity)
	{
		// if starting class available
		if( entity != null ) 
		{
			traverse( entity,  new ArrayList<String>(), 0  );
		}
		// get roots and traverse each root
		else
		{
			List<String> roots = getRoots( model );
		
			for( int i = 0; i < roots.size(); i++ )
			{
				traverse( model, roots.get( i ), new ArrayList<String>(), 0 );
			}
		}
	}
	*/
	//////////////////////////////////////////////////////
	public static List<String> getEntities(String concept, String relation){

		String entitiesQuery=
		 "SELECT DISTINCT ?entity "
		+"WHERE {"
		+	"?subclass rdfs:subClassOf "+ concept+" ."
		+	"?entity rdf:type ?subclass ."
		+"}";

		String entities = query(entitiesQuery);
//		System.out.println(entities);
		return parseResults(entities);
	}
	//////////////////////////////////////////////////////
	//Send SPARQL query through HTTP GET request to the Sparql end-point
	private static String sendQuery(String type, String query) {
//		System.out.println("path:"+REQUEST_PATH);
		HTTPrequest request = new HTTPrequest(REQUEST_PATH + type);		
		
		request.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		request.addRequestProperty("Accept", "application/sparql-results+json, */*; q=0.9");
		
		request.addParam(type, query);

		
		//request.addURLParam("query="+query);
		String response = request.Response();
		request.close();
//		System.out.println("----------------------------------");
//		System.out.println(response);
//		System.out.println("----------------------------------");
		return response;
	}
	
	//parse the response results of RDF triples
	private static List<String> parseResults(String results) {
//		System.out.println(results);
		
		String output = results.substring(results.indexOf("[", results.indexOf("bindings")), results.lastIndexOf("]"));
		String [] splitResults = output.split("}");
		
		List<String> parsedOutput = new LinkedList<String>();
				
		for(String e: splitResults) {
			if(e.length() > 2) {
				String f = e.substring(e.indexOf(":", e.indexOf("value")));
//				System.out.println(f.substring(f.indexOf("\"")+1, f.lastIndexOf("\"")));
				parsedOutput.add(f.substring(f.indexOf("\"")+1, f.lastIndexOf("\"")));
			}
		}
		return parsedOutput;
	}

    
    public static String ParseResponseToURI(String SparqlJsonResults) {
    	
        InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);

        String uri = null;
//        System.out.println("============================================");
        if(!results.hasNext())
        	return uri;
        if (results.hasNext()) {

            QuerySolution soln = results.nextSolution();
//            System.out.println("soln: "+soln.toString());
            uri = String.valueOf(soln.getResource("subject")); 
            if(uri.contains("#"))
        		uri = uri.substring(uri.indexOf("#") + 1);
            if(!uri.contains(":"))
            	uri = ":" + uri;
        }
        if(uri == null)
        	return "Couldn't parse URI but it exists";
        
        return uri;
    }

	//Gets json results from a sparql endpoint and returns a jena model
	public static Model ParseResponseToModel(String SparqlJsonResults) {

        InputStream in = new ByteArrayInputStream(SparqlJsonResults.getBytes(StandardCharsets.UTF_8));
        ResultSet results = ResultSetFactory.fromJSON(in);

        Model model = org.apache.jena.sparql.resultset.RDFOutput.encodeAsModel(results);

		//RDFClass.printModel(model);
		return model;
		/*
		
        // org.apache.jena.rdf.model.NodeIterator iterator = model.listObjects();

        StmtIterator iterator = model.listStatements();
        while (iterator.hasNext()) {
            // RDFNode node = iterator.next();
            // System.out.println(node.toString());

            Statement stmt = iterator.nextStatement(); // get next statement
            Resource subject = stmt.getSubject(); // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject(); // get the object
			System.out.println("00000000000000000000000000000");
			RDFClass.printNameOrURI(subject.toString() + " ");
			RDFClass.printNameOrURI(predicate.toString()+ " ");
			if (object instanceof Resource) {
				RDFClass.printNameOrURI(object.toString());
			} else {
				// object is a literal
				RDFClass.printNameOrURI(" \"" + object.toString() + "\"");
			}

			System.out.println(" .");
			System.out.println();

		}
		*/
	}
	
	public static List<Resource> modelToResource(Model model) {
		List<Statement> statements = model.listStatements().toList();
		List<Resource> result = new LinkedList<Resource>();
		for(Statement statement: statements) {
				result.add(statement.getSubject());
				result.add(statement.getPredicate());
				result.add((Resource) statement.getObject()); // can be a String
		}
		
		return result;			
	}

}