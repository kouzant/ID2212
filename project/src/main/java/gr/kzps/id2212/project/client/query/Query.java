package gr.kzps.id2212.project.client.query;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Query implements Serializable {
	private static final long serialVersionUID = 3344487334305397999L;

	private final QueryParameter<String> author;
	private final QueryParameter<Date> creationDate;
	private final QueryParameter<List<String>> keywords;
	private final QueryParameter<String> title;
	
	public Query(QueryParameter<String> author, QueryParameter<Date> creationDate,
			QueryParameter<List<String>> keywords, QueryParameter<String> title) {
		this.author = author;
		this.creationDate = creationDate;
		this.keywords = keywords;
		this.title = title;
	}
	
	public QueryParameter<String> getAuthor() {
		return author;
	}
	
	public QueryParameter<Date> creationDate() {
		return creationDate;
	}
	
	public QueryParameter<List<String>> getKeywords() {
		return keywords;
	}
	
	public QueryParameter<String> getTitle() {
		return title;
	}
}
