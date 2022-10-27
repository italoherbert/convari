
var statusManager = new StatusManager();
function StatusManager() {
	
	this.lastMouseAndKeyCaptured = new Date();
	
	this.startMouseAndKeyCapture = function() {
		var instance = this;
		var element = document.getElementsByTagName( 'body' )[0]; 		
		element.onmousemove = function( e ) {
			instance.lastMouseAndKeyCaptured = new Date();
		};		
		element.onkeyup = function( e ) {			
			instance.lastMouseAndKeyCaptured = new Date();
		};
	};
	
	this.loadStatus = function( requestParams ) {		
		var pars = urlutil.params();
		sender.send( { 
			cmd : "get-status",
			parameters : "uid="+pars[ 'uid' ]+"&lastcontroldate="+this.lastMouseAndKeyCaptured.getTime(),
			success : function( params ) {
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				var user = body.getElementsByTagName( 'user' )[0];
				if( user != null ) {
					if( user.getAttribute( 'found' ) == 'true' ) {				
						var login = user.getElementsByTagName( 'login' )[0];
						var statusIndex = nodeValue( login, 'status' );
												
						statusPanel.markOptionByIndex( statusIndex );						
					}				
				}
				if( typeof( params.invoker.responseProcessed ) == 'function' )
					params.invoker.responseProcessed.call( this );			
			},
			fail : ( requestParams ? requestParams.fail : undefined ),
			invoker : {
				responseProcessed : ( requestParams ? requestParams.responseProcessed : undefined )
			}
		} );
	};

	this.setStatus = function( statusIndex ) {
		
		var params = urlutil.params();
		var uid = params['uid'];
			
		sender.send( { 
			cmd : "alter-status",
			parameters : "newstatus="+statusIndex+"&uid="+uid,
			success : function( params ) {
				var statusIndex = params.invoker.statusIndex;
				
				statusPanel.markOptionByIndex( statusIndex );						
			}, 
			alertActive : false,
			invoker : {
				statusIndex : statusIndex
			}
		} );
	};

	
}