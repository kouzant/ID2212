package gr.kzps.id2212.project.client.query;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Query used to match files metadata by the agent
 * @author Antonis Kouzoupis
 *
 */
public class Query implements Serializable {
	private static final long serialVersionUID = 3344487334305397999L;

	private final QueryParameter<String> author;
	private final KeywordsParameter<List<String>> keywords;
	private final QueryParameter<String> title;
	private final DateParameter<Date> date;
	
	public Query(QueryParameter<String> author, KeywordsParameter<List<String>> keywords,
			QueryParameter<String> title, DateParameter<Date> date) {
		this.author = author;
		this.keywords = keywords;
		this.title = title;
		this.date = date;
	}
	
	public QueryParameter<String> getAuthor() {
		return author;
	}
	
	public KeywordsParameter<List<String>> getKeywords() {
		return keywords;
	}
	
	public QueryParameter<String> getTitle() {
		return title;
	}
	
	public DateParameter<Date> getDate() {
		return date;
	}
}
