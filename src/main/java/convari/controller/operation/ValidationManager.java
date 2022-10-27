package convari.controller.operation;

import italo.validate.RequestValidatorParam;
import italo.validate.ValidationException;
import italo.validate.Validator;
import italo.validate.ValidatorParam;
import italo.validate.ValidatorParamImpl;
import italo.validate.contain.KnownValidator;
import italo.validate.contain.KnownValidatorListener;
import italo.validate.date.DateValidator;
import italo.validate.date.DateValidatorListener;
import italo.validate.length.LengthValidator;
import italo.validate.length.LengthValidatorListener;
import italo.validate.mail.MailValidator;
import italo.validate.mail.MailValidatorListener;
import italo.validate.notnull.NotNullValidator;
import italo.validate.notnull.NotNullValidatorListener;
import italo.validate.number.IntValidator;
import italo.validate.number.IntValidatorListener;
import italo.validate.number.LongValidator;
import italo.validate.number.LongValidatorListener;
import italo.validate.password.PasswordValidator;
import italo.validate.password.PasswordValidatorListener;
import italo.validate.required.RequiredValidator;
import italo.validate.required.RequiredValidatorListener;

import java.util.Set;

import convari.response.KeyResponseMessageConstants;





public class ValidationManager implements NotNullValidatorListener, RequiredValidatorListener, 
				IntValidatorListener, LongValidatorListener, 
				LengthValidatorListener, KnownValidatorListener, 
				MailValidatorListener, PasswordValidatorListener, DateValidatorListener {
	
	public LengthValidator createLengthValidator( int max ) {
		return this.createLengthValidator( 0, max );
	}
	
	public LengthValidator createLengthValidator( int min, int max ) {
		LengthValidator validator = this.createLengthValidator();
		validator.setMin( min );
		validator.setMax( max );
		return validator;
	}
	
	public KnownValidator createKnownValidator( String... values ) {
		KnownValidator validator = this.createKnownValidator();
		for( String v:values )
			validator.getSet().add( v );
		return validator;
	}
		
	public DateValidator createDateValidator( String pattern ) {
		DateValidator validator = this.createDateValidator();
		validator.setPattern( pattern );
		return validator;
	}
	
	public NotNullValidator createNotNullValidator() {
		NotNullValidator validator = new NotNullValidator();
		validator.setNotNullValidatorListener( this );
		return validator;
	}
		
	public IntValidator createIntValidator( int min, int max ) {
		IntValidator validator = this.createIntValidator();
		validator.setMin( min );
		validator.setMax( max );
		return validator;
	}
	
	public LongValidator createLongValidator( long min, long max ) {
		LongValidator validator = this.createLongValidator();
		validator.setMin( min );
		validator.setMax( max );
		return validator;
	}
	
	public PasswordValidator createPasswordValidator() {
		PasswordValidator validator = new PasswordValidator();
		validator.setPasswordValidatorListener( this );
		return validator;
	}
	
	public MailValidator createMailValidator() {
		MailValidator validator = new MailValidator();
		validator.setMailValidatorListener( this );
		return validator;
	}
	
	public LengthValidator createLengthValidator() {
		LengthValidator validator = new LengthValidator();
		validator.setLengthValidatorListener( this );
		return validator;
	}
	
	public KnownValidator createKnownValidator() {
		KnownValidator validator = new KnownValidator();
		validator.setKnownValidatorListener( this );
		return validator;
	}
	
	public RequiredValidator createRequiredValidator() {
		RequiredValidator validator = new RequiredValidator();
		validator.setRequiredValidatorListener( this );
		return validator;
	}
	
	public LongValidator createLongValidator() {
		LongValidator validator =  new LongValidator();
		validator.setLongValidatorListener( this );
		return validator;
	}
	
	public IntValidator createIntValidator() {
		IntValidator validator =  new IntValidator();
		validator.setIntValidatorListener( this );
		return validator;
	}
	
	public DateValidator createDateValidator() {
		DateValidator validator = new DateValidator();
		validator.setDateValidatorListener( this );
		return validator;
	}
	
	public RequestValidatorParam createParam( String name, String label ) {
		RequestValidatorParam param = this.createParam( name );
		param.setLabel( label );
		return param;
	}	
	
	public RequestValidatorParam createParam( String name ) {
		RequestValidatorParam param = new ValidatorParamImpl();
		param.setName( name );
		return param;
	}
			
	public boolean validateAuthNeed( OperationParameters parameters ) {
		boolean logged = parameters.getWebLogic().isUserLogged( parameters.getRequest() );  
		if( !logged ) {
			parameters.getResponseBuilder().processKeyErrorMSG( KeyResponseMessageConstants.AUTH_NEED_ERROR );
			return false;
		}
		return true;
	}
	
	public boolean validate( OperationParameters parameters, RequestValidatorParam param ) throws ValidationException {
		ValidatorParam vparam = (ValidatorParam)param; 
		vparam.setValue( parameters.getRequest().getParameter( param.getName() ) );
		vparam.setTO( parameters );
		
		for( Validator validator:param.getValidators() ) {
			boolean valid = validator.validate( vparam );
			if( !valid )
				return false;
		}
		return true;
	}	
		
	public void paramNull( ValidatorParam param ) throws ValidationException {
		processInvalidParam( param, KeyResponseMessageConstants.NULL_REQUEST_PARAM_ERROR );
	}

	public void paramRequired(ValidatorParam param) throws ValidationException {
		processInvalidParam( param, KeyResponseMessageConstants.REQUIRED_REQUEST_PARAM_ERROR );
	}
	
	public void paramMailInvalid( ValidatorParam param, String mail ) throws ValidationException {
		processInvalidParam( param, KeyResponseMessageConstants.MAIL_REQUEST_PARAM_ERROR );
	}

	public void paramPasswordInvalid(ValidatorParam param, String password)	throws ValidationException {
		processInvalidParam( param, KeyResponseMessageConstants.PASSWORD_REQUEST_PARAM_ERROR );
	}

	public void paramDateFormatInvalid(ValidatorParam param, String date, String pattern) throws ValidationException {
		processInvalidParam( param, KeyResponseMessageConstants.DATE_REQUEST_PARAM_ERROR );
	}

	public void paramNotLong(ValidatorParam param) throws ValidationException {
		processInvalidParam( param, KeyResponseMessageConstants.NOTNUMBER_REQUEST_PARAM_ERROR );
	}

	public void paramIntervalExceed(ValidatorParam param, long value, long min, long max) throws ValidationException {
		String interval = value+": ["+min+"-"+max+"]"; 
		processInvalidParam( param, KeyResponseMessageConstants.INTERVAL_EXCEED_REQUEST_PARAM_ERROR, interval );
	}

	public void paramNotInteger(ValidatorParam param) throws ValidationException {
		processInvalidParam( param, KeyResponseMessageConstants.NOTNUMBER_REQUEST_PARAM_ERROR );
	}

	public void paramIntervalExceed(ValidatorParam param, int value, int min, int max) throws ValidationException {
		String interval = value+": ["+min+"-"+max+"]"; 
		processInvalidParam( param, KeyResponseMessageConstants.INTERVAL_EXCEED_REQUEST_PARAM_ERROR, interval );
	}

	public void paramUnknown(ValidatorParam param, Set<String> set, String value) throws ValidationException {
		processInvalidParam( param, KeyResponseMessageConstants.UNKNOWN_REQUEST_PARAM_ERROR, value );

	}

	public void paramLengthInvalid(ValidatorParam param, int length, int min, int max) throws ValidationException {
		processInvalidParam( param, KeyResponseMessageConstants.LENGTH_REQUEST_PARAM_ERROR, String.valueOf( min ), String.valueOf( max ) ); 
	}

	private void processInvalidParam( ValidatorParam param, String messageKey, String... messageParams ) throws ValidationException {
		OperationParameters parameters = (OperationParameters)param.getTO();
		String label = ( param.getLabel() != null ? param.getLabel() : param.getName() );
		String[] pars = new String[ messageParams.length+1 ];
		pars[0] = label;
		int i = 1;
		for( String p:messageParams ) {
			pars[i] = p;
			i++;
		}
		parameters.getResponseBuilder().processKeyErrorMSG( messageKey, pars );		
	}
	
}

