/**
 * 
 */
package co.phystech.aosorio.services;

import static spark.Spark.halt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import spark.Request;
import spark.Response;

/**
 * @author AOSORIO
 *
 */
public class AuthorizeSvc {
	
	private final static Logger slf4jLogger = LoggerFactory.getLogger(AuthorizeSvc.class);
	
	public static String authorizeUser(Request pRequest, Response pResponse) {

		if ( pRequest.requestMethod().equals("OPTIONS") ) {
			pResponse.status(200);
			return "OK";
		}
		
		String jsonResponse = "";
		
		String route = "/auth/access/";
		
		String serverPath = "https://rugged-yosemite-61189.herokuapp.com";
		//String serverPath = "http://localhost:4568";
			
		pResponse.type("application/json");
		
		try {
			
			String header = pRequest.headers("Authorization");
			
			if ( header == null | header.length() == 0) {
				slf4jLogger.info("Arriving auth header is null"); 
			} 
				
			slf4jLogger.info("Arriving auth header: " + header);
			
			//String header = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhbmRyZXMub3NvcmlvQG91dGxvb2suY29tIiwiaWF0IjoxNDk5MjA4Njk4LCJzdWIiOiJhbmRyZXMub3NvcmlvQG91dGxvb2suY29tIiwiaXNzIjoicGh5c3RlY2guY28iLCJleHAiOjE0OTk4MTM0OTgsImF1ZCI6ImFkbWluIn0.Jr244ugr8I6uDiMIabiDS5LjUTf0c6oRwdgj2d4Z0Rs";
			//String header = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhbmRyZXMub3NvcmlvQG91dGxvb2suY29tIiwiaWF0IjoxNDk5MjA4Njk4LCJzdWIiOiJhbmRyZXMub3NvcmlvQG91dGxvb2suY29tIiwiaXNzIjoicGh5c3RlY2guY28iLCJleHAiOjE0OTk4MTM0OTgsImF1ZCI6ImFkbWluIn0.Jr244ugr8I6uDiMIabiDS5LjUTf0c6oRwdgj2d4Z0Rs";
			//String header = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhbmRyZXMub3NvcmlvQG91dGxvb2suY29tIiwiaWF0IjoxNDk5Mzg4ODM3LCJzdWIiOiJhbmRyZXMub3NvcmlvQG91dGxvb2suY29tIiwiaXNzIjoicGh5c3RlY2guY28iLCJleHAiOjE0OTk5OTM2MzcsImF1ZCI6ImFkbWluIn0.PE6XyltcKPd-Js5vU7y9NzvhNlbIlChtQ5_R874nqfs";
						
			URL appUrl = new URL(serverPath + route);

			HttpURLConnection urlConnection = (HttpURLConnection) appUrl.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("Accept", "application/json");
			urlConnection.setRequestProperty("Content-type", "application/json");
			urlConnection.setRequestProperty ("Authorization", header);
			urlConnection.setRequestMethod("POST");
						
			int httpResult = urlConnection.getResponseCode();
			String httpMessage = urlConnection.getResponseMessage();

			slf4jLogger.info( String.valueOf(httpResult) + " " + httpMessage);
			
			InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
			BufferedReader reader = new BufferedReader(in);

			String text = "";
			while ((text = reader.readLine()) != null) {
				jsonResponse += text;
			}

			reader.close();
			in.close();
			urlConnection.disconnect();

			JsonParser parser = new JsonParser();
			JsonObject json = parser.parse(jsonResponse).getAsJsonObject();

			slf4jLogger.info(jsonResponse);	
			slf4jLogger.info(json.get("value").getAsString());
	
			if( json.get("value").getAsString().equals("valid") ) {
				pResponse.status(200);
			}
			
		} catch (ConnectException e) {
			pResponse.status(500);
			slf4jLogger.info("Problem in connection");
			return "Not OK";
			
		} catch (NullPointerException e) {
			pResponse.status(401);
			slf4jLogger.info("No token present in headers");
			halt(401, "Not authorized");
			return "Not OK";
				
		} catch (Exception e) {
			pResponse.status(401);
			halt(401, "Not authorized");
			e.printStackTrace();
			return "Not OK";
	
		}
				
		return "OK";

	}

}
