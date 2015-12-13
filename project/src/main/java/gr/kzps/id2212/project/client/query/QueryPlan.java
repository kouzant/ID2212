package gr.kzps.id2212.project.client.query;

import java.util.List;

public interface QueryPlan {
	public QueryParameter<String> getTitle();
	public QueryParameter<String> getAuthor();
	public QueryParameter<List<String>> getKeywords();
	// Date should be in the form yyyy-MM-ddT
	public DateParameter<String> getDate();
}
