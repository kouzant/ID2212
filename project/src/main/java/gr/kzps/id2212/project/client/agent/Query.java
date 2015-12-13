package gr.kzps.id2212.project.client.agent;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Query implements Serializable {
	private static final long serialVersionUID = 3344487334305397999L;

	private final String author;
	private final Date creationDate;
	private final List<String> keywords;
	private final String title;
	
	public Query(String author, Date creationDate, List<String> keywords,
			String title) {
		this.author = author;
		this.creationDate = creationDate;
		this.keywords = keywords;
		this.title = title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public Date creationDate() {
		return creationDate;
	}
	
	public List<String> getKeywords() {
		return keywords;
	}
	
	public String getTitle() {
		return title;
	}
}
