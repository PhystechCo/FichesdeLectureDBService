/**
 * 
 */
package co.phystech.aosorio.controllers;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.phystech.aosorio.config.Constants;
import co.phystech.aosorio.models.BackendMessage;
import co.phystech.aosorio.models.Fiche;
import co.phystech.aosorio.models.NewFichePayload;
import co.phystech.aosorio.services.FileSvc;
import spark.Request;
import spark.Response;

/**
 * @author AOSORIO
 *
 */
public class FicheController {

	private final static Logger slf4jLogger = LoggerFactory.getLogger(FicheController.class);

	public static Object createFiche(Request pRequest, Response pResponse) {

		Sql2o sql2o = SqlController.getInstance().getAccess();

		BackendMessage returnMessage = new BackendMessage();

		pResponse.type("application/json");

		try {

			ObjectMapper mapper = new ObjectMapper();

			IModel model = new Sql2oModel(sql2o);

			slf4jLogger.info(pRequest.body());

			NewFichePayload newFiche = mapper.readValue(pRequest.body(), NewFichePayload.class);

			if (!newFiche.isValid()) {
				slf4jLogger.info("Invalid body object");
				pResponse.status(Constants.HTTP_BAD_REQUEST);
				return returnMessage.getNotOkMessage("Invalid body object");
			}

			slf4jLogger.info(newFiche.toString());

			UUID id;
			
			if ( !model.existFiche(newFiche))			
				id = model.addFiche(newFiche.getId(), newFiche.getBook(), newFiche.getComments());
			else {
				id = new UUID(0,0);
				slf4jLogger.info("Fiche already exist - not added");
			}
				
			pResponse.status(200);

			return returnMessage.getOkMessage(String.valueOf(id));

		} catch (IOException jpe) {
			slf4jLogger.debug("Problem adding fiche");
			pResponse.status(Constants.HTTP_BAD_REQUEST);
			return returnMessage.getNotOkMessage("Problem adding fiche");
		}

	}

	public static Object readFiche(Request pRequest, Response pResponse) {

		Sql2o sql2o = SqlController.getInstance().getAccess();

		// BackendMessage returnMessage = new BackendMessage();

		IModel model = new Sql2oModel(sql2o);

		int id = Integer.valueOf(pRequest.params("id"));
		UUID uuid = UUID.fromString(pRequest.params("uuid").toString());

		slf4jLogger.debug("Parameters: " + id + " " + uuid);

		Fiche fiche = model.getFiche(id, uuid);

		pResponse.status(200);
		pResponse.type("application/json");

		return fiche;

	}

	public static Object readFiches(Request pRequest, Response pResponse) {

		Sql2o sql2o = SqlController.getInstance().getAccess();

		IModel model = new Sql2oModel(sql2o);

		pResponse.status(200);
		pResponse.type("application/json");
		return model.getAllFiches();

	}

	public static Object updateFiche(Request pRequest, Response pResponse) {

		Sql2o sql2o = SqlController.getInstance().getAccess();

		BackendMessage returnMessage = new BackendMessage();

		pResponse.type("application/json");

		try {

			ObjectMapper mapper = new ObjectMapper();

			IModel model = new Sql2oModel(sql2o);

			slf4jLogger.info("updater: " + pRequest.body());

			NewFichePayload updatedFiche = mapper.readValue(pRequest.body(), NewFichePayload.class);

			if (!updatedFiche.isValid()) {
				slf4jLogger.info("Invalid body object");
				pResponse.status(Constants.HTTP_BAD_REQUEST);
				return returnMessage.getNotOkMessage("Invalid body object");
			}

			boolean success = model.updateFiche(updatedFiche);

			slf4jLogger.info("update done");

			pResponse.status(200);

			return returnMessage.getOkMessage(String.valueOf(success));

		} catch (IOException jpe) {
			jpe.printStackTrace();
			slf4jLogger.debug("Problem adding fiche");
			pResponse.status(Constants.HTTP_BAD_REQUEST);
			return returnMessage.getNotOkMessage("Problem updating fiche");

		} catch (Exception ex) {
			pResponse.status(404);
			ex.printStackTrace();
			return returnMessage.getNotOkMessage("Generic exception updating fiche");

		}

	}

	public static Object deleteFiche(Request pRequest, Response pResponse) {

		Sql2o sql2o = SqlController.getInstance().getAccess();

		BackendMessage returnMessage = new BackendMessage();

		IModel model = new Sql2oModel(sql2o);

		UUID uuid = UUID.fromString(pRequest.params("uuid").toString());

		boolean status = model.deleteFiche(uuid);

		pResponse.status(200);
		pResponse.type("application/json");

		return returnMessage.getOkMessage(String.valueOf(status));

	}

	public static Object deleteAll(Request pRequest, Response pResponse) {

		Sql2o sql2o = SqlController.getInstance().getAccess();

		BackendMessage returnMessage = new BackendMessage();

		IModel model = new Sql2oModel(sql2o);
		slf4jLogger.info(pRequest.body());
		boolean status = model.deleteAll();

		pResponse.status(200);
		pResponse.type("application/json");

		return returnMessage.getOkMessage(String.valueOf(status));

	}
	
	public static Object uploadFiches(Request pRequest, Response pResponse) {

		Sql2o sql2o = SqlController.getInstance().getAccess();

		BackendMessage returnMessage = new BackendMessage();

		pResponse.type("application/json");

		try {

			String fileName = FileSvc.uploadFile(pRequest);
			
			slf4jLogger.info(fileName);
			
			IModel model = new Sql2oModel(sql2o);
			
			model.createFichesFromCSV(fileName);
			
			pResponse.status(200);
			
			FileSvc.deleteFile(fileName);
			
			return returnMessage.getOkMessage(String.valueOf(1));

		} catch (IOException | ServletException jpe) {
			
			slf4jLogger.debug("Problem adding fiches");
			pResponse.status(Constants.HTTP_BAD_REQUEST);
			return returnMessage.getNotOkMessage("Problem adding fiches");

		} 

	}

}
