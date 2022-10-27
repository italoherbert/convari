
var cookieManager = new CookieManager();
function CookieManager() {
		
	this.put = function( name, value, expires ) {	
		var cookie_content = name + "=" + value;
		if( expires ) {
			var currentDate = new Date();
			currentDate.setDate( currentDate.getDate() + expires );
			cookie_content += "; expires=" + currentDate.toUTCString();
		}
		cookie_content += "; path=/";
		document.cookie = cookie_content;	
	};

	this.get = function( name ) {
		var nameEQ = name + "=";
		var ca = document.cookie.split(';');
		for(var i=0;i < ca.length;i++) {
			var c = ca[i];
			while (c.charAt(0)==' ') 
				c = c.substring(1,c.length);
			if (c.indexOf(nameEQ) == 0) 
				return c.substring( nameEQ.length, c.length );
		}
		return null;
	};
	
	this.remove = function( name ) {
		this.put( name, '', -1 );
	};

}