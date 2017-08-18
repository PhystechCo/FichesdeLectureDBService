/**
 * 
 */
package co.phystech.aosorio.models;

/**
 * @author AOSORIO
 *
 */
public class NewBookPayload implements IValidable {


	private String title;
	private String subTitle;
	private String author;
	private int yearPub;
	private String editor;
	private String collection;
	private int pages;
	private String language;
	
	@Override
	public boolean isValid() {
		return true;
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


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NewBookPayload [title=" + title + ", subTitle=" + subTitle + ", author=" + author + ", yearPub="
				+ yearPub + ", editor=" + editor + ", collection=" + collection + ", pages=" + pages + ", language="
				+ language + "]";
	}
	
	

}
