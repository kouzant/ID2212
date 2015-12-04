package gr.kzps.id2212.conv.controllers.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "stringValidator")
public class DefaultTextValidator implements Validator {

	@Override
	public void validate(FacesContext ctx, UIComponent comp, Object value)
			throws ValidatorException {
		String valueStr = value.toString();
		
		// This is the case where I should use regex but it doesn't work!!!
		if (valueStr.startsWith(" ") || valueStr.endsWith(" ") ||
				valueStr.equals("From") || valueStr.equals("from") ||
				valueStr.equals("To") || valueStr.equals("to")) {
			String msg = "\'" + valueStr + "\'" + " is not a valid format, remove whitespaces "
					+ "or default values";
			
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
			throw new ValidatorException(facesMsg);
		}
	}

}
