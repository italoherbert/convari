
var systemTester = new SystemTester();
function SystemTester() {	
	
	this.maximunConnectionFailAttempts = 1;
	this.maximunSessionFailAttempts = 1;
	this.connectionTestLocked = false;
	this.sessionTestLocked = false;
	
	this.verifyConnection = function( requestParams, count, locked ) {
		if( this.connectionTestLocked == true )
			if( locked == undefined || locked == null || locked == true )
				return;
		
		this.connectionTestLocked = true;
		var ajax = new Ajax();
		ajax.send( "servlet/ConnectionTest?cmd=connection", {
			success : function( params ) {		
				var source = params.invoker.source;
				var requestParams = params.invoker.requestParams;
				var count = params.invoker.count;				
				
				if( typeof( requestParams.connectionSuccess ) == 'function' ) {
					requestParams.connectionSuccess.call( this, { 
						count : parseInt( count ) 
					} ); 	
				}
				source.connectionTestLocked = false;
			},
			fail : function( params ) {
				var source = params.invoker.source;				
				var requestParams = params.invoker.requestParams;
				
				var count = parseInt( params.invoker.count );							
				if( count < source.maximunConnectionFailAttempts ) {
					source.verifyConnection( requestParams, count+1, false );		
				} else {
					if( typeof( requestParams.connectionFail ) == 'function' )
						requestParams.connectionFail.call( this );
					source.connectionTestLocked = false;
				}
			},
			invoker : {
				source : this,
				count : ( count ? count : 1 ),
				requestParams : requestParams
			}
		} );
	};
	
	this.verifySession = function( requestParams, count, locked ) {
		if( this.sessionTestLocked == true )
			if( locked == undefined || locked == null || locked == true )
				return;
		
		this.sessionTestLocked = true;				
		var ajax = new Ajax();
		ajax.send( "servlet/TestServlet?cmd=session", {
			success : function( params ) {
				var requestParams = params.invoker.requestParams;				

				var response = params.xmlhttp.responseText;
				requestParams.responseProcessed.call( this, { 
					status : ( response == 'true' ? 'exists' : 'notexists' ),
					invoker : requestParams.invoker
				} );				
			},
			fail : function( params ) {
				var source = params.invoker.source;
				var requestParams = params.invoker.requestParams;
			
				var count = parseInt( params.invoker.count );							
				if( count < source.maximunSessionFailAttempts ) {
					alert( count );
					source.verifySession( requestParams, count+1, false );
				} else {
					requestParams.responseProcessed.call( this, { 
						status : 'fail',
						invoker : requestParams.invoker
					} );
					this.sessionTestLocked = false;
				}												
			},
			invoker : {
				source : this,
				count : ( count ? count : 1 ),
				requestParams : requestParams
			}
		} );
				
	};
		
}
