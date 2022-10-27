
var urlutil = new UrlUtil();
function UrlUtil() {	
		
	this.beforeRedirect = undefined;
	
	this.redirect = function( url ) {
		if( typeof( this.beforeRedirect ) == 'function' )
			this.beforeRedirect.call( this, url );
		window.location.href = url;		
	};
	
	this.refresh = function() {
		this.redirect( this.rmAnchorName() );
	};
	
	this.meshParamsAndRedirect = function( url, url2 ) {
		this.redirect( this.meshParams( url, url2 ) );
	};			
		
	this.setParamAndRedirect = function( pname, pvalue, url ) {
		var new_url = this.setParam( pname, pvalue, url );
		new_url = this.rmAnchorName( new_url );
		this.redirect( new_url );
	};
	
	this.setParamsAndRedirect = function( params, url ) {
		var new_url = this.setParams( params, url );
		new_url = this.rmAnchorName( new_url );
		this.redirect( new_url );
	};
	
	this.meshParams = function( url, url2 ) {
		var new_url = this.getDefinedURL( url ); 
		var params = this.params( this.getDefinedURL( url2 ) ); 
		var newUrlParams = this.params( new_url );
						
		for( var name in params ) {
			if( newUrlParams[ name ] )
				continue;
			var value = params[ name ];			
			new_url = this.setParam( name, value, new_url );
		}
		return new_url;		
	};	
	
	this.setParams = function( params, url ) {
		var new_url = this.getDefinedURL( url );
		for( var name in params )
			new_url = this.setParam( name, params[ name ], new_url );		
		return new_url;
	};
	
	this.setParam = function( pname, pvalue, url ) {
		var new_url = this.getDefinedURL( url );
		var pars = this.params( new_url );	
		if( pars[ pname ] ) {
			var p = pname+"="+pars[ pname ];
			var newP = pname+"="+pvalue;
			new_url = new_url.replace( p, newP );
		} else {			
			var paramsStr = this.paramsString( new_url );
			var new_params =  ( paramsStr.length > 0 ? "&" : "?" );
			new_params += pname+"="+pvalue;			
			
			var anchorName = this.anchorName( new_url );
			if( anchorName.length > 0 ) {
				new_url = new_url.replace( anchorName, new_params+anchorName );
			} else new_url += new_params;
		}
		return new_url;
	};
	
	this.rmParamsAndAnchorName = function( url ) {
		var new_url = this.getDefinedURL( url );
		var index = new_url.indexOf( '?' );
		var index2 = new_url.indexOf( '#' );
		if( index2 < index )
			index = index2;
		if( index > -1 )
			return new_url.substring( 0, index );	
		return new_url;
	};
	
	this.rmAnchorName = function( url ) {		
		var new_url = this.getDefinedURL( url );
		return new_url.replace( this.anchorName( new_url ), '' );
	};
	
	this.rmParam = function( pname, url ) {
		var new_url = this.getDefinedURL( url );
		var pars = this.params( new_url );
		if( pars[ pname ] ) {
			var p =  pname+'='+pars[ pname ];
			var index = new_url.indexOf( p );
			if( index > 0 ) {
				var ch =  new_url.charAt( index-1 );
				if( ch == '?' ) {
					if( index + p.length < new_url.length ) {
						var ch2 = new_url.charAt( index + p.length );
						if( ch2 == '&')
							p = p+ch2;
						else p = ch+p;
					} else {
						p = ch+p;
					}
				} else if( ch == '&' ) {
					p = ch+p;
				}
				new_url = new_url.replace( p, '' );
			}						
		}
		return new_url;
	};
	
	this.rmParams = function( url ) {	
		var new_url = this.getDefinedURL( url );
		var index = new_url.indexOf( '?' );
		if( index > -1 ) {
			new_url = new_url.substring( 0, index );
			var index2 = new_url.indexOf( '#' );		
			if( index2 > index )
				new_url += new_url.substring( index2, new_url.length );
			return new_url;
		}
		return new_url;
	};
	
	this.anchorName = function( url ) {	
		var new_url = this.getDefinedURL( url );
		var index = new_url.indexOf( '#' );
		var index2 = new_url.indexOf( '?' );
		if( index > -1 ) {
			if( index2 <= index )
				index2 = new_url.length;
			return new_url.substring( index, index2 );	
		}
		return "";
	};
			
	this.params = function( url ) {
		var new_url = this.getDefinedURL( url );
		var pars = new Array();		
		var pstr = this.paramsString( new_url );
		if( pstr.length > 0 ) {
			var pageparams = pstr.split("&");
			for( var i in pageparams ) {
				var p = pageparams[i];
				var map = p.split("=");
				if( map.length == 2 ) {
					var name = map[0];
					var value = map[1];
					pars[name] = value;					
				} else {
					throw "Parametros de url em formato inválido. URL="+new_url;
				}
			}
		}
		return pars;
	};

	this.paramsString = function( url ) {
		var new_url = this.getDefinedURL( url );
		var index1 = new_url.indexOf( '?' );
		var index2 = new_url.indexOf( '#' );
		if( index2 < 0 || index2 <= index1 )
			index2 = new_url.length;
			
		if( index1 > -1 )		
			return new_url.substring( index1+1, index2 );			
		return "";
	};
	
	this.getDefinedURL = function( url ) {		
		return ( url ? url : window.location.href );
	};
	
}
