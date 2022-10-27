
var userRegister = new UserRegister();
function UserRegister() {
	
	this.save = function() {
		sender.send( {
			cmd : "register",
			parameters : function( params ) {			
				return {
					'username' : document.register_form.username.value,
					'password' : document.register_form.password.value,
					'name'	   : document.register_form.name.value,
					'lastname' : document.register_form.lastname.value,
					'sex' 	   : document.register_form.sex_cb.value,
					'website'  : document.register_form.website.value,
										
					'mail'	   : document.register_form.mail.value,
					'mail2'    : document.register_form.mail2.value,
					'captcha'  : document.register_form.captcha.value
				};
			},
			infoId : 'register_info_msg',
			errorId : 'register_error_msg', 
			showProcessingMessage : true,
			responseReceived : function( params ) {
				var doc = params.doc;				
				var body = doc.getElementsByTagName( 'body' )[0];
				var loadCaptcha = nodeValue( body, 'load-captcha' );
				if( loadCaptcha == 'true' ) {
					params.invoker.source.loadNewCaptcha();
					document.register_form.captcha.value = "";
				}
			},
			validate : function( params ) {
				var form = document.register_form;	
				return validator.validateAll( [
					{ type : validator.REQUIRED, labelKey : 'label.field.username', value : form.username.value },
					{ type : validator.REQUIRED, labelKey : 'label.field.password', value : form.password.value },
					{ type : validator.REQUIRED, labelKey : 'label.field.name', value : form.name.value },
					{ type : validator.REQUIRED, labelKey : 'label.field.lastname', value : form.lastname.value },
					{ type : validator.REQUIRED, labelKey : 'label.field.sex', value : form.sex_cb.value },
					{ type : validator.REQUIRED, labelKey : 'label.field.mail', value : form.mail.value },
					{ type : validator.REQUIRED, labelKey : 'label.field.mail.confirm', value : form.confirmmail.value },
					{ type : validator.REQUIRED, labelKey : 'label.field.captcha', value : form.captcha.value },
					{ type : validator.MAIL, value : form.mail.value },
					{ type : validator.EQUAL, 
						param1 : { labelKey : 'label.field.password', value : form.password.value }, 
						param2 : { labelKey : 'label.field.password.confirm', value : form.confirmpassword.value } },
					{ type : validator.EQUAL,
					    param1 : { labelKey : 'label.field.mail', value : form.mail.value }, 
					    param2 : { labelKey : 'label.field.mail.confirm', value : form.confirmmail.value } },
					{ type : validator.EQUAL,
					    param1 : { labelKey : 'label.field.mail2', value : form.mail2.value }, 
					    param2 : { labelKey : 'label.field.mail2.confirm', value : form.confirmmail2.value } }
				] );
			}, 
			invoker : {
				source : this
			}
		} );
	};
		
	this.loadNewCaptcha = function() {
		imageUtil.setImageById( 'register_captcha_image', 'servlet/CaptchaServlet?cmd=register' );
	};
			
}
	

