
var topic_simple_find_onload = function() {
	document.getElementById( 'label.blur.find.topic' ).onclick = function() {
		document.topic_simple_find_form.tquery.focus();
	};			
	
	document.topic_simple_find_form.tquery.onfocus = function() {
		elementManager.showById( 'label.blur.find.topic', false );
	};

	document.topic_simple_find_form.tquery.onblur = function() {
		if( this.value.length == 0 ) {
			elementManager.showById( 'label.blur.find.topic', true );
		}
	};
	
	document.getElementById( 'topic_simple_find_form_bt' ).onclick = function() {
		topicFinder.params = { tquery : document.topic_simple_find_form.tquery.value }; 
		componentLoader.loadComponentByName( TOPIC_FIND_PAGE );
	};
	
	document.topic_simple_find_form.tquery.onkeypress = function( e ) {
		var code = ( window.event ? window.event.keyCode : e.keyCode );
		if( code == 13 ) {
			topicFinder.params = { tquery : document.topic_simple_find_form.tquery.value }; 
			componentLoader.loadComponentByName( TOPIC_FIND_PAGE );
			return false;
		}
		return true;
	};	
};