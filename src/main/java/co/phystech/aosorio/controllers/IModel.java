/**
 * 
 */
package co.phystech.aosorio.controllers;

import java.util.List;
import java.util.UUID;

import co.phystech.aosorio.models.Book;
import co.phystech.aosorio.models.Comment;
import co.phystech.aosorio.models.Fiche;
import co.phystech.aosorio.models.NewFichePayload;

/**
 * @author AOSORIO
 *
 */
public interface IModel {

	
	UUID addFiche(int id, Book book, List<Comment> comments);
	
	UUID addBook(String title, 
			String subTitle, 
			String author, 
			int yearPub, 
			String editor, 
			String collection, 
			int pages,
			String language,
			String translation,
			String optionalOne);

	UUID addComment(UUID bookUuid, 
			String author, 
			String aboutAuthor, 
			String aboutGenre, 
			String aboutCadre, 
			String aboutCharacters, 
			String resume, 
			String extrait,
			String appreciation,
			boolean isCompleted,
			String optionalOne,
			String optionalTwo);
	
	Fiche getFiche(int id, UUID uuid);
	
	List<Fiche> getAllFiches();
	
	List<Book> getAllBooks();

	List<Comment> getAllComments();
	
	List<Comment> getAllCommentsOn(UUID book);
	
	boolean updateFiche(NewFichePayload fiche);
	
	boolean existFiche(UUID book);
	
	boolean existBook(UUID book);
	
	boolean existComment(UUID comment);
	
	boolean deleteAll();
	
	boolean deleteFiche(UUID uuid);
	
	boolean deleteBook(UUID uuid);
	
	boolean deleteComment(UUID uuid);
	
	boolean deleteComments(UUID uuid);

}
