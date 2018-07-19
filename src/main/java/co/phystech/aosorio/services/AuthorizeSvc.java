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

import co.phystech.aosorio.config.Constants;
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
		String serverPath = "";
		
		String route = Constants.DEFAULT_AUTH_ROUTE;
		
		if (System.getenv("AUTHSVC_URL") != null) {
			serverPath = System.getenv("AUTHSVC_URL");
		} else
			serverPath = Constants.DEFAULT_AUTH_SERVICE;
				
		pResponse.type("application/json");
		
		try {
			
			String header = pRequest.headers(Constants.DEFAULT_AUTH_HEADER);
			
			if ( header == null | header.length() == 0)
				throw new NullPointerException();
				
			slf4jLogger.info("Arriving auth header: " + header);
						
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
			
		} catch (ConnectException ex) {
			pResponse.status(500);
			slf4jLogger.info("Problem in connection");
			halt(500, "Not authorized");
			
		} catch (NullPointerException ex) {
			pResponse.status(401);
			slf4jLogger.info("No token present in headers");
			halt(401, "Not authorized");

		} catch (Exception ex) {
			pResponse.status(401);
			slf4jLogger.info("Unconsidered exception " + ex.getLocalizedMessage());
			halt(401, "Not authorized");
		}

		return "OK";

	}

}
