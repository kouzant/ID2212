package gr.kzps.id2212.project.client.query;

import java.io.Serializable;

import gr.kzps.id2212.project.client.query.parameterOperators.ParameterSwitch;

/**
 * Generic parameter of the search query
 * @author Antonis Kouzoupis
 *
 * @param <T>
 */
public class QueryParameter<T> implements Serializable {
	private static final long serialVersionUID = 4504299340508028881L;
	private final T parameter;
	private final ParameterSwitch parameterSwitch;
	
	/**
	 * @param parameter The actual value of the parameter
	 * @param parameterSwitch Whether the parameter is active or not
	 */
	public QueryParameter(T parameter, ParameterSwitch parameterSwitch) {
		this.parameterSwitch = parameterSwitch;
		this.parameter = parameter;
	}
	
	public T getParameter() {
		return parameter;
	}
	
	public ParameterSwitch getParameterSwitch() {
		return parameterSwitch;
	}

	/**
	 * Print the parameter
	 * @return String representation of the parameter
	 */
	public String export() {
		if (parameterSwitch.equals(ParameterSwitch.OFF)) {
			return "Inactive field";
		}
		
		return (String) parameter;
	}
}
