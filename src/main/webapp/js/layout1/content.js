
var home_page_onload = function() {
	
};

var presentation_page_onload = function() {
	
};

var security_aspects_page_onload = function() {
	
};

var profile_page_onload = function() {
	profile.load();
};

var edit_profile_page_onload = function() {
	var pars = urlutil.params();
	document.user_image_upload_form.method = "post";
	document.user_image_upload_form.action = "servlet/ImageUploadServlet?cmd=user-image-upload&uid="+pars[ 'uid' ];
	
	document.getElementById( 'image_send_bt' ).value = messageManager.getLabel( 'label.button.send' );
	
	profile.editLoad();
};

var user_find_page_onload = function() {	
	userFinder.find();
};

var user_find_form_onload = function() {	
	var nameQueryTF = document.user_find_form.namequery;
	if( nameQueryTF ) {
		nameQueryTF.onkeypress = function( e ) {
			var code = ( window.event ? window.event.keyCode : e.keyCode );
			if( code == 13 ) {
				userFinder.params = { namequery : document.user_find_form.namequery.value }; 
				componentLoader.loadComponentByName( USER_FIND_PAGE );
				return false;
			}
			return true;
		};

	}
	
	document.getElementById( 'user_find_bt' ).onclick = function() {
		userFinder.find( { 
			responseProcessed : function() {
				var panelEl = document.getElementById( 'user_find_panel' );
				var linkEl = document.getElementById( 'user_find_link' );
				if( panelEl && linkEl ) {
					elementManager.show( panelEl, false );
					elementManager.show( linkEl, true );
				}
			} 
		} );
	};
};

var topics_page_onload = function() {	
	topicFinder.find();
};

var topic_find_page_onload = function() {
	topicFinder.find();
};

var topic_page_onload = function() {
	post.loadTopic();
};

var topic_find_form_include_cbs_visible = function() {
	elementManager.showById( 'include_cbs', true );
};

var user_topic_find_form_onload = function() {
	document.getElementById( 'topic_find_bt' ).onclick = function() {
		var pars = urlutil.params();
		topicFinder.find( { 
			userId : pars[ 'uid' ],
			responseProcessed : function() {
				var panelEl = document.getElementById( 'topic_find_panel' );
				var linkEl = document.getElementById( 'topic_find_link' );
				if( panelEl && linkEl ) {
					elementManager.show( panelEl, false );
					elementManager.show( linkEl, true );
				}
			} 
		} );
	};
};

var topic_find_form_onload = function() {	
	document.getElementById( 'topic_find_bt' ).onclick = function() {
		topicFinder.find( { 
			responseProcessed : function() {
				var panelEl = document.getElementById( 'topic_find_panel' );
				var linkEl = document.getElementById( 'topic_find_link' );
				if( panelEl && linkEl ) {
					elementManager.show( panelEl, false );
					elementManager.show( linkEl, true );
				}
			} 
		} );
	};
};

var topics_page_onload = function() {
	var pars = urlutil.params();
	topicFinder.find( { 
		userId : pars[ 'uid' ],
		responseProcessed : function() {
			if( user.visibility == 'owner' ) {
				document.getElementById( "user_topics_title" ).innerHTML = messageManager.getLabel( 'label.page.title.topics.owner' );
			} else {
				document.getElementById( "user_topics_title" ).innerHTML = messageManager.getLabel( 'label.page.title.topics.user', { 1 : user.name } );
			}
			
			var panelEl = document.getElementById( 'topic_find_panel' );
			var linkEl = document.getElementById( 'topic_find_link' );
			if( panelEl && linkEl ) {
				elementManager.show( panelEl, false );
				elementManager.show( linkEl, true );
			}
		} 
	} );
};

var posts_page_onload = function() {
	post.loadPostsCountByTopicOfUser();
};

var notifications_page_onload = function() {
	//notification.loadNotifications( { undefinedControlDate : true } );
};

var invitations_page_onload = function() {
	//contact.loadInvitations();
};

var configurations_page_onload = function() {
	userConfig.loadConfigPanel();
};

var comment_page_onload = function() {
	document.getElementById( 'comment_characteres_length' ).innerHTML = COMMENT_MAXIMUN_LENGTH;
	
	document.comment_form.message.onkeyup = function() {
		var length = this.value.length;
		if( length > COMMENT_MAXIMUN_LENGTH ) {
			this.value = this.value.substring( 0, length-1 );
		} else {
			var chLen = document.getElementById( 'comment_characteres_length' );
			chLen.innerHTML = COMMENT_MAXIMUN_LENGTH - length;
		}
	};
};

var add_topic_page_onload = function() {
	document.getElementById( 'add_topic_characteres_length' ).innerHTML = TOPIC_DESCRIPTION_MAXIMUN_LENGTH;
		
	document.add_topic_form.description.onkeyup = function() {
		var length = this.value.length;
		if( length > TOPIC_DESCRIPTION_MAXIMUN_LENGTH ) {
			this.value = this.value.substring( 0, length-1 );
		} else {
			var chLen = document.getElementById( 'add_topic_characteres_length' );
			chLen.innerHTML = TOPIC_DESCRIPTION_MAXIMUN_LENGTH - length;
		}
	};
};

var add_post_page_onload = function() {
	document.getElementById( 'add_post_characteres_length' ).innerHTML = POST_MESSAGE_MAXIMUN_LENGTH;
	
	document.add_post_form.message.onkeyup = function() {
		var length = this.value.length;
		if( length > POST_MESSAGE_MAXIMUN_LENGTH ) {
			this.value = this.value.substring( 0, length-1 );
		} else {
			var chLen = document.getElementById( 'add_post_characteres_length' );
			chLen.innerHTML = POST_MESSAGE_MAXIMUN_LENGTH - length;
		}
	};
};


