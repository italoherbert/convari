
var post = new Post();
function Post() {		
	
	var controlDate = -1;
	
	this.autoRefresh = false; 
	this.autoScroll = true;
		
	this.autoRefreshOnclick = function( el ) {
		this.autoRefresh = el.checked;
		var postsDiv = document.getElementById( 'posts_div' );
		var style = ( postsDiv.style ? postsDiv.style : postsDiv );
		style.height = ( el.checked ? '300px' : 'auto' );
		style.overflowY = ( el.checked ? 'scroll' : 'auto' );
	};
		
	this.refreshTopic = function( requestParams ) {
		
		if( !document.getElementById( 'new_post' ) ) {
			if( requestParams )
				if( typeof( requestParams.responseProcessed ) == 'function' )
					requestParams.responseProcessed.call( this );
			return;
		}
		
		var pars = urlutil.params();
		
		sender.send( {
			cmd : "get-posts",
			parameters : {
				'uid' : pars[ 'uid' ],
				'tid' : pars[ 'tid' ],
				'lang' : messageManager.getLang(),
				'controldate' : (controlDate > -1 ? controlDate : undefined )
			},
			infoId : 'topic_info_msg',		
			errorId : 'topic_error_msg',
			success : function( params ) {
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];

				var html = "";
				var posts = body.getElementsByTagName( 'posts')[0];
				controlDate = posts.getAttribute( 'controldate' );
				var postList = posts.getElementsByTagName( 'post' );
				for( var i = 0; i < postList.length; i++ ) {
					var post = postList[i];
					
					var pMessage = nodeValue( post, 'message' );
					var pDate = nodeValue( post, 'date' );
					
					var pUser = post.getElementsByTagName( 'user' )[0];
					var pUserId = nodeValue( pUser, 'id' );
					var pUserName = nodeValue( pUser, 'name' );
					var pUserImagePath = nodeValue( pUser, 'image' );
					
					html += htmlBuilder.buildPostPanel( {
						message : pMessage, date : pDate, 
						userId : pUserId, userName : pUserName,
						userImagePath : pUserImagePath
					} );												
				}
				html += "<span id='new_post'></span>";
				var new_post_el = document.getElementById( 'new_post' );
				if( new_post_el ) {
					new_post_el.id = undefined;
					new_post_el.innerHTML = html;
					if( params.invoker.autoScroll ) {
						var postsDiv = document.getElementById( 'posts_div' );
						postsDiv.scrollTop = postsDiv.scrollHeight;
					}
				}
				if( typeof( params.invoker.responseProcessed ) == 'function' )
					params.invoker.responseProcessed.call( this );				
			},
			invoker : {
				autoScroll : this.autoScroll,
				responseProcessed : ( requestParams ? requestParams.responseProcessed : undefined )
			}
		} );					
	};
	
	this.loadTopic = function( requestParams ) {
		var pars = urlutil.params();
		
		sender.send( {
			cmd : "get-topic",
			parameters : {
				'uid' : pars[ 'uid' ],
				'tid' : pars[ 'tid' ],
				'lang' : messageManager.getLang(),
				'controldate' : (controlDate > -1 ? controlDate : undefined )
			},
			infoId : 'topic_info_msg',	
			errorId : 'topic_error_msg',
			showProcessingMessage : true,
			success : function( params ) {				
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				var topic = body.getElementsByTagName( 'topic' )[0];
				
				var tDescription = nodeValue( topic, 'description' );
				var tDate = nodeValue( topic, 'date' );
				
				var tUser = topic.getElementsByTagName( 'user' )[0];
				var tUserId = nodeValue( tUser, 'id' );
				var tUserName = nodeValue( tUser, 'name' );
				var tUserImagePath = nodeValue( tUser, 'image' );
				
				var thtml = htmlBuilder.buildVisualizationTopicPanel( {
					description : tDescription, date : tDate,
					userId : tUserId, userName : tUserName, userImagePath : tUserImagePath
				} );

				var html = "";
				var posts = topic.getElementsByTagName( 'posts')[0];
				controlDate = posts.getAttribute( 'controldate' );
				var postList = posts.getElementsByTagName( 'post' );
				for( var i = 0; i < postList.length; i++ ) {
					var post = postList[i];
					
					var pMessage = nodeValue( post, 'message' );
					var pDate = nodeValue( post, 'date' );
					
					var pUser = post.getElementsByTagName( 'user' )[0];
					var pUserId = nodeValue( pUser, 'id' );
					var pUserName = nodeValue( pUser, 'name' );
					var pUserImagePath = nodeValue( pUser, 'image' );
					
					html += htmlBuilder.buildPostPanel( {
						message : pMessage, date : pDate, 
						userId : pUserId, userName : pUserName,
						userImagePath : pUserImagePath
					} );				
				}
				
				document.getElementById( 'topic' ).innerHTML = thtml;			
				document.getElementById( 'posts' ).innerHTML = html;
				
				if( typeof( params.invoker.responseProcessed ) == 'function' )
					params.invoker.responseProcessed.call( this );							
			},
			invoker : {
				responseProcessed : ( requestParams ? requestParams.responseProcessed : undefined )
			}
		} );
					
	};		
				
	this.addNewTopic = function() {		
		sender.send( {
			cmd : "add-topic",
			parameters : function( params ) {
				var pars = urlutil.params();
				return {
					'uid' : pars[ 'uid' ],
					'description' : document.add_topic_form.description.value,
					'visibility' : document.add_topic_form.topic_visibility_cb.selectedIndex
				};
			},
			infoId : 'add_topic_info_msg',
			errorId : 'add_topic_error_msg',
			showProcessingMessage : true,
			success : function( params ) {
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				var topic = body.getElementsByTagName( 'topic' )[0];
				var tid = topic.getAttribute( 'id' );
				window.location.href = urlutil.setParams( { 'page' : 'topic', 'tid' : tid } );
			},
			validate : function() {
				var form = document.add_topic_form;
				return validator.validateAll( [
  				    { type : validator.REQUIRED, labelKey : 'label.field.topic', value : form.description.value } 				   				
  				] );
			},
			invoker : {
				source : this
			}
		} );
	};
	
	this.addNewPost = function() {
		sender.send( {
			cmd : 'add-post',
			parameters : function( params ) {
				var pars = urlutil.params();
				return {
					'uid' : pars[ 'uid' ],
					'tid' : pars[ 'tid' ],
					'message' : document.add_post_form.message.value
				};
			},
			infoId : 'add_post_info_msg',
			errorId : 'add_post_error_msg',
			showProcessingMessage : true,
			success : function( params ) {
				params.invoker.source.refreshTopic();				
				document.add_post_form.reset();
			},
			validate : function( params ) {
				var form = document.add_post_form;
				return validator.validateAll( [
 				    { type : validator.REQUIRED, labelKey : 'label.field.post', value : form.message.value } 				   				
 				] );
			},
			invoker : {
				source : this
			}
		} );							
	};
	
}