var LOAD_CONTENT_PAGE = "load-content-page";

var USER_FIND_PAGE = "user-find";
var TOPIC_FIND_PAGE = "topic-find";

var TOPIC_PAGE = "topic";

var HOME_PAGE = "home";
var PRESENTATION_PAGE = "presentation";
var SECURITY_ASPECTS_PAGE = "security-aspects";
var PROFILE_PAGE = "profile";
var TOPICS_PAGE = "topics";
var POSTS_PAGE = "posts";
var EDIT_PROFILE_PAGE = "edit-profile";
var INVITATIONS_PAGE = "invitations";
var CREATE_TOPIC_PAGE = "create-topic";
var CONFIGURATIONS_PAGE = "configurations";
var NOTIFICATIONS_PAGE = "notifications";
var COMMENT_PAGE = "comment";

var currentContentPage = HOME_PAGE;

var COMMENT_MAXIMUN_LENGTH = 512;
var TOPIC_DESCRIPTION_MAXIMUN_LENGTH = 512;
var POST_MESSAGE_MAXIMUN_LENGTH = 512;

htmlBuilder.dependences.getLabel = dependencesImpl.getLabel;
htmlBuilder.dependences.getLabelForParams = dependencesImpl.getLabelForParams;

profile.dependences.getLabel = dependencesImpl.getLabel;

componentLoader.dependences.getVisibility = function() {
	return user.visibility;
};

urlutil.beforeRedirect = function( url ) {
	if( user.visibility == OWNER_VISIBILITY 
			|| user.visibility == CONTACT_VISIBILITY 
			|| user.visibility == USER_VISIBILITY ) { 
		controller.finishAll();
	}
};

topicFinder.pagesPanelId = 'topics_pagespanel';
topicFinder.limit = 3;

userFinder.pagesPanelId = 'users_pagespanel';
userFinder.limit = 3; 

languageManager.beforeLanguageChange = function( params ) {
	
};

var headMenubar = {
	menubar : true,
	align : 'right',
	itens : []
};	

var contentLabels = new Array();

window.onload = function() {			
	var pars = urlutil.params();

	messageManager.initialize( MESSAGES_CONFIG_FILE, { context : MAIN_CONTEXT, lang : pars[ 'lang' ] } ); 
	componentLoader.initialize( MAIN_LOADER_FILE );
		
	contentLabels[ HOME_PAGE ] = messageManager.getLabel( "label.page.title.home" );
	contentLabels[ PROFILE_PAGE ] = messageManager.getLabel( 'label.page.title.profile' ); 
	contentLabels[ INVITATIONS_PAGE ] = messageManager.getLabel( 'label.page.title.pending.invitations' );
	contentLabels[ CREATE_TOPIC_PAGE ] = messageManager.getLabel( 'label.page.title.create.topic' );
	contentLabels[ CONFIGURATIONS_PAGE ] = messageManager.getLabel( 'label.page.title.configurations' );
	contentLabels[ NOTIFICATIONS_PAGE ] = messageManager.getLabel( 'label.page.title.notifications' );
	contentLabels[ TOPICS_PAGE ] = messageManager.getLabel( 'label.page.title.topics' );
	contentLabels[ COMMENT_PAGE ] = messageManager.getLabel( 'label.page.title.comment' );
	contentLabels[ USER_FIND_PAGE ] = messageManager.getLabel( 'label.page.title.find.user' );
	contentLabels[ TOPIC_FIND_PAGE ] = messageManager.getLabel( 'label.page.title.find.topic' );
	contentLabels[ TOPIC_PAGE ] = messageManager.getLabel( 'label.page.title.topic' );
			
	user.loadPermissions( {
		redirect : function( params ) {
			var uid = params.uid;
			window.location.href = 	urlutil.setParam( 'uid', uid );
		},
		operationProcessed : function( params ) {
			var element = params.element;
			var name = params.name;		
			componentLoader.operations[ name ] = { name : name, element : element };
		},
		responseProcessed : function( params ) {			
			componentLoader.load();
		},
		error : function( params ) {			
			var errorHtml = "";
			for( var i in params.errors ) {
				var error = params.errors[ i ];
				errorHtml += error + "<br />";
			}				
			document.body.innerHTML = errorHtml;
		}, 
		fail : function( params ) {
			componentLoader.load();
		}
	} );
		
};
