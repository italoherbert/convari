package italo.validate.password;

import italo.validate.ValidationException;
import italo.validate.Validator;
import italo.validate.ValidatorParam;
import italo.validate.notnull.NotNullValidator;

public class PasswordValidator implements Validator {

	private NotNullValidator notNullValidator = new NotNullValidator();
	private PasswordValidatorListener listener;

	public boolean validate(ValidatorParam param) throws ValidationException {
		boolean valid = notNullValidator.validate( param );
		if( valid ) {
			String value = param.getValue();
			if( value.length() > 6 ) {
				return true;
			} else if ( listener != null ) {
				listener.paramPasswordInvalid( param, value );
			}
		}		
		return false;
	}
	
	public void setPasswordValidatorListener(PasswordValidatorListener listener) {
		this.listener = listener;
	}

}

