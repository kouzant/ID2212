package userplan;

import gr.kzps.id2212.project.client.query.QueryPlan;

public class UserQueryPlan implements QueryPlan {

	private final String title;
	
	public UserQueryPlan() {
		// TODO Auto-generated constructor stub
		title = "Some title";
	}
	
	@Override
	public String getTitle() {
		return title;
	}

}
