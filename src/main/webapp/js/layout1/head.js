
var logged_head_page_onload = function() {
	headMenubar.itens.push( { label : messageManager.getLabel( 'label.menubar.option.exit' ), onclick : 'javascript:login.logout();' } );
	
	var menu_util = new Menu( headMenubar );
	menu_util.build( 'head_menubar' );	
};

var unlogged_head_page_onload = function() {
	headMenubar.itens.push( { label : messageManager.getLabel( 'label.menubar.option.login' ), onclick : 'javascript:urlutil.meshParamsAndRedirect( LOGIN_PAGE_FILE );' } );

	var menu_util = new Menu( headMenubar );
	menu_util.build( 'head_menubar' );
};


var user_simple_find_onload = function() {		
	document.getElementById( 'label.blur.find.user' ).onclick = function() {
		document.user_simple_find_form.namequery.focus();
	};			
	
	document.user_simple_find_form.namequery.onfocus = function() {
		elementManager.showById( 'label.blur.find.user', false );
	};

	document.user_simple_find_form.namequery.onblur = function() {
		if( this.value.length == 0 )
			elementManager.showById( 'label.blur.find.user', true );		
	};
	
	
	document.getElementById( 'user_simple_find_form_bt' ).onclick = function() {
		userFinder.params = { namequery : document.user_simple_find_form.namequery.value }; 
		componentLoader.loadComponentByName( USER_FIND_PAGE );
	};
	
	document.user_simple_find_form.namequery.onkeypress = function( e ) {
		var code = ( window.event ? window.event.keyCode : e.keyCode );
		if( code == 13 ) {
			userFinder.params = { namequery : document.user_simple_find_form.namequery.value }; 
			componentLoader.loadComponentByName( USER_FIND_PAGE );
			return false;
		}
		return true;
	};
};