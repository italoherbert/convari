package italo.validate.number;

import italo.validate.ValidationException;
import italo.validate.Validator;
import italo.validate.ValidatorParam;
import italo.validate.notnull.NotNullValidator;

public class IntValidator implements Validator {

	private NotNullValidator notNullValidator = new NotNullValidator();
	private IntValidatorListener listener;
	private int min = Integer.MIN_VALUE;
	private int max = Integer.MAX_VALUE;
	
	public boolean validate(ValidatorParam param) throws ValidationException {
		boolean valid = notNullValidator.validate( param );
		if( valid ) {
			try {
				int value = Integer.parseInt( param.getValue() );
				if( value < min || value > max ) {
					if( listener != null )
						listener.paramIntervalExceed( param, value, min, max );
				} else {
					return true;
				}
			} catch( NumberFormatException e ) {
				if( listener != null )
					listener.paramNotInteger( param );
			}		
		}		
		return false;
	}
	
	public void setIntValidatorListener( IntValidatorListener listener ) {
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
