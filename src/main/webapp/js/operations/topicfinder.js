
var topicFinder = new TopicFinder();
function TopicFinder() {		
	
	var instance = this;
	
	this.limit = undefined;
	this.pagesPanelId = undefined;
	
	this.params = undefined;
	
	var finder = new Finder();
	finder.cmd = "find-topics";
	finder.infoElementId = "topics_info_msg";
	finder.errorElementId = "topics_error_msg";
	finder.beforeSend = function( params ) {
		document.getElementById( 'topics' ).innerHTML = "";		
	};
	finder.success = function( params ) {
		var doc = params.doc;
		var body = doc.getElementsByTagName( 'body' )[0];				
		var topics = body.getElementsByTagName( 'topics' )[0];
		var html = "";
		var topicsList = topics.getElementsByTagName( 'topic' );
		for( var i = 0; i < topicsList.length; i++ ) {
			var topic = topicsList[i];
			
			var id = nodeValue( topic, 'id' );
			var description = nodeValue( topic, 'description' );
			var date = nodeValue( topic, 'date' );
			var user = topic.getElementsByTagName( 'user' )[0];
			var userId = nodeValue( user, 'id' );
			var userName = nodeValue( user, 'name' );
			var userImagePath = nodeValue( user, 'image' );
			
			html += htmlBuilder.buildTopicPanel( {
				id : id, description : description, date : date,
				userId : userId, userName : userName, 
				userImagePath : userImagePath
			} );
		}

		document.getElementById( 'topics' ).innerHTML = html;	
	};
	
	this.find = function( params ) {
		if( !params )
			params = this.params;
		var pagesPanelId = this.pagesPanelId;		
		var limit = this.limit;
		var userid = undefined;				
		var tquery = undefined;
		var uquery = undefined;
		var responseProcessed = undefined;
		if( params ) {
			if( params.userId )
				userid = params.userId;
			if( params.tquery )
				tquery = params.tquery;
			if( params.uquery )
				uquery = params.uquery;
			if( params.responseProcessed )
				responseProcessed = params.responseProcessed;
		}
		
		var form = document.topic_find_form;
		if( form ) {
			if( !tquery && form.include_description.checked &&	form.tquery.value.length > 0 )
				tquery = form.tquery.value;					
			if( !uquery && form.include_creater.checked && form.uquery.value.length > 0  )
				uquery = form.uquery.value;			
		}
				
		var buildParameters = function( parameters ) {
			if( tquery )
				parameters[ 'tquery' ] = tquery;
			if( uquery )
				parameters[ 'uquery' ] = uquery;
			if( userid )
				parameters[ 'userid' ] = userid;
			parameters[ 'lang' ] = messageManager.getLang();
		};
		
		finder.find( {
			pagesPanelId : pagesPanelId,
			limit : limit,			
			responseProcessed : responseProcessed,
			first : function( parameters ) {
				buildParameters.call( this, parameters );
				instance.params = undefined;
			},
			find : buildParameters,
			back : buildParameters,
			next : buildParameters			
		} );		
	};
						
}