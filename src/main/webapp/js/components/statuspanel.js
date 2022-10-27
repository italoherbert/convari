
var statusPanel = new StatusPanel();
function StatusPanel() {

	var instance = this;

	this.elementId = undefined;
	this.markOptionAfterSelection = true;
	
	this.options = new Array();	
	this.optionNamesMap = new Array();
	this.currentOptionElement = null;
	this.selectedLabelElement = null;
	
	this.alterPermitted = false;
	this.alterPermittedIcon = undefined;
	this.hoverAlterPermittedIcon = undefined;
	
	this.build = function( optionName ) {
		var el = document.getElementById( this.elementId );
		if( !el )
			throw "ID de element inválido. ID="+this.elementId;
				
		var statusPanelElement = document.createElement( 'div' );
		statusPanelElement.className = ( this.alterPermitted == true ? 'statuspanel' : 'statuspanel-notalter' );
				
		var statusSubpanelElement = document.createElement( 'span' );
		statusSubpanelElement.id = this.elementId+'_'+'submenu';
		statusSubpanelElement.className = 'statuspanel-sub';

		var optionStatusPanelElement = document.createElement( 'div' );				
												
		this.currentOptionElement = document.createElement( 'span' );
		this.currentOptionElement.className = ( this.alterPermitted == true ? 'statuspanel-option' : 'statuspanel-option-notalter' );				
		optionStatusPanelElement.appendChild( this.currentOptionElement );		
		
		statusPanelElement.appendChild( optionStatusPanelElement );
		statusPanelElement.appendChild( statusSubpanelElement );
		
		el.appendChild( statusPanelElement );				
		
		elementManager.show( statusSubpanelElement, false );
		
		if( this.alterPermitted == true ) {
			if( this.alterPermittedIcon ) {
				var alterPermissionIconElement = document.createElement( 'img' );
				alterPermissionIconElement.id = this.elementId+'_'+'alterpermittedicon';
				alterPermissionIconElement.className = 'statuspanel-alterpermitted-icon';
				alterPermissionIconElement.src = this.alterPermittedIcon;
				optionStatusPanelElement.appendChild( alterPermissionIconElement );
				optionStatusPanelElement.appendChild( document.createElement( 'br' ) );
			}
			
			optionStatusPanelElement.onmouseover = function() {
				var submenuId = instance.elementId+'_'+'submenu';
				elementManager.showById( submenuId, true );				
				if( instance.hoverAlterPermittedIcon ) {
					var alterIconId = instance.elementId+'_'+'alterpermittedicon';
					var alterIconElement = document.getElementById( alterIconId );
					if( alterIconElement )
						alterIconElement.src = instance.hoverAlterPermittedIcon;					
				}

			};
			optionStatusPanelElement.onmouseout = function() {
				var submenuId = instance.elementId+'_'+'submenu';
				elementManager.showById( submenuId, false );								
				if( instance.alterPermittedIcon ) {
					var alterIconId = instance.elementId+'_'+'alterpermittedicon';
					var alterIconElement = document.getElementById( alterIconId );
					if( alterIconElement ) 
						alterIconElement.src = instance.alterPermittedIcon;
				}
			};
			statusSubpanelElement.onmouseover = function() {
				elementManager.show( this, true );
			};
			statusSubpanelElement.onmouseout = function() {
				elementManager.show( this, false );
			};
		}		
		
		for( var name in this.options ) {
			var option = this.options[ name ];			
			
			var optionElement = document.createElement( 'div' );
			optionElement.name = this.elementId+'_'+name;
			optionElement.className = ( this.alterPermitted == true ? 'statuspanel-option' : 'statuspanel-option-notalter' );
			optionElement.onclick = function() {
				var opName = this.name.replace( instance.elementId+'_', '' ); 
				instance.executeOption( opName );
			};
			if( option.visible == false )
				elementManager.show( optionElement, false );			
			
			if( option.icon ) {
				var iconElement = document.createElement( 'img' );
				iconElement.className = 'statuspanel-option-icon';
				iconElement.src = option.icon;
				optionElement.appendChild( iconElement );
				
				option.iconElement = iconElement;
			}
			
			var optionLabelElement = document.createElement( 'span' );
			optionLabelElement.className = 'statuspanel-option-label';						
			optionLabelElement.innerHTML = option.label;			
			optionElement.appendChild( optionLabelElement );
			
			statusSubpanelElement.appendChild( optionElement );
			
			option.element = optionElement;
			option.labelElement = optionLabelElement;
			this.optionNamesMap.push( name );
		}
		
		this.markOption( 'unoccupied' ); 		
	};
	
	this.markOptionByIndex = function( index ) {
		var name = this.optionNamesMap[ index ];
		if( name != undefined && name != null )
			this.markOption( name );
	};
	
	this.executeOptionByIndex = function( index ) {
		var name = this.optionNamesMap[ index ];
		if( name != undefined && name != null )
			this.executeOption( name );
	};
	
	this.markOption = function( name ) {
		var option = this.options[ name ];
		if( option ) {
			var el = option.element;
			if( el ) {
				this.currentOptionElement.innerHTML = el.innerHTML;			
				if( instance.alterPermitted == true ) {					
					var labelElement = option.labelElement;
					if( labelElement ) {
						elementManager.addClass( labelElement, 'statuspanel-option-selected' );
						if( this.selectedLabelElement )
							elementManager.removeClass( this.selectedLabelElement, 'statuspanel-option-selected' );					
						this.selectedLabelElement = labelElement;
					}
				}
			}
		}		
	};
	
	this.executeOption = function( name ) {
		var option = this.options[ name ];
		if( option.onclick ) {
			if( typeof( option.onclick ) == 'function' )
				this.onclick.call( this, name, option );
			else eval( option.onclick ); 
		}
	};
	
}
