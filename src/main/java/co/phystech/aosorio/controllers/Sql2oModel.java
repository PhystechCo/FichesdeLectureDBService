/**
 * 
 */
package co.phystech.aosorio.controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
				book.getOptional_one());

		Iterator<Comment> commentItr = comments.iterator();

		while (commentItr.hasNext()) {

			Comment current = commentItr.next();
			UUID commentUuid = addComment(bookUuid, current.getAuthor(), current.getAboutAuthor(),
					current.getAboutGenre(), current.getAboutCadre(), current.getAboutCharacters(), current.getResume(),
					current.getExtrait(), current.getAppreciation(), current.getIsCompleted(),
					current.getOptional_one(), current.getOptional_two());

			slf4jLogger.debug("Added comment with UUID: " + commentUuid.toString());
		}

		return bookUuid;

	}

	@Override
	public UUID addBook(String title, String subTitle, String author, int yearPub, String editor, String collection,
			int pages, String language, String translation, String optionalOne) {

		try (Connection conn = sql2o.beginTransaction()) {
			UUID postUuid = uuidGenerator.generate();
			conn.createQuery(
					"insert into books(book_uuid, title, subtitle, author, yearpub, editor, collection, pages, language, translation, optional_one) VALUES (:book_uuid, :title, :subtitle, :author, :yearpub, :editor, :collection, :pages, :language, :translation, :optional_one)")
					.addParameter("book_uuid", postUuid).addParameter("title", title).addParameter("subtitle", subTitle)
					.addParameter("author", author).addParameter("yearpub", yearPub).addParameter("editor", editor)
					.addParameter("collection", collection).addParameter("pages", pages)
					.addParameter("language", language).addParameter("translation", translation)
					.addParameter("optional_one", optionalOne).executeUpdate();
			conn.commit();
			return postUuid;
		}

	}

	@Override
	public UUID addComment(UUID bookUuid, String author, String aboutAuthor, String aboutGenre, String aboutCadre,
			String aboutCharacters, String resume, String extrait, String appreciation, boolean isCompleted,
			String optionalOne, String optionalTwo) {

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
					"insert into comments(comment_uuid, book_uuid, author, aboutauthor, aboutgenre, aboutcadre, aboutcharacters, resume, extrait, appreciation, optional_one, optional_two, submission_date, iscompleted, completion_date) VALUES (:comment_uuid, :book_uuid, :author, :aboutauthor, :aboutgenre, :aboutcadre, :aboutcharacters, :resume, :extrait, :appreciation, :optional_one, :optional_two, :submission_date, :iscompleted, :completion_date)")
					.addParameter("comment_uuid", commentUuid).addParameter("book_uuid", bookUuid)
					.addParameter("author", author).addParameter("aboutauthor", aboutAuthor)
					.addParameter("aboutgenre", aboutGenre).addParameter("aboutcadre", aboutCadre)
					.addParameter("aboutcharacters", aboutCharacters).addParameter("resume", resume)
					.addParameter("extrait", extrait).addParameter("appreciation", appreciation)
					.addParameter("optional_one", optionalOne).addParameter("optional_two", optionalTwo)
					.addParameter("submission_date", timeStampNew).addParameter("iscompleted", isCompleted)
					.addParameter("completion_date", timeStampComplete).executeUpdate();
			return commentUuid;
		}

	}

	public UUID updateComment(UUID bookUuid, String author, String aboutAuthor, String aboutGenre, String aboutCadre,
			String aboutCharacters, String resume, String extrait, String appreciation, boolean isCompleted,
			Timestamp submitted_date, String optionalOne, String optionalTwo) {

		try (Connection conn = sql2o.open()) {
			UUID commentUuid = uuidGenerator.generate();

			Timestamp timeStampComplete = new Timestamp(System.currentTimeMillis());

			if (!isCompleted) {
				timeStampComplete = new Timestamp(0);
			}

			conn.createQuery(
					"insert into comments(comment_uuid, book_uuid, author, aboutauthor, aboutgenre, aboutcadre, aboutcharacters, resume, extrait, appreciation, optional_one, optional_two, submission_date, iscompleted, completion_date) VALUES (:comment_uuid, :book_uuid, :author, :aboutauthor, :aboutgenre, :aboutcadre, :aboutcharacters, :resume, :extrait, :appreciation, :optional_one, :optional_two, :submission_date, :iscompleted, :completion_date)")
					.addParameter("comment_uuid", commentUuid).addParameter("book_uuid", bookUuid)
					.addParameter("author", author).addParameter("aboutauthor", aboutAuthor)
					.addParameter("aboutgenre", aboutGenre).addParameter("aboutcadre", aboutCadre)
					.addParameter("aboutcharacters", aboutCharacters).addParameter("resume", resume)
					.addParameter("extrait", extrait).addParameter("appreciation", appreciation)
					.addParameter("optional_one", optionalOne).addParameter("optional_two", optionalTwo)
					.addParameter("submission_date", submitted_date).addParameter("iscompleted", isCompleted)
					.addParameter("completion_date", timeStampComplete).executeUpdate();
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
					"update books set title=:title, subtitle=:subtitle, author=:author, yearpub=:yearpub, editor=:editor, collection=:collection, pages=:pages, language=:language, translation=:translation, optional_one=:optional_one where book_uuid=:book_uuid")
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
					.addParameter("optional_one", fiche.getBook().getOptional_one()).executeUpdate();

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
							comment.getAboutGenre(), comment.getAboutGenre(), comment.getAboutCharacters(),
							comment.getResume(), comment.getExtrait(), comment.getAppreciation(),
							comment.getIsCompleted(), comment.getOptional_one(), comment.getOptional_two());
				} else {
					slf4jLogger.info("we have to update a comment !!!");
					updateComment(fiche.getBook().getBook_uuid(), comment.getAuthor(), comment.getAboutAuthor(),
							comment.getAboutGenre(), comment.getAboutGenre(), comment.getAboutCharacters(),
							comment.getResume(), comment.getExtrait(), comment.getAppreciation(),
							comment.getIsCompleted(), comment.getSubmission_date(), comment.getOptional_one(),
							comment.getOptional_two());
				}

			}

			slf4jLogger.info("updated comments success");

		} catch (Exception ex) {
			ex.printStackTrace();
			slf4jLogger.info("Problem with DB connection");
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

				NewFichePayload newFiche = mapper.readValue(ficheData.get(i).toString(), NewFichePayload.class);

				if (!newFiche.isValid()) {
					slf4jLogger.info("Invalid body object");
					continue;
				}

				UUID id = addFiche(newFiche.getId(), newFiche.getBook(), newFiche.getComments());

				slf4jLogger.info("New fiche added: " + String.valueOf(id));
				nfiches++;
			}

		} catch (IOException e) {
			throw e;
		}

		return nfiches;

	}

}
