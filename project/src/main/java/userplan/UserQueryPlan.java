package userplan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gr.kzps.id2212.project.client.query.ParameterSwitch;
import gr.kzps.id2212.project.client.query.QueryParameter;
import gr.kzps.id2212.project.client.query.QueryPlan;

public class UserQueryPlan implements QueryPlan {
	private final QueryParameter<String> title;
	private final QueryParameter<String> author;
	private final QueryParameter<List<String>> keywords;
	private final QueryParameter<Date> date;
	
	public UserQueryPlan() {
		title = new QueryParameter<>("Some title", ParameterSwitch.ON);
		author = new QueryParameter<>("Antonis Kouzoupis", ParameterSwitch.ON);
		List<String> keys = new ArrayList<>();
		keys.add("YARN");
		keys.add("Cloud");
		keywords = new QueryParameter<List<String>>(keys, ParameterSwitch.ON);
		date = new QueryParameter<Date>(new Date(), ParameterSwitch.OFF);
	}

	@Override
	public QueryParameter<String> getTitle() {
		return title;
	}

	@Override
	public QueryParameter<String> getAuthor() {
		return author;
	}

	@Override
	public QueryParameter<List<String>> getKeywords() {
		return keywords;
	}

	@Override
	public QueryParameter<Date> getDate() {
		return date;
	}
	
}
