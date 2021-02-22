package IP;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import IP.Client.HTTPrequest;

public class SolrTest {

	public static void main(String[] args) {
		solrInsert("C:/Users/user/Desktop/resume_dataset.csv");
//		solrQuery("*");

	}
	
	public static void solrQuery(String query) {
		HTTPrequest request= new HTTPrequest("http://localhost:8983/solr/fusekiTest/select?");

		request.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		request.addRequestProperty("Accept", "application/sparql-results+json, */*; q=0.9");

		request.addParam("q", query);
		
		//request.addURLParam("query="+query);
		String response = request.GETrequest();
		request.close();
		System.out.println(response);
		solrResponseParser(response);
		
	}
	//solr response parser, should remove hard coded numbers
	private static void solrResponseParser(String response) {
		String params = response.substring(response.indexOf("params")-1, response.indexOf("}")+1);
		System.out.println(params);
		System.out.println();
		String responseInfo = response.substring(response.indexOf("}")+3, response.indexOf("["));
		System.out.println(responseInfo);
		System.out.println();
		String [] docs = response.substring(response.indexOf("[")).split("}");
		for(String e: docs) {
			if(e.length() > 2) {
				e = e.substring(2);
				String [] fields = e.split(",");
				for(String f: fields) {
					//Could not replace \\n values yet
					f = f.replaceAll("\\n", " ");
					System.out.println(f);	
				}
				System.out.println();
			}
		}
	}
	
public static void solrInsert(String path)  {
		
		HTTPrequest request= new HTTPrequest("http://localhost:8983/solr/fusekiTest/");

		request.addRequestProperty("Content-Type", "application/csv; charset=UTF-8");
		request.addRequestProperty("Accept", "application/sparql-results+json, */*; q=0.9");
//		request.addURLParam("header=true&fieldnames=id,Category,Resume&commit=true");
		
		Path p = new File(path).toPath();
		List<String> f;
		String insert = ""; 
		try {
			f = Files.readAllLines(p);
			for(String s: f) {
				insert += s;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(insert);
		
		request.addParam("update", insert);
		
		//request.addURLParam("query="+query);
//		String response = request.GetResponse();
		request.close();
//		System.out.println(response);
		
		
	}

}
