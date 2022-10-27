
var status_page_onload = function() {
	statusPanel.options[ 'unoccupied' ] = { 
		icon : 'images/status/unoccupied.gif', 
		label : messageManager.getLabel( 'label.status.unoccupied' ),
		onclick : 'javascript:statusManager.setStatus( 0 );'
	};

	statusPanel.options[ 'occupied' ] = { 
		icon : 'images/status/occupied.gif', 
		label : messageManager.getLabel( 'label.status.occupied' ),
		onclick : 'javascript:statusManager.setStatus( 1 );'
	};

	statusPanel.options[ 'invisible' ] = { 
		icon : 'images/status/invisible.gif', 
		label : messageManager.getLabel( 'label.status.invisible' ),
		onclick : 'javascript:statusManager.setStatus( 2 );'
	};	

	statusPanel.options[ 'absent' ] = { 
		icon : 'images/status/absent.gif', 
		label : messageManager.getLabel( 'label.status.absent' ),
		visible : false
	};
	
	statusPanel.options[ 'disconnected' ] = { 
		icon : 'images/status/disconnected.gif', 
		label : messageManager.getLabel( 'label.status.disconnected' ),
		visible : false
	};

	statusPanel.elementId = 'status_page';
	statusPanel.alterPermittedIcon = 'images/alterstatus.gif';
	statusPanel.hoverAlterPermittedIcon = 'images/hoveralterstatus.gif';
	
	statusPanel.alterPermitted = ( user.visibility == 'owner' );
	statusPanel.build( 'unoccupied' );
		
	statusManager.startMouseAndKeyCapture();
};

