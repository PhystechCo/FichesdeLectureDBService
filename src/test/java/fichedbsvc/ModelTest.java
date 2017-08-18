package fichedbsvc;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import co.phystech.aosorio.config.Constants;
import co.phystech.aosorio.controllers.CfgController;
import co.phystech.aosorio.controllers.IModel;
import co.phystech.aosorio.controllers.Sql2oModel;
import co.phystech.aosorio.models.Book;
import co.phystech.aosorio.models.Comment;
import co.phystech.aosorio.models.Fiche;
import co.phystech.aosorio.models.NewBookPayload;
import co.phystech.aosorio.models.NewCommentPayload;
import co.phystech.aosorio.models.NewFichePayload;

public class ModelTest {

	private final static Logger slf4jLogger = LoggerFactory.getLogger(ModelTest.class);

	CfgController dbConf = new CfgController(Constants.CONFIG_FILE);

	public static final String title = "The Phoenix Project";
	public static final String subTitle = "A Novel about IT, Devops";
	public static final String author = "Gene Kim, Kevin Behr, George Spafford";
	public static final int yearPub = 2013;
	public static final String editor = "IT Revolution Press";
	public static final String collection = "Business";
	public static final int pages = 312;
	public static final String language = "English";

	public static final String commentAuthor = "Andres Osorio";
	public static final String aboutAuthor = "Gen Kim and team are experts in DevOps";
	public static final String aboutGenre = "Similar type of novel as the Goal";
	public static final String aboutCadre = "Important IT business, link between success and DevOps";
	public static final String aboutCharacters = "Main character Bill";
	public static final String resume = "Bill is asked to take charge of IT operations and improve the IT department";
	public static final String extrait = "DevOps";
	public static final String appreciation = "Great novel; very interesting";

	@Test
	public void bookCreationTest() {
		
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

		NewBookPayload abook = new NewBookPayload();

		abook.setTitle(title);
		abook.setSubTitle(subTitle);
		abook.setAuthor(author);
		abook.setYearPub(yearPub);
		abook.setEditor(editor);
		abook.setCollection(collection);
		abook.setPages(pages);
		abook.setLanguage(language);

		UUID id = model.addBook(abook.getTitle(), abook.getSubTitle(), abook.getAuthor(),
				abook.getYearPub(), abook.getEditor(), abook.getCollection(), abook.getPages(),
				abook.getLanguage());

		boolean test = model.existBook(id);
		
		assertTrue(test);
		
		model.deleteBook(id);
		
	}

	@Test
	public void commentCreationTest() {
		
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

		NewCommentPayload acomment = new NewCommentPayload();

		acomment.setAuthor(commentAuthor);
		acomment.setAboutAuthor(aboutAuthor);
		acomment.setAboutGenre(aboutGenre);
		acomment.setAboutCadre(aboutCadre);
		acomment.setAboutCharacters(aboutCharacters);
		acomment.setResume(resume);
		acomment.setExtrait(extrait);
		acomment.setAppreciation(appreciation);
		acomment.setIsCompleted(false);

		List<Book> books = new ArrayList<Book>();
		books = model.getAllBooks();

		UUID bookUuid = null;

		for (Iterator<Book> it = books.iterator(); it.hasNext();) {
			Book abook = it.next();
			if (abook.getTitle().equals("The Phoenix Project")) {
				bookUuid = abook.getBook_uuid();
				break;
			}
		}

		acomment.setBook_uuid(bookUuid);

		UUID id = model.addComment(acomment.getBook_uuid(), acomment.getAuthor(), acomment.getAboutAuthor(),
				acomment.getAboutGenre(), acomment.getAboutCadre(), acomment.getAboutCharacters(), acomment.getResume(),
				acomment.getExtrait(), acomment.getAppreciation(), acomment.getIsCompleted(), "");

		boolean test = model.existComment(id);
		
		assertTrue(test);
		
		model.deleteComment(id);
		
	}

	@Test
	public void ficheCreationTest() {
		
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

		NewFichePayload fiche = new NewFichePayload();

		Book book = new Book();

		book.setTitle(title);
		book.setSubTitle(subTitle);
		book.setAuthor(author);
		book.setYearPub(yearPub);
		book.setEditor(editor);
		book.setCollection(collection);
		book.setPages(pages);
		book.setLanguage(language);

		List<Comment> comments = new ArrayList<Comment>();
		comments.add(new Comment());

		fiche.setId(1);
		fiche.setBook(book);
		fiche.setComments(comments);

		UUID id = model.addFiche(fiche.getId(), fiche.getBook(), fiche.getComments());

		slf4jLogger.info(id.toString());

		boolean test = model.existFiche(id);
		
		assertTrue(test);
		
		model.deleteFiche(id);

	}
	
	@Test
	public void ficheUpdateTest() {
		
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

		NewFichePayload fiche = new NewFichePayload();

		Book book = new Book();

		book.setTitle(title);
		book.setSubTitle(subTitle);
		book.setAuthor(author);
		book.setYearPub(yearPub);
		book.setEditor(editor);
		book.setCollection(collection);
		book.setPages(pages);
		book.setLanguage(language);

		List<Comment> comments = new ArrayList<Comment>();
		
		Comment acomment = new Comment();
		acomment.setAuthor(commentAuthor);
		comments.add(acomment);
		
		fiche.setId(1);
		fiche.setBook(book);
		fiche.setComments(comments);

		UUID id = model.addFiche(fiche.getId(), fiche.getBook(), fiche.getComments());

		slf4jLogger.info(id.toString() + " * " + String.valueOf(model.existFiche(id)) );
		
		NewFichePayload updatedFiche = new NewFichePayload();

		List<Comment> bComments = new ArrayList<Comment>();
		
		Comment bcomment = new Comment();
		bcomment.setAuthor("Edgar Osorio");
		bComments.add(bcomment);
		
		book.setBook_uuid(id);
		book.setPages(2001);
		
		updatedFiche.setId(1);	
		updatedFiche.setBook(book);
		updatedFiche.setComments(bComments);
		
		boolean test = model.updateFiche(updatedFiche);
		
		Fiche fiche2 = model.getFiche(0, id);

		slf4jLogger.info("updated fiche: " + fiche2.getBook().getPages() + " - " + fiche2.getComments().size());
		
		assertTrue(test);

		model.deleteFiche(id);

	}

	@Test
	public void bookParsingTest() {

		boolean result = false;

		JsonObject bookJson = new JsonObject();

		bookJson.addProperty("title", ModelTest.title);
		bookJson.addProperty("subTitle", ModelTest.subTitle);
		bookJson.addProperty("author", ModelTest.author);
		bookJson.addProperty("yearPub", ModelTest.yearPub);
		bookJson.addProperty("editor", ModelTest.editor);
		bookJson.addProperty("collection", ModelTest.collection);
		bookJson.addProperty("pages", ModelTest.pages);
		bookJson.addProperty("language", ModelTest.language);

		String newBook = bookJson.toString();

		ObjectMapper mapper = new ObjectMapper();

		NewBookPayload payload;

		try {
			payload = mapper.readValue(newBook, NewBookPayload.class);

			if (!payload.isValid()) {
				slf4jLogger.info("Invalid body object");
			} else {

				slf4jLogger.info(payload.toString());
				result = true;
			}

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertTrue(result);

	}

	@Test
	public void ficheParsingTest() {

		boolean result = false;

		Book book = new Book();
		book.setBook_uuid(new UUID(0, 1));
		book.setTitle(title);
		book.setSubTitle(subTitle);
		book.setAuthor(author);
		book.setYearPub(yearPub);
		book.setEditor(editor);
		book.setCollection(collection);
		book.setPages(pages);
		book.setLanguage(language);

		List<Comment> comments = new ArrayList<Comment>();
		
		Comment acomment = new Comment();

		acomment.setAuthor(commentAuthor);
		acomment.setAboutAuthor(aboutAuthor);
		acomment.setAboutGenre(aboutGenre);
		acomment.setAboutCadre(aboutCadre);
		acomment.setAboutCharacters(aboutCharacters);
		acomment.setResume(resume);
		acomment.setExtrait(extrait);
		acomment.setAppreciation(appreciation);
		acomment.setIsCompleted(false);
		
		comments.add(acomment);
		
		Gson gson = new GsonBuilder().create();

		NewFichePayload fiche = new NewFichePayload();
		
		fiche.setId(1);
		fiche.setBook(book);
		fiche.setComments(comments);
		String json = gson.toJson(fiche);

		slf4jLogger.info(json);

		ObjectMapper mapper = new ObjectMapper();

		NewFichePayload payload;

		try {
			payload = mapper.readValue(json, NewFichePayload.class);
			if (!payload.isValid()) {
				slf4jLogger.info("Invalid body object");
			} else {

				slf4jLogger.info(payload.toString());
				result = true;
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertTrue(result);

	}
	
	@Test
	public void ficheUpdateDatesTest() { 
		
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

		NewFichePayload fiche = new NewFichePayload();

		Book book = new Book();

		book.setTitle(title);
		book.setSubTitle(subTitle);
		book.setAuthor(author);
		book.setYearPub(yearPub);
		book.setEditor(editor);
		book.setCollection(collection);
		book.setPages(pages);
		book.setLanguage(language);

		List<Comment> comments = new ArrayList<Comment>();
		
		Comment acomment = new Comment();
		acomment.setAuthor(commentAuthor);
		acomment.setIsCompleted(false);
		comments.add(acomment);
		
		fiche.setId(1);
		fiche.setBook(book);
		fiche.setComments(comments);
		
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(fiche);
		slf4jLogger.info(json);

		UUID id = model.addFiche(fiche.getId(), fiche.getBook(), fiche.getComments());

		slf4jLogger.info(id.toString() + " * " + String.valueOf(model.existFiche(id)) );	
		slf4jLogger.info( gson.toJson(model.getFiche(0, id).getComments().get(0)) );
		
		NewFichePayload updatedFiche = new NewFichePayload();

		List<Comment> bComments = new ArrayList<Comment>();
		
		Comment bcomment = new Comment();
		bcomment.setAuthor("Edgar Osorio");
		bcomment.setAboutCharacters("Great characters, full of personality");
		bcomment.setIsCompleted(true);
		bComments.add(bcomment);
		
		book.setBook_uuid(id);
		book.setPages(2001);
		
		updatedFiche.setId(1);	
		updatedFiche.setBook(book);
		updatedFiche.setComments(bComments);
				
		boolean test = model.updateFiche(updatedFiche);
		
		Fiche fiche2 = model.getFiche(0, id);

		slf4jLogger.info( gson.toJson(fiche2.getComments().get(0)) );
		slf4jLogger.info("updated fiche: " + fiche2.getBook().getPages() + " - " + fiche2.getComments().size());
		
		assertTrue(test);

		model.deleteFiche(id);
		
		
		
	}
	
}
