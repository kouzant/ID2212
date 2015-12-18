package gr.kzps.id2212.project.client.query;

import java.io.Serializable;

import gr.kzps.id2212.project.client.query.parameterOperators.ParameterSwitch;

public class QueryParameter<T> implements Serializable {
	private static final long serialVersionUID = 4504299340508028881L;
	private final T parameter;
	private final ParameterSwitch parameterSwitch;
	
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

	public String export() {
		if (parameterSwitch.equals(ParameterSwitch.OFF)) {
			return "Inactive field";
		}
		
		return (String) parameter;
	}
}
