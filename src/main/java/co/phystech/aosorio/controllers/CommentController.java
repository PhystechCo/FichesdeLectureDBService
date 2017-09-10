package co.phystech.aosorio.controllers;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.phystech.aosorio.config.Constants;
import co.phystech.aosorio.models.BackendMessage;
import co.phystech.aosorio.models.NewCommentPayload;
import spark.Request;
import spark.Response;

public class CommentController {
	
private final static Logger slf4jLogger = LoggerFactory.getLogger(CommentController.class);
	
	public static Object createComment(Request pRequest, Response pResponse) {
		
		Sql2o sql2o = SqlController.getInstance().getAccess();
		
		IModel model = new Sql2oModel(sql2o);
			
		BackendMessage returnMessage = new BackendMessage();
		
		pResponse.type("application/json");
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			slf4jLogger.info(pRequest.body());
			
			NewCommentPayload comment = mapper.readValue(pRequest.body(), NewCommentPayload.class);
			
			if (!comment.isValid()) {
				slf4jLogger.info("Invalid body object");
				pResponse.status(Constants.HTTP_BAD_REQUEST);
				return returnMessage.getNotOkMessage("Invalid body object");
			} 

			slf4jLogger.info(comment.toString());
			
			UUID id = model.addComment(comment.getBook_uuid(), 
					comment.getAuthor(), 
					comment.getAboutAuthor(), 
					comment.getAboutGenre(), 
					comment.getAboutCadre(), 
					comment.getAboutCharacters(), 
					comment.getResume(),
					comment.getExtrait(), 
					comment.getAppreciation(),
					comment.getIsCompleted(),
					comment.getOptional_one(),
					comment.getOptional_two(),
					comment.getComment_text(),
					comment.getOther_details());
			
			pResponse.status(200);
					
			return returnMessage.getOkMessage(String.valueOf(id));
			
		} catch (IOException jpe) {
			slf4jLogger.debug("Problem adding comment");
			pResponse.status(Constants.HTTP_BAD_REQUEST);
			return returnMessage.getNotOkMessage("Problem adding comment");
		}
				
	}
	
	public static Object readComments(Request pRequest, Response pResponse) {
		
		Sql2o sql2o = SqlController.getInstance().getAccess();

		IModel model = new Sql2oModel(sql2o);
		
		pResponse.status(200);
		pResponse.type("application/json");
		return model.getAllComments();
			
	}	

}
