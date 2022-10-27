
var componentLoader = new ComponentLoader();
function ComponentLoader() {
		
	this.dependences = {
		processComponentChildNode : undefined,
		getVisibility : undefined
	};
	
	this.components = new Array();
	this.operations = new Array();
	this.afterCallbacks = new Array();
	this.layoutPageName = null;	
		
	this.initialize = function( file ) {
		this.loadFile( file, true );
	};	
	
	this.load = function( params ) {
		this.loadComponentByName( this.layoutPageName, params );
	};
	
	this.loadFile = function( file, is_root_file ) {
		var ajax = new Ajax();
		ajax.sendGetRequest( file, {
			success : function( params ) {
				var source = params.invoker.source;
				var is_root_file = params.invoker.is_root_file;
				var doc = loadXml( params.xmlhttp.responseText ).documentElement;
				
				var nodes = doc.childNodes;
				for( var i = 0; i < nodes.length; i++ ) {
					var item = nodes.item( i );
					if( item.nodeType == 1 ) {
						if( item.tagName == 'component' ) {
							var name = item.getAttribute( 'name' );
							if( name != undefined && name != null )
								source.components[ name ] = { element : item };
						} else if( item.tagName == 'file' ) {
							var path = item.getAttribute( 'path' );							
							source.loadFile( path, false );
						} else if( item.tagName == 'layout' ) {
							if( is_root_file == true )
								source.layoutPageName = item.getAttribute( 'pageName' );					
						}						
					}
				}
												
			}, 
			assync : false,
			invoker : {
				source : this,
				is_root_file : is_root_file
			}
		} );
	};
		
	this.getComponentNodeByName = function( name ) {
		var comp = this.components[ name ];
		if( !comp )
			throw "Componente não reconhecido. Nome="+name;
		return ( comp ? comp.element : null );
	};
		
	this.loadComponentByName = function( name, params ) {		
		var comp_obj = this.components[ name ];
		if( !comp_obj )
			throw "Componente não reconhecido. Nome="+name;
		this.loadComponent( comp_obj.element, params, name );
	};
	
	this.loadComponent = function( comp_el, params, componentName ) {			
		var id = comp_el.getAttribute( 'id' );
		var src = comp_el.getAttribute( 'src' );
		var assync = comp_el.getAttribute( 'assync' );		
		var loadResources = true;
		if( params ) {
			if( assync == undefined || assync == null )			
				assync = params.assync;
			loadResources = (params.loadResources == false ? false : true );
		}
		if( typeof( assync ) == 'string' )
			assync = (assync != 'false');		
		
		var after = ( params ? params.after : undefined ); 
		var cb_invoker = ( params ? params.invoker : undefined );		
				
		if( id != undefined && id != null ) {
			var el = document.getElementById( id );
			if( el == undefined | el == null )
				throw "ID não encontrado. ID="+id+"; SRC="+src;
		}
		
		var authorized = true;		
		if( typeof( this.dependences.getVisibility ) == 'function' ) {
			var visibility = this.dependences.getVisibility();
			authorized = this.verifyDependences( comp_el, visibility );
		}
		
		if( authorized == true ) {
			var processComponentParams = { 
				id : id, src : src, 
				element : comp_el, componentName : componentName, 
				loadResources : loadResources,
				afterCB : after, cbInvoker : cb_invoker 
			};
			
			if( src ) {
				var ajax = new Ajax();
				ajax.send( src, {
					assync : assync,
					success : function( params ) {
						var source = params.invoker.source;			
						var processComponentParams = params.invoker.processComponentParams;
						
						var id = params.invoker.id;
						var html = params.xmlhttp.responseText;
						
						document.getElementById( id ).innerHTML = html;						
						source.processComponent( processComponentParams );
					},
					fail : function( params ) {
						throw "SRC não encontrado. SRC="+src;
					},
					invoker : {
						source : this,
						processComponentParams : processComponentParams,
						id : id
					}
				} );
				/*loader.loadResource( id, src, {
					assync : assync,
					after_load : function( params ) {
						var source = params.invoker.source;			
						var processComponentParams = params.invoker.processComponentParams;
						source.processComponent( processComponentParams );						
					},
					invoker : {
						source : this,
						processComponentParams : processComponentParams
					}
				} );*/
			} else {
				this.processComponent( processComponentParams );
			}
			
		}		
	};
	
	this.processComponent = function( params ) {
		if( !params )
			throw "params - parametro necessário.";
		var id = params.id;
		var src = params.src;
		var element = params.element;
		var componentName = params.componentName;
		var loadResources = params.loadResources;
		var afterCB = params.afterCB;
		var cbInvoker = params.cbInvoker;					
		
		var nodes = element.childNodes;
		for( var i = 0; i < nodes.length; i++ ) {
			var item = nodes.item( i );
			if( item.nodeType == 1 ) {
				if( item.tagName == 'resources' ) {
					if( loadResources == false )
						continue;
					var resChilds = item.childNodes;
					for( var j = 0; j < resChilds.length; j++ ) {
						var resItem = resChilds.item( j );
						if( resItem.nodeType == 1 ) {
							if( resItem.tagName == 'component' ) {
								var ref = resItem.getAttribute( 'ref' );
								if( ref ) {
									var assync = resItem.getAttribute( 'assync' );
									this.loadComponentByName( ref, { assync : assync } );
								} else {
									this.loadComponent( resItem );
								}
							}
						}
					}
				} else {
					if( typeof( this.dependences.processComponentChildNode ) == 'function' )
						this.dependences.processComponentChildNode.call( this, item );
				}
			}
		}
				
		var jsAfter = element.getAttribute( 'jsAfter' );				
		if( jsAfter != undefined && jsAfter != null )
			eval( jsAfter );
		if( typeof( afterCB ) == 'function' )
			afterCB.call( this, { invoker : cbInvoker } );
		
		if( componentName ) {
			for( var i in this.afterCallbacks ) {
				if( i == id ) {
					var callbackObj = this.afterCallbacks[ i ];
					var callback = callbackObj.callback;
					if( typeof( callback ) == 'function' )
						callback.call( this, { id : id, src : src, componentName : componentName, invoker : callbackObj.invoker } );
					break;
				}
			}
		}
	};
	
	this.verifyDependences = function( componentNode, visibility ) {
		var authorized = true;
		
		var nodes = componentNode.childNodes;
		for( var i = 0; authorized == true && i < nodes.length; i++ ) {
			var item = nodes.item( i );
			if( item.nodeType == 1 ) {
				if( item.tagName == 'dependences' ) {
					if( authorized == true ) {
						var visibilityAttr = item.getAttribute( 'visibility' );
						if( visibilityAttr ) {
							authorized = false;
							var vlist = visibilityAttr.split( ' ' );	
							for( var j = 0; !authorized && j < vlist.length; j++ ) {
								var v = vlist[ j ];
								authorized = ( v == 'all' || v == visibility );
							}				
						}
					}
					if( authorized == true ) {
						var depNodes = item.childNodes;
						for( var j = 0; authorized == true && j < depNodes.length; j++ ) {
							var depItem = depNodes.item( j );
							if( depItem.nodeType == 1 ) {
								if( depItem.tagName == 'operation' ) {
									var name = depItem.getAttribute( 'name' );
									if( !this.operations[ name ] )
										authorized = false;
								}
							}
						}
					}
				}
			}
		}
		
		return authorized;
	};
		
} 