package IP.RML;

import be.ugent.rml.Executor;
import be.ugent.rml.Utils;
import be.ugent.rml.functions.FunctionLoader;
import be.ugent.rml.functions.lib.IDLabFunctions;
import be.ugent.rml.records.RecordsFactory;
import be.ugent.rml.store.QuadStore;
import be.ugent.rml.store.QuadStoreFactory;
import be.ugent.rml.store.RDF4JStore;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class Mapping {

  public static void main(String[] args)  throws Exception{
      Mapping mapping = new Mapping();
      System.out.println(mapping.map("RMLMapperCV.ttl", "turtle"));
    }

    public String map(String mappingFilename, String TargetType){
        try {
            String mapPath = mappingFilename; //path to the mapping file that needs to be executed
            File mappingFile = new File(mapPath);

            // Get the mapping string stream
            InputStream mappingStream = new FileInputStream(mappingFile);

            // Load the mapping in a QuadStore
            QuadStore rmlStore = QuadStoreFactory.read(mappingStream);

            // Set up the basepath for the records factory, i.e., the basepath for the (local file) data sources
            RecordsFactory factory = new RecordsFactory("./"/*mappingFile.getParent()*/);

            // Set up the functions used during the mapping
            @SuppressWarnings("rawtypes")
			Map<String, Class> libraryMap = new HashMap<>();
            libraryMap.put("IDLabFunctions", IDLabFunctions.class);

            FunctionLoader functionLoader = new FunctionLoader(null, libraryMap);

            // Set up the outputstore (needed when you want to output something else than nquads
            QuadStore outputStore = new RDF4JStore();

            // Create the Executor
            Executor executor = new Executor(rmlStore, factory, functionLoader, outputStore, Utils.getBaseDirectiveTurtle(mappingStream));

            // Execute the mapping
            QuadStore result = executor.execute(null);

            // Output the result
            //BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
            StringWriter out = new StringWriter();
            result.write(out, TargetType);
            return (out.toString());
            //out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return (e.getMessage());
            //fail("No exception was expected.");
        }
    }
}