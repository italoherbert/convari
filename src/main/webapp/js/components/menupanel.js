
var menuPanel = new MenuPanel();
function MenuPanel() {

	var instance = this;

	this.UNMARK_ITEM_STYLE = '0';
	this.MARK_ITEM_STYLE1 = '1';	
	
	this.elementId = undefined;
	this.menubar = false;
	this.markItemAfterExecution = false;
	this.markItemByStyleDelay = 500;
	
	this.itens = new Array();	
	
	this.currentMenuItem = null;
	
	this.maxMarkItemS1Count = 4;
	this.markItemS1Count = 0; 
	
	this.build = function() {
		var el = document.getElementById( this.elementId );
		if( !el )
			throw "ID de element inválido. ID="+this.elementId;
				
		var menuPanelElement = document.createElement( 'div' );
		menuPanelElement.className = 'menupanel';
		
		for( var name in this.itens ) {
			var item = this.itens[ name ];
			var label = item.label;

			var menuItemElement; 
			if( this.menubar == true )
				menuItemElement = document.createElement( 'span' );
			else menuItemElement = document.createElement( 'div' );
			
			menuItemElement.id = this.elementId+"_"+name;
			menuItemElement.name = this.elementId+"_"+name;
			menuItemElement.className = 'menupanel-item';
			menuItemElement.onclick = function() {
				var name = this.name.replace( instance.elementId+'_', '' );
				instance.executeItem( name );
				instance.unmarkItemByStyle( name );
				if( instance.markItemAfterExecution == true )
					instance.markItem( name );
			};
						
			menuItemElement.innerHTML = label;				
			menuPanelElement.appendChild( menuItemElement );
			
			this.itens[ name ].element = menuItemElement;
			this.itens[ name ].markStyle = { marked : false, style : instance.UNMARK_ITEM_STYLE, interval : null, unmark : false };
		}
		el.appendChild( menuPanelElement );
	};
			
	this.markItemByStyle = function( name, style ) {		
		var item = this.itens[ name ];
		if( item ) {
			var markStyle = item.markStyle;
			if( markStyle.marked == false ) {
				var el = item.element;
				if( el ) {
					markStyle.style = style;
					markStyle.marked = true;
					markStyle.unmark = false;
					this.markItemS1Count = 0;
					this.startMarkItemByStyle( item );
				}
			}
		}		
	};
	
	this.unmarkItemByStyle = function( name ) {
		var item = this.itens[ name ];
		if( item ) {
			var markStyle = item.markStyle;
			if( markStyle.marked == true ) {
				markStyle.marked = false;
				if( this.markItemS1Count >= this.maxMarkItemS1Count ) {
					var el = item.element;
					var style = markStyle.style;						
					elementManager.removeClass( el, 'menupanel-item-mark-'+style );
				}	
				this.markItemS1Count = 0;
			}
		}		
	};
	
	this.startMarkItemByStyle = function( item ) {
		if( this.markItemS1Count >= this.maxMarkItemS1Count )
			return;		
		
		var el = item.element;
		if( el ) {
			var markStyle = item.markStyle;
			var style = markStyle.style;
			var marked = markStyle.marked;
			if( marked == true ) {
				if( markStyle.unmark == true ) {
					elementManager.removeClass( el, 'menupanel-item-mark-'+style );
					markStyle.unmark = false;
				} else {
					elementManager.addClass( el, 'menupanel-item-mark-'+style );
					markStyle.unmark = true;
					this.markItemS1Count++;
				}
				window.setTimeout( function() {						
					instance.startMarkItemByStyle( item ); 
				}, instance.markItemByStyleDelay );
			} else {
				elementManager.removeClass( el, 'menupanel-item-mark-'+style );
			}
		}		
	};
	
	this.markItem = function( name ) {
		var item = this.itens[ name ];
		if( item ) {
			var el = item.element;
			if( el ) {
				elementManager.addClass( el, 'menupanel-item-selected' );
				if( this.currentMenuItem )
					elementManager.removeClass( this.currentMenuItem, 'menupanel-item-selected' );
				this.currentMenuItem = el;
				return true;
			}
		}		
		return false;
	};
		
	this.executeItem = function( name ) {
		var item = this.itens[ name ];
		if( typeof( item.onclick ) == 'function' )
			item.onclick.call( this );
		else eval( item.onclick );
	};
	
}
