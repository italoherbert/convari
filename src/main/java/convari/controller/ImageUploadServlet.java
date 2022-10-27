package convari.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import convari.controller.operation.OperationException;
import convari.controller.operation.OperationParameters;
import convari.response.ResponseBuilder;



public class ImageUploadServlet extends GenericServlet {

	private static final long serialVersionUID = 1L;
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		response.setContentType( "text/html" );
		
		ResponseBuilder responseBuilder = super.createHtmlResponseBuilder( request, response );
		OperationParameters params = super.buildOperationParameters( request, response, responseBuilder );
					
		try {
			String cmd = request.getParameter( "cmd" );
			if( cmd == null ) {
				responseBuilder.processTextErrorMSG( "Parametro \"cmd\" necess�rio." ); 
			} else {
				operationController.execute( "user-image-upload", params );
			}
		} catch ( OperationException e ) {
			responseBuilder.processTextErrorMSG( "Funcionalidade indispon�vel!." );
			responseBuilder.processLN();
			responseBuilder.processTextErrorMSG( "Tente novamente mais tarde." ); 
		}
	}	
	
}
