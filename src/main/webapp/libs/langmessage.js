
var messageManager = new MessageManager();
function MessageManager() {
	
	this.debug = false;
	this.langCookieName = 'lang';
	this.langCookieActive = true;

	this.propertiesMap = new Array();
	this.textsMap = new Array();
	this.htmlsMap = new Array();
	
	this.supportedLanguages = new Array();
	this.configFilesNodes = new Array();
	
	this.defaultLang = undefined;
	this.lang = undefined;
	this.context = undefined;	
							
	this.initialize = function( file, params ) {
		if( params ) {
			if( params.context )
				this.context = params.context;
			if( params.lang ) {
				this.lang = params.lang;
			} else if( this.langCookieActive == true ) {				
				var lang = cookieManager.get( this.langCookieName );
				if( lang )
					this.lang = lang;
			}									
		}
		this.processConfigFile( file );
	};
	
	this.processConfigFile = function( file ) {		
		var ajax = new Ajax();
		ajax.send( file, {
			assync : false,
			success : function( params ) {
				var source = params.invoker.source;
				
				var responseText = params.xmlhttp.responseText;
				var doc = loadXml( responseText ).documentElement;				
				
				source.processRootNode( doc );				
				source.processMessagesNodeByContext( source.context );				
			},	
			fail : function( params ) {
				if( this.debug == true )					
					throw "Arquivo de mensagens não encontrado. SRC="+file;
			},
			invoker : {
				source : this
			}
		} );				
	};
			
	this.processRootNode = function( root ) {		
		var nodes = root.childNodes;
		for( var i = 0; i < nodes.length; i++ ) {
			var item = nodes.item( i );
			if( item.nodeType == 1 ) {
				if( item.tagName == 'langs' ) {
					this.defaultLang = item.getAttribute( 'default' );
					var languageNodes = item.childNodes;
					for( var j = 0; j < languageNodes.length; j++ ) {
						var languageItem = languageNodes.item( j );
						if( languageItem.nodeType == 1 ) {
							if( languageItem.tagName == 'lang' ) {
								var l = languageItem.getAttribute( 'name' );
								this.propertiesMap[ l ] = new Array();
								this.textsMap[ l ] = new Array();
								this.htmlsMap[ l ] = new Array();
								this.supportedLanguages.push( l );
							}	
						}
					}
				} else if( item.tagName == 'messageFiles' ) {
					this.configFilesNodes.push( item );														
				}
			}
		}
	};
	
	this.processBasedirNode = function( element, globalElement, lang, name ) {
		var basedir = element.getAttribute( name );
		if( basedir != undefined && basedir != null ) {
			return basedir.replace( '{lang}', lang );
		} else if( globalElement ) {
			basedir = globalElement.getAttribute( name );
			if( basedir != undefined && basedir != null )
				return basedir.replace( '{lang}', lang );
		}		
		return null;
	};
	
	this.processMessagesNodeByContext = function( context ) {
		for( var i in this.configFilesNodes ) {
			var node = this.configFilesNodes[ i ];
			var c = node.getAttribute( 'context' );
			if( c != undefined && c != null && c == context ) {
				this.processMessagesNode( node, this.defaultLang );
				if( this.lang != null && this.lang != undefined && this.lang != this.defaultLang )
					this.processMessagesNode( node, this.lang );
				return node;
			}			
		}
		return undefined;
	};
		
	this.processMessagesNode = function( element, lang ) {
		var globalElement = undefined;		
		var ext = element.getAttribute( 'extends' );
		if( ext != undefined && ext != null )
			globalElement = this.processMessagesNodeByContext( ext );		
				
		var basedir = this.processBasedirNode( element, globalElement, lang, 'basedir' );
		var htmlbasedir = this.processBasedirNode( element, globalElement, lang, 'htmlBasedir' );
				
		if( basedir == null ) basedir = "";
		if( htmlbasedir == null ) htmlbasedir = basedir;
		
		var nodes = element.childNodes;
		for( var i = 0; i < nodes.length; i++ ) {
			var item = nodes.item( i );
			if( item.nodeType == 1 )
				if( item.tagName == 'messageFile' )
					this.processMessageNode( item, basedir, htmlbasedir, lang );							
		}
	};		
	
	this.processMessageNode = function( element, basedir, htmlbasedir, lang ) {
		var src = basedir + element.getAttribute( 'path' ); 
		src = src.replace( '{lang}', lang );			

		var type = element.getAttribute( 'type' );
		if( type == 'properties' ) {
			this.processPropertiesMessageNode( this.propertiesMap, src, lang );				
		} else if( type == 'text' ) {
			this.processTextMessageNode( src, lang );				
		} else if( type == 'htmlmap' ) {
			this.processPropertiesMessageNode( this.htmlsMap, src, lang, htmlbasedir );
		} else {
			if( this.debug == true )				
				throw "MessageManager.processMessageNode - Tipo de arquivo inválido, TYPE="+type+"; SRC="+src;
		}					
	};
		
	this.processPropertiesMessageNode = function( map, src, lang, htmlbasedir ) {
		propertiesUtil.process( { src : src,
			assync : false,
			messageReaded : function( name, value, params ) {
				var lang = params.invoker.lang;
				var basedir = params.invoker.htmlbasedir;
				params.invoker.map[ lang ][ name ] = basedir + value;
			},
			fail : function( params ) {
				var source = params.invoker.source;
				if( source.debug == true )
					throw "Arquivo não encontrado: "+src;
			},
			invoker : {
				map : map,
				lang : lang,
				htmlbasedir : ( htmlbasedir ? htmlbasedir : "" )
			}
		} );
	};
	
	this.processTextMessageNode = function( src, lang ) {
		var ajax = new Ajax();
		ajax.send( src, {
			assync : false,
			success : function( params ) {
				var source = params.invoker.source;
				var lang = params.invoker.lang;				
				
				var responseText = params.xmlhttp.responseText;
				var doc = loadXml( responseText ).documentElement;				
								
				var nodes = doc.childNodes;
				for( var i = 0; i < nodes.length; i++ ) {
					var item = nodes.item( i );
					if( item.nodeType == 1 ) {
						if( item.tagName == 'text' ) {
							var key = item.getAttribute( 'key' );														
							source.textsMap[ lang ][ key ] = item;
						}
					}
				}
				
			},	
			fail : function( params ) {
				var source = params.invoker.source;
				if( source.debug == true )
					throw "Arquivo de mensagens não encontrado. SRC="+file;
			},
			invoker : {
				source : this,
				lang : lang
			}
		} );	
	};
	
	this.loadLabel = function( element, key, params ) {
		var el;
		if( typeof( element ) ==  'string' )
			el = document.getElementById( element );
		else el = element;
		if( el == undefined || el == null )
			throw "MessageManager - loadLabel: Elemento não encontrado. ID="+element;
		
		var label = this.getMapValue( this.propertiesMap, key );		
		if( label )
			el.innerHTML = this.processValueParams( label, params );		
	};
	
	this.loadText = function( element, key, params ) {
		var el;
		if( typeof( element ) ==  'string' )
			el = document.getElementById( element );
		else el = element;
		if( el == undefined || el == null )
			throw "MessageManager - loadLabel: Elemento não encontrado. ID="+element;
		
		var textNode = this.getMapValue( this.textsMap, key );		
		if( textNode ) {
			var text = textNode.firstChild.nodeValue;
			el.innerHTML = this.processValueParams( text, params );
		}
	};
	
	this.loadHTML = function( element, key, params, requestParams ) {
		var el;
		if( typeof( element ) ==  'string' )
			el = document.getElementById( element );
		else el = element;
		if( el == undefined || el == null )
			throw "Elemento não encontrado. ID="+element;

		var src = this.getMapValue( this.htmlsMap, key );		
		if( src ) {
			var ajax = new Ajax();
			ajax.send( src, {
				assync : false,
				success : function( params ) {
					var source = params.invoker.source;
					var element = params.invoker.element;
					var replaceParams = params.invoker.replaceParams;										
					var html = params.xmlhttp.responseText;														
					
					element.innerHTML = source.processValueParams( html, replaceParams );
				},	
				fail : function( params ) {
					if( typeof( params.invoker.fail ) == 'function' ) {
						params.invoker.fail( params );
					} else {
						var source = params.invoker.source;
						if( source.debug == true )
							throw "Arquivo HTML não encontrado. SRC="+file;
					}
				},
				invoker : {
					source : this,
					element : el,
					replaceParams : params,
					fail : ( requestParams ? requestParams.fail : undefined )
				}
			} );
		}		
	};
			
	this.getLabel = function( key, params ) {
		var label = this.getMapValue( this.propertiesMap, key );
		if( label )
			return this.processValueParams( label, params );		
		return undefined;
	};
	
	this.getText = function( key, params ) {
		var textNode = this.getMapValue( this.textsMap, key );		
		if( textNode ) {
			var text = textNode.firstChild.nodeValue;
			return this.processValueParams( text, params );
		}
		return undefined;
	};
		
	this.processValueParams = function( value, params ) {
		var newValue = value;
		if( newValue && params ) {
			for( var i in params ) {
				var param = params[ i ];
				newValue = newValue.replace( '{'+ i +'}', param );
			}												
		}
		return newValue;
	};
			
	this.getMapValue = function( map, key ) {
		var value = this.getMapValueByLang( map, key, this.lang );
		if( !value )
			value = this.getMapValueByLang( map, key, this.defaultLang );		
		return value;
	};
	
	this.getMapValueByLang = function( map, key, lang ) {
		var value = undefined;
		if( lang ) {
			var langMap = map[ lang ];
			if( langMap )
				value = langMap[ key ];
		}
		return value;
	};
	
	this.verifyLangSupported = function( lang ) {
		for( var i in this.supportedLanguages )
			if( lang == this.supportedLanguages[i] ) 
				return true;
		return false;
	};
	
	this.getLang = function() {
		return ( this.lang ? this.lang : this.defaultLang );
	};
	
	this.getLabelForParams = function( params ) {
		if( params.label ) {
			if( typeof( params.label ) == "string" )
				return params.label;
			return this.getLabel( params.label.key, params.label.params );
		} else {
			return this.getLabel( params.labelKey );
		}		
	};
	
	this.compatibility = {
		componentLoader : {
			processComponentChildNode : function( item ) {
				if( item.tagName == 'messages' ) {
					var messagesNodes = item.childNodes;
					for( var j = 0; j < messagesNodes.length; j++ ) {
						var messageNode = messagesNodes.item( j );
						if( messageNode.nodeType == 1 ) {
							if( messageNode.tagName == 'message' ) {
								var mkey = messageNode.getAttribute( 'key' );
								var mid = messageNode.getAttribute( 'id' );
								if( mid == undefined || mid == null )
									mid = mkey;
								
								var mtype = messageNode.getAttribute( 'type' );
								if( mtype == undefined || mtype == null ) {
									messageManager.loadLabel( mid, mkey );
								} else if( mtype == 'label' ) {
									messageManager.loadHTML( mid, mkey );
								} else if( mtype == 'text' ){								 
									messageManager.loadText( mid, mkey );
								} else if( mtype == 'html' ) {
									messageManager.loadHTML( mid, mkey );
								} else {
									throw "Tipo de mensagem não suportado, ID="+mtype;																	
								}
							}
						}
					}
				}
			}
		}
	};
	
};
