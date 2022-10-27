package italo.validate.required;

import italo.validate.ValidationException;
import italo.validate.ValidatorParam;
import italo.validate.notnull.NotNullValidatorListener;

public interface RequiredValidatorListener extends NotNullValidatorListener {

	public void paramRequired( ValidatorParam param ) throws ValidationException;
	
}
