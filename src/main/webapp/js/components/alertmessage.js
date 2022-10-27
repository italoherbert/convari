
var alertMessage = new AlertMessage();
function AlertMessage() {
	
	this.elementId = 'alert_message';
	this.divElementId = 'alert_message_div';
	this.panelElementId = 'alert_message_panel';
	this.loadingAnimationElementId = 'alert_message_points';
	
	this.panelCSSClassName = 'message';
	
	this.loadingAnimationInterval = 500;
	this.loadingAnimationPointsCount = 3;
	
	this.finish = false;
	
	var loadingAnimationInterval = null;	
	
	this.showAndStartProcessingUpdate = function( params ) {
		var pars = params;
		pars.showLoadingAnimation = true;
		pars.className = 'processing';
		this.show( pars );
		this.startProcessingUpdate();
	};
	
	this.hideAndStopProcessingUpdate = function() {
		this.stopProcessingUpdate();
		this.hide();
	};
	
	this.show = function( params ) {			
		var html = "";
		var loadHTML = undefined;
		var showLoadingAnimation = undefined;		
		var className = this.panelCSSClassName;
		if( params ) {
			if( params.loadHTML )
				loadHTML = params.loadHTML;
			else {
				showLoadingAnimation = params.showLoadingAnimation;
				if( params.html )
					html = params.html;				
			}
			if( params.className )
				className = params.className;
			if( params.finish )
				this.finish = params.finish;			
		}
		var panelElement = document.getElementById( this.panelElementId );
		if( panelElement ) {
			panelElement.className = className;	
				
			var ok = true;
			if( typeof( loadHTML ) == 'function' ) {
				var resp = loadHTML.call( this, { id : this.elementId } );
				ok = ( resp == true ? true : false );
			} else {
				var el = document.getElementById( this.elementId );
				el.innerHTML = html + ( showLoadingAnimation == true ? this.buildLoadingAnimation() : "" );
			}
			
			if( ok )
				elementManager.showById( this.divElementId, true );
			
			return ok;
		}
		return false;
	};
	
	this.hide = function() {	
		if( this.finish == true )
			return true;
		var el = document.getElementById( this.elementId );
		if( el ) {
			el.innerHTML = "";
			elementManager.showById( this.divElementId, false );
			return true;
		}
		return false;
	};
	
	this.startProcessingUpdate = function() {		
		var instance = this;
		var count = 0;
		loadingAnimationInterval = window.setInterval( function() {
			var el = document.getElementById( instance.loadingAnimationElementId );
			if( el != null) {
				if( count == 0 )
					el.innerHTML = ".";
				else el.innerHTML += ".";
				count = (count+1) % instance.loadingAnimationPointsCount;				
			}
		}, this.loadingAnimationInterval );
	};
	
	this.stopProcessingUpdate = function() {		
		if( loadingAnimationInterval != null ) {
			window.clearInterval( loadingAnimationInterval );
			loadingAnimationInterval = null;
		}
	};
	
	this.buildLoadingAnimation = function() {
		return "<span id='" + this.loadingAnimationElementId + "'></span>";
	};
	
}