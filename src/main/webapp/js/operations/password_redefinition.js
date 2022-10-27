
var passwordRedefinition = new PasswordRedefinition();
function PasswordRedefinition() {
		
	this.redefinePassword = function() {
		sender.send( {
			cmd : "password-redefinition",
			infoId : 'password_redefinition_info_msg',
			errorId : 'password_redefinition_error_msg',
			showProcessingMessage : true,
			parameters : function( params ) {
				var pars = urlutil.params();	
				var captcha = document.password_redefinition_form.captcha.value;
				var password = document.password_redefinition_form.password.value;
				password = document.encrypter.encrypt( password );
				return { 
					'password' : password, 
					'code' : pars[ 'code' ], 
					'captcha' : captcha 
				}; 
			},
			responseReceived : function( params ) {
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				var loadCaptcha = nodeValue( body, 'load-captcha' );
				if( loadCaptcha == 'true' ) {
					params.invoker.source.loadNewCaptcha();
					document.login_recovery_form.captcha.value = "";
				}
			},
			validate : function( params ) {
				var form = document.password_redefinition_form;				
				return validator.validateAll( [
   				    { type : validator.REQUIRED, labelKey : 'label.field.password.new', value : form.password.value },
   				    { type : validator.REQUIRED, labelKey : 'label.field.password.new.confirm', value : form.confirmpassword.value },
   				    { type : validator.REQUIRED, labelKey : 'label.field.captcha', value : form.captcha.value },
   				    { type : validator.EQUAL, 
						param1 : { labelKey : 'label.field.password.new', value : form.password.value }, 
						param2 : { labelKey : 'label.field.password.new.confirm', value : form.confirmpassword.value } }
					
   				] );
			}, 
			invoker : {
				source : this
			}
		} );
	};
	
	this.loadNewCaptcha = function() {
		imageUtil.setImageById( 'password_redefinition_captcha_image', 'servlet/CaptchaServlet?cmd=password-redefinition' );
	};
	
}