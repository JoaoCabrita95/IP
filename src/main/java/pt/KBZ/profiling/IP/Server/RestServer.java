package IP.Server;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jena.fuseki.FusekiLogging;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import IP.Model.SparqlEndPoint;
import IP.config.Configuration;
import Utils.IP;

public class RestServer {
	
	private static Logger Log = Logger.getLogger(JobpostingService.class.getName());

	static {
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s");
	}
	
	public static final int PORT = 7777;
	public static final String SERVICE = "Matching";
	public static String SERVER_BASE_URI = "http://%s:%s/";
	
	public static void main(String[] args) throws Exception {
		
//		new Thread() {
//			public void run() {
//				try {
//					FusekiLogging.setLogging();
//					Dataset ds = DatasetFactory.create();
//					FusekiServer server = FusekiServer.create()
//					  .port(3030)
//					  .add("/QualiChain", ds, true)
//					  .build();
//					server.start();
//					Log.info("Fuseki Server Inicialized");
//				}
//				catch(Exception e) {
//					Log.info(e.getMessage());
//				}
//			}
//		}.start();

		Configuration cnf=new Configuration();
		if(args.length > 0) {
			if(args[0].equals("test"))
				cnf.LoadConfiguration(true);
			else if(args[0].equals("internal")) 
				SparqlEndPoint.REQUEST_PATH = "http://localhost:4343/QualiChain/";
			else
				cnf.LoadConfiguration(false);
		}
		else
			cnf.LoadConfiguration(false);
		
			
		Log.setLevel( Level.FINER );

		String ip = IP.hostAddress();
		String serverURI = String.format(SERVER_BASE_URI, ip, Configuration.port);
		
		ResourceConfig config = new ResourceConfig();

		config.register(CORSResponseFilter.class);
		config.register(new CVService()); 
		config.register(new JobpostingService()); 
		config.register(new MatchingService()); 
		config.register(new PersonService()); 
		config.register(new SkillService());
		config.register(new GeneralService());

		
		JdkHttpServerFactory.createHttpServer( URI.create(serverURI.replace(ip, "0.0.0.0")), config);
		Log.info(String.format("%s Server ready @ %s\n",  SERVICE, serverURI));
	}
	
}
