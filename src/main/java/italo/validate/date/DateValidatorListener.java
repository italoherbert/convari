package italo.validate.date;

import italo.validate.ValidationException;
import italo.validate.ValidatorParam;

public interface DateValidatorListener {

	public void paramDateFormatInvalid( ValidatorParam param, String date, String pattern ) throws ValidationException;
	
}
