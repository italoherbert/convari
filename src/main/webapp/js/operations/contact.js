
var contact = new Contact();
function Contact() {
		
	this.contactControlDate = undefined;
	this.invitationControlDate = undefined;
	
	this.contactPanelIdPrefix = 'contact_panel_';
	this.invitationPanelIdPrefix = 'invitation_panel_';
		
	this.refreshContactsAndInvitations = function( requestParams ) {
		var pars = urlutil.params();
		
		var contactsElement = document.getElementById( 'contacts' );
		var invitationsElement = document.getElementById( 'invitations' );
		var newContactsElement = document.getElementById( 'new_contacts' );
		var newInvitationsElement = document.getElementById( 'new_invitations' );
								
		if( contactsElement && !newContactsElement ) {
			this.contactControlDate = undefined;
			this.loadContacts( requestParams );
			return;
		}			
		
		if( invitationsElement && !newInvitationsElement ) {
			this.invitationControlDate = undefined;
			this.loadInvitations( requestParams );
			return;
		}
		
		sender.send( {
			cmd : "get-invitation-news",
			parameters : {
				'uid' : pars['uid'],
				'contactsonly' : ( newInvitationsElement ? false : true ),
				'ccontroldate' : this.contactControlDate,
				'icontroldate' : this.invitationControlDate,
				'lang' : messageManager.getLang()
			},
			success : function( params ) {				
				var source = params.invoker.source;
				var contactPanelIdPrefix = source.contactPanelIdPrefix;
				var invitationPanelIdPrefix = source.invitationPanelIdPrefix;
				var newContactsElement = document.getElementById( 'new_contacts' );
				var newInvitationsElement = document.getElementById( 'new_invitations' );
				
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				var invitations = body.getElementsByTagName( 'invitations' )[0];

				var cControlDate = invitations.getAttribute( 'ccontroldate' );
				var iControlDate = invitations.getAttribute( 'icontroldate' );
				var invitationList = invitations.getElementsByTagName( 'invitation' );
				if( invitationList.length > 0 && (newContactsElement || newInvitationsElement) ) {
					
					var contactsHtml = "";
					var invitationsHtml = "";
					for( var i = 0; i < invitationList.length; i++ ) {
						var invitation = invitationList[ i ];
						var contact = invitation.getElementsByTagName( 'contact' )[0];
						
						var status = nodeValue( contact, 'status' );
						if( status == '0' || status == '3' || status == '4' ) {
							if( status == '0' ) {
								invitationsHtml += htmlBuilder.buildInvitationsPanel( {
									type : invitation.getAttribute( 'type' ),
									id : nodeValue( invitation, 'id' ),
									imagePath : nodeValue( contact, 'imagepath' ),
									contactId : nodeValue( contact, 'id' ),
									name : nodeValue( contact, 'name' ),
									lastname : nodeValue( contact, 'lastname' ),
									senddate : nodeValue( contact, 'senddate' ),
									responsedate : nodeValue( contact, 'responsedate' ),
									panelIdPrefix : invitationPanelIdPrefix
								} );
							} else if( status == '3' || status == '4' ) {
								var id = nodeValue( invitation, 'id' );
								var el = document.getElementById( invitationPanelIdPrefix + id );
								if( el )
									el.innerHTML = "";
							}
						} else if( status == '1' || status == '2' ) {
							if( status == '1' ) {
								contactsHtml += htmlBuilder.buildContactPanelItem( {
									id : nodeValue( contact, 'id' ), 
									name : nodeValue( contact, 'name' ), 
									lastname : nodeValue( contact, 'lastname' ), 
									imagePath : nodeValue( contact, 'imagepath' ),
									panelIdPrefix : contactPanelIdPrefix
								} );

								var id = nodeValue( invitation, 'id' );
								var el = document.getElementById( invitationPanelIdPrefix + id );
								if( el )
									el.innerHTML = "";								
							} else if( status == '2' ) {
								var id = nodeValue( contact, 'id' );
								var el = document.getElementById( contactPanelIdPrefix + id );
								if( el )
									el.innerHTML = "";
							}
						}												
					}
																				
					if( newContactsElement && contactsHtml.length > 0 ) {
						params.invoker.source.contactControlDate = cControlDate;					

						contactsHtml = "<span id='new_contacts'></span>"+contactsHtml;
						newContactsElement.id = undefined;
						newContactsElement.innerHTML = contactsHtml;											
					}
					
					if( newInvitationsElement && invitationsHtml.length > 0 ) {
						params.invoker.source.invitationControlDate = iControlDate;	

						invitationsHtml = "<span id='new_invitations'></span>"+invitationsHtml;
						newInvitationsElement.id = undefined;
						newInvitationsElement.innerHTML = invitationsHtml;											
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
	
	this.loadContacts = function( requestParams ) {
		var pars = urlutil.params();
		
		sender.send( {
			cmd : "get-contacts",
			parameters : "uid="+pars['uid'],
			infoId : 'contacts_panel_info_msg',
			errorId : 'contacts_panel_error_msg',
			success : function( params ) {
				var source = params.invoker.source;
				var panelIdPrefix = source.contactPanelIdPrefix;
				
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				var contacts = body.getElementsByTagName( 'contacts' )[0];
				var controlDate = contacts.getAttribute( 'controldate' );
				
				var html = "";
				var contactsList = contacts.getElementsByTagName( 'contact' );				
				for( var i = 0; i < contactsList.length; i++ ) {
					var contact = contactsList[ i ];
					html += htmlBuilder.buildContactPanelItem( {
						id : nodeValue( contact, 'id' ), 
						name : nodeValue( contact, 'name' ), 
						lastname : nodeValue( contact, 'lastname' ), 
						imagePath : nodeValue( contact, 'imagepath' ),
						panelIdPrefix : panelIdPrefix
					} );
				}
				params.invoker.source.contactControlDate = controlDate;

				html = "<span id='new_contacts'></span>"+html;
				document.getElementById( 'contacts' ).innerHTML = html;
				
				if( typeof( params.invoker.responseProcessed ) == 'function' )
					params.invoker.responseProcessed.call( this );	
			},
			invoker : {				
				source : this,
				responseProcessed : ( requestParams ? requestParams.responseProcessed : undefined )
			}
		} );	
	};
	
	this.loadInvitations = function( requestParams ) {
		var pars = urlutil.params();
				
		sender.send( {
			cmd : "get-invitations",
			parameters : {
				'uid' : pars['uid'],
				'lang' : messageManager.getLang()
			},
			infoId : 'invitations_info_msg',
			errorId : 'invitations_error_msg',
			showProcessingMessage : true,
			success : function( params ) {
				var panelIdPrefix = params.invoker.source.invitationPanelIdPrefix;

				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				var invitations = body.getElementsByTagName( 'invitations' )[0];
				var controlDate = invitations.getAttribute( 'controldate' );
				var invitationsList = invitations.getElementsByTagName( 'invitation' );
				var html = "";
				for( var i = 0; i < invitationsList.length; i++ ) {
					var invitation = invitationsList[ i ];
					var contactEl = invitation.getElementsByTagName( 'contact' )[0];
					
					html += htmlBuilder.buildInvitationsPanel( {
						type : invitation.getAttribute( 'type' ),
						id : nodeValue( invitation, 'id' ),
						imagePath : nodeValue( contactEl, 'imagepath' ),
						contactId : nodeValue( contactEl, 'id' ),
						name : nodeValue( contactEl, 'name' ),
						lastname : nodeValue( contactEl, 'lastname' ),
						senddate : nodeValue( contactEl, 'senddate' ),
						responsedate : nodeValue( contactEl, 'responsedate' ),
						panelIdPrefix : panelIdPrefix
					} );		
				}
				params.invoker.source.invitationControlDate = controlDate;

				html = "<span id='new_invitations'></span>"+html;
				document.getElementById( 'invitations' ).innerHTML = html;						
				
				if( typeof( params.invoker.responseProcessed ) == 'function' )
					params.invoker.responseProcessed.call( this );	
			},
			invoker : {
				source : this,
				responseProcessed : ( requestParams ? requestParams.responseProcessed : undefined )
			}
		} );	
	};
	
	this.loadInvitationStatus = function() {
		var pars = urlutil.params();
		
		sender.send( {
			cmd : "get-invitation-status",
			parameters : "uid="+pars['uid'],
			success : function( params ) {
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				var invitation = body.getElementsByTagName( 'invitation' )[0];
							
				var of = invitation.getAttribute( 'of' );
				var statusCode = invitation.getAttribute( 'status' );			
				
				var html = htmlBuilder.buildInvitationPanel( { of : of, status : statusCode } );
				document.getElementById( 'invite_contact' ).innerHTML = html;
			}
		} );	
	};
	
	this.manageInvitation = function( op ) {
		var pars = urlutil.params();
		
		sender.send( {
			cmd : "manage-invitation",
			parameters : "uid="+pars['uid']+"&op="+op,
			success : function( params ) {
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				var invitation = body.getElementsByTagName( 'invitation' )[0];
							
				var of = invitation.getAttribute( 'of' );
				var statusCode = invitation.getAttribute( 'status' );				

				var html = htmlBuilder.buildInvitationPanel( { of : of, status : statusCode } );
				document.getElementById( 'invite_contact' ).innerHTML = html;
								
			}
		} );
	};
	
	this.respInvitation = function( op, cid ) {	
		sender.send( {
			cmd : "manage-invitation",
			parameters : "uid="+cid+"&op="+op,
			success : function( params ) {
				var cid = params.invoker.cid;
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				var invitation = body.getElementsByTagName( 'invitation' )[0];
							
				var statusCode = invitation.getAttribute( 'status' );
				
				var html = htmlBuilder.buildInvitationStatusMessage( { statusCode : statusCode } );				
				document.getElementById( 'notification_invitation_op_'+cid ).innerHTML = html;
			
			},
			invoker : {
				cid : cid
			}
		} );
	};			
			
}
