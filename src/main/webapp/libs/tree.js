
function Tree( _tree, _params ) {
	var element;
	var params = _params;
	
	set_default_configuration();	
	
	if( typeof( _tree ) == 'object' )
		element = _tree;
	else element = document.getElementById( _tree );
	
	var tree_id = ( element ? element.id : undefined );
	if( !tree_id )
			throw "Tree - ID indefinido.!!";
			
	var title_elements_map = new Array();
	var item_elements_map = new Array();
		
	this.build = function() {					
		title_elements_map = new Array();
		item_elements_map = new Array();
		
		var list = new Array();
		if( element ) {
			if( element.tagName == 'UL' ) {
				var ul_style = get_style( element );
				if( !ul_style.paddingLeft )
					ul_style.paddingLeft = 0;													
				list.push( { element : element, nivel : 0 } );	
			}
		}
		var elements_count = 0;						
		while( list.length > 0 ) {
			var e = list.pop();
			if( !e.element )
				continue;
			if( e.element.nodeType != 1 )
				continue;
			var leaf = e.element.getAttribute( 'leaf' );
			if( leaf != undefined && leaf != null )			
				continue;			
			if( !params.list_style_type )
				get_style( e.element ).listStyleType = 'none';
				
			var nodes = e.element.childNodes;
			var i;
			var title_element = null;
			for( i = 0; i < nodes.length; i++ ) {
				var item = nodes.item( i );			
				if( item.nodeType == 1 ) {
					var tag = item.tagName.toUpperCase();
					var style = get_style( item );						
					if( !item.id )
						item.id = tree_id + '_element_' + elements_count;								
					if( is_title_element( tag ) ) {
						var is_tree_title = item.getAttribute( 'tree-title' );
						if( is_tree_title != undefined && is_tree_title != null ) {
							if( !style.cursor )
								style.cursor = 'pointer';							
							title_elements_map[ item.id ] = { 
									title : item, ul : null, expanded : false, nivel : e.nivel+1,
									image : null, left_expanded_image : null, right_expanded_image : null 
							};																
						title_element = item;	
						} else {
							title_element = null;
						}
					} else if( tag == 'UL' ) {	
						if( title_element ) {
							title_elements_map[ title_element.id ].ul = item;
							title_element.onclick = function() {		
								this.expandByTitle( this, !title_elements_map[ this.id ].expanded );																						
							};
						}						
					} else if( tag != 'LI' ) {
						var is_item = item.getAttribute( 'tree-item' );
						if( is_item != undefined && is_item != null )
							item_elements_map[ item.id ] = item;
					}
					list.unshift( { element: item, nivel : e.nivel+1 } );
					elements_count++;
				}			
			}															
		}
		add_images_in_titles();
		add_images_in_items();
		expandByTitles();
		
	};
	
	this.expandByTitles = function() {
		for( var id in title_elements_map ) {
			var title_element = title_elements_map[ id ];
			this.expandByTitle( title_element.title, title_element.expanded );					
		}
	};
	
	this.expandAll = function( expand ) {
		for( var id in title_elements_map ) {
			var title_element = title_elements_map[ id ];
			this.expandByTitle( title_element.title, expand );					
		}								
	};
	
	this.recursiveExpandByTitle = function( _title, expand ) {
		var element;
		if( typeof( _title ) == 'object' )
			element = _title;
		else element = document.getElementById( _title );
		if( element != null ) {
			this.expandByTitle( element, expand );
			if( element.parentNode ) {
				var list = new Array();
				list.push( element.parentNode );
				while( list.length > 0 ) {
					var e = list.pop();
					var childs = e.childNodes;
					var i;
					for( i = 0; i < childs.length; i++ ) {
						var child = childs.item( i );
						if( child.nodeType == 1 ) {
							var tag = child.tagName.toUpperCase();
							if( is_title_element( tag ) ) {
								this.expandByTitle( child, expand );
							}
							list.push( child );
						}
					}
				}
			}
		}
	};

	this.expandByTitle = function( _title, expand ) {
		var element;
		if( typeof( _title ) == 'object' )
			element = _title;
		else element = document.getElementById( _title );
		if( element != null ) {
			var title_element = title_elements_map[ element.id ];
			if( !title_element )
				return;
			var ul = title_element.ul;	
			if( ul ) {
				var style = get_style( ul );
				var tag = element.tagName;
				var title_op = get_title_op( tag );
				if( expand ) {
					style.display = 'block';
					style.visibility = 'visible';
					if( title_op ) {
						if( title_op.not_expanded_image && params.images_show && title_op.images_show ) {	
							var img_element = title_elements_map[ element.id ].image;
							var lt_exp_img_element = title_elements_map[ element.id ].left_expanded_image;
							var rt_exp_img_element = title_elements_map[ element.id ].right_expanded_image;
							if( img_element ) {
								img_element.src = title_op.not_expanded_image;
							} else {
								if( lt_exp_img_element )
									lt_exp_img_element.src = title_op.not_expanded_image; 								
								if( rt_exp_img_element )
									rt_exp_img_element.src = title_op.not_expanded_image;											
							}
						}
					}
				} else {
					style.display = 'none';
					style.visibility = 'hidden';
					if( title_op ) {
						if( title_op.expanded_image && params.images_show && title_op.images_show ) {						
							var img_element = title_elements_map[ element.id ].image;
							var lt_exp_img_element = title_elements_map[ element.id ].left_expanded_image;
							var rt_exp_img_element = title_elements_map[ element.id ].right_expanded_image;
							if( img_element ) {
								img_element.src = title_op.expanded_image;
							} else {
								if( lt_exp_img_element )
									lt_exp_img_element.src = title_op.expanded_image; 								
								if( rt_exp_img_element )
									rt_exp_img_element.src = title_op.expanded_image;								
							}									
						}
					}
				}
			}
			title_element.expanded = expand;			
		}
	};
	
	function add_images_in_items() {
		if( !params.images_show || !params.item.images_show )
			return;		
		for( var id in item_elements_map ) {		
			var item_element = item_elements_map[ id ];
			
			var img = document.createElement( 'image' );
			img.setAttribute( 'src', params.item.image );
			img.setAttribute( 'style', 'position:relative; top : 3px; margin-right:3px' );
			
			var span = document.createElement( 'span' );
			span.innerHTML = item_element.innerHTML;
			
			item_element.innerHTML = "";
			item_element.appendChild( img );
			item_element.appendChild( span );
			
			var parent = item_element.parentNode;
			if( parent )
				get_style( parent ).listStyleType = 'none';			
			get_style( item_element ).listStyleType = 'none';
		}
	}
	
	function add_images_in_titles() {
		if( !params.images_show )
			return;
		for( var id in title_elements_map ) {			
			var tree_element = title_elements_map[ id ];			
			var title_tag = tree_element.title.tagName.toUpperCase();	
			var title_op = get_title_op( title_tag );
			var title_element = tree_element.title;	
			var parent = title_element.parentNode;
			var style = get_style( title_element );
			if( parent ) {
				var parentStyle = get_style( parent );
				parentStyle.listStyleType = 'none';
			}			
			style.listStyleType = 'none';
			
			if( !title_op.images_show )
				continue;													
			var left = (title_op.images_float == 'left');				
			var right = (title_op.images_float == 'right');
			if( !left && !right )
				left = right = ( title_op.images_float == 'left_and_right');
							
			if( left || right ) {
				var text_element = document.createTextNode( title_element.innerHTML );
				title_element.innerHTML = "";
				title_element.appendChild( text_element );
				
				if( left ) {
					var left_img = document.createElement( 'img' );
					left_img.setAttribute( 'style', 'position: relative; float: left; top:-1px; margin-right: 4px'  );
					left_img.setAttribute( 'src', title_op.not_expanded_image );
					title_element.insertBefore( left_img, text_element );
					tree_element.left_expanded_image = left_img;
				}
				if( right ) {
					var right_img = document.createElement( 'img' );
					right_img.setAttribute( 'style', 'position: relative; float: right; top: -1px' );
					right_img.setAttribute( 'src', title_op.not_expanded_image );
					title_element.appendChild( right_img );
					tree_element.right_expanded_image = right_img;
				}				
			}				
		}			
	}
	
	function set_default_configuration() {
		if( !params ) params = {};		
		if( !params.h1 ) params.h1 = {};
		if( !params.h2 ) params.h2 = {};
		if( !params.h3 ) params.h3 = {};
		if( !params.h4 ) params.h4 = {};
		if( !params.h5 ) params.h5 = {};
		if( !params.h6 ) params.h6 = {};
		if( !params.span ) params.span = {};
		if( !params.item ) params.item = {};
		if( !params.images_show ) params.images_show = true;
		if( !params.images_style ) params.images_style = 'black';
		if( params.list_style_type == undefined ) params.list_style_type = true;
		
		if( !params.root_dir )
			params.root_dir = '';
		if( !params.libs_dir )
			params.libs_dir = 'libs/';

		var basedir = params.root_dir + params.libs_dir;			
		var default_images_show = false;
		var images_dir = 'images/';		
		var style_dir = params.images_style + '/';
		
		if( !params.item.image )
			params.item.image = basedir + images_dir + 'link_tree_item.gif';
		if( params.item.images_show == undefined )
			params.item.images_show = default_images_show;
		
		var tags = [ 'H1', 'H2', 'H3', 'H4', 'H5', 'H6', 'SPAN' ];
		for( var i in tags ) {
			var tag = tags[i];
			var default_images_float = ( i < 4 ? 'left_and_right' : 'left' );
			var default_expanded_image = ( i < 4 ? 'tree_h1234_exp.gif' : 'tree_h56_exp.gif' );
			var default_not_expanded_image = ( i < 4 ? 'tree_h1234_nexp.gif' : 'tree_h56_nexp.gif' );
			var title_op = get_title_op( tag );
			if( title_op ) {			
				if( title_op.images_show == undefined )
					title_op.images_show = default_images_show;																	
				if( !title_op.list_style )
					title_op.images_float = default_images_float;
				if( !title_op.expanded_image )
					title_op.expanded_image = basedir + images_dir + style_dir + default_expanded_image;
				if( !title_op.not_expanded_image )
					title_op.not_expanded_image = basedir + images_dir + style_dir + default_not_expanded_image;
			} else {
				title_op = {
					images_show : default_images_show,
					list_style : default_list_style,
					images_float: default_images_float
				};
				title_op.expanded_image = basedir + images_dir + style_dir + default_expanded_image;
				title_op.not_expanded_image = basedir + images_dir + style_dir + default_not_expanded_image;
			}
		}
	}
	
	function get_title_op( tag ) {
		if( tag == 'H1' ) {
			return params.h1;
		} else if( tag == 'H2' ) {
			return params.h2;
		} else if( tag == 'H3' ) {
			return params.h3;
		} else if( tag == 'H4' ) {
			return params.h4;
		} else if( tag == 'H5' ) {
			return params.h5;
		} else if( tag == 'H6' ) {
			return params.h6;
		} else if( tag == 'SPAN' ) {
			return params.span;
		}
		return undefined;
	}
	
	function is_title_element( tag ) {
		return ( tag == 'H1' || tag == 'H2' || tag == 'H3' || tag == 'H4' || tag == 'H5' || tag == 'H6' || tag == 'SPAN' );
	}

	function get_style( element ) {
		return ( element.style ? element.style : element );
	}
	
	
}

