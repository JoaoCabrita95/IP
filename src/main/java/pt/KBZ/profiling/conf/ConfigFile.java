package conf;

public class ConfigFile {

	public static final String dataPath = "DataSet/";
	public static final String ontologiesPath = "ontologies/";
	public static final String ontologyName = "EducationOntology";
	public static final String defaultExtention = ".ttl";
	public static final String defaultFormat = "TURTLE";
	public static final String defaultPrefix ="http://mccarthy.dia.fi.upm.es/" + ontologyName+ "#";
	public static final String defaultRelationPrefix = "http://www.w3.org/2000/01/rdf-schema#";
	
	public static final String word2vecServerURL ="http://localhost:5000/word2vec";
}
