
function loadXml( xml ) {
	try {
		var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
		xmlDoc.async = false;				
		xmlDoc.loadXML( xml );
		return xmlDoc;
	} catch ( e ) {
		return ( new DOMParser() ).parseFromString( xml, "text/xml" );
	}

}

function nodeValue( parent, node_name ) {
	var el = parent.getElementsByTagName( node_name )[0];	
	if( el == undefined || el == null )
		return undefined;
	
	var value = "";
	if( el.firstChild != undefined && el.firstChild != null ) {
		var nodeValue = el.firstChild.nodeValue;
		if( nodeValue != undefined && nodeValue != null )
			value = nodeValue;
	}	
	if( value == undefined || value == null || value == 'null' )
		return "";
	return value.replace( /\n/gi, "<br />" );	
}