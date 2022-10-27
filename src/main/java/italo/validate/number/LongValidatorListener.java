package italo.validate.number;

import italo.validate.ValidationException;
import italo.validate.ValidatorParam;

public interface LongValidatorListener {

	public void paramNotLong( ValidatorParam param ) throws ValidationException;
	
	public void paramIntervalExceed( ValidatorParam param, long value, long min, long max ) throws ValidationException;

	
}
