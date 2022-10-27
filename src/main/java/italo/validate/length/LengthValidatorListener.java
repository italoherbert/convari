package italo.validate.length;

import italo.validate.ValidationException;
import italo.validate.ValidatorParam;

public interface LengthValidatorListener {

	public void paramLengthInvalid( ValidatorParam param, int value, int min, int max ) throws ValidationException;
	
}
