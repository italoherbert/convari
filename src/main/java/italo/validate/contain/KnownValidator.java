package italo.validate.contain;

import italo.validate.ValidationException;
import italo.validate.Validator;
import italo.validate.ValidatorParam;
import italo.validate.notnull.NotNullValidator;

import java.util.HashSet;
import java.util.Set;

public class KnownValidator implements Validator {

	private NotNullValidator notNullValidator = new NotNullValidator();
	private KnownValidatorListener listener;
	private Set<String> set = new HashSet<String>();

	public boolean validate(ValidatorParam param) throws ValidationException {
		boolean valid = notNullValidator.validate( param );
		if( valid ) {
			String value = param.getValue();
			for( String v:set ) 
				if( v.equals( value ) )
					return true;			
			if( listener != null )
				listener.paramUnknown( param, set, value );						
		}		
		return false;
	}
	
	public void setKnownValidatorListener(KnownValidatorListener listener) {
		this.listener = listener;
	}

	public Set<String> getSet() {
		return set;
	}

}
