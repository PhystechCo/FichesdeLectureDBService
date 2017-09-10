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
	private String translation;
	private String author_nationality;
	private String author_period;	
	private String optional_one; 
	private String optional_two;
	
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


	/**
	 * @return the translation
	 */
	public String getTranslation() {
		return translation;
	}


	/**
	 * @param translation the translation to set
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


	/**
	 * @return the author_nationality
	 */
	public String getAuthor_nationality() {
		return author_nationality;
	}


	/**
	 * @param author_nationality the author_nationality to set
	 */
	public void setAuthor_nationality(String author_nationality) {
		this.author_nationality = author_nationality;
	}


	/**
	 * @return the author_period
	 */
	public String getAuthor_period() {
		return author_period;
	}


	/**
	 * @param author_period the author_period to set
	 */
	public void setAuthor_period(String author_period) {
		this.author_period = author_period;
	}



	
	

}
