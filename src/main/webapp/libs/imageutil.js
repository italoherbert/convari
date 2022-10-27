
var imageUtil = new ImageUtil();
function ImageUtil() {		
				
	this.setImageById = function( el_id, src ) {
		var el = document.getElementById( el_id );
		if( !el )
			throw "Elemento não encontrado por id. ID="+el_id;	
		this.setImage( el, src );
	};
	
	this.setImage = function( el, src ) {
		el.src = this.buildSRC( src );
	};
		
	this.buildSRC = function( src ) {
		return src + ( src.indexOf( '?' ) > -1 ? '&' : '?' ) + "control="+new Date().getTime();
	};
	
}
