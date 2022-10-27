package italo.validate.date;

import italo.validate.ValidationException;
import italo.validate.Validator;
import italo.validate.ValidatorParam;
import italo.validate.notnull.NotNullValidator;

public class DateValidator implements Validator {

	private NotNullValidator notNullValidator = new NotNullValidator();
	private DateValidatorListener listener;
	private String pattern = "\\d{2}/\\d{2}/\\d{4}";

	public boolean validate(ValidatorParam param) throws ValidationException {
		boolean valid = notNullValidator.validate( param );
		if( valid ) {
			String value = param.getValue();			
			if( value.matches( pattern ) ) {
				return true;
			} else if( listener != null ) {
				listener.paramDateFormatInvalid( param, value, pattern );
			}							
		}		
		return false;
	}
	
	public void setDateValidatorListener(DateValidatorListener listener) {
		this.listener = listener;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
}
