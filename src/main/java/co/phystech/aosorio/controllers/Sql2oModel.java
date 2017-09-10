/**
 * 
 */
package co.phystech.aosorio.controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;

import co.phystech.aosorio.models.Book;
import co.phystech.aosorio.models.Comment;
import co.phystech.aosorio.models.Fiche;
import co.phystech.aosorio.models.NewFichePayload;
import co.phystech.aosorio.services.CSVReader;
import co.phystech.aosorio.services.IUuidGenerator;
import co.phystech.aosorio.services.RandomUuidGenerator;

/**
 * @author AOSORIO
 *
 */
public class Sql2oModel implements IModel {

	private final static Logger slf4jLogger = LoggerFactory.getLogger(Sql2oModel.class);

	private Sql2o sql2o;
	private IUuidGenerator uuidGenerator;

	public Sql2oModel(Sql2o sql2o) {
		this.sql2o = sql2o;
		uuidGenerator = new RandomUuidGenerator();
	}

	@Override
	public UUID addFiche(int id, Book book, List<Comment> comments) {

		UUID bookUuid = addBook(book.getTitle(), book.getSubTitle(), book.getAuthor(), book.getYearPub(),
				book.getEditor(), book.getCollection(), book.getPages(), book.getLanguage(), book.getTranslation(),
				book.getOptional_one(), book.getAuthor_nationality(), book.getAuthor_period());

		Iterator<Comment> commentItr = comments.iterator();

		while (commentItr.hasNext()) {

			Comment current = commentItr.next();
			UUID commentUuid = addComment(bookUuid, current.getAuthor(), current.getAboutAuthor(),
					current.getAboutGenre(), current.getAboutCadre(), current.getAboutCharacters(), current.getResume(),
					current.getExtrait(), current.getAppreciation(), current.getIsCompleted(),
					current.getOptional_one(), current.getOptional_two(), current.getComment_text(),
					current.getOther_details());

			slf4jLogger.debug("Added comment with UUID: " + commentUuid.toString());
		}

		return bookUuid;

	}

	@Override
	public UUID addBook(String title, String subTitle, String author, int yearPub, String editor, String collection,
			int pages, String language, String translation, String optionalOne, String nationality, String period) {

		try (Connection conn = sql2o.beginTransaction()) {
			UUID postUuid = uuidGenerator.generate();
			conn.createQuery(
					"insert into books(book_uuid, title, subtitle, author, yearpub, editor, collection, pages, language, translation, optional_one, author_nationality, author_period) VALUES (:book_uuid, :title, :subtitle, :author, :yearpub, :editor, :collection, :pages, :language, :translation, :optional_one, :author_nationality, :author_period)")
					.addParameter("book_uuid", postUuid).addParameter("title", title).addParameter("subtitle", subTitle)
					.addParameter("author", author)
					.addParameter("yearpub", yearPub)
					.addParameter("editor", editor)
					.addParameter("collection", collection)
					.addParameter("pages", pages)
					.addParameter("language", language)
					.addParameter("translation", translation)
					.addParameter("optional_one", optionalOne)
					.addParameter("author_nationality", nationality)
					.addParameter("author_period", period)
					.executeUpdate();
			conn.commit();
			return postUuid;
		}

	}

	@Override
	public UUID addComment(UUID bookUuid, String author, String aboutAuthor, String aboutGenre, String aboutCadre,
			String aboutCharacters, String resume, String extrait, String appreciation, boolean isCompleted,
			String optionalOne, String optionalTwo, String commentText, String otherDetails) {

		try (Connection conn = sql2o.open()) {
			UUID commentUuid = uuidGenerator.generate();

			Timestamp timeStampNew = new Timestamp(System.currentTimeMillis());
			Timestamp timeStampComplete;

			if (isCompleted) {
				timeStampComplete = timeStampNew;
			} else {
				timeStampComplete = new Timestamp(0);
			}
			conn.createQuery(
					"insert into comments(comment_uuid, book_uuid, author, aboutauthor, aboutgenre, aboutcadre, aboutcharacters, resume, extrait, appreciation, optional_one, optional_two, submission_date, iscompleted, completion_date, comment_text, other_details) VALUES (:comment_uuid, :book_uuid, :author, :aboutauthor, :aboutgenre, :aboutcadre, :aboutcharacters, :resume, :extrait, :appreciation, :optional_one, :optional_two, :submission_date, :iscompleted, :completion_date, :comment_text, :other_details)")
					.addParameter("comment_uuid", commentUuid).addParameter("book_uuid", bookUuid)
					.addParameter("author", author)
					.addParameter("aboutauthor", aboutAuthor)
					.addParameter("aboutgenre", aboutGenre)
					.addParameter("aboutcadre", aboutCadre)
					.addParameter("aboutcharacters", aboutCharacters)
					.addParameter("resume", resume)
					.addParameter("extrait", extrait)
					.addParameter("appreciation", appreciation)
					.addParameter("optional_one", optionalOne)
					.addParameter("optional_two", optionalTwo)
					.addParameter("submission_date", timeStampNew)
					.addParameter("iscompleted", isCompleted)
					.addParameter("completion_date", timeStampComplete)
					.addParameter("comment_text", commentText)
					.addParameter("other_details", otherDetails)
					.executeUpdate();
			return commentUuid;
		}

	}

	public UUID updateComment(UUID bookUuid, String author, String aboutAuthor, String aboutGenre, String aboutCadre,
			String aboutCharacters, String resume, String extrait, String appreciation, boolean isCompleted,
			Timestamp submitted_date, String optionalOne, String optionalTwo, String commentText, String otherDetails) {

		try (Connection conn = sql2o.open()) {
			UUID commentUuid = uuidGenerator.generate();

			Timestamp timeStampComplete = new Timestamp(System.currentTimeMillis());

			if (!isCompleted) {
				timeStampComplete = new Timestamp(0);
			}

			conn.createQuery(
					"insert into comments(comment_uuid, book_uuid, author, aboutauthor, aboutgenre, aboutcadre, aboutcharacters, resume, extrait, appreciation, optional_one, optional_two, submission_date, iscompleted, completion_date, comment_text, other_details) VALUES (:comment_uuid, :book_uuid, :author, :aboutauthor, :aboutgenre, :aboutcadre, :aboutcharacters, :resume, :extrait, :appreciation, :optional_one, :optional_two, :submission_date, :iscompleted, :completion_date, :comment_text, :other_details)")
					.addParameter("comment_uuid", commentUuid)
					.addParameter("book_uuid", bookUuid)
					.addParameter("author", author)
					.addParameter("aboutauthor", aboutAuthor)
					.addParameter("aboutgenre", aboutGenre)
					.addParameter("aboutcadre", aboutCadre)
					.addParameter("aboutcharacters", aboutCharacters)
					.addParameter("resume", resume)
					.addParameter("extrait", extrait)
					.addParameter("appreciation", appreciation)
					.addParameter("optional_one", optionalOne)
					.addParameter("optional_two", optionalTwo)
					.addParameter("submission_date", submitted_date)
					.addParameter("iscompleted", isCompleted)
					.addParameter("completion_date", timeStampComplete)
					.addParameter("comment_text", commentText)
					.addParameter("other_details", otherDetails)
					.executeUpdate();
			return commentUuid;
		}

	}

	@Override
	public List<Fiche> getAllFiches() {

		List<Fiche> fiches = new ArrayList<Fiche>();

		try (Connection conn = sql2o.open()) {

			List<Book> books = conn.createQuery("select * from books order by book_uuid").executeAndFetch(Book.class);
			Iterator<Book> bookItr = books.iterator();
			int id = 1;
			while (bookItr.hasNext()) {
				Book currentBook = bookItr.next();
				Fiche currentFiche = new Fiche();
				currentFiche.setFiche_uuid(currentBook.getBook_uuid());
				currentFiche.setId(id);
				currentFiche.setBook(currentBook);
				currentFiche.setComments(getAllCommentsOn(currentBook.getBook_uuid()));
				fiches.add(currentFiche);
				id += 1;
			}

			return fiches;

		}

	}

	@Override
	public List<Book> getAllBooks() {
		try (Connection conn = sql2o.open()) {
			List<Book> books = conn.createQuery("select * from books").executeAndFetch(Book.class);
			return books;
		}
	}

	@Override
	public List<Comment> getAllCommentsOn(UUID book) {
		try (Connection conn = sql2o.open()) {
			return conn.createQuery("select * from comments where book_uuid=:book_uuid order by submission_date")
					.addParameter("book_uuid", book).executeAndFetch(Comment.class);
		}
	}

	@Override
	public boolean existFiche(UUID book) {
		try (Connection conn = sql2o.open()) {
			List<Book> books = conn.createQuery("select * from books where book_uuid=:book_uuid")
					.addParameter("book_uuid", book).executeAndFetch(Book.class);
			return books.size() > 0;
		}
	}

	@Override
	public boolean existBook(UUID book) {
		try (Connection conn = sql2o.open()) {
			List<Book> books = conn.createQuery("select * from books where book_uuid=:book_uuid")
					.addParameter("book_uuid", book).executeAndFetch(Book.class);
			return books.size() > 0;
		}
	}

	@Override
	public boolean existComment(UUID comment) {
		try (Connection conn = sql2o.open()) {
			List<Comment> comments = conn.createQuery("select * from comments where comment_uuid=:comment_uuid")
					.addParameter("comment_uuid", comment).executeAndFetch(Comment.class);
			return comments.size() > 0;
		}
	}

	@Override
	public List<Comment> getAllComments() {
		try (Connection conn = sql2o.open()) {
			List<Comment> books = conn.createQuery("select * from comments").executeAndFetch(Comment.class);
			return books;
		}
	}

	@Override
	public boolean deleteAll() {
		try (Connection conn = sql2o.open()) {
			conn.createQuery("delete from comments").executeUpdate();
			conn.createQuery("delete from books").executeUpdate();
			return true;
		}
	}

	@Override
	public boolean deleteFiche(UUID uuid) {
		try (Connection conn = sql2o.open()) {
			conn.createQuery("delete from comments where book_uuid=:book_uuid").addParameter("book_uuid", uuid)
					.executeUpdate();
			conn.createQuery("delete from books where book_uuid=:book_uuid").addParameter("book_uuid", uuid)
					.executeUpdate();
			return true;
		}
	}

	@Override
	public boolean deleteComments(UUID uuid) {
		try (Connection conn = sql2o.open()) {
			conn.createQuery("delete from comments where book_uuid=:book_uuid").addParameter("book_uuid", uuid)
					.executeUpdate();
			return true;
		}
	}

	@Override
	public boolean deleteBook(UUID uuid) {
		try (Connection conn = sql2o.open()) {
			conn.createQuery("delete from books where book_uuid=:book_uuid").addParameter("book_uuid", uuid)
					.executeUpdate();
			return true;
		}
	}

	@Override
	public boolean deleteComment(UUID uuid) {
		try (Connection conn = sql2o.open()) {
			conn.createQuery("delete from comments where comment_uuid=:comment_uuid").addParameter("comment_uuid", uuid)
					.executeUpdate();
			return true;
		}
	}

	@Override
	public Fiche getFiche(int id, UUID uuid) {

		try (Connection conn = sql2o.open()) {
			List<Book> bookSearch = conn.createQuery("select * from books where book_uuid=:book_uuid")
					.addParameter("book_uuid", uuid).executeAndFetch(Book.class);

			Fiche currentFiche = new Fiche();
			currentFiche.setId(id);
			currentFiche.setFiche_uuid(bookSearch.get(0).getBook_uuid());
			currentFiche.setBook(bookSearch.get(0));
			currentFiche.setComments(getAllCommentsOn(uuid));

			return currentFiche;

		}

	}

	@Override
	public boolean updateFiche(NewFichePayload fiche) {

		try (Connection conn = sql2o.open()) {
			conn.createQuery(
					"update books set title=:title, subtitle=:subtitle, author=:author, yearpub=:yearpub, editor=:editor, collection=:collection, pages=:pages, language=:language, translation=:translation, optional_one=:optional_one, author_nationality=:author_nationality, author_period=:author_period where book_uuid=:book_uuid")
					.addParameter("book_uuid", fiche.getBook().getBook_uuid())
					.addParameter("title", fiche.getBook().getTitle())
					.addParameter("subtitle", fiche.getBook().getSubTitle())
					.addParameter("author", fiche.getBook().getAuthor())
					.addParameter("yearpub", fiche.getBook().getYearPub())
					.addParameter("editor", fiche.getBook().getEditor())
					.addParameter("collection", fiche.getBook().getCollection())
					.addParameter("pages", fiche.getBook().getPages())
					.addParameter("language", fiche.getBook().getLanguage())
					.addParameter("translation", fiche.getBook().getTranslation())
					.addParameter("optional_one", fiche.getBook().getOptional_one())
					.addParameter("author_nationality", fiche.getBook().getAuthor_nationality())
					.addParameter("author_period", fiche.getBook().getAuthor_period())
					.executeUpdate();

			slf4jLogger.info("updated book");

			boolean result = deleteComments(fiche.getBook().getBook_uuid());

			if (result) {
				slf4jLogger.info("deleted comments success");
			}

			Iterator<Comment> itrComment = fiche.getComments().iterator();

			while (itrComment.hasNext()) {
				Comment comment = itrComment.next();

				if (comment.getSubmission_date() == null) {
					slf4jLogger.info("updateFiche> we have a new comment!!!");
					addComment(fiche.getBook().getBook_uuid(), comment.getAuthor(), comment.getAboutAuthor(),
							comment.getAboutGenre(), 
							comment.getAboutGenre(), 
							comment.getAboutCharacters(),
							comment.getResume(), 
							comment.getExtrait(), 
							comment.getAppreciation(),
							comment.getIsCompleted(), 
							comment.getOptional_one(), 
							comment.getOptional_two(),
							comment.getComment_text(), 
							comment.getOther_details());
				} else {
					slf4jLogger.info("we have to update a comment !!!");
					updateComment(fiche.getBook().getBook_uuid(), 
							comment.getAuthor(), 
							comment.getAboutAuthor(),
							comment.getAboutGenre(), 
							comment.getAboutGenre(), 
							comment.getAboutCharacters(),
							comment.getResume(), 
							comment.getExtrait(), 
							comment.getAppreciation(),
							comment.getIsCompleted(), 
							comment.getSubmission_date(), 
							comment.getOptional_one(),
							comment.getOptional_two(),
							comment.getComment_text(), 
							comment.getOther_details());
				}

			}

			slf4jLogger.info("updateFiche> updated comments success");

		} catch (Exception ex) {			
			slf4jLogger.info("updateFiche> Cannot update fiche - exception caught:");
			slf4jLogger.info(ex.getLocalizedMessage());
		}

		return true;
	}

	@Override
	public int createFichesFromCSV(String fileName) throws IOException {

		int nfiches = 0;
		CSVReader reader = new CSVReader(fileName);

		try {

			ObjectMapper mapper = new ObjectMapper();

			JsonArray ficheData = (JsonArray) reader.readFile();

			for (int i = 0; i < ficheData.size(); i++) {

				UUID id;

				NewFichePayload newFiche = mapper.readValue(ficheData.get(i).toString(), NewFichePayload.class);

				if (!newFiche.isValid()) {
					slf4jLogger.info("Invalid body object");
					continue;
				}

				if (!existFiche(newFiche)) {
					id = addFiche(newFiche.getId(), newFiche.getBook(), newFiche.getComments());
					slf4jLogger.info("New fiche added: " + String.valueOf(id));
					nfiches++;
				} else {
					id = new UUID(0, 0);
					slf4jLogger.info("Fiche already exist - not added");
				}

			}

		} catch (IOException e) {
			throw e;
		}

		return nfiches;

	}

	@Override
	public boolean existFiche(NewFichePayload fiche) {

		Book book = fiche.getBook();

		Map<String, String> items = new HashMap<String, String>();

		items.put("title", book.getTitle());
		items.put("author", book.getAuthor());
		items.put("yearpub", String.valueOf(book.getYearPub()));
		items.put("editor", book.getEditor());

		StringBuilder query = new StringBuilder("select * from books where ");

		items.values().removeIf(Objects::isNull);

		List<String> list = new ArrayList<String>();

		items.forEach((key, value) -> {
			list.add(" " + key + " = " + "\'" + value + "\' ");
		});

		query.append(String.join("and", list));

		slf4jLogger.info(query.toString());

		try (Connection conn = sql2o.open()) {
			List<Book> results = conn.createQuery(query.toString()).executeAndFetch(Book.class);

			if (results.size() > 0)
				return true;
		}

		return false;
	}

}
