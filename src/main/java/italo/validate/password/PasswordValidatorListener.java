package italo.validate.password;

import italo.validate.ValidationException;
import italo.validate.ValidatorParam;

public interface PasswordValidatorListener {

	public void paramPasswordInvalid( ValidatorParam param, String password ) throws ValidationException;
	
}

