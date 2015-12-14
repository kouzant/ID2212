package gr.kzps.id2212.project.client.query;

import java.util.List;

public interface QueryPlan {
	public QueryParameter<String> getTitle();
	public QueryParameter<String> getAuthor();
	public KeywordsParameter<List<String>> getKeywords();
	// Date should be in the form "yyyy-MM-ddT" unless if
	// operation is OFF where date can be empty string or
	// in any other format
	public DateParameter<String> getDate();
}
