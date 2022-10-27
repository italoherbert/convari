package italo.validate.required;

import italo.validate.ValidationException;
import italo.validate.Validator;
import italo.validate.ValidatorParam;
import italo.validate.notnull.NotNullValidator;

public class RequiredValidator implements Validator {

	private NotNullValidator notNullValidator = new NotNullValidator();
	private RequiredValidatorListener listener;

	public boolean validate(ValidatorParam param) throws ValidationException {
		boolean notNull = notNullValidator.validate( param );
		if( notNull )
			if( param.getValue().length() > 0 )
				return true;		
		if( listener != null )			
			listener.paramRequired( param );
		return false;		
	}

	public void setRequiredValidatorListener( RequiredValidatorListener listener ) {
		this.listener = listener;
	}

}
