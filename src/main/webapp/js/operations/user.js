
var user = new User();
function User() {
		
	this.visibility = 'public';	
	this.name = 'Desconhecido';
	this.logged = false;
			
	this.loadPermissions = function( requestParams ) {
		var pars = urlutil.params();

		sender.send( { 
			cmd : "get-permissions",
			parameters : "uid="+pars[ 'uid' ],
			success : function( params ) {
				var doc = params.doc;
				var source = params.invoker.source;
				var requestParams = params.invoker.requestParams;
                                
				var body = doc.getElementsByTagName( 'body' )[0];
				var user_el = body.getElementsByTagName( 'user' )[0];
				var redirect = user_el.getAttribute( 'redirect' );			
				if( redirect == 'true' ) {
					requestParams.redirect.call( this, { 
						uid : nodeValue( user_el, 'id' )  
					} );
				} else {				
					var permissions_el = body.getElementsByTagName( 'permissions' )[0];
					var operations = permissions_el.getElementsByTagName( 'operation' );
					for( var i = 0; i < operations.length; i++ ) {
						var operation = operations[i];
						var name = operation.getAttribute( 'name' );
						requestParams.operationProcessed.call( this, { element : operation, name : name } );
					}
					var visibilityIndex = nodeValue( body, 'visibility' );
					var vindexmap = { '0' : 'owner', '1' : 'contact', '2' : 'user', '3' : 'public' };
					
					source.visibility = vindexmap[ visibilityIndex ];
					source.name = nodeValue( user_el, 'name' );
					
					var logged = user_el.getAttribute( 'logged' );
					if( logged == 'true' )
						source.logged = true;					
					
					requestParams.responseProcessed.call( this, {} );
				}
			},
			error : requestParams.error, 
			fail : requestParams.fail,
			invoker : {
				source : this,
				requestParams : requestParams
			}
		} );
	};

	this.loadBasicProfile = function( requestParams ) {
		var pars = urlutil.params();
		sender.send( { 
			cmd : "get-basic-user-data",
			parameters : "uid="+pars[ 'uid' ],
			infoId : 'basic_profile_info_msg',
			errorId : 'basic_profile_error_msg',
			success : function( params ) {				
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				var user = body.getElementsByTagName( 'user' )[0];
				if( user != null ) {
					if( user.getAttribute( 'found' ) == 'true' ) {										
						params.invoker.requestParams.responseProcessed.call( this, {
							image : nodeValue( user, 'image' ),
							name : nodeValue( user, 'name' ),
							lastname : nodeValue( user, 'lastname' ),
							professionalData : nodeValue( user, 'professionaldata' )
						} );						
					}				
				}
			},
			invoker : {
				requestParams : requestParams
			}
		} );
	};
	
	this.loadNavigationMenuUser = function( requestParams ) {
		var pars = urlutil.params();
		sender.send( { 
			cmd : "get-navigation-user-info",
			parameters : "uid="+pars[ 'uid' ],
			success : function( params ) {
				var requestParams = params.invoker.requestParams;
				
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				var user = body.getElementsByTagName( 'user' )[0];
				var contactUser = body.getElementsByTagName( 'contact' )[0];
				if( user != null ) {
					var contact = undefined;									
					if( contactUser != null ) {
						if( contactUser.getAttribute( 'isOwner' ) == 'false' ) {							
							contact = { 
								id : nodeValue( contactUser, 'id' ), 
								name : nodeValue( contactUser, 'name' ) 
							};																
						}
					}
					
					requestParams.responseProcessed.call( this, {
						id : nodeValue( user, 'id' ),
						name : nodeValue( user, 'name' ),
						contact : contact
					} );																						
				}
			},
			invoker : {
				requestParams : requestParams
			}
		} );
	};

}