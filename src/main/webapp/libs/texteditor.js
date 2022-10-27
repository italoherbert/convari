
function TextEditor( _elementId) {
	
	this.cssPrefix = '-texteditor';
	this.elementId = _elementId;
	
	this.build = function() {
		var element = document.getElementById( this.elementId );
		if( !element )
			throw "TextEditor - Elemento não encontrado. ID="+this.elementId;
				
		var className = this.elementId+this.cssPrefix;
		var html = "";
		if( document.getElementById && document.designMode ) { 
			html += "<iframe id='"+this.elementId+"' class='"+className+"'></iframe>";
		} else {
			html += "<textarea id='"+this.elementId+"' name='"+this.elementId+"' class='"+className+"'></textarea>";
		}
		
		element.id = undefined;			
		element.innerHTML = html;
		
		document.getElementById( this.elementId ).contentWindow.document.designMode = "On";
		document.getElementById( this.elementId ).contentDocument.designMode = "On";
	};

	this.executeCommand = function( command ) {
		var editor = document.getElementById( this.elementId ).contentWindow;
		try {
			editor.document.execCommand( command, false, '' );
			editor.focus();
		} catch( e ) {
			
		}		
	};
	
	this.getHTMLContent = function() {
		return document.getElementById( this.elementId ).contentWindow.document.body.innerHTML;
	};
	
}