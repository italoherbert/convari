package italo.validate.mail;

import italo.validate.ValidationException;
import italo.validate.Validator;
import italo.validate.ValidatorParam;
import italo.validate.notnull.NotNullValidator;

public class MailValidator implements Validator {

	private NotNullValidator notNullValidator = new NotNullValidator();
	private MailValidatorListener listener;

	public boolean validate(ValidatorParam param) throws ValidationException {
		boolean valid = notNullValidator.validate( param );
		if( valid ) {
			String value = param.getValue();
			if( value.matches( "[\\w-\\.]+@[\\w]+\\.[\\w]+(\\.[\\w]+)*" ) ) {
				return true;
			} else if ( listener != null ) {
				listener.paramMailInvalid( param, value );
			}
		}		
		return false;
	}
	
	public void setMailValidatorListener(MailValidatorListener listener) {
		this.listener = listener;
	}

}

