
var elementManager = new ElementManager();
function ElementManager() {		
				
	this.findClassById = function( el_id, className ) {
		var el = document.getElementById( el_id );
		if( !el )
			throw "Elemento não encontrado por id. ID="+el_id;			
		return this.findClass( el, className );
	};
	
	this.addClassById = function( el_id, className ) {
		var el = document.getElementById( el_id );
		if( !el )
			throw "Elemento não encontrado por id. ID="+el_id;				
		this.addClass( el, className );
	};
	
	this.removeClassById = function( el_id, className ) {
		var el = document.getElementById( el_id );
		if( !el )
			throw "Elemento não encontrado por id. ID="+el_id;		
		this.removeClass( el, className );
	};
	
	this.showOrHideById = function( el_id, params ) {
		var el = document.getElementById( el_id );
		if( el == null )
			throw "Elemento não encontrado por id. ID="+el_id;		
		this.showOrHide( el, params );
	};
	
	this.showById = function( el_id, visible ) {
		var el = document.getElementById( el_id );
		if( el == null )
			throw "Elemento não encontrado por id. ID="+el_id;
		this.show( el, visible );
	};
	
	this.getStyleById = function( el_id ) {
		var el = document.getElementById( el_id );
		if( el == null )
			throw "Elemento não encontrado por id. ID="+el_id;
		return this.getStyle( el );
	};
	
	this.findClass = function( el, className ) {
		var cName = ( el.className ? el.className+" " : "" );		 
		return( cName.indexOf( className ) > -1 );
	};
	
	this.addClass = function( el, className ) {
		var cName = ( el.className ? el.className+" " : "" );		 
		el.className = cName + className;
	};
	
	this.removeClass = function( el, className ) {
		var cName = el.className;		 
		if( cName ) {
			cName = cName.replace( className, '' );
			cName = cName.replace( /\s+/gi, ' ');
		} else cName = "";
		el.className = cName;
	};
		
	this.showOrHide = function( el, params ) {
		var style = ( el.style ? el.style : el );
		var isHidden = style.visibility == 'hidden';
		if( isHidden == true ) {
			style.display = 'block';
			style.visibility = 'visible';
		} else {
			style.display = 'none';
			style.visibility = 'hidden';
		}
		if( params )
			if( typeof( params.callback ) == 'function' )
				params.callback.call( this, { visible : isHidden, invoker : params.invoker } );
	};
			
	this.show = function( el, visible ) {
		var style = ( el.style ? el.style : el );
		if( visible == true ) {
			style.display = 'block';
			style.visibility = 'visible';
		} else {
			style.display = 'none';
			style.visibility = 'hidden';
		}
		
	};
			
	this.getStyle = function( el ) {
		return ( el.style ? el.style : el );
	};
	
}
