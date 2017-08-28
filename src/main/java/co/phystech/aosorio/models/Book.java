/**
 * 
 */
package co.phystech.aosorio.models;

import java.util.UUID;

/**
 * @author AOSORIO
 *
 */
public class Book {

	UUID book_uuid;
	private String title;
	private String subTitle;
	private String author;
	private int yearPub;
	private String editor;
	private String collection;
	private int pages;
	private String language;
	private String translation;

	private String optional_one; 
	private String optional_two;
	
	public UUID getBook_uuid() {
		return book_uuid;
	}

	public void setBook_uuid(UUID post_uuid) {
		this.book_uuid = post_uuid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getYearPub() {
		return yearPub;
	}

	public void setYearPub(int yearPub) {
		this.yearPub = yearPub;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the translation
	 */
	public String getTranslation() {
		return translation;
	}

	/**
	 * @param translation
	 *            the translation to set
	 */
	public void setTranslation(String translation) {
		this.translation = translation;
	}

	/**
	 * @return the optional_one
	 */
	public String getOptional_one() {
		return optional_one;
	}

	/**
	 * @param optional_one the optional_one to set
	 */
	public void setOptional_one(String optional_one) {
		this.optional_one = optional_one;
	}

	/**
	 * @return the optional_two
	 */
	public String getOptional_two() {
		return optional_two;
	}

	/**
	 * @param optional_two the optional_two to set
	 */
	public void setOptional_two(String optional_two) {
		this.optional_two = optional_two;
	}

}
