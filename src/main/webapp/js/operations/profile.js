
var profile = new Profile();
function Profile() {			

	this.dependences = {
		getLabel : function( key, params ) {
			throw "getLabel - Implementação necessária.";
		}	
	};		
	
	this.userImagePB = new ProgressBar( 'send_user_image_pb' );
	this.cancelUserImageUpload = false;
		
	this.uploadUserImage = function() {		
		this.userImagePB.buildOrReinit();
		elementManager.showById( 'user_image_upload_form_div', false );
		this.processUploadProgress();		
	};
	
	this.cancelUploadUserImage = function() {
		elementManager.showById( 'send_user_image_pb_right', false );		
		this.cancelUserImageUpload = true;
	};
		
	this.processUploadProgress = function() {				
		var op = undefined;
		if( this.cancelUserImageUpload == true ) {
			op = 'cancel';
			this.cancelUserImageUpload = false;
		} else {
			op = 'progress';
		}
		
		var pars = urlutil.params();
		sender.send( { 
			cmd : "user-image-upload-progress",
			parameters : { 
				'uid' : pars[ 'uid' ],
				'op'  : op
			},
			infoId : 'load_user_image_info_msg',
			errorId : 'load_user_image_error_msg',
			showProcessingMessage : false,
			success : function( params ) {
				var source = params.invoker.source;
				
				var doc = params.doc;
				var body = doc.getElementsByTagName( 'body' )[0];
				var progress = body.getElementsByTagName( 'progress' )[0];
				
				source.processProgressBarResponse( {
					status : progress.getAttribute( 'status' ),
					value : progress.getAttribute( 'value' ),
					image : progress.getAttribute( 'imagepath' )
				} );
			},
			error : function( params ) {
				elementManager.showById( 'user_image_upload_form_div', true );
				elementManager.showById( source.userImagePB.rootElementId, false );
			},
			invoker : {
				source : this
			}
		} );
			
	};
	
	this.processProgressBarResponse = function( params ) {
		var status = params.status;
		var value = params.value;
		var image = params.image;
		
		if( status == 'ready' || status == 'processing' ) {
			if( status == 'processing' ) {
				this.userImagePB.setProgressValue( value );
			}
			this.processUploadProgress();
		} else {
			elementManager.showById( 'send_user_image_pb_right', false );
			if( status == 'canceling' || status == 'finishing' ) {
				if( status == 'canceling' ) {
					this.userImagePB.setLabel( this.dependences.getLabel( 'label.progressbar.canceling' ) );
				} else if( status == 'finishing' ) {
					this.userImagePB.setLabel( this.dependences.getLabel( 'label.progressbar.finishing' ) );
				}
				this.processUploadProgress();
			} else if( status == 'finished' || status == 'canceled' || status == 'failed' ) {					
				if( status == 'finished' ) {
					imageUtil.setImageById( 'user_image_img', image );				
					this.userImagePB.setLabel( this.dependences.getLabel( 'label.progressbar.finished' ) );
				} else if( status == 'canceled' ) {
					this.userImagePB.setLabel( this.dependences.getLabel( 'label.progressbar.canceled' ) );
				}						
				elementManager.showById( 'user_image_upload_form_div', true );
				elementManager.showById( this.userImagePB.rootElementId, false );
			} else {
				throw "Upload de Imagem - status inválido. STATUS="+status;
			}
		}
	};
		
	this.load = function() {
		var pars = urlutil.params();
						
		sender.send( {
			cmd : "get-profile-data",
			parameters : {
				'uid' : pars[ 'uid' ]
			},
			infoId : 'load_profile_info_msg',
			errorId : 'load_profile_error_msg',
			showProcessingMessage : true,
			success : function( params ) {
				var getLabel = params.invoker.getLabel;
				
				var doc = params.doc;

				var body = doc.getElementsByTagName( 'body' )[0];
				var user = body.getElementsByTagName( 'user' )[0];
							
				var generalData = user.getElementsByTagName( 'general-data' )[0];
				var contactData = user.getElementsByTagName( 'contact-data' )[0];
				var professionalData = user.getElementsByTagName( 'professional-data' )[0];

				var enderEl = contactData.getElementsByTagName( 'ender' )[0];				
				var telEl = contactData.getElementsByTagName( 'tel' )[0];
				var mailEl = contactData.getElementsByTagName( 'mail' )[0];

				var enderVisible = enderEl.getAttribute( 'visible' ); 
				var telVisible = telEl.getAttribute( 'visible' ); 
				var mailVisible = mailEl.getAttribute( 'visible' ); 

				var name = nodeValue( generalData, 'name' );
				var lastname = nodeValue( generalData, 'lastname' );
				var sex = nodeValue( generalData, 'sex' );
				var website = nodeValue( generalData, 'website' );				

				var ender = nodeValue( contactData, 'ender' );
				var city = nodeValue( contactData, 'city' );
				var state = nodeValue( contactData, 'state' );
				var country = nodeValue( contactData, 'country' );				
				var tel = nodeValue( contactData, 'tel' );
				var tel2 = nodeValue( contactData, 'tel2' );
				var mail = nodeValue( contactData, 'mail' );								
				var mail2 = nodeValue( contactData, 'mail2' );
						
				var occupation = nodeValue( professionalData, 'occupation' );
				var academic = nodeValue( professionalData, 'academic' );
				
				if( sex == 'm' )
					sex = getLabel( 'label.sex.m' );
				else if( sex == 'f')
					sex = getLabel( 'label.sex.f' );				
													
				var generalDataElement = "";				
				var contactDataElement = "";			
				var professionalDataElement = "";
				
				generalDataElement += htmlBuilder.buildProfileFieldValue( { value : name+" "+lastname } );
				generalDataElement += htmlBuilder.buildProfileFieldValue( { value : sex } );
				if( website && website.length > 0 )
					generalDataElement += "<a href='"+website+"'>" + htmlBuilder.buildProfileFieldValue( { value : website } ) + "</a>";
				
				if( enderVisible == 'true' ) {
					if( city && city.length > 0 && state && state.length > 0 )
							state = " - "+state;																	
					if( ( city && city.length > 0 || state && state.length > 0 ) && country && country.length > 0 )
						country = ", " + country;						
													
					contactDataElement += htmlBuilder.buildProfileFieldValue( { value : ender } );
					contactDataElement += htmlBuilder.buildProfileFieldValue( { value : city+state+country } ); 
				}
				if( telVisible == 'true' ) {
					if( tel && tel.length > 0 && tel2 && tel2.length > 0 )
						tel2 = ", "+tel2;
					contactDataElement += htmlBuilder.buildProfileFieldValue( { value : tel+tel2 } );
				}
				if( mailVisible == 'true' ) {
					if( mail && mail.length > 0 )
						mail = "<a href='mailto:"+mail+"'>" + mail + "</a>";
					if( mail && mail.length > 0 && mail2 && mail2.length > 0 )
						mail2 = ", " + "<a href='mailto:"+mail2+"'>" + mail2 + "</a>"; 					
					contactDataElement += htmlBuilder.buildProfileFieldValue( { value : mail+mail2 } ); 
				}
								
				professionalDataElement += htmlBuilder.buildProfileFieldValue( { value : occupation } );
				professionalDataElement += htmlBuilder.buildProfileFieldValue( { value : academic } );
				
				document.getElementById( 'general_data' ).innerHTML = generalDataElement;
				document.getElementById( 'contact_data' ).innerHTML = contactDataElement;
				document.getElementById( 'professional_data' ).innerHTML = professionalDataElement;				
			},
			invoker : {
				getLabel : this.dependences.getLabel
			}
		} );						
	};

	this.editLoad = function() {		
		var pars = urlutil.params();
				
		sender.send( {
			cmd : "get-profile-data",
			parameters : {
				'uid' : pars[ 'uid' ]
			},
			errorId : 'load_profile_error_msg', 
			infoId : 'load_profile_info_msg',
			showProcessingMessage : true,
			success : function( params ) {
				var doc = params.doc;

				var body = doc.getElementsByTagName( 'body' )[0];
				var user = body.getElementsByTagName( 'user' )[0];
							
				var generalData = user.getElementsByTagName( 'general-data' )[0];
				var contactData = user.getElementsByTagName( 'contact-data' )[0];
				var professionalData = user.getElementsByTagName( 'professional-data' )[0];

				var name = nodeValue( generalData, 'name' );
				var lastname = nodeValue( generalData, 'lastname' );
				var sex = nodeValue( generalData, 'sex' );
				var website = nodeValue( generalData, 'website' );				

				var ender = nodeValue( contactData, 'ender' );
				var city = nodeValue( contactData, 'city' );
				var state = nodeValue( contactData, 'state' );
				var country = nodeValue( contactData, 'country' );				
				var tel = nodeValue( contactData, 'tel' );
				var tel2 = nodeValue( contactData, 'tel2' );
				var mail = nodeValue( contactData, 'mail' );								
				var mail2 = nodeValue( contactData, 'mail2' );
						
				var occupation = nodeValue( professionalData, 'occupation' );
				var academic = nodeValue( professionalData, 'academic' );
																
				document.profile_form.name.value = name;
				document.profile_form.lastname.value = lastname;
				document.profile_form.sex_cb.selectedIndex = ( sex == 'f' ? 1 : 0 ) ;
				document.profile_form.website.value = website;
				
				document.profile_form.ender.value = ender;
				document.profile_form.city.value = city;
				document.profile_form.state.value = state;
				document.profile_form.country.value = country;
				
				document.profile_form.tel.value = tel;
				document.profile_form.tel2.value = tel2;
				document.profile_form.mail.value = mail;
				document.profile_form.confirmmail.value = mail;
				document.profile_form.mail2.value = mail2;
				document.profile_form.confirmmail2.value = mail2;
				
				document.profile_form.occupation.value = occupation;
				document.profile_form.academic.value = academic;
			}				
		} );					
	};
	
	this.saveGeneralData = function() {		
		sender.send( {
			cmd : "save-general-data",
			infoId : 'save_general_data_info_msg',
			errorId : 'save_general_data_error_msg',
			showProcessingMessage : true,
			parameters : function( params ) {
				var pars = urlutil.params();
				return {
					'uid'		: pars[ 'uid' ],
					'name' 		: document.profile_form.name.value,
					'lastname'  : document.profile_form.lastname.value,
					'sex'  		: document.profile_form.sex_cb.value,
					'website'  	: document.profile_form.website.value
				};
			},
			validate : function( params ) {
				var form = document.profile_form;		
				return validator.validateAll( [
   					{ type : validator.REQUIRED, labelKey : 'label.field.name', value : form.name.value },
   					{ type : validator.REQUIRED, labelKey : 'label.field.lastname', value : form.lastname.value }
   				] );
				return new Array();
			},
			success : function( params ) {
				var name = document.profile_form.name.value;
				var lastname = document.profile_form.lastname.value;
				
				var el = document.getElementById( 'name_userinfo' );
				if( el )				
					el.innerHTML = name+" "+lastname;
				el = document.getElementById( 'navigation_user_name' );
				if( el )				
					el.innerHTML = name+" "+lastname;				
			}
		} );
	};		
	
	this.saveContactData = function() {
		sender.send( {
			cmd : "save-contact-data",
			infoId : 'save_contact_data_info_msg',
			errorId : 'save_contact_data_error_msg',
			showProcessingMessage : true,
			parameters : function( params ) {
				var pars = urlutil.params();
				return {
					'uid'		: pars[ 'uid' ],
					'ender'		: document.profile_form.ender.value,
					'city'		: document.profile_form.city.value,
					'state'		: document.profile_form.state.value,
					'country'	: document.profile_form.country.value,
					'tel'		: document.profile_form.tel.value,
					'tel2'		: document.profile_form.tel2.value,
					'mail'		: document.profile_form.mail.value,
					'mail2'		: document.profile_form.mail2.value
				};
			},
			validate : function( params ) {
				var form = document.profile_form;				
				var errors = validator.validateAll( [
   					{ type : validator.REQUIRED, labelKey : 'label.field.mail', value : form.mail.value },
   					{ type : validator.EQUAL,
   					    param1 : { labelKey : 'label.field.mail', value : form.mail.value }, 
   					    param2 : { labelKey : 'label.field.mail.confirm', value : form.confirmmail.value } },
   					{ type : validator.EQUAL,
   	   				    param1 : { labelKey : 'label.field.mail2', value : form.mail2.value }, 
   	   				    param2 : { labelKey : 'label.field.mail2.confirm', value : form.confirmmail2.value } }   					  
   				] );
				
				if( errors.length == 0 ) {
					validator.validateMail( errors, { labelKey : 'label.field.mail', type : validator.MAIL, value : form.mail.value } );
					if( form.mail2.value && form.mail2.value.length > 0 )
						validator.validateMail( errors, { labelKey : 'label.field.mail2', type : validator.MAIL, value : form.mail2.value } );
				}
				return errors;
			}
		} );		
	};
	
	this.saveProfessionalData = function() {
		sender.send( {
			cmd : "save-professional-data",
			infoId : 'save_professional_data_info_msg',
			errorId : 'save_professional_data_error_msg',
			showProcessingMessage : true,
			parameters : function( params ) {
				var pars = urlutil.params();
				return {
					'uid'		: pars[ 'uid' ],
					'occupation' : document.profile_form.occupation.value,
					'academic'  : document.profile_form.academic.value
				};
			}
		} );		
	};
		
}