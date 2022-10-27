
var userConfig = new UserConfig();
function UserConfig() {
	
	this.loadConfigPanel = function() {
		var pars = urlutil.params();
		
		sender.send( {
			parameters : "cmd=get-config&uid="+pars['uid'],
			infoId : 'user_configurations_info_msg',
			errorId : 'user_configurations_error_msg',
			showProcessingMessage : true,
			success : function( params ) {
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				var user = body.getElementsByTagName( 'user' )[0];
							
				var config = user.getElementsByTagName( 'config' )[0];
				
				var enderVID = nodeValue( config, 'ender-visibility' );
				var telVID = nodeValue( config, 'tel-visibility' );
				var mailVID = nodeValue( config, 'mail-visibility' );

				document.user_config_form.ender_visibility_cb.selectedIndex = enderVID;
				document.user_config_form.tel_visibility_cb.selectedIndex = telVID;
				document.user_config_form.mail_visibility_cb.selectedIndex = mailVID;
			}
		} );		
	};
	
	this.saveDataUserConfig = function() {
		sender.send( {
			cmd : "save-data-user-config",
			infoId : 'save_data_user_config_info_msg',
			errorId : 'save_data_user_config_error_msg',
			showProcessingMessage : true,
			parameters : function( params ) {
				var pars = urlutil.params();
				return {
					'uid'		  : pars[ 'uid' ],
					'enderv' 	  : document.user_config_form.ender_visibility_cb.selectedIndex,
					'telv'  	  : document.user_config_form.tel_visibility_cb.selectedIndex,
					'mailv'		  : document.user_config_form.mail_visibility_cb.selectedIndex
				};
			}
		} );
	};
		
}
