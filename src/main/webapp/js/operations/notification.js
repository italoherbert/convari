
var notification = new Notification();
function Notification() {
		
	this.panelIdPrefix = 'notification_panel_';
	this.infoIdPrefix = 'notification_panel_info_';
	this.errorIdPrefix = 'notification_panel_error_';

	this.controlDate = undefined;
	
	this.loadNotifications = function( requestParams ) {
		var pars = urlutil.params();
		
		if( !document.getElementById( 'notifications' ) ) {
			this.controlDate = undefined;
			if( requestParams )
				if( typeof( requestParams.responseProcessed ) == 'function' )
					requestParams.responseProcessed.call( this );
			return;
		}
				
		sender.send( {
			cmd : "get-notifications",
			parameters : {
				'uid' : pars[ 'uid' ],
				'controldate' : this.controlDate,
				'lang' : messageManager.getLang()
			},
			success : function( params ) {
				var source = params.invoker.source;
				var panelIdPrefix = source.panelIdPrefix;
				var infoIdPrefix = source.infoIdPrefix;
				var errorIdPrefix = source.errorIdPrefix;
				
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				var notifications = body.getElementsByTagName( 'notifications' )[0];
				var controlDate = notifications.getAttribute( 'controldate' );
				
				var notificationsList = notifications.getElementsByTagName( 'notification' );
				if( notificationsList.length > 0 ) {
					var html = "";
					for( var i = 0; i < notificationsList.length; i++ ) {
						var notification = notificationsList[ i ];							
						
						html += htmlBuilder.buildNotificationPanel( {
							id : nodeValue( notification, 'id' ),
							text : nodeValue( notification, 'text' ),
							date : nodeValue( notification, 'date' ),
							panelIdPrefix : panelIdPrefix,
							infoIdPrefix : infoIdPrefix,
							errorIdPrefix : errorIdPrefix
						} );		
					}
					html = "<span id='new_notifications'></span>"+html;
					source.controlDate = controlDate;	
	
					var newNotificationsElement = document.getElementById( 'new_notifications' );
					if( newNotificationsElement ) {
						newNotificationsElement.id=undefined;
						newNotificationsElement.innerHTML = html;
					} else {
						document.getElementById( 'notifications' ).innerHTML = html;					
					}
				}
				if( typeof( params.invoker.responseProcessed ) == 'function' )
					params.invoker.responseProcessed.call( this );		
			},
			invoker : {
				source : this,
				responseProcessed : ( requestParams ? requestParams.responseProcessed : undefined )
			}
		} );	
	};
			
	this.removeNotification = function( id ) {
		var panelId = this.panelIdPrefix + id;
		var infoId = this.infoIdPrefix + id;
		var errorId = this.errorIdPrefix + id;
		
		var pars = urlutil.params();
		sender.send( {
			cmd : "remove-notification",
			parameters : "uid="+pars['uid']+"&nid="+id,
			infoId : infoId,
			errorId : errorId,
			showProcessingMessage : true,
			success : function( params ) {
				document.getElementById( params.invoker.panelId ).innerHTML = "";
			}, invoker : {
				panelId : panelId
			}
		} );	
	};
		
}
