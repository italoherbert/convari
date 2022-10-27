
var validator = new ValidatorManager();
function ValidatorManager() {
	
	this.dependences = {
		getLabel : function( key, params ) {
			throw "getLabel - Implementação necessária.";
		},
		getLabelForParams : function( key, params ) {
			throw "getLabelForParams - Implementação necessária.";
		}
	};
	
	this.REQUIRED = 'required';
	this.EQUAL = "equal";
	this.MAIL = "mail";
	
	this.validateAll = function( params ) {
		var errors = new Array();
		this.validateGroup( errors, params );
		return errors;
	};
	
	this.validateGroup = function( errors, params ) {
		var valid = true;
		for( var i in params )
			if( !this.validate( errors, params[i] ) )
				valid = false;
		return valid;
	};
	
	this.validate = function ( errors, params ) {
		if( params.type == this.REQUIRED )
			return this.validateRequired( errors, params );
		else if( params.type == this.EQUAL )
			return this.validateEqual( errors, params );
		else if( params.type == this.MAIL )
			return this.validateMail( errors, params );
		else throw "Validator - Tipo de validação inválido. TIPO="+type;
	};
	
	this.validateRequired = function( errors, params ) {
		var label = this.dependences.getLabelForParams( params );
		var value = params.value;
		
		var valid = false;
		if( value != undefined && value != null )
			valid = value.length > 0;		
		
		if( valid == false ) {				
			errors.push( this.dependences.getLabel( 'error.request.param.required', { '1' : label } ) );
		}
		return valid;
	};
	
	this.validateEqual = function( errors, params ) {
		var p1 = params.param1;
		var p2 = params.param2;
		if( p1.value != p2.value ) {
			var label1 = this.dependences.getLabelForParams( p1 );
			var label2 = this.dependences.getLabelForParams( p2 );
			errors.push( this.dependences.getLabel( 'error.request.param.notequal', { '1' : label1 , '2' : label2 } ) );
			return false;
		}
		return true;
	};
		
	this.validateMail = function( errors, params ) {
		var value = params.value;
		if( /^[\w\._\-]+@[\w]+\.[\w]+(\.[\w]+)?$/.test( value ) == false ) {
			errors.push( this.dependences.getLabel( 'error.request.param.mail' ) );
			return false;
		}
		return true;		
	};		
	
}