package gr.kzps.id2212.project.client.query;

import gr.kzps.id2212.project.client.query.parameterOperators.ParameterSwitch;

/**
 * Query parameter for keywords field
 * @author Antonis Kouzoupis
 *
 * @param <T>
 */
public class KeywordsParameter<T> extends QueryParameter<T> {
	private static final long serialVersionUID = 8492103208225171199L;
	private final Integer successThreshold;
	
	/**
	 * @param keywords Keywords that should exist in the metadata
	 * @param parameterSwitch Whether this parameter is active or not
	 * @param successThreshold The number of keywords that should exist in order
	 * for the query to be successful
	 */
	public KeywordsParameter(T keywords, ParameterSwitch parameterSwitch,
			Integer successThreshold) {
		super(keywords, parameterSwitch);
		this.successThreshold = successThreshold;
	}

	public Integer getSuccessThreshold() {
		return successThreshold;
	}
	
	/**
	 * Print the keywords parameter
	 * @return String representation of the keywords parameter
	 */
	@Override
	public String export() {
		if (getParameterSwitch().equals(ParameterSwitch.OFF)) {
			return "Inactive field";
		}
		
		return getParameter().toString() + " Success threshold: " + successThreshold;
	}
}
