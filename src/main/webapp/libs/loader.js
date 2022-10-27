var loader = new Loader();
function Loader() {
							
	var ajax = new Ajax();
			
	this.createLayoutElement = function( params ) {
		if( !params )
			throw "params - parametro obrigatorio.";
		var content_id = this.content_id;
		var body = document.getElementsByTagName( 'body' )[0];
		var layoutElement = document.createElement( 'span' );
		layoutElement.setAttribute( 'id', params.id );
		
		var body_html = body.innerHTML;
		body.innerHTML = "";
		body.appendChild( layoutElement );
		
		this.loadResource( params.id, params.src, params.params );		
		
		if( typeof( params.body_to_content ) == 'boolean' )
			document.getElementById( content_id ).innerHTML = body_html;	
	};
		
	this.loadResources = function( resources ) {
		if( resources ) {
			for( var i in resources ) {
				var resource = resources[i];
				this.loadResource( resource.id, resource.src, resource.params );
			}
		}
	};
	
	this.loadResource = function( id, src, load_params ) {
		if( !id )
			throw "id - Parametro obrigatorio.";
		if( !src )
			throw "src - Parametro obrigatorio.";
		if( !document.getElementById( id ) )
			throw "Elemento nulo, ID="+id+"; SRC="+src;
		ajax.send( src, { 
			success : function( params ) {
				if( params.invoker.load_params ) 
					if( params.invoker.load_params.before_load )
						params.invoker.load_params.before_load.call( this, { xmlhttp : params.xmlhttp, invoker : params.invoker.load_params.invoker } );
				
				var html = params.xmlhttp.responseText;
				if( params.invoker.load_params ) {
					if( params.invoker.load_params.adjust ) {
						var root_dir = ( params.invoker.load_params.adjust.root_dir ? params.invoker.load_params.adjust.root_dir : "" );
						html = params.invoker.html_rootdir_adjust( html, root_dir );
					}
				}				
				document.getElementById( params.invoker.id ).innerHTML = html;
				if( params.invoker.load_params ) {
					if( params.invoker.load_params.after_load )
						params.invoker.load_params.after_load.call( this, { xmlhttp : params.xmlhttp, invoker : params.invoker.load_params.invoker } );
				}
			},
			sended : ( load_params ? load_params.sended : undefined ),
			fail : ( load_params ? load_params.fail : undefined ),
			waiting : ( load_params ? load_params.waiting : undefined ),
			ajax_support : ( load_params ? load_params.ajax_support : undefined ),
			assync : ( load_params ? load_params.assync : undefined ),			
			invoker : {
				id : id,
				load_params : load_params,
				html_rootdir_adjust : html_rootdir_adjust,
				assync : ( load_params ? load_params.assync : undefined )
			}
		} );					
	};
	
	function html_rootdir_adjust( html, root_dir ) {
		
		var new_html = "";
		
		var attrs = [ 'src', 'href' ];
		var counts = [ 0, 0 ];
		
		var i = 0;
		while ( i < html.length ) {
			new_html += html.charAt( i );
			var j;
			for( j = 0; j < attrs.length; j++ ) {
				if ( counts[j] < attrs[j].length ) {
					if ( html.charAt( i ) == attrs[j].charAt( counts[j] ) ) {
						counts[j]++;
					} else {
						counts[j] = 0;
					}
				} else {
					var prox = ['=', '\"'];
					var index = 0;
					var subst = true, it1 = true;
					while( i < html.length && index < prox.length && subst ) {	
						if( html.charAt( i ) != ' ' ) {
							if ( html.charAt( i ) == prox[ index ] )
								index++;
							else subst = false;
						}
						if( it1 ) it1 = false;
						else new_html += html.charAt( i );
						i++;
					}
					if( subst ) {
						var attr3 = "http://";
						if( html.charAt( i ) != '#' && (i + attr3.length) < html.length )
							if( attr3 != html.substring( i, i+attr3.length ) )
								new_html += root_dir;	
					}						
					new_html += html.charAt( i );
					counts[j] = 0;
				}
			}
			i++;
		}
		return new_html;	
	}
	
}

