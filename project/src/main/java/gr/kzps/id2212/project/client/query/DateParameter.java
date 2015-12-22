package gr.kzps.id2212.project.client.query;

import gr.kzps.id2212.project.client.query.parameterOperators.DateOperators;
import gr.kzps.id2212.project.client.query.parameterOperators.ParameterSwitch;

/**
 * Query parameter for a date field
 * @author Antonis Kouzoupis
 *
 * @param <T>
 */
public class DateParameter<T> extends QueryParameter<T> {
	private static final long serialVersionUID = 6928480110225062544L;
	private final DateOperators operator;
	
	/**
	 * @param date Value of the date parameter
	 * @param parameterSwitch Whether parameter is active or not
	 * @param operator If metadata should be BEFORE, EQUAL or AFTER the query date
	 */
	public DateParameter(T date, ParameterSwitch parameterSwitch,
			DateOperators operator) {
		super(date, parameterSwitch);
		this.operator = operator;
	}
	
	public DateOperators getOperator() {
		return operator;
	}
	
	/**
	 * Print the date query
	 * @return String representation of date parameter
	 */
	public String export() {
		if (getParameterSwitch().equals(ParameterSwitch.OFF)) {
			return "Inactive field";
		}
		
		return operator.toString() + " " + (String) getParameter().toString();
	}
}
