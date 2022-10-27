package italo.validate.notnull;

import italo.validate.ValidationException;
import italo.validate.ValidatorParam;


public interface NotNullValidatorListener {

	public void paramNull( ValidatorParam param ) throws ValidationException;
	
}
