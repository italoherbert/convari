var MESSAGES_CONFIG_FILE = "messages/config.xml";

var LOGIN_CONTEXT = "login";
var REGISTER_CONTEXT = "register";
var LOGIN_RECOVERY_CONTEXT = "login-recovery";
var PASSWORD_REDEFINITION_CONTEXT = "password-redefinition";
var MAIN_CONTEXT = "main";

var MAIN_LOADER_FILE = "loader/main.xml";
var LOGIN_LOADER_FILE = "loader/login.xml";
var REGISTER_LOADER_FILE = "loader/register.xml";
var LOGIN_RECOVERY_LOADER_FILE = "loader/login-recovery.xml";
var PASSWORD_REDEFINITION_LOADER_FILE = "loader/password-redefinition.xml";

var MAIN_PAGE_FILE = "index.html";
var LOGIN_PAGE_FILE = "login.html";
var LOGIN_RECOVERY_PAGE_FILE = "login_recovery.html";
var PASSWORD_REDEFINITION_PAGE_FILE = "password_redefinition.html";
var REGISTER_PAGE_FILE = "register.html";

var PUBLIC_VISIBILITY = "public";
var USER_VISIBILITY = "user";
var CONTACT_VISIBILITY = "contact";
var OWNER_VISIBILITY = "owner";

var dependencesImpl = {
	getLabel : function getLabel( key, params ) {
		return messageManager.getLabel( key, params );
	},
	getLabelForParams : function( param ) {
		return messageManager.getLabelForParams( param );
	},
	processComponentChildNode : function( item ) {
		messageManager.compatibility.componentLoader.processComponentChildNode( item );
	}
};

sender.dependences.getLabel = dependencesImpl.getLabel;

validator.dependences.getLabel = dependencesImpl.getLabel;
validator.dependences.getLabelForParams = dependencesImpl.getLabelForParams;

componentLoader.dependences.processComponentChildNode = dependencesImpl.processComponentChildNode;




sender.servlet = "servlet/InvokerServlet";
sender.requestSended = function( params, showProcessingMessage ) {
	if( showProcessingMessage == true ) {
		var label = messageManager.getLabel( 'label.alert.processing' );
		alertMessage.showAndStartProcessingUpdate( { html : label } );		
	}
};

sender.responseReceived = function( params, showProcessingMessage ) {
	if( showProcessingMessage == true )
		alertMessage.hideAndStopProcessingUpdate();	
};

sender.failCatch = function( params ) {
	/*systemTester.maximunConnectionFailAttempts = 3;
	systemTester.verifyConnection( {
		connectionFail : function() {
			processFail();
		}
	} );*/
};

sender.systemErrorCatch = function( params ) {
	if( params.status == 201 )
		processFail();	
};

function processFail() {
	var showed = alertMessage.show( {							
		loadHTML : function( params ) {
			var id = params.id;
			messageManager.loadHTML( id, 'html.fail.system', {
				fail : function( params ) {
					messageManager.loadHTML( document.body, 'html.fail.system' );
				}
			} );
		}, 
		finish : true 
	} );
	if( showed == false )
		messageManager.loadHTML( document.body, 'html.fail.system' );
}	