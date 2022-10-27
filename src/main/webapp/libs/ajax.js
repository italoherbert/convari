
function Ajax () {		
	
	this.sendGetRequest = function( action, params ) {			
		this.sendFormRequest( action, "get", null, params );
	};
	
	this.sendPostRequest = function( action, parameters, params ) {
		this.sendFormRequest( action, "post", parameters, params );
	};		
	
	this.sendFormRequest = function( action, method, parameters, params ) {
		
		var headers = ( params.header ? params.header : [
			{ name : 'Content-type', value : 'application/x-www-form-urlencoded; charset=UTF-8' }
		] );
				
		this.send( action, {
			method : method,
			params : parameters,
			headers : headers,
			invoker : ( params ? params.invoker : undefined ),
			assync : ( params ? params.assync : undefined ),
			success : ( params ? params.success : undefined ),
			fail : ( params ? params.fail : undefined ),
			waiting : ( params ? params.waiting : undefined ),
			requestSended : ( params ? params.requestSended : undefined ),
			responseReceived : ( params ? params.responseReceived : undefined ),
			ajaxSupport : ( params ? params.ajaxSupport : undefined )
		} );
	};		
				
	this.send = function ( src, params ) {		
		var xmlhttp = this.getXMLHttpRequest();			
		if ( xmlhttp != null ) {	  
	        xmlhttp.onreadystatechange = function() {	        
	        	if (xmlhttp.readyState == 4) {
	        		if ( params.responseReceived )
						if ( typeof( params.responseReceived ) == 'function' )
							params.responseReceived.call( this, {xmlhttp : xmlhttp, invoker : params.invoker} );
					if (xmlhttp.status == 200) {						
						if ( params.success )
							if ( typeof( params.success ) == 'function' )
								params.success.call( this, {xmlhttp : xmlhttp, invoker : params.invoker} );
					} else {
						if ( params.fail )
							if ( typeof( params.fail ) == 'function' )
								params.fail.call( this, {status : xmlhttp.status, invoker : params.invoker} );
					}
				} else {
					if ( params.waiting )
						if ( typeof( params.waiting ) == 'function' )
							params.waiting.call( this, { readyState : xmlhttp.readyState, invoker : params.invoker } );
				}
	        };  
			var method = ( params.method ? params.method : 'GET' );
			var assync = ( ( params.assync == undefined || params.assync == null ) ? true : params.assync );
			var post_params = ( params.params ? params.params : null );		
					
			xmlhttp.open( method, src, assync );
			if( params.headers ) {
				for( var i in params.headers ) {
					var header = params.headers[i];
					xmlhttp.setRequestHeader( header.name, header.value );				       
				}
			}
			xmlhttp.send( post_params );	  
			if ( params.requestSended )
				if ( typeof( params.requestSended ) == 'function' )
					params.requestSended.call( this, {invoker : params.invoker} );
	    } else {
			if ( params.ajax_support )
				if ( typeof( params.ajax_support ) == 'function' )
					params.ajax_support.call( this, {support : (xmlhttp != null), invoker : params.invoker} );
	    }	
		
	};  
	
	this.getXMLHttpRequest = function() {
	    if (window.ActiveXObject) {
	        var versoes = ["Microsoft.XMLHttp", "MSXML2.XMLHttp",
	                        "MSXML2.XMLHttp.3.0", "MSXML2.XMLHttp.4.0",
	                        "MSXML2.XMLHttp.5.0", "MSXML2.XMLHttp.6.0"];
	        for (var i=0; i<versoes.length; i++) {
	            try {
	                var xmlHttp = new ActiveXObject(versoes[i]);
	                return xmlHttp;
	            } catch (ex) {}
	        }
	    } else if (window.XMLHttpRequest) {			
	        return new XMLHttpRequest();
	    } else {
	        return null;
	    }
	};				
		
}