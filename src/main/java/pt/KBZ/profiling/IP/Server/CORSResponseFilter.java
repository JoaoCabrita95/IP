package IP.Server;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

public class CORSResponseFilter 
    implements ContainerResponseFilter {

        public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
                throws IOException {
    
            MultivaluedMap<String, Object> headers = responseContext.getHeaders();
    
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");	
            headers.add("Access-Control-Allow-Credentials", "true");
            headers.add("Access-Control-Allow-Headers", "origin, accept, X-Requested-With, Content-Type, X-Codingpedia, authorization, X-Auth-Token, Accept-Version, Content-MD5, CSRF-Token");
            // headers.add("Access-Control-Allow-Headers", "*");
            //if (requestContext.getHeaderString("Access-Control-Request-Headers") != null) {
            //    headers.add("Access-Control-Allow-Headers", requestContext.getHeaderString(headers).toLowerCase().contains("authorization"));
            //   }

        }
}