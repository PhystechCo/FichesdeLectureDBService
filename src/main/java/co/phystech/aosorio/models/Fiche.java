/**
 * 
 */
package co.phystech.aosorio.models;

import java.util.List;
import java.util.UUID;

/**
 * @author AOSORIO
 *
 */
public class Fiche {
	
	UUID fiche_uuid;
	int id;
	Book book;
	List<Comment> comments;
	/**
	 * @return the fiche_uuid
	 */
	public UUID getFiche_uuid() {
		return fiche_uuid;
	}
	/**
	 * @param fiche_uuid the fiche_uuid to set
	 */
	public void setFiche_uuid(UUID fiche_uuid) {
		this.fiche_uuid = fiche_uuid;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the book
	 */
	public Book getBook() {
		return book;
	}
	/**
	 * @param book the book to set
	 */
	public void setBook(Book book) {
		this.book = book;
	}
	/**
	 * @return the comments
	 */
	public List<Comment> getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	

}
