package gr.kzps.id2212.conv.controllers.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "numberValidator")
public class NumberValidator implements Validator {

	@Override
	public void validate(FacesContext ctx, UIComponent comp, Object value) throws ValidatorException {
		Float valueF = Float.parseFloat(value.toString());
		
		// This is the case where I should use regex but it doesn't work!!!
		if (valueF.equals(0.0F)) {
			String msg = "\'" + valueF + "\'" + " is not a valid number";

			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
			throw new ValidatorException(facesMsg);
		}
	}

}
