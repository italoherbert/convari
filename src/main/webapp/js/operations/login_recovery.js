
var loginRecovery = new LoginRecovery();
function LoginRecovery() {
		
	this.sendPasswordRedefinitionLink = function() {
		sender.send( {
			cmd : "login-recovery",
			infoId : 'login_recovery_info_msg',
			errorId : 'login_recovery_error_msg',
			showProcessingMessage : true,
			parameters : { 
				'username' : document.login_recovery_form.username.value,
				'captcha' : document.login_recovery_form.captcha.value,
				'lang' : messageManager.getLang() 
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
				var form = document.login_recovery_form;
				return validator.validateAll( [
				    { type : validator.REQUIRED, labelKey : 'label.field.username', value : form.username.value },
				    { type : validator.REQUIRED, labelKey : 'label.field.captcha', value : form.captcha.value }
				] );
			},
			invoker : {
				source : this
			}
		} );
	};
	
	this.loadNewCaptcha = function() {
		imageUtil.setImageById( 'login_recovery_captcha_image', 'servlet/CaptchaServlet?cmd=login-recovery' );
	};
	
}