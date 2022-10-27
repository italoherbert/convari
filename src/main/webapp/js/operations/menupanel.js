
var menupanelManager = new MenupanelManager();
function MenupanelManager() {
	
	this.loadCounts = function( requestParams ) {	
		var pars = urlutil.params();
		sender.send( { 
			cmd : "get-user-counts",
			parameters : {
				'uid' : pars[ 'uid' ],
				'includetcount' : ( requestParams ? requestParams.includetcount : false )
			},
			success : function( params ) {
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				
				if( typeof( params.invoker.responseProcessed ) == 'function' ) {
					params.invoker.responseProcessed.call( this, {
						notificationsCount : parseInt( nodeValue( body, 'notifications-count' ) ),
						pendingInvitationsCount : parseInt( nodeValue( body, 'invitations-count' ) ),
						topicsCount : parseInt( nodeValue( body, 'topics-count' ) ),
						isOwner : nodeValue( body, 'is-owner' )
					} );	
				}								
			},
			invoker : {
				responseProcessed : ( requestParams ? requestParams.responseProcessed : undefined )
			}
		} );	
	};
	
	this.processLoadCountsResponse = function( params ) {
		var notificationsCount = params.notificationsCount;
		var pendingInvitationsCount = params.pendingInvitationsCount;
		
		var notificationCountElement = document.getElementById( 'notifications_count' );
		if( notificationCountElement ) {
			var currentCount = parseInt( notificationCountElement.innerHTML );
			var newCount = parseInt( notificationsCount );
			if( newCount != currentCount ) {
				notificationCountElement.innerHTML = notificationsCount;
				if( currentContentPage != NOTIFICATIONS_PAGE && newCount > currentCount ) 
					menuPanel.markItemByStyle( NOTIFICATIONS_PAGE, menuPanel.MARK_ITEM_STYLE1 );
			}
		}
		
		var pendingInvitationCountElement = document.getElementById( 'pending_invitations_count' );
		if( pendingInvitationCountElement ) {
			var currentCount = parseInt( pendingInvitationCountElement.innerHTML );
			var newCount = parseInt( pendingInvitationsCount );
			if( newCount != currentCount ) {
				pendingInvitationCountElement.innerHTML = pendingInvitationsCount;
				if( currentContentPage != INVITATIONS_PAGE && newCount > currentCount  )
					menuPanel.markItemByStyle( INVITATIONS_PAGE, menuPanel.MARK_ITEM_STYLE1 );
			}
		}
	};
	
}