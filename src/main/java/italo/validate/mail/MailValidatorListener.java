package italo.validate.mail;

import italo.validate.ValidationException;
import italo.validate.ValidatorParam;

public interface MailValidatorListener {

	public void paramMailInvalid( ValidatorParam param, String mail ) throws ValidationException;
	
}

