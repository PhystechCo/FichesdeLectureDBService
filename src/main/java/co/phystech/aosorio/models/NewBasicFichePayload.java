/**
 * 
 */
package co.phystech.aosorio.models;


/**
 * @author AOSORIO
 *
 */
public class NewBasicFichePayload implements IValidable {

	int id;
	Book book;

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


	@Override
	public boolean isValid() {
		return true;
	}

}
