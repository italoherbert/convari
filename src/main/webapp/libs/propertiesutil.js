
var propertiesUtil = new PropertiesUtil();
function PropertiesUtil() {
		
	this.process = function( params ) {
		if( !params )
			throw "params - parâmetro necessário.";
		if( !params.src )
			throw "params.src - parâmetro necessário.";
		
		var src = params.src;
				
		var ajax = new Ajax();
		ajax.send( src, {
			assync : params.assync != false,
			success : function( params ) {
				var responseText = params.xmlhttp.responseText;

				var responseProcessed = params.invoker.requestParams.responseProcessed;
				var messageReaded = params.invoker.requestParams.messageReaded;
				var requestInvoker = params.invoker.requestParams.invoker;
				var manager = params.invoker.requestParams.manager;
				
				var lines = responseText.split( /\n/gi );
				for( var i = 0; i < lines.length; i++ ) {
					var line = lines[ i ];
					var index = line.indexOf( '=' );
					if( index > -1 ) {
						var name = line.substring( 0, index );
						var value = line.substring( index+1 );
						if( typeof( messageReaded ) == 'function' )
							messageReaded.call( this, name, value, { invoker : requestInvoker } );
					}
				}
				
				if( typeof( responseProcessed ) == 'function' )
					responseProcessed.call( this, { manager : manager, responseText : responseText, invoker : requestInvoker } );				
			},				
			fail : params.fail,
			invoker : {
				requestParams : params
			}
		} );
	};
	
};