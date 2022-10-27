
var menupanel_page_onload = function() {
	menuPanel.elementId = 'menupanel';
	menuPanel.menubar = false;
	menuPanel.itens[ TOPIC_FIND_PAGE ] = { label : messageManager.getLabel( 'label.menu.option.home' ), onclick : 'javascript:componentLoader.loadComponentByName( TOPIC_FIND_PAGE );' };
	menuPanel.itens[ PROFILE_PAGE ] = { label : messageManager.getLabel( 'label.menu.option.profile' ), onclick : 'javascript:componentLoader.loadComponentByName( PROFILE_PAGE );' };

	menupanelManager.loadCounts( {
		includetcount : true,
		responseProcessed : function( params ) {
			var notificationsCount = params.notificationsCount;
			var pendingInvitationsCount = params.pendingInvitationsCount;
			var topicsCount = params.topicsCount;
			var isOwner = params.isOwner;
						
			if( notificationsCount > -1 ) {
				var label = messageManager.getLabel( 'label.menu.option.notifications', 
					{ '1' : '<span id=\"notifications_count\">'+notificationsCount+'</span>' } 
				);
				menuPanel.itens[ NOTIFICATIONS_PAGE ] = { 
					label : label, 
					onclick : 'javascript:componentLoader.loadComponentByName( NOTIFICATIONS_PAGE );' 
				};
			}
			
			if( pendingInvitationsCount > -1 ) {
				var label = messageManager.getLabel( 'label.menu.option.pending.invitations', 
					{ '1' : '<span id=\"pending_invitations_count\">'+pendingInvitationsCount+'</span>' } 
				);					
				menuPanel.itens[ INVITATIONS_PAGE ] = { 
					label : label,
					onclick : 'javascript:componentLoader.loadComponentByName( INVITATIONS_PAGE );' 
				};
			}
						
			if( topicsCount > -1 ) {
				var label = messageManager.getLabel( 'label.menu.option.topics', 
					{ '1' : '<span id=\"user_topics_count\">'+topicsCount+'</span>' } 
				);					
					
				menuPanel.itens[ TOPICS_PAGE ] = { 
					label : label,
					onclick : 'javascript:componentLoader.loadComponentByName( TOPICS_PAGE );' 
				};
			}
			
			if( isOwner == 'true') {
				menuPanel.itens[ CREATE_TOPIC_PAGE ] = { 
					label : messageManager.getLabel( 'label.menu.option.create.topic' ), 
					onclick : 'javascript:componentLoader.loadComponentByName( CREATE_TOPIC_PAGE );' 
				};
				menuPanel.itens[ CONFIGURATIONS_PAGE ] = { 
					label : messageManager.getLabel( 'label.menu.option.configurations' ), 
					onclick : 'javascript:componentLoader.loadComponentByName( CONFIGURATIONS_PAGE );' 
				};
			}
									
			menuPanel.build();
			componentLoader.loadComponentByName( LOAD_CONTENT_PAGE );			
		} 
	} );
};
