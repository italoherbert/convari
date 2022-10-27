package italo.validate.number;

import italo.validate.ValidationException;
import italo.validate.ValidatorParam;

public interface IntValidatorListener {

	public void paramNotInteger( ValidatorParam param ) throws ValidationException;
	
	public void paramIntervalExceed( ValidatorParam param, int value, int min, int max ) throws ValidationException;
	
}
