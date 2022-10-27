
var userFinder = new UserFinder();
function UserFinder() {		
	
	var instance = this;
	
	this.limit = undefined;
	this.pagesPanelId = undefined;
	
	this.params = undefined;
	
	var finder = new Finder();
	finder.cmd = "find-users";
	finder.infoElementId = "users_info_msg";
	finder.errorElementId = "users_error_msg";
	finder.beforeSend = function( params ) {
		document.getElementById( 'users' ).innerHTML = "";		
	};
	finder.success = function( params ) {
		var doc = params.doc;
		var body = doc.getElementsByTagName( 'body' )[0];				
		var users = body.getElementsByTagName( 'users' )[0];
		var html = "";
		var usersList = users.getElementsByTagName( 'user' );
		for( var i = 0; i < usersList.length; i++ ) {
			var user = usersList[i];
			
			var id = nodeValue( user, 'id' );
			var name = nodeValue( user, 'name' );
			var imagePath = nodeValue( user, 'image' );
			
			html += htmlBuilder.buildFindUserPanel( {
				id : id, name : name, imagePath : imagePath
			} );
		}

		document.getElementById( 'users' ).innerHTML = html;	
	};
	
	this.find = function( params ) {
		if( !params )
			params = this.params;
		var pagesPanelId = this.pagesPanelId;		
		var limit = this.limit;
		var namequery = undefined;
		var responseProcessed = undefined;
		if( params ) {
			if( params.namequery )
				namequery = params.namequery;
			if( params.responseProcessed )
				responseProcessed = params.responseProcessed;
		}
		
		var form = document.user_find_form;
		if( form ) {
			if( !namequery && form.namequery.value.length > 0  )
				namequery = form.namequery.value;			
		}
				
		var buildParameters = function( parameters ) {
			if( namequery )
				parameters[ 'namequery' ] = namequery;
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