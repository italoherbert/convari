
var layout_page_onload = function() {	
	componentLoader.afterCallbacks[ 'content' ] = {
		callback : function( params ) {
			if( contentLabels[ params.componentName ] ) {
				currentContentPage = params.componentName;				
				var link = document.getElementById( 'navigation_page_label' );
				if( link ) {
					link.onclick = "javascript:componentLoader.loadComponentByName( "+ currentContentPage +" );";
					link.innerHTML = contentLabels[ currentContentPage ];
				}
				menuPanel.markItem( currentContentPage );
			}
		}
	};
};

var content_page_onload = function() {
	var pars = urlutil.params();
	
	var current_page = HOME_PAGE;
	var page = pars[ 'page' ]; 
	if( page ) {
		var el = componentLoader.getComponentNodeByName( page );
		if( el != null )
			if( el.getAttribute( 'id' ) == 'content' )
				current_page = page;							
	} else {
		if( pars['tid'] )		
			current_page = TOPIC_PAGE;			
	}	
	componentLoader.loadComponentByName( current_page );

	if( user.visibility == OWNER_VISIBILITY 
			|| user.visibility == CONTACT_VISIBILITY 
			|| user.visibility == USER_VISIBILITY ) { 
		controller.startAll();
	}
};

var north_page_onload = function() {
	
};

var south_page_onload = function() {
	
};

var head_page_onload = function() {
	
};

var leftpanel_page_onload = function() {
	var el = document.getElementById( 'leftpanel_td' );
	el.className = 'leftpanel_dim';
};

var rightpanel_page_onload = function() {
	var el = document.getElementById( 'rightpanel_td' );
	el.className = 'rightpanel_dim';	
};
