
var controller = new Controller();
function Controller() {
			
	var instance = this; 
	
	var updateTopic = true;
	var updateStatus = true;
	var updateMenupanel = true;
	var updateNotifications = true;
	var refreshContactsAndInvitations = true;
	
	this.updateTopicTimeout = 1000;
	this.updateStatusTimeout = 1000;
	this.updateMenupanelTimeout = 1000;
	this.updateNotificationsTimeout = 1000;
	this.refreshContactsAndInvitationsTimeout = 1000;
	
	this.startAll = function() {
		this.startUpdateStatus();
		this.startUpdateMenupanel();
		this.startUpdateNotification();
		this.startUpdateTopic();
		this.startRefreshContactsAndInvitations();
	};
	
	this.finishAll = function() {
		this.finishUpdateStatus();
		this.finishUpdateMenupanel();
		this.finishUpdateNotification();
		this.finishUpdateTopic();
		this.finishRefreshContactsAndInvitations();
	};
	
	this.startUpdateMenupanel = function() {
		if( updateMenupanel == true ) {
			menupanelManager.loadCounts( {
				includetcount : false,
				responseProcessed : function( params ) {	
					menupanelManager.processLoadCountsResponse( params );
					
					window.setTimeout( function() {
						instance.startUpdateMenupanel();					
					}, instance.updateMenupanelTimeout );
				}
			} );
		}
	};
			
	this.startUpdateStatus = function() {
		if( updateStatus == true ) {
			statusManager.loadStatus( {
				responseProcessed : function() {			
					window.setTimeout( function() {
						instance.startUpdateStatus();					
					}, instance.updateStatusTimeout );
				}
			} );								
		}
	};
	
	this.startUpdateNotification = function() {
		if( updateNotifications == true ) {
			notification.loadNotifications( {
				responseProcessed : function() {						
					window.setTimeout( function() {
						instance.startUpdateNotification();					
					}, instance.updateNotificationsTimeout );
				}
			} );
		}
	};
	
	this.startRefreshContactsAndInvitations = function() {
		if( refreshContactsAndInvitations == true ) {
			contact.refreshContactsAndInvitations( {
				responseProcessed : function() {
					window.setTimeout( function() {
						instance.startRefreshContactsAndInvitations();					
					}, instance.refreshContactsAndInvitationsTimeout );
				}
			} );								
		}
	};
	
	this.startUpdateTopic = function() {
		if( updateTopic == true ) {
			window.setTimeout( function() {
				if( post.autoRefresh == true ) {
					post.refreshTopic( {
						responseProcessed : function() {					
							instance.startUpdateTopic();
						}
					} );
				} else {
					instance.startUpdateTopic();
				}					
			}, instance.updateTopicTimeout );
		}
	};
	
	this.finishRefreshContactsAndInvitations = function() {
		refreshContactsAndInvitations = false;
	};
	
	this.finishUpdateNotification = function() {
		updateNotifications = false;
	};
		
	this.finishUpdateStatus = function() {
		updateStatus = false;
	};
	
	this.finishUpdateMenupanel = function() {
		updateMenupanel = false;
	};
	
	this.finishUpdateTopic = function() {
		updateTopic = false;
	};
		
}