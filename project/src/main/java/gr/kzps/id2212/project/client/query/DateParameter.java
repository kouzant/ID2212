package gr.kzps.id2212.project.client.query;

import gr.kzps.id2212.project.client.query.parameterOperators.DateOperators;
import gr.kzps.id2212.project.client.query.parameterOperators.ParameterSwitch;

public class DateParameter<T> extends QueryParameter<T> {
	private static final long serialVersionUID = 6928480110225062544L;
	private final DateOperators operator;
	
	public DateParameter(T date, ParameterSwitch parameterSwitch,
			DateOperators operator) {
		super(date, parameterSwitch);
		this.operator = operator;
	}
	
	public DateOperators getOperator() {
		return operator;
	}
}
