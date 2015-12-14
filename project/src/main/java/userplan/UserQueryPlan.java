package userplan;

import java.util.ArrayList;
import java.util.List;

import gr.kzps.id2212.project.client.query.DateParameter;
import gr.kzps.id2212.project.client.query.KeywordsParameter;
import gr.kzps.id2212.project.client.query.QueryParameter;
import gr.kzps.id2212.project.client.query.QueryPlan;
import gr.kzps.id2212.project.client.query.parameterOperators.DateOperators;
import gr.kzps.id2212.project.client.query.parameterOperators.ParameterSwitch;

public class UserQueryPlan implements QueryPlan {
	private final QueryParameter<String> title;
	private final QueryParameter<String> author;
	private final KeywordsParameter<List<String>> keywords;
	private final DateParameter<String> date;
	
	public UserQueryPlan() {
		title = new QueryParameter<>("Some title", ParameterSwitch.OFF);
		author = new QueryParameter<>("Antonis Kouzoupis", ParameterSwitch.OFF);
		List<String> keys = new ArrayList<>();
		keys.add("YARN");
		keys.add("Cloud");
		keywords = new KeywordsParameter<List<String>>(keys, ParameterSwitch.OFF, 2);
		
		date = new DateParameter<String>("2015-12-14T", ParameterSwitch.ON, DateOperators.BEFORE);
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
	public KeywordsParameter<List<String>> getKeywords() {
		return keywords;
	}

	@Override
	public DateParameter<String> getDate() {
		return date;
	}
	
}
