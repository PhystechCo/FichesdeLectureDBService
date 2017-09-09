/**
 * 
 */
package co.phystech.aosorio.services;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.data.Table;

import com.google.gson.JsonArray;
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

	private final static Logger slf4jLogger = LoggerFactory.getLogger(StatisticsSvc.class);

	public static Object getBasicStats(Request pRequest, Response pResponse) {

		JsonObject json = new JsonObject();

		Sql2o sql2o = SqlController.getInstance().getAccess();

		IModel model = new Sql2oModel(sql2o);

		json.addProperty("books", model.getAllBooks().size());
		json.addProperty("comments", model.getAllComments().size());

		return json;

	}

	public static Object getAdvancedStats(Request pRequest, Response pResponse) {

		JsonObject json = new JsonObject();

		Sql2o sql2o = SqlController.getInstance().getAccess();

		try (Connection conn = sql2o.beginTransaction()) {

			json = (JsonObject) countGroups(conn);

		} catch (Exception ex) {
			slf4jLogger.info(ex.getLocalizedMessage());
		}

		return json;
	}

	private static Object countGroups(Connection dbConnection) {

		List<Map<String, Object>> result;
		
		Table table = dbConnection
				.createQuery("SELECT optional_one, COUNT (optional_one) FROM books GROUP BY optional_one")
				.executeAndFetchTable();

		result = GeneralSvc.tableToList(table);
		
		JsonObject resultJson = new JsonObject();
		JsonArray counting = new JsonArray();
		
		result.forEach( item -> {

			JsonObject counters = new JsonObject();
			
			Collection<Object>values = item.values();
			
			String key;
			if( (String)values.toArray()[0] == null ) key = "NA";
			else key = (String)values.toArray()[0];
			counters.addProperty( key, String.valueOf(values.toArray()[1]));
			counting.add(counters);
			
		}
		);

		resultJson.add("groups", counting);
		slf4jLogger.info(resultJson.toString());
		
		return resultJson;

	}

}
