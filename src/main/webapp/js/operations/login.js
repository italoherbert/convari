
var login = new Login();
function Login() {
	
	var urlutil = new UrlUtil();
	
	this.login = function() {					
		sender.send( {
			cmd : "login",
			infoId : 'login_info_msg',
			errorId : 'login_error_msg',
			showProcessingMessage : true,
			parameters : function( params ) {
				var username = document.login_form.username.value;
				var password = document.login_form.password.value;
				var captcha = '';
				if( document.login_form.captcha )
					captcha = document.login_form.captcha.value;
				//password = document.encrypter.encrypt( password );
				return { 
					'username' : username, 
					'password' : password, 
					'captcha' : captcha 
				};
			},
			validate : function( params ) {
				var form = document.login_form;				
				return validator.validateAll( [
   					{ type : validator.REQUIRED, labelKey : 'label.field.username', value : form.username.value },
   					{ type : validator.REQUIRED, labelKey : 'label.field.password', value : form.password.value }
   				] );
			},
			success : function( params ) {
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				var id = nodeValue( body, 'userid' );
				urlutil.meshParamsAndRedirect( 'index.html?uid='+id );
			},
			error : function( params ) {
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				var loadCaptcha = nodeValue( body, 'load-captcha' );
				if( loadCaptcha == 'true' )
					params.invoker.source.loadNewCaptcha();
			},
			invoker : {
				source : this
			}
		} );
				
	};
	
	this.logout = function() {		
		controller.finishAll();
		sender.send( {
			cmd : "logout",
			showProcessingMessage : true,
			success : function( params ) {								
				var url = urlutil.rmParam( 'uid' );
				urlutil.meshParamsAndRedirect( 'index.html', url );
			}
		} );		
	};

	this.loadNewCaptcha = function( loadParams ) {
		var captcha_image_el = document.getElementById( 'login_captcha_image' );
		if( captcha_image_el )
			imageUtil.setImage( captcha_image_el, 'servlet/CaptchaServlet?cmd=login' );		
		else {
			componentLoader.loadComponentByName( 'login-captcha', {
				after : function( params ) {
					document.login_form.captcha.value = "";
				}
			} ); 			
		}
	};
	
}
