
var languageManager = new LanguageManager();
function LanguageManager() {
		
	this.beforeLanguageChange = undefined;
	
	this.setLanguage = function( lang ) {
		var supported = messageManager.verifyLangSupported( lang );
		if( supported == true ) {
			if( typeof( this.beforeLanguageChange ) == 'function' )
				this.beforeLanguageChange.call( this, { lang : lang } );			
			cookieManager.put( 'lang', lang, 10 );	
			urlutil.redirect( urlutil.setParam( 'lang', lang ) );
		}
	};
	
}