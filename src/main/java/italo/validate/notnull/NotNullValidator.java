package italo.validate.notnull;

import italo.validate.ValidationException;
import italo.validate.Validator;
import italo.validate.ValidatorParam;


public class NotNullValidator implements Validator {
	
	private NotNullValidatorListener listener;
		
	public boolean validate( ValidatorParam param ) throws ValidationException {
		if( param.getValue() != null )
			return true;
		
		if( listener != null )
			listener.paramNull( param );		
		return false;
	}
	
	public void setNotNullValidatorListener(NotNullValidatorListener listener) {
		this.listener = listener;
	}

}
