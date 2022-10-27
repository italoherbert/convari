package convari.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import convari.controller.operation.OperationException;
import convari.controller.operation.OperationParameters;
import convari.controller.operation.SessionOperationException;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.response.ResponseBuilderException;
import convari.response.SysCodeConstants;




public class InvokerServlet extends GenericServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {		
		response.setContentType( "text/xml; charset=UTF-8" );		
		try {			
			String status = "ok";
			
			ResponseBuilder responseBuilder = super.createXmlResponseBuilder( request );					
			OperationParameters params = super.buildOperationParameters( request, response, responseBuilder );								
			if( responseBuilder.getErrorMessagesCount() == 0 ) {
				try {
					String cmd = params.getRequest().getParameter( "cmd" );
					if( cmd == null ) {
						params.getResponseBuilder().processKeyErrorMSG( KeyResponseMessageConstants.REQUIRED_REQUEST_PARAM_ERROR, "cmd" );  
					} else {
						operationController.execute( cmd, params );
					}	
					responseBuilder.setSystemStatus( SysCodeConstants.OK );
				} catch ( SessionOperationException e ) {
					status = "error";
				} catch ( OperationException e ) {
					e.printStackTrace();
					responseController.addSystemErrorMessage( KeyResponseMessageConstants.SYSTEM_ERROR );
					responseController.addSystemInfoMessage( KeyResponseMessageConstants.SYSTEM_INFO );
					status = "error";
				}
			}
			
			if( responseBuilder.getErrorMessagesCount() > 0 )
				status = "error";
			responseBuilder.getStatusAttr().setNodeValue( status );
			
			response.getWriter().println( params.getResponseBuilder().build() );
		} catch (ResponseBuilderException e) {			
			throw new ServletException( e );
		}			
	}
	
}
