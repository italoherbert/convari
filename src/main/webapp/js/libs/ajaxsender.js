
var sender = new AjaxSender();
function AjaxSender() {	
			
	this.dependences = {
		getLabel : function( params ) {
			throw "getLabel - Chamada a metodo não implementado!";
		}
	};
	
	this.servlet = undefined;
		
	this.requestSended = undefined;
	this.responseReceived = undefined;
	this.failCatch = undefined;
	this.systemErrorCatch = undefined;
	
	this.send = function( requestParams ) {
		if( !requestParams )
			throw "AjaxRequest - params nulo.";			
		
		var servlet = requestParams.servlet;
		var cmd = requestParams.cmd;
		var invoker = requestParams.invoker;
		var infoId = requestParams.infoId;
		var errorId = requestParams.errorId;
		var validate = requestParams.validate;
		var parameters = requestParams.parameters;
		var beforeSend = requestParams.beforeSend;
		var valid = true;
		
		if( !servlet )
			servlet = this.servlet;	
		
		if( infoId ) {
			var el = document.getElementById( infoId );
			if( el != null)
				el.innerHTML = "";
		}
		if( errorId ) {
			var el = document.getElementById( errorId );
			if( el != null)
				el.innerHTML = "";
		}
		if( validate ) {
			if( typeof( validate ) == 'function' ) {
				var errors = validate.call( this, { invoker : invoker } );
				valid = this.validate( errors, errorId );
			}
		}
		
		if( valid ) {
			var reqParams = "";
			if( cmd )
				reqParams += "cmd="+cmd;			
			if( parameters ) {
				var params = undefined;				
				if( typeof( parameters ) == 'function' ) {
					params = parameters.call( this, { invoker : invoker } );					
				} else if( typeof( parameters ) == 'string' ) {
					params = new Array();
					var pars = parameters.split('&');
					for( var i in pars ) {
						var p = pars[i];
						var map = p.split( '=' );
						if( map.length == 2 )
							params[ map[0] ] = map[1];
						else throw "Formato inválido dos parâmetros da requisição. PARAMS="+parameters;
					}										
				} else {
					params = parameters;
				}				
				if( params ) {
					for( var name in params ) {
						var value = params[ name ];
						if( value != null && value != undefined && value != 'undefined' ) {
							if( value.length <= 0 )
								continue;
							if( reqParams.length > 0 ) 
								reqParams += "&";
							reqParams += name + "=" + value;
						}
					}
				}
				
			}			
			
			if( typeof( beforeSend ) == 'function' )
				beforeSend.call( this, { servlet : servlet, requestParameters : reqParams, invoker : invoker } );				
					
			var ajax = new Ajax();								
			ajax.sendPostRequest( servlet, reqParams, {
				requestSended : function( params ) {	
					var source = params.invoker.source;
					var requestParams = params.invoker.requestParams;										
					source.requestSended( params, requestParams.showProcessingMessage );
				},
				responseReceived : function( params ) {
					var source = params.invoker.source;					
					var requestParams = params.invoker.requestParams;					
					source.responseReceived( params, requestParams.showProcessingMessage );
				},
				success : function( params ) {
					var responseText = params.xmlhttp.responseText;
					var doc = loadXml( responseText ).documentElement;
									
					var requestParams = params.invoker.requestParams;
					var infoId = requestParams.infoId;
					var errorId = requestParams.errorId;
					var alertActive = requestParams.alertActive;
					var responseReceived = requestParams.responseReceived;
					var success = requestParams.success;					
					var error = requestParams.error;			
					var invoker = requestParams.invoker;					
					
					var source = params.invoker.source;
					
					var result = source.messagesProcess( doc, { 
						infoId : infoId, errorId : errorId,	alertActive : alertActive
					} );
					var infos = result.infos;
					var errors = result.errors;
					
					if( typeof( responseReceived ) == 'function' )
						responseReceived.call( this, { doc : doc, infos : infos, errors : errors, responseText : responseText, invoker : invoker } );
				
					if( doc.getAttribute( 'status' ) == 'ok' ) {
						if( typeof( success ) == 'function' )
							success.call( this, { doc : doc, infos : infos, responseText : responseText, invoker : invoker } );
					} else {
						if( typeof( error ) == 'function' )
							error.call( this, { doc : doc, errors : errors, responseText : responseText, invoker : invoker } );
					}		
					
					source.processSystemNode( { doc : doc, infos : infos, errors : errors, responseText : responseText, invoker : invoker } );					
				
				}, 
				fail : function( params ) {
					var requestParams = params.invoker.requestParams;
					var fail = requestParams.fail;
					var invoker = requestParams.invoker;
					var failCatch = params.invoker.source.failCatch;
					
					if( requestParams.showProcessingMessage == true )
						alertMessage.hideAndStopProcessingUpdate();
					
					if( typeof( fail ) == 'function' )
						fail.call( this, { status : params.status, invoker : invoker } );
					
					if( typeof( failCatch ) == 'function' )
						failCatch.call( this, { status : params.status, invoker : invoker } );
				},
				invoker : {
					requestParams : requestParams,
					source : this
				}
			} );
		}				
	};
			
	this.processSystemNode = function( params ) {
		if( params == null )
			throw "AjaxSender.processSystemNode - params nulo.";		
		
		var doc = params.doc;
		var headNode = doc.getElementsByTagName( 'head' )[0];
		var systemNode = headNode.getElementsByTagName( 'system' )[0];
		var statusValue = systemNode.getAttribute( 'status' );
		params.status = parseInt( statusValue );
		
		if( typeof( this.systemErrorCatch ) == 'function' )
			this.systemErrorCatch.call( this, params );					
	};
	
	this.messagesProcess = function( doc, params ) {
		if( params == null )
			throw "AjaxSender.messagesProcess - params nulo.";
		var info_msg_id = params.infoId;
		var error_msg_id = params.errorId;
		var alertActive = params.alertActive;
		
		var infos = new Array();
		var errors = new Array();
		
		var head = doc.getElementsByTagName( 'head')[0];
		var messagesList = head.getElementsByTagName( 'messages' )[0];
		var errorHTML = "";
		var infoHTML = "";
		
		var messages = messagesList.getElementsByTagName( 'message' );
		
		for( var i = 0; i < messages.length; i++ ) {
			var message = messages[i];
			var key = message.getAttribute( 'key' );
			var value;
			if( key ) {
				var params = new Array();				
				var paramsList = message.getElementsByTagName( 'param' );
				for( var j = 0; j < paramsList.length; j++ ) {
					var param = paramsList[ j ];
					var index = param.getAttribute( 'index' );
					var pvalue = param.firstChild.nodeValue;
					params[ index ] = pvalue;
				}				
				value = this.dependences.getLabel.call( this, key, params );
			} else {
				value = message.firstChild.nodeValue;	
			}
			
			if( message.getAttribute( 'type' ) == 'error' ) {
				errorHTML += htmlBuilder.buildMessage( { 
					text : value, 
					lineDelimiter : ( error_msg_id == null ? "\n" : "<br />" )
				} );
				errors.push( value );				
			} else {
				infoHTML += htmlBuilder.buildMessage( { 
					text : value, 
					lineDelimiter : ( info_msg_id == null ? "\n" : "<br />" )
				} );				
				infos.push( value );
			}
		}
		
		if( errorHTML.length > 0 ) {
			if( error_msg_id == null ) {
				if( alertActive == true )
					alert( errorHTML );				
			} else {
				var el = document.getElementById( error_msg_id );
				if( el != null)
					el.innerHTML = errorHTML;
			}
		}
		if( infoHTML.length > 0 ) {
			if( info_msg_id == null ) {
				if( alertActive == true )
					alert( infoHTML );				
			} else {				
				var el = document.getElementById( info_msg_id );
				if( el != null )
					el.innerHTML = infoHTML;		
			}
		}
		return { infos : infos, errors : errors };
	};
	
	this.validate = function( errors, error_msg_el_id ) {
		if( errors.length > 0 ) {
			var html = "";
			for( var i in errors )
				html += errors[i] + "<br />";		
			document.getElementById( error_msg_el_id ).innerHTML = html;
			return false;
		}	
		return true;
	};
	
}
