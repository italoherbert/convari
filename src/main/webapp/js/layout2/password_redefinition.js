﻿
window.onload = function() {	
	var pars = urlutil.params();
	
	messageManager.initialize( MESSAGES_CONFIG_FILE, { context : PASSWORD_REDEFINITION_CONTEXT, lang : pars[ 'lang'] } );
	componentLoader.initialize( PASSWORD_REDEFINITION_LOADER_FILE );
	componentLoader.load();		
};

var layout_page_onload = function() {
	
};

var head_page_onload = function() {	
	new Menu( {
		menubar : true,
		align : 'right',
		itens : [
		    { label : messageManager.getLabel( 'label.menubar.option.login' ), onclick : 'javascript:urlutil.meshParamsAndRedirect( LOGIN_PAGE_FILE );' }
		]
	} ).build( 'head_menubar' );
};
