
function ProgressBar( _rootElementId ) {
	
	var progressBarPrefix = "_progressbar";
	var contentPrefix = "_content";
	var contentFilledPrefix = "_content_filled";
	var labelPrefix = "_label";
	var rightPrefix = "_right";
	var builded = false;
	
	this.rootElementId = _rootElementId;		
	
	this.build = function() {
		var rootElement = document.getElementById( this.rootElementId );
		if( !rootElement )
			throw "ProgressBar - Elemento não encontrado. ID="+this.rootElementId;
				
		var progressBarElement = document.createElement( 'div' );
		progressBarElement.id = this.rootElementId + progressBarPrefix;
		progressBarElement.className = 'progressbar';
		
		var contentElement = document.createElement( 'div' );
		contentElement.id = this.rootElementId + contentPrefix;
		contentElement.className = 'progressbar-content';
		
		var contentFilledElement = document.createElement( 'div' );
		contentFilledElement.id = this.rootElementId + contentFilledPrefix;
		contentFilledElement.className = 'progressbar-content-filled';
		
		var labelElement = document.createElement( 'span' );
		labelElement.id = this.rootElementId + labelPrefix;
		labelElement.className = 'progressbar-label';

		var rightElement = document.getElementById( this.rootElementId + rightPrefix );
		if( rightElement ) {
			var className = rightElement.className;
			if( className )
				className = className + ' progressbar-right';
			else className = 'progressbar-right';			
			rightElement.className = className;
			rootElement.insertBefore( progressBarElement, rightElement );
			
			show( rightElement, true );
		}
		else rootElement.appendChild( progressBarElement );
		progressBarElement.appendChild( contentElement );		
		contentElement.appendChild( labelElement );
		contentElement.appendChild( contentFilledElement );
		this.setProgressValue( 0 );
		builded = true;
	};		
	
	this.isBuilded = function() {
		return builded;
	};
	
	this.buildOrReinit = function() {
		if( builded == true ) {
			this.setProgressValue( 0 );
			
			var rootElement = document.getElementById( this.rootElementId );			
			var rightElement = document.getElementById( this.rootElementId + rightPrefix );
			show( rootElement, true );						
			show( rightElement, true );
		} else {
			this.build();
		}
	};
	
	this.setLabel = function( label ) {		
		var label_el_id = this.rootElementId + labelPrefix;
		var label_el = document.getElementById( label_el_id );
		
		if( !label_el )
			throw "ProgressBar (Label) - Elemento não encontrado. ID="+label_el_id;
		
		label_el.innerHTML = label;	
	};
	
	this.setProgressValue = function( value ) {		
		var fill_el_id = this.rootElementId + contentFilledPrefix;
		var label_el_id = this.rootElementId + labelPrefix;
		var fill_el = document.getElementById( fill_el_id );
		var label_el = document.getElementById( label_el_id );
		if( !fill_el )
			throw "ProgressBar (Fill) - Elemento não encontrado. ID="+fill_el_id;
		
		fill_el.setAttribute( 'style', 'width:'+(2*value)+'px' ); 
		
		if( !label_el )
			throw "ProgressBar (Label) - Elemento não encontrado. ID="+label_el;
		
		label_el.innerHTML = value+'% Concluído';		
	};
	
	function show( el, visible ) {
		if( el ) {
			var style = (el.style ? el.style : el );
			if( visible == 'false' ) {
				style.visibility = 'hidden';
				style.display = 'none';
			} else {
				style.visibility = 'visible';
				style.display = 'block';
			}
		}
	}
	
}