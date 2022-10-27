package italo.validate.contain;

import italo.validate.ValidationException;
import italo.validate.ValidatorParam;

import java.util.Set;

public interface KnownValidatorListener {

	public void paramUnknown( ValidatorParam param, Set<String> set, String value ) throws ValidationException;
	
}
