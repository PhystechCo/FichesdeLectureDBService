/**
 * 
 */
package co.phystech.aosorio.services;

import org.sql2o.Sql2o;

import com.google.gson.JsonObject;

import co.phystech.aosorio.controllers.IModel;
import co.phystech.aosorio.controllers.Sql2oModel;
import co.phystech.aosorio.controllers.SqlController;
import spark.Request;
import spark.Response;

/**
 * @author AOSORIO
 *
 */
public class StatisticsSvc {
	
	public static Object getBasicStats(Request pRequest, Response pResponse) {
		
		JsonObject json = new JsonObject();
		
		Sql2o sql2o = SqlController.getInstance().getAccess();

		IModel model = new Sql2oModel(sql2o);
		
		//pResponse.status(200);
		//pResponse.type("application/json");
		
		json.addProperty("books", model.getAllBooks().size());
		json.addProperty("comments", model.getAllComments().size());
		
		return json;
		
		
	}
	
}
