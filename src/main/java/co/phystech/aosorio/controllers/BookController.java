/**
 * 
 */
package co.phystech.aosorio.controllers;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.phystech.aosorio.config.Constants;
import co.phystech.aosorio.models.BackendMessage;
import co.phystech.aosorio.models.NewBookPayload;
import spark.Request;
import spark.Response;

/**
 * @author AOSORIO
 *
 */
public class BookController {
	
	private final static Logger slf4jLogger = LoggerFactory.getLogger(BookController.class);
	
	public static Object createBook(Request pRequest, Response pResponse) {
		
		Sql2o sql2o = SqlController.getInstance().getAccess();
		
		IModel model = new Sql2oModel(sql2o);
			
		BackendMessage returnMessage = new BackendMessage();
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			slf4jLogger.info(pRequest.body());
			
			NewBookPayload creation = mapper.readValue(pRequest.body(), NewBookPayload.class);
			if (!creation.isValid()) {
				slf4jLogger.info("Invalid body object");
				pResponse.status(Constants.HTTP_BAD_REQUEST);
				return returnMessage.getNotOkMessage("Invalid body object");
			} 

			slf4jLogger.info(creation.toString());
			
			UUID id = model.addBook(creation.getTitle(), 
					creation.getSubTitle(), 
					creation.getAuthor(),
					creation.getYearPub(), 
					creation.getEditor(), 
					creation.getCollection(), 
					creation.getPages(), 
					creation.getLanguage(),
					creation.getTranslation(),
					creation.getOptional_one());
			
			pResponse.status(200);
			pResponse.type("application/json");
			
			return returnMessage.getOkMessage(String.valueOf(id));
			
		} catch (IOException jpe) {
			slf4jLogger.debug("Problem adding book");
			pResponse.status(Constants.HTTP_BAD_REQUEST);
			return returnMessage.getNotOkMessage("Problem adding book");
		}
				
	}
	
	public static Object readBooks(Request pRequest, Response pResponse) {
		
		Sql2o sql2o = SqlController.getInstance().getAccess();

		IModel model = new Sql2oModel(sql2o);
		
		pResponse.status(200);
		pResponse.type("application/json");
		return model.getAllBooks();
			
	}

}
