
var comment = new Comment();
function Comment() {
		
	this.addComment = function() {
		sender.send( {
			cmd : 'add-comment',
			parameters : function( params ) {
				var pars = urlutil.params();
				return {
					'uid' : pars[ 'uid' ],
					'message' : document.comment_form.message.value
				};
			},
			infoId : 'add_comment_info_msg',
			errorId : 'add_comment_error_msg',
			showProcessingMessage : true,			
			validate : function( params ) {
				var form = document.comment_form;
				return validator.validateAll( [
   				    { type : validator.REQUIRED, labelKey : 'label.field.comment', value : form.message.value }
   				] );				
			}, 
			success : function( params ) {
				document.comment_form.message.value = "";
			}
		} );
		
	};
		
}
