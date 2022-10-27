
var public_top_page_onload = function() {
	var menu = {
		menubar : true,
		align : 'left',
		itens : [
			{ label : messageManager.getLabel( 'label.menubar.option.home' ), onclick : 'javascript:componentLoader.loadComponentByName( HOME_PAGE );' },		         
			{ label : messageManager.getLabel( 'label.menubar.option.presentation' ), onclick : 'javascript:componentLoader.loadComponentByName( PRESENTATION_PAGE );' },
			{ label : messageManager.getLabel( 'label.menubar.option.login' ), onclick : 'javascript:urlutil.meshParamsAndRedirect( LOGIN_PAGE_FILE );' },
			{ label : messageManager.getLabel( 'label.menubar.option.register' ), onclick : 'javascript:urlutil.meshParamsAndRedirect( REGISTER_PAGE_FILE );' }
		]		
	};		
	var menu_util = new Menu( menu );
	menu_util.build( 'top' );
};

var user_top_page_onload = function() {		
	user.loadNavigationMenuUser( {
		responseProcessed : function( params ) {
			var id = params.id;
			var name = params.name;
			var contact = params.contact;
			document.getElementById( 'top' ).innerHTML = htmlBuilder.buildTopPanel( {
				id : id, name : name, contact : contact
			} );
		}
	} );
		
};
