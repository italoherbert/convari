package italo.validate.length;

import italo.validate.ValidationException;
import italo.validate.Validator;
import italo.validate.ValidatorParam;
import italo.validate.notnull.NotNullValidator;

public class LengthValidator implements Validator {

	private NotNullValidator notNullValidator = new NotNullValidator();
	private LengthValidatorListener listener;
	private int min = 0;
	private int max = Integer.MAX_VALUE;

	public boolean validate(ValidatorParam param) throws ValidationException {
		boolean valid = notNullValidator.validate( param );
		if( valid ) {
			int length = param.getValue().length();
			if( length < min || length > max ) {
				if( listener != null )
					listener.paramLengthInvalid( param, length, min, max );
			} else {
				return true;
			}				
		}		
		return false;
	}
	
	public void setLengthValidatorListener(LengthValidatorListener listener) {
		this.listener = listener;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

}
