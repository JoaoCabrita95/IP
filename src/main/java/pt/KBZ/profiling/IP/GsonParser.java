package IP;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.util.UUID;

import com.google.gson.Gson;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import IP.Model.*;
import IP.RML.*;

public class GsonParser {

    Gson g;
    String jobpostURI = "https://w3id.org/saro";
    String cvURI = "http://rdfs.org/resume-rdf/cv.rdfs";
    String QualiChainURI = "http://w3id.org/qualichain";

    private Model model;

    public static void main(String[] args){
        Mapping mapping = new Mapping();
        String triples = mapping.map("RMLMapperCV.ttl", "turtle");

        Model model = ModelFactory.createDefaultModel();
        model.read(new ByteArrayInputStream(triples.getBytes()), null, "turtle");

        StringWriter out = new StringWriter();
        model.write(out, "turtle");
        System.out.println(out.toString()); 
    }
    public GsonParser() {
        g = new Gson();
        this.model = ModelFactory.createDefaultModel();
        // model.read("D:/Google Drive/KnowledgeBiz/Qualichain/Ontology
        // Development/qualichain.ttl") ;

        this.model.setNsPrefix("saro", jobpostURI);
        this.model.setNsPrefix("cv", cvURI);
        this.model.setNsPrefix("qc", QualiChainURI);

    }

    public Application toApplication(String data) {
        Application p = g.fromJson(data, Application.class);
        return p;
    }

    public JobPosting toJobPosting(String data) {
    	
        JobPosting p = g.fromJson(data, JobPosting.class);
        
//        System.out.println("Request Parsed");
//        String uri = "job-" + UUID.randomUUID();
//        //ModelData modelD = new ModelData();
//        //modelD.addPropertyToResource(modelD, resURI, propURI, prop_value);
////        p.setURI(uri);
//
//        Resource jobpost = model.createResource(uri);
//
//        jobpost.addProperty(RDF.type, model.createProperty(jobpostURI + "JobPosting"));
//        // jobpost.addProperty(model.createProperty("rdfs:label"),p.getLabel());
//        if (p.getLabel()!=null) jobpost.addProperty(RDFS.label, p.getLabel());
//        if (p.getJobDescription()!=null) jobpost.addProperty(model.createProperty("rdfs:comment"), p.getJobDescription());
//        if (p.getJobDescription()!=null) jobpost.addProperty(model.createProperty(jobpostURI+"describes"), p.getJobDescription());
//        
////        for (Skill skill : p.getSkillReq()) {
////            if (skill.getLabel()!=null){
////                jobpost.addProperty(model.createProperty("saro:requiresSkill"),skill.getLabel());
////                jobpost.addProperty(model.createProperty(QualiChainURI + "requiresExperience"), skill.getLabel());
////            }
////        }
//        
//        System.out.println("Jobposting created:"+uri);
        return p;

    }

    public CV toCV(String data) {
        
        CV p = g.fromJson(data, CV.class);

        //ModelData modelD = new ModelData();
        //modelD.addPropertyToResource(modelD, resURI, propURI, prop_value);

//        Resource cv = model.createResource(cvURI + p.getLabel() + "-" + UUID.randomUUID());
//
//        cv.addProperty(RDF.type, model.createProperty(cvURI + "CV"));
//        // cv.addProperty(model.createProperty("rdfs:label"),p.getLabel());
//        if (p.getLabel()!=null)      
//            cv.addProperty(RDFS.label, p.getLabel());
//        if (p.getDescription()!=null)
//            cv.addProperty(model.createProperty("rdfs:comment"), p.getDescription());
//        if (p.getPersonURI()!=null)
//            cv.addProperty(model.createProperty("cv:aboutPerson"), p.getPersonURI());
//
//        for (Skill skill : p.getSkills()) {
//            if (skill.getLabel()!=null){
//                cv.addProperty(model.createProperty(QualiChainURI + "refersToAccomplishment"),skill.getLabel());
//                cv.addProperty(model.createProperty(QualiChainURI + "refersToExperience"), skill.getLabel());
//            }
//        }

        return p;
    }
    
    public Person toPerson(String data) {
        Person p = g.fromJson(data, Person.class);
        String uri = "person-" + UUID.randomUUID();
        
        Resource person = model.createResource(uri);

        person.addProperty(RDF.type, model.createProperty(QualiChainURI + "Person"));
        // jobpost.addProperty(model.createProperty("rdfs:label"),p.getLabel());
        if (p.getLabel()!=null) person.addProperty(RDFS.label, p.getLabel());
        if (p.getName()!=null) person.addProperty(FOAF.name, p.getName());
        if (p.getsurname()!=null) person.addProperty(FOAF.familyName, p.getsurname());
  
        return p;
    }
    
    public Skill toSkill(String data) {
    	Skill s = g.fromJson(data,  Skill.class);
    	
    	return s;
    }
    
    public CVSkillRef toCVSkillRef(String data) {
    	CVSkillRef ref = g.fromJson(data, CVSkillRef.class);
    	return ref;
    }

    public void SavetoFile(String filename) {
        // System.out.println("-------------------");
        // System.out.println(model);
        System.out.println("Saving to file----------");
        model.write(System.out, "TTL");
        System.out.println("-------------------");
        OutputStream out;
        try {
            out = new FileOutputStream(filename);
            model.write(out, "TTL");
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String toString() {
        return this.toString("NTRIPLES");    
    }

    public String toString(String type) {
        StringWriter out = new StringWriter();
        model.write(out, type);
        return out.toString();    
    }

    public String toStringData() {
        StringWriter out = new StringWriter();
        model.write(out, "TTL");
        out.getBuffer().delete(0, out.getBuffer().indexOf("\n\n")); 
        return out.toString();    
    }

    public String toStringHeader() {
        StringWriter out = new StringWriter();
        model.write(out, "TTL");
        out.getBuffer().delete(out.getBuffer().indexOf("\n\n"),out.getBuffer().length()-1); 
        String header = out.toString();
        //header = header.replaceAll("@", "");
        //header = header.replaceAll(".", "");
        return header;    
    }


    
}