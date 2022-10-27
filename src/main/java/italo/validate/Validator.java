package italo.validate;

public interface Validator {

	public boolean validate( ValidatorParam param ) throws ValidationException;
	
}
