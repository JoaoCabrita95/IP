package IP.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import IP.Model.SparqlEndPoint;


public class Configuration {

    public static int port = 8000;
    public static String dataPath = "DataSet/";
	public static String ontologiesPath = "ontologies/";
	public static String ontologyName = "EducationOntology";
	public static String defaultExtention = ".ttl";
	public static String defaultFormat = "TURTLE";
	public static String defaultPrefix ="http://mccarthy.dia.fi.upm.es/" + ontologyName+ "#";
	public static String defaultRelationPrefix = "http://www.w3.org/2000/01/rdf-schema#";
	
    public Configuration() {
    
    }

    public void LoadConfiguration(boolean test) {

        Properties prop = new Properties();
        try {
            InputStream is = this.getClass().getResourceAsStream("/app.config");
            prop.load(is);

            port = Integer.parseInt(prop.getProperty("port","8000"));
            if(!test) {
            	SparqlEndPoint.REQUEST_PATH = prop.getProperty("sparqlendpoint");
                SparqlEndPoint.QUERY_PATH = prop.getProperty("queryengine");
            }
            else {
            	SparqlEndPoint.REQUEST_PATH = prop.getProperty("sparqlendpointtest");
                SparqlEndPoint.QUERY_PATH = prop.getProperty("queryenginetest");
            }
            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}