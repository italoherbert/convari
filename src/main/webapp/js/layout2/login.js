
window.onload = function() {	
	var pars = urlutil.params();
	
	messageManager.initialize( MESSAGES_CONFIG_FILE, { context : LOGIN_CONTEXT, lang : pars[ 'lang'] } );
	componentLoader.initialize( LOGIN_LOADER_FILE );
	componentLoader.load();		
};

var layout_page_onload = function() {
	
};

var login_page_onload = function() {
	document.login_form.username.focus();
	document.login_form.password.onkeypress = function( e ) {
		var code = ( window.event ? window.event.keyCode : e.keyCode );
		if( code == 13 ) {
			login.login();
			return false;
		}
		return true;
	};
};

var head_page_onload = function() {	
	new Menu( {
		menubar : true,
		align : 'right',
		itens : [
		    { label : messageManager.getLabel( 'label.menubar.option.register' ), onclick : 'javascript:urlutil.meshParamsAndRedirect( REGISTER_PAGE_FILE );' }
		]
	} ).build( 'head_menubar' );
};