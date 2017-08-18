/**
 * 
 */
package co.phystech.aosorio.models;

import java.util.List;

/**
 * @author AOSORIO
 *
 */
public class NewFichePayload implements IValidable {

	int id;
	Book book;
	List<Comment> comments;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @param book
	 *            the book to set
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
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
