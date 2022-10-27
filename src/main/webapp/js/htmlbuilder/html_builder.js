
var htmlBuilder = new HtmlBuilder();
function HtmlBuilder() {

	this.dependences = {
		getLabel : function( key, params ) {
			throw "getLabel - Implementação necessária.";
		},
		getLabelForParams : function( key, params ) {
			throw "getLabelForParams - Implementação necessária.";
		}
	};
	
	this.buildIndisponibleSystemMessage = function() {
		var html = "";
		html += "<div class='error-msg'>";
		html += this.dependences.getLabel( 'error.system.indisponible' );
		html += "</div>";
		html += "<div class='info-msg'>";
		html += this.dependences.getLabel( 'info.system.indisponible' );
		html += "</div>";
		return html;
	};
	
	this.buildMessage = function( params ) {
		var text = params.text;
		var lineDelimiter = params.lineDelimiter; 		
		if( !lineDelimiter )
			lineDelimiter = "\n";
		
		var html = "";		
		if( text == ':line' )
			html += lineDelimiter;
		else html += text;
		html += lineDelimiter;		
		return html;
	};
	
	this.buildTopPanel = function( params ) {		
		var userId = params.id;
		var userName = params.name;
		
		var html = "<div style='padding:2px 10px'>";							 					
		html += "<a id=\"navigation_user_name\" href=\"#\" onclick=\"urlutil.setParamAndRedirect( 'uid', " + userId + " );\">";
		html += userName;
		html += "</a>";
		
		if( params.contact ) {
				var contactId = params.contact.id;
				var contactName = params.contact.name;
				
				html += "&nbsp;&raquo;&nbsp;";						
				html += "<a href=\"#\" onclick=\"urlutil.setParamAndRedirect( 'uid', " + contactId + " );\">";
				html += contactName;
				html += "</a>";																	
		}
		
		html += "&nbsp;&raquo;&nbsp;";						
		html += "<span id='navigation_page_label'>" + contentLabels[ currentContentPage ] + "</span>";	
		
		html += "</div>";
		return html;
	};
		
	this.buildBasicProfile = function( params ) {
		var name = params.name;
		var lastname = params.lastname;
		var professionalData = params.professionalData;
		var html = name+" "+lastname;
		if( professionalData && professionalData.length > 0 )
			professionalData = professionalData.split( ' ' )[0];
			professionalData = professionalData.replace( /\W/, '' );
			html += "<br />"+professionalData;
		return html;			
	};
	
	this.buildProfileFieldValue = function( params ) {
		var value = params.value;
		if( value && value.length > 0 )
			value += "<br />";
		return value;
	};
	
	this.buildUserImage = function( params ) {
		var imagePath = params.imagePath;
		return "<img id='user_image_img' src='" + imageUtil.buildSRC( imagePath ) + "' class='user-image' />";
	};
			
	this.buildFindUserPanel = function( params ) {
		var id = params.id;
		var name = params.name;
		var imagePath = params.imagePath;
		
		var html = "<div class='panel'>";
				
		html += "<div class='panel-content'>";
		
		html += "<table border='0'>";
		html += "<tr>";
		html += "<td>";
		html += "<a href='#' onclick='javascript:urlutil.setParamAndRedirect( \"uid\", "+id+" );'>";
		html += "<img src='"+ imageUtil.buildSRC( imagePath )+"' width='50px' height='50px' />";
		html += "</a>";
		html += "</td>";
		html += "<td align='left' valign='top' padding='5px'>";
		html += name;
		html += "</td>";
		html += "</tr>";
		html += "</table>";
		
		html += "</div>";
		
		html += "</div>";
		html += "<br />";
		return html;
	};
	
	this.buildVisualizationTopicPanel = function( params ) {		
		var description = params.description;
		var date = params.date;
		var userId = params.userId;
		var userName = params.userName;
		var userImagePath = params.userImagePath;
		
		var html = "<div>";
		
		html += "<div class='title'>";
		html += this.buildWithDateTitlePanel( date, 'label.created.by', { '1' : userName } );
		html += "</div>";
		
		html += "<div class='panel-content post-panel-content-topic'>";		
		html += "<a href='#' onclick='javascript:urlutil.setParamAndRedirect( \"uid\", "+userId+" );'>";
		html += "<img src='" + imageUtil.buildSRC( userImagePath ) + "' class='post-panel-topic-img' />";
		html += "</a>";		
		html += description;
		html += "</div>";		
				
		html += "</div>";
		
		return html;
	};
	
	this.buildTopicPanel = function( params ) {
		var id = params.id;
		var description = params.description;
		var date = params.date;
		var userId = params.userId;
		var name = params.userName;
		var userImagePath = params.userImagePath;

		var html = "<div class='panel'>";

		html += "<div class='panel-title'>";
		html += this.buildWithDateTitlePanel( date, 'label.created.by', { '1' : name } );
		html += "</div>";
		
		html += "<div class='panel-content post-panel-content'>";
		html += "<a href='#' onclick=\"javascript:urlutil.setParamsAndRedirect( { 'page' : TOPIC_PAGE, 'uid' : "+userId+", 'tid' : "+id+" } );\">";
		html += "<img src='" + imageUtil.buildSRC( userImagePath ) + "' class='post-panel-img' />";
		html += "</a>";		
		html += "<a class='text' href='#' onclick=\"javascript:urlutil.setParamsAndRedirect( { 'page' : TOPIC_PAGE, 'tid' : "+id+" } );\">";
		html += description;
		html += "</a>";
		html += "</div>";
				
		html += "</div>";

		html += "<br />";
		return html;
	};
	
	this.buildPostPanel = function( params ) {
		var message = params.message;
		var date = params.date;		
		var userId = params.userId;
		var userName = params.userName;
		var userImagePath = params.userImagePath;
		
		var html = "<div class='panel'>";
		html += "<div class='panel-title'>";
		html += this.buildWithDateTitlePanel( date, 'label.posted.by', { '1' : userName } );
		html += "</div>";
				
		html += "<div class='panel-content post-panel-content'>";		
		html += "<a href='#' onclick='javascript:urlutil.setParamAndRedirect( \"uid\", "+userId+" );'>";
		html += "<img src='" + imageUtil.buildSRC( userImagePath ) + "' class='post-panel-img' />";
		html += "</a>";		
		html += message;
		html += "</div>";					
		
		html += "</div>";
		html += "<br />";
		
		return html;		
	};
	
	this.buildContactPanelItem = function( params ) {
		var id = params.id;
		var name = params.name;
		var lastname = params.lastname;
		var imagePath = params.imagePath;		
		var prefix = params.panelIdPrefix;
				
		var html = "<span id='"+prefix+id+"'>"; 
		html += "<div class='contacts-panel-item' onclick='urlutil.setParamAndRedirect( \"uid\", " + id + ");'>";
		
		html += "<table class='table-layout'>";
		html += "<tr>";
		html += "<td valign='middle'>";
		html += "<img src='" + imageUtil.buildSRC( imagePath ) + "' class='contacts-panel-item-image' />";
		html += "</td>";
		html += "<td align='left' valign='middle'>";
		html += "<span class='contacts-panel-item-text'>"+name+" "+lastname+"</span>";					
		html += "</td>";
		html += "</tr>";
		html += "</table>";
		
		html += "</div>";
		html += "</span>";
		return html;
	};
	
	this.buildInvitationPanel = function( params ) {
		var of = params.of;
		var status = params.status;
		
		var html = "";
		if( status == '0' || status == '3' ) {
			html += "<a href='#' onclick='javascript:contact.manageInvitation( \"invite\" );'>";
			html += this.dependences.getLabel( 'label.invitation.invite' );
			html += "</a>";
		} else if( status == '1' ) {
			if( of == 'from' ) {
				html += "<a href='#' onclick='javascript:contact.manageInvitation( \"cancel\" );'>";
				html += this.dependences.getLabel( 'label.invitation.cancel' );
				html += "</a>";
			} else if( of == 'to' ) {
				html += "<a href='#' onclick='javascript:contact.manageInvitation( \"accept\" );'>";
				html += this.dependences.getLabel( 'label.invitation.accept' );
				html += "</a>";
				html += "<br />";
				html += "<a href='#' onclick='javascript:contact.manageInvitation( \"refuse\" );'>";
				html += this.dependences.getLabel( 'label.invitation.refuse' );
				html += "</a>";
			}
		} else if( status == '2' ) {
			html += "<a href='#' onclick='javascript:contact.manageInvitation( \"remove\" );'>";
			html += this.dependences.getLabel( 'label.invitation.remove' );
			html += "</a>";
		}
		return html;
	};
	
	this.buildInvitationStatusMessage = function( params ) {
		var statusCode = params.statusCode;
		
		var status;
		if( statusCode == '0' ) {
			status = this.dependences.getLabel( 'label.invitation.status.canceled' );
		} else if( statusCode == '1' ) {
			status = this.dependences.getLabel( 'label.invitation.status.pending' );
		} else if( statusCode == '2' ){
			status = this.dependences.getLabel( 'label.invitation.status.accepted' );
		} else if( statusCode == '3' ) {
			status = this.dependences.getLabel( 'label.invitation.status.refused' );
		} else {
			status = this.dependences.getLabel( 'label.invitation.status.invalid' );
		}
		return "<span class='info-msg'>"+status+"</span>";
	};
		
	this.buildInvitationsPanel = function( params ) {				
		var type = params.type;

		var id = params.id;
		var imagePath = params.imagePath;
		var contactId = params.contactId;
		var name = params.name;
		var lastname = params.lastname;
		var senddate = params.senddate;
		var prefix = params.panelIdPrefix;

		var html = "<span id='"+prefix+id+"'>";
		html += "<div class='panel'>";
		html += "<div class='panel-title'>";
		
		if( type == 'from' ) {
			html += this.buildWithDateTitlePanel( senddate, 'label.pending.invitation.panel.title.from', { '1' : name, '2' : lastname } );
		} else if( type == 'to' ) {
			html += this.buildWithDateTitlePanel( senddate, 'label.pending.invitation.panel.title.to', { '1' : name, '2' : lastname } );
		} else {
			html += this.buildWithDateTitlePanel( senddate, 'label.pending.invitation.panel.title.unknown' );
		}
		
		html += "</div>";
		
		html += "<div class='panel-content'>";
		html += "<table border='0'>";
		html += "<tr>";
		html += "<td>";
		html += "<a href='#' onclick='urlutil.setParamAndRedirect( \"uid\", "+contactId+" );'>";
		html += "<img src='" + imageUtil.buildSRC( imagePath ) + "' width='100px' height='100px' />";
		html += "</a>";
		html += "</td>";
		html += "<td align='left' valign='top' padding='5px'>";
		
		html += "<div style='padding:5px 20px'>";
		if( type == 'from' ) {
			html += this.dependences.getLabel( 'label.pending.invitation.panel.message.from', { '1' : name } );
		} else if( type == 'to' ) {
			html += this.dependences.getLabel( 'label.pending.invitation.panel.message.to' );
		}
		
		html += "<br /><br />";
		
		html += "<div id='notification_invitation_op_"+contactId+"'>";		
		
		if( type == 'from' ) {
			html += "<a href='#' onclick='javascript:contact.respInvitation( \"cancel\", \""+contactId+"\" )'>";
			html += this.dependences.getLabel( 'label.invitation.cancel' );
			html += "</a>";
			html += "<br />";			
		} else if( type == 'to' ) {
			html += "<a href='#' onclick='javascript:contact.respInvitation( \"accept\", \""+contactId+"\" )'>";
			html += this.dependences.getLabel( 'label.invitation.accept' );
			html += "</a>";
			html += "<br />";
			html += "<a href='#' onclick='javascript:contact.respInvitation( \"refuse\", \""+contactId+"\" )'>";
			html += this.dependences.getLabel( 'label.invitation.refuse' );
			html += "</a>";
			html += "<br />";
		}
		html += "</div>";
		
		html += "</div>";
		
		html += "</td>";
		html += "</table>";
		
		html += "</div>";
		
		html += "</div>";
		html += "<br />";
		html += "</span>";
		return html;
	};
	
	this.buildNotificationPanel = function( params ) {
		var id = params.id;
		var text = params.text;
		var date = params.date;
		
		var panelId = params.panelIdPrefix + id;
		var panelInfoId = params.infoIdPrefix + id;
		var panelErrorId = params.errorIdPrefix + id;
		
		var rlink = "<a href='#' onclick='javascript:notification.removeNotification( "+id+" )'>";
		rlink += "remover";
		rlink += "</a>";
		
		var html = "<span id='"+panelInfoId+"' class='info-msg'></span>";
		html += "<span id='"+panelId+"'>";
		html += "<div class='panel'>";
		

		html += "<div class='panel-title'>";
		html += this.buildWithDateTitlePanel( date, 'label.notification.panel.title', { '1' : rlink } );
		html += "</div>";
		
		html += "<div class='panel-content'>";
		html += text;
		html += "<span id='"+panelErrorId+"' class='error-msg'></span>";
		html += "</div>";		
		html += "</div>";
		html += "</span>";
		html += "<br />";
		return html;
	};
	
	this.buildWithDateTitlePanel = function( date, lkey, lparams ) {
		var text = this.dependences.getLabelForParams( { label : { key : lkey, params : lparams } } );
		
		var html = "<table class='table-layout' width='100%'>";
		html += "<tr>";
		html += "<td align='left' valign='top'>";
		html += "<span class='panel-title-text'>" + text + "</span>";
		html += "</td>";
		html += "<td align='right' valign='top'>";
		html += "<span class='panel-title-date'>" + date + "<span>";
		html += "</td>";
		html += "</tr>";
		html += "</table>";
		return html;
	};
	
}