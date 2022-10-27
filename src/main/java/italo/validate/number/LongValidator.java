package italo.validate.number;

import italo.validate.ValidationException;
import italo.validate.Validator;
import italo.validate.ValidatorParam;
import italo.validate.notnull.NotNullValidator;

public class LongValidator implements Validator {

	private NotNullValidator notNullValidator = new NotNullValidator();
	private LongValidatorListener listener;
	private long min = Long.MIN_VALUE;
	private long max = Long.MAX_VALUE;
		
	public boolean validate(ValidatorParam param) throws ValidationException {
		boolean valid = notNullValidator.validate( param );
		if( valid ) {
			try {
				long value = Long.parseLong( param.getValue() );
				if( value < min || value > max ) {
					if( listener != null )
						listener.paramIntervalExceed( param, value, min, max );
				} else {
					return true;
				}
			} catch( NumberFormatException e ) {
				if( listener != null )
					listener.paramNotLong( param );
			}		
		}		
		return false;
	}
	
	public void setLongValidatorListener( LongValidatorListener listener ) {
		this.listener = listener;
	}

	public NotNullValidator getNotNullValidator() {
		return notNullValidator;
	}

	public long getMin() {
		return min;
	}

	public void setMin(long min) {
		this.min = min;
	}

	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}

}
