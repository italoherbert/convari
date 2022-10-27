
var basic_profile_page_onload = function() {
	user.loadBasicProfile( { 
		responseProcessed : function( params ) {
			var image = params.image;
			var name = params.name;
			var lastname = params.lastname;
			var professionalData = params.professionalData;
			document.getElementById( 'basic_profile_image' ).innerHTML = htmlBuilder.buildUserImage( { imagePath : image } ); 
			document.getElementById( 'basic_profile_name' ).innerHTML = htmlBuilder.buildBasicProfile( {
				name : name, lastname : lastname, professionalData : professionalData
			} );
		}
	} );
};