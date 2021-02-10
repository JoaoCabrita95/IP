//package IP;
//
//import IP.Model.*;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.io.StringBufferInputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.jena.query.DatasetAccessor;
//import org.apache.jena.query.DatasetAccessorFactory;
//import org.apache.jena.query.QueryExecution;
//import org.apache.jena.query.QueryExecutionFactory;
//import org.apache.jena.query.QuerySolution;
//import org.apache.jena.query.ResultSet;
//import org.apache.jena.query.ResultSetFactory;
//
//
//class RDFBeansTest {
//
//
//    public static void main(String[] args) throws Exception {
//    
//        //DatasetAccessor accessor =     DatasetAccessorFactory.createHTTP("localhost:3030"); 
//        //List<CV> cvs =  getCVs("http://localhost:3030/QualiChain");
//        //System.out.println(cvs.get(0).toString());
//
//        
//        List<CV> cvs = CV.getCVs();
//        System.out.println("------------");
//        System.out.println(cvs.get(0).toString());
//
//
//    }
///*
//    public static List<CV> getCVs(String service) {
//
//        List<CV> CVs = new ArrayList<>();  
//        StringBuilder query = new StringBuilder(MatchingQueries.getCVs);     
//        QueryExecution qe = QueryExecutionFactory.sparqlService(service, query.toString());  
//        ResultSet results = qe.execSelect();  
//        //ResultSetFormatter.outputAsJSON
//        
//        while (results.hasNext()) {
//            QuerySolution soln = results.nextSolution();
//
//            //for (Iterator i = soln.varNames(); i.hasNext(); ) 
//            //    System.out.println(i.next());
//            
//            System.out.println("soln: "+soln.toString());
//            CV cv = new CV();
//
//            String label = String.valueOf(soln.getResource("label"));   
//            cv.setLabel(label);
//            
//            String ID = String.valueOf(soln.getResource("subject"));
//            System.out.println(ID);   
//            //cv.setId(ID);   
//            cv.setId(StringUtils.substringAfter(ID,"http://rdfs.org/resume-rdf/cv.rdfs#"));   
//
//            CVs.add(cv);  
//        } 
//        qe.close();  
//        return CVs; 
//    }
//
//    /*
//    public List<JobPosting> getJobOffersThatMatchEmployee(CV employee, String service) {
//          List<JobPosting> offers = new ArrayList<>();  
//          StringBuilder query = new StringBuilder(MatchingQueries.matchEmployeeWithJobOffers);     
//        QueryExecution qe = QueryExecutionFactory.sparqlService(service, query.toString());  
//        ResultSet results = qe.execSelect();  
//        while (results.hasNext()) {   
//            JobPosting offer = new JobPosting();
//            QuerySolution soln = results.nextSolution();
//            String offerId = String.valueOf(soln.getResource("job"));   
//            offer.setId(StringUtils.substringAfter(offerId,"http://erecruitment.com/jobOffer/"));   
//            offers.add(offer);  
//        }  
//        qe.close();  
//        return offers; 
//    }
//    */
//}
