/**
 * 
 */
package fichedbsvc;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import co.phystech.aosorio.app.Main;
import co.phystech.aosorio.config.Constants;
import co.phystech.aosorio.config.Routes;
import co.phystech.aosorio.controllers.CfgController;
import co.phystech.aosorio.controllers.IModel;
import co.phystech.aosorio.controllers.Sql2oModel;
import co.phystech.aosorio.models.Book;
import co.phystech.aosorio.models.Comment;
import co.phystech.aosorio.models.NewFichePayload;
import spark.Spark;

/**
 * @author AOSORIO
 *
 */
public class ControllerTest {

	private final static Logger slf4jLogger = LoggerFactory.getLogger(ControllerTest.class);

	static CfgController dbConf = new CfgController(Constants.CONFIG_FILE);
	
	private static ArrayList<String> testDataUuid = new ArrayList<String>();
	
	@BeforeClass
	public static void beforeClass() {
		String[] args = {"test"};
		Main.main(args);
	}

	@AfterClass
	public static void afterClass() {
		Spark.stop();
		cleanUp();
	}

	@Test
	public void readBooksTest() {

		int httpResult = 0;
		String httpMessage = "";
		String jsonResponse = "";
		StringBuilder result = new StringBuilder();

		String route = Routes.BOOKS;
		String serverPath = "http://localhost:4567";

		try {
			URL appUrl = new URL(serverPath + route);

			HttpURLConnection urlConnection = (HttpURLConnection) appUrl.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("Content-type", "application/json");
			urlConnection.setRequestMethod("GET");
			httpResult = urlConnection.getResponseCode();
			httpMessage = urlConnection.getResponseMessage();

			InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
			BufferedReader reader = new BufferedReader(in);

			String text = "";
			while ((text = reader.readLine()) != null) {
				jsonResponse += text;
				result.append(text);
			}

			reader.close();
			in.close();
			urlConnection.disconnect();

			slf4jLogger.info(jsonResponse);
			slf4jLogger.info(result.toString());

			JsonParser parser = new JsonParser();
			JsonArray json = parser.parse(result.toString()).getAsJsonArray();

			slf4jLogger.info(String.valueOf(json.size()));

			assertEquals(200, httpResult);
			assertEquals("OK", httpMessage);
			
		} catch (ConnectException e) {
			slf4jLogger.info("Problem in connection");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}

	}

	@Test
	public void createBooksTest() {

		int httpResult = 0;
		String httpMessage = "";
		String jsonResponse = "";
		StringBuilder result = new StringBuilder();

		String route = Routes.BOOKS;
		String serverPath = "http://localhost:4567";

		try {
			URL appUrl = new URL(serverPath + route);

			HttpURLConnection urlConnection = (HttpURLConnection) appUrl.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("Content-type", "application/json");
			urlConnection.setRequestMethod("POST");

			JsonObject bookJson = new JsonObject();

			bookJson.addProperty("title", ModelTest.title);
			bookJson.addProperty("subTitle", ModelTest.subTitle);
			bookJson.addProperty("author", ModelTest.author);
			bookJson.addProperty("yearPub", ModelTest.yearPub);
			bookJson.addProperty("editor", ModelTest.editor);
			bookJson.addProperty("collection", ModelTest.collection);
			bookJson.addProperty("pages", ModelTest.pages);
			bookJson.addProperty("language", ModelTest.language);
			bookJson.addProperty("author_nationality", ModelTest.nationality);
			bookJson.addProperty("author_period", ModelTest.period);

			String newBook = bookJson.toString();

			slf4jLogger.info("New book object: " + newBook);

			Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
			writer.write(newBook);
			writer.flush();

			httpResult = urlConnection.getResponseCode();
			httpMessage = urlConnection.getResponseMessage();

			slf4jLogger.info(httpMessage);

			InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
			BufferedReader reader = new BufferedReader(in);

			String text = "";
			while ((text = reader.readLine()) != null) {
				jsonResponse += text;
				result.append(text);
			}

			reader.close();
			in.close();

			slf4jLogger.info(jsonResponse);
			slf4jLogger.info(result.toString());

			JsonParser parser = new JsonParser();
			JsonObject json = parser.parse(result.toString()).getAsJsonObject();

			slf4jLogger.info(String.valueOf(json.size()));

			assertEquals(200, httpResult);
			assertEquals("OK", httpMessage);
			
			testDataUuid.add( json.get("value").getAsString());

		} catch (ConnectException e) {
			slf4jLogger.info("Problem in connection");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}

	}

	@Test
	public void createFicheTest() {

		int httpResult = 0;
		String httpMessage = "";
		String jsonResponse = "";
		StringBuilder result = new StringBuilder();

		String route = Routes.FICHES;
		String serverPath = "http://localhost:4567";

		try {
			URL appUrl = new URL(serverPath + route);

			HttpURLConnection urlConnection = (HttpURLConnection) appUrl.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("Content-type", "application/json");
			urlConnection.setRequestMethod("POST");

			Book book = new Book();
			book.setBook_uuid(new UUID(0, 1));
			book.setTitle(ModelTest.title);
			book.setSubTitle(ModelTest.subTitle);
			book.setAuthor(ModelTest.author);
			book.setYearPub(ModelTest.yearPub);
			book.setEditor(ModelTest.editor);
			book.setCollection(ModelTest.collection);
			book.setPages(ModelTest.pages);
			book.setLanguage(ModelTest.language);
			book.setAuthor_nationality(ModelTest.nationality);
			book.setAuthor_period(ModelTest.period);
			
			List<Comment> comments = new ArrayList<Comment>();

			Comment comment = new Comment();

			comment.setAuthor(ModelTest.commentAuthor);
			comment.setAboutAuthor(ModelTest.aboutAuthor);
			comment.setAboutGenre(ModelTest.aboutGenre);
			comment.setAboutCadre(ModelTest.aboutCadre);
			comment.setAboutCharacters(ModelTest.aboutCharacters);
			comment.setResume(ModelTest.resume);
			comment.setExtrait(ModelTest.extrait);
			comment.setAppreciation(ModelTest.appreciation);
			comment.setComment_text(ModelTest.commentText);
			comment.setOther_details(ModelTest.otherDetails);

			comments.add(comment);

			Gson gson = new GsonBuilder().create();

			NewFichePayload fiche = new NewFichePayload();

			fiche.setId(1);
			fiche.setBook(book);
			fiche.setComments(comments);
			String body = gson.toJson(fiche);

			slf4jLogger.info("New fiche object: " + body);

			Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
			writer.write(body);
			writer.flush();

			httpResult = urlConnection.getResponseCode();
			httpMessage = urlConnection.getResponseMessage();

			slf4jLogger.info(httpMessage);

			InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
			BufferedReader reader = new BufferedReader(in);

			String text = "";
			while ((text = reader.readLine()) != null) {
				jsonResponse += text;
				result.append(text);
			}

			reader.close();
			in.close();

			slf4jLogger.info(jsonResponse);
			slf4jLogger.info(result.toString());

			JsonParser parser = new JsonParser();
			JsonObject json = parser.parse(result.toString()).getAsJsonObject();

			slf4jLogger.info(String.valueOf(json.size()));

			testDataUuid.add( json.get("value").getAsString());
			
			assertEquals(200, httpResult);
			assertEquals("OK", httpMessage);

		} catch (ConnectException e) {
			slf4jLogger.info("Problem in connection");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}

	}
	
	private static void cleanUp() {
		
		String address = dbConf.getDbAddress();
		String dbUsername = dbConf.getDbUser();
		String dbPassword = dbConf.getDbPass();

		Sql2o sql2o = new Sql2o(address, dbUsername, dbPassword, new PostgresQuirks() {
			{
				// make sure we use default UUID converter.
				converters.put(UUID.class, new UUIDConverter());
			}
		});

		IModel model = new Sql2oModel(sql2o);
		
		Iterator<String> itrUuid = testDataUuid.iterator();
		
		while(itrUuid.hasNext()) {
			String uuid = itrUuid.next();
			slf4jLogger.info("To be deleted: " + uuid);
			model.deleteFiche(UUID.fromString(uuid));
		}
		
	}

}
