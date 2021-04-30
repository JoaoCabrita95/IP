//https://www.baeldung.com/java-http-request
package IP.Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPrequest {
	private URL url;
	private HttpURLConnection con;
	private static final String USER_AGENT = "Mozilla/5.0";

	public HTTPrequest(String aurl) {
		try {
			this.url = new URL(aurl);
			this.con = (HttpURLConnection) url.openConnection();

			//Setting Request Headers
			//con.setRequestProperty("Content-Type", "application/json; utf-8");
			//con.setRequestProperty("Accept", "application/json");

			//Configuring Timeouts
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			//Handling Cookies
			//String cookiesHeader = con.getHeaderField("Set-Cookie");
			//List<HttpCookie> cookies = HttpCookie.parse(cookiesHeader);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void addRequestProperty(String key, String Value){
		this.con.setRequestProperty(key, Value);
	}

	public void addParam(String param,String value){
		this.con.setDoOutput(true);
		Map<String, String> parameters = new HashMap<>();
		parameters.put(param, value);
		DataOutputStream out;
		try {
			out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
			out.flush();
			out.close();        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void addURLParam(String urlParameters){
		this.con.setDoOutput(true);
		//String jsonInputString = "query: SELECT * WHERE {?s ?p ?o .}";
		//OutputStream os = this.con.getOutputStream();
		//byte[] input = jsonInputString.getBytes("utf-8");
		//os.write(input, 0, input.length);           

		//OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
		//osw.write("query: SELECT * WHERE {?s ?p ?o .}");
		//osw.flush();
		//osw.close();
		//os.close(); 

		byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
		@SuppressWarnings("unused")
		int    postDataLength = postData.length;
		try( DataOutputStream wr = new DataOutputStream( this.con.getOutputStream())) {
			wr.write( postData );
			wr.flush();
			wr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public String GETrequest() {
		try {
			this.con.setRequestMethod("GET");
			this.con.setRequestProperty("User-Agent", USER_AGENT);
			//String contentType = con.getHeaderField("Content-Type");
			int status = this.con.getResponseCode();
//			System.out.println(status);
			return con.getResponseMessage();
//			if (status == HttpURLConnection.HTTP_OK) { // success
//				return con.getResponseMessage();
//			} 
//			else if(status == HttpURLConnection.HTTP_BAD_REQUEST) {
//				return con.getResponseMessage();
//			}
//			else {
//				System.out.println("GET request not worked");
//			}
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> ResponseList(){                                
		try{
			List<String> results= new ArrayList<>();
			//Reading the Response
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));

			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {                                
				results.add(responseLine.trim());
			}
			br.close();

			return(results);

		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String Response() {                                
		try{
			//Reading the Response
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));

			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			br.close();
			return(response.toString());

		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			return "IO ERROR";
		}
		return null;
	}

	public void close(){
		this.con.disconnect();
	}


	public String POSTrequest(String query) {
		try{
			this.con.setRequestMethod("POST");
			//this.con.setRequestProperty("User-Agent", USER_AGENT);

			// For POST only - START
			//String POST_PARAMS = "userid=100&userName=uname";
			//this.con.setDoOutput(true);
			//OutputStream os = con.getOutputStream();
			//os.write(POST_PARAMS.getBytes());
			//os.flush();
			//os.close();
			// For POST only - END

			this.con.setDoOutput(true);
			OutputStream os = this.con.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");    
			osw.write(query);
			osw.flush();
			osw.close();
			os.close();

			int responseCode = con.getResponseCode();
			System.out.println("POST Response Code :: " + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) { //success
				return con.getResponseMessage();
			}
			else if (responseCode == HttpURLConnection.HTTP_NO_CONTENT){
				return con.getResponseMessage();
			} else {
				System.out.println("POST request not worked");
			}
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String PUTrequest(String query) {
		return null;
	}

	public String DELETErequest() {
		try {
			this.con.setRequestMethod("DELETE");
			this.con.setRequestProperty("User-Agent", USER_AGENT);

			int status = this.con.getResponseCode();
//			System.out.println(status);
			if (status == HttpURLConnection.HTTP_OK) { // success
				return con.getResponseMessage();
			} else {
				System.out.println("GET request not worked");
			}
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
