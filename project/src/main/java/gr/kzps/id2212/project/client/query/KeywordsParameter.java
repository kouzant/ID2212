package gr.kzps.id2212.project.client.query;

import gr.kzps.id2212.project.client.query.parameterOperators.ParameterSwitch;

public class KeywordsParameter<T> extends QueryParameter<T> {
	private static final long serialVersionUID = 8492103208225171199L;
	private final Integer successThreshold;
	
	public KeywordsParameter(T keywords, ParameterSwitch parameterSwitch,
			Integer successThreshold) {
		super(keywords, parameterSwitch);
		this.successThreshold = successThreshold;
	}

	public Integer getSuccessThreshold() {
		return successThreshold;
	}
	
	@Override
	public String export() {
		if (getParameterSwitch().equals(ParameterSwitch.OFF)) {
			return "Inactive field";
		}
		
		return getParameter().toString() + " Success threshold: " + successThreshold;
	}
}
