

function Menu( _menu ) {
	
	if( !_menu )
		throw 'Menu nulo ou indefinido.';
	
	var item_count = 0;
	var menus_map = new Array();
	var menuitens_map = new Array();
	var labels_map = new Array();	
				
	this.build = function( e ) {		
		if( !e )
			throw 'Elemento nulo ou indefinido.';		
		var element = get_element( e );
		menus_map = new Array();
		menuitens_map = new Array();
		labels_map = new Array();		
		item_count = 0;
		var menu_el = build_menu( _menu, element, 1000 );				
		element.appendChild( menu_el );	
	};
	
	function build_menu( menu, parent, z_index, is_submenu ) {
		is_submenu = (is_submenu == undefined ? false : is_submenu);
		var is_menubar = menu.menubar != undefined && menu.menubar == true;		
		var element_type = ( is_submenu ? 'submenu' : ( is_menubar ? 'menubar' : 'menu' ) );
		var content_element_type = element_type + '-content';
		var element_name = get_tagname( menu, element_type ); 
		var menuDiv = document.createElement( element_name );
		var contentDiv = document.createElement( element_name );				
		menuDiv.className = element_type;	
		contentDiv.className = content_element_type;
		if( menuDiv.align )
			contentDiv.align = menuDiv.align;
		build_html_attributes( menuDiv, menu );				
		
		menus_map [ menuDiv.id ] = { element : menuDiv, parent : parent, ismb : is_menubar, issubm : is_submenu, menuitens : new Array() };		
		if( menu.itens ) {	
			if( is_menubar ) {	
				var table = document.createElement( 'table' );
				table.setAttribute( 'border', '0' );
				table.setAttribute( 'cellspacing', '0' );
				table.setAttribute( 'cellpadding', '0' );
				try {
					table.insertRow();
					add_itens_to_menu( menu.itens, menuDiv, z_index, function( menuitem_element ) {	
						var td = document.createElement( 'td' );					
						td.appendChild( menuitem_element );
						var row = table.tBodies[0].rows[0];					
						row.appendChild( td );
					} );
				} catch( exception ) {
					var tr = document.createElement( 'tr' );
					add_itens_to_menu( menu.itens, menuDiv, z_index, function( menuitem_element ) {	
						var td = document.createElement( 'td' );					
						td.appendChild( menuitem_element );
						tr.appendChild( td );
					} );
					table.appendChild( tr );
				}
				contentDiv.appendChild( table );
			} else {
				add_itens_to_menu( menu.itens, menuDiv, z_index, function( menuitem_element ) {	
					contentDiv.appendChild( menuitem_element );
				} );	
			}
		}		
		menuDiv.appendChild( contentDiv );
		show( menuDiv, true );
		return menuDiv;
	}
	
	function add_itens_to_menu( itens, menuDiv, z_index, callback ) {
		for( var i in itens ) {
			var menuitem = itens[i];
			var menuitemDiv = build_menuitem( menuitem, menuDiv, z_index, (itens.length-i), true );
			if( typeof( callback ) == 'function' )
				callback.call( this, menuitemDiv );	
			menus_map[ menuDiv.id ].menuitens.unshift( menuitemDiv );
		}								
	}
	
	function build_menuitem( menuitem, parent, z_index, index, is_menubar_item ) {
		var element_type = ( is_menubar_item == true ? 'menubar-item' : 'menu-item' );
		var content_element_type = element_type + '-content';
		var element_name = get_tagname( menuitem, element_type ); 
		var menuItemDiv = document.createElement( element_name );
		var contentDiv = document.createElement( element_name );						
		menuItemDiv.className = element_type;
		contentDiv.className = content_element_type;
		get_style( menuItemDiv ).zIndex = z_index + index;
		build_html_attributes( menuItemDiv, menuitem.menuitem );		
		if( menuItemDiv.align )
			contentDiv.align = menuItemDiv.align;
		
		menuitens_map[ menuItemDiv.id ] = { 
			element : menuItemDiv, parent : parent, 
			onmouseover : menuItemDiv.onmouseover, onmouseout : menuItemDiv.onmouseout,
			label : null, submenu : null 
		};
					
		if( menuitem.label ) {
			var labelSpan = build_label( menuitem, menuItemDiv, is_menubar_item );									
			contentDiv.appendChild( labelSpan );
			menuitens_map[ menuItemDiv.id ].label = labelSpan;
			
			if( menuitem.menu && !is_old_ie_version() ) {
				var menuDiv = build_menu( menuitem.menu, menuItemDiv, z_index + 1, true );						
				contentDiv.appendChild( menuDiv );	
				menuitens_map[ menuItemDiv.id ].submenu = menuDiv;
				
				menuItemDiv.onmouseover = function() {				
					var map = menuitens_map[ this.id ];
					var submenu = map.submenu;
					show( submenu, true );					
					if( typeof( map.onmouseover ) == 'function' )
						map.onmouseover.call( this );
				};
				menuItemDiv.onmouseout = function() {
					var map = menuitens_map[ this.id ];
					var submenu = map.submenu;
					show( submenu, false );
					if( typeof( map.onmouseout ) == 'function' )
						map.onmouseout.call( this );
				};				
				show( menuDiv, false );														
			}							
		}	
		menuItemDiv.appendChild( contentDiv );			
		show( menuItemDiv, true );			
		return menuItemDiv;
	}
	
	function build_label( menuitem, parent, is_menubar_label ) {
		var element_type = (is_menubar_label == true ? 'menubar-label' : 'menu-label' );
		var content_element_type = element_type + '-content';
		
		var element_name = get_tagname( menuitem, element_type ); 
		var labelSpan = document.createElement( element_name );
		var contentSpan = document.createElement( element_name );
		
		build_html_attributes( labelSpan, menuitem );				
		if( labelSpan.align )
			contentSpan.align = labelSpan.align;
		
		if( menuitem.label )
			contentSpan.innerHTML = menuitem.label;
		
		var addclass = ( menuitem.addclass == undefined || menuitem.addclass == 'true' );
		var labelClassName = "", contentClassName = "";
		if( addclass ) {
			if( menuitem.onclick ) {
				labelClassName = element_type + (' ' + element_type + '-option');
				contentClassName = content_element_type + (' ' + content_element_type + '-option');
			} else {
				labelClassName = element_type + (' ' + element_type + '-notoption');
				contentClassName = content_element_type + (' ' + content_element_type + '-notoption');
			}
			if( is_old_ie_version() ) {
				labelClassName += (' ' + element_type + '-old-ie');
			}
			labelSpan.className = labelClassName;
			contentSpan.className = contentClassName;
		}						
			
		labels_map[ labelSpan.id ] = { element : labelSpan, parent : parent, 
			addclass : addclass, className : labelClassName, elementType : element_type,
			onmouseover : labelSpan.onmouseover, onmouseout : labelSpan.onmouseout, 
			onclick : labelSpan.onclick, isMenubarLabel : is_menubar_label
		};
		
		labelSpan.onmouseover = function() {
			var map = labels_map[ this.id ];
			if( map.addclass )
				this.className = map.className + (' ' + map.elementType + '-hover');
			var menuitem_map = menuitens_map[ map.parent.id ];
			var menu_map = menus_map[ menuitem_map.parent.id ];		
			while( menu_map.issubm ) {
				menuitem_map = menuitens_map[ menu_map.parent.id ];				
				var label_map = labels_map [ menuitem_map.label.id ];
				label_map.element.className = label_map.className + (' ' + label_map.elementType + '-hover');				
				menu_map = menus_map[ menuitem_map.parent.id ];
			}
			if( typeof( map.onmouseover ) == 'function' )
				map.onmouseover.call( this );
		};
		
		labelSpan.onmouseout = function() {
			var map = labels_map[ this.id ];
			if( map.addclass )
				this.className = map.className;
			var menuitem_map = menuitens_map[ map.parent.id ];
			var menu_map = menus_map[ menuitem_map.parent.id ];		
			while( menu_map.issubm ) {
				menuitem_map = menuitens_map[ menu_map.parent.id ];				
				var label_map = labels_map [ menuitem_map.label.id ];
				label_map.element.className = label_map.className;				
				menu_map = menus_map[ menuitem_map.parent.id ];
			}
			if( typeof( map.onmouseout ) == 'function' )
				map.onmouseout.call( this );
		};		
										
		labelSpan.appendChild( contentSpan );
		show( labelSpan, true );
		return labelSpan;
	}
	
	function build_html_attributes( element, item ) {
		if( !element)
			return;
		if( !item ) {
			element.id = element.tagName + '_' + (++item_count);
			return;
		}
		var style = get_style( element );									
			
		if( item.id )
			element.id = item.id;
		else element.id = element.tagName + '_' + (++item_count);
		
		if( item.name )
			element.name = item.name;					
		if( item.align )
			element.align = item.align;	
		if( item.position )
			style.position = item.position;				
		if( item.left )
			style.left = item.left;
		else if( !style.left )
			style.left = '0px';
			
		if( item.top )
			style.top = item.top;
		else if( !style.top )
			style.top = '0px';
			
		if( item.width )
			style.width = item.width;
		if( item.height )
			style.height = item.height;	
		if( item.zIndex )
			style.zIndex = parseInt( item.zIndex );
		if( item.margin )
			style.margin = item.margin;
		if( item.marginLeft )
			style.marginLeft = item.marginLeft;
		if( item.marginTop )
			style.marginTop = item.marginTop;
		if( item.padding )
			style.padding = item.padding;
		if( item.border )
			style.border = item.border;
		if( item.background )
			style.background = item.background;	
		if( item.style )
			element.style = item.style;		
		if( item.className ) {
			if( element.className )
				element.className += (' '+item.className);
			else element.className = item.className;
		}	
		if( typeof( item.onclick ) == 'function' ) {
			element.onclick = item.onclick;
		} else if( typeof( item.onclick ) == 'string' ) {
			element.onclick = function() { 
				eval( item.onclick );
			};
		}
			
		if( typeof( item.onmouseover ) == 'function' ) {
			element.onmouseover = item.onmouseover;	
		} else if( typeof( item.onmouseover ) == 'string' ) {
			element.onmouseover = function() {
				eval( item.onmouseover );
			};
		}
			
		if( typeof( item.onmouseout ) == 'function' ) {
			element.onmouseout = item.onmouseout;			
		} else if( typeof( item.onmouseout ) == 'string' ) {
			element.onmouseout = function() {
				eval( item.onmouseout );
			};
		}
	}
	
	function isSubmenusBuild() {
		return ( ie_version() > 7 );
	}
	
	function show( e, is_show ) {
		var element = get_element( e );
		var style = get_style( element );
		if( is_show ) {
			style.display = 'block';
			style.visibility = 'visible';
		} else {
			style.display = 'none';
			style.visibility = 'hidden';
		}
	}
			
	function get_tagname( item, type, _default ) {
		if( item )
			if( item.element )
				return item.element;
		return (_default ? _default : 'div' );	
	}
				
	function get_element( e ) {
		if( typeof( e ) == 'string' )
			return document.getElementById( e );
		return e;
	}	
	
	function is_old_ie_version() {
		return( ie_version() < 8 );
	}
	
	function ie_version() {
		if( is_ie() ) {
			var version = parseFloat( navigator.appVersion.split( 'MSIE' )[1] );
			return version;
		}
		return 100;
	}
	
	function is_ie() {
		return navigator.appVersion.indexOf( 'MSIE' ) != -1;
	}
	
	function get_style( e ) {
		return ( e.style ? e.style : e );
	}
	
	function recursiveShow( e, is_show ) {
		var element = get_element( e );
		var i;
		for( i = 0; i < element.childNodes.length; i++ ) {
			var item = element.childNodes.item( i );
			if( item.nodeType == 1 )
				recursiveShow( item, is_show );		
		}
		show( element, is_show );		
	}
	
	function show( e, is_show ) {
		var element = get_element( e );
		var style = get_style( element );
		if( is_show ) {
			style.display = 'block';
			style.visibility = 'visible';
		} else {
			style.display = 'none';
			style.visibility = 'hidden';
		}
	}		
		
}
