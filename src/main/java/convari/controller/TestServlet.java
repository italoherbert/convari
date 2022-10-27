package convari.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import convari.controller.operation.OperationException;
import convari.controller.operation.OperationParameters;
import convari.response.ResponseBuilder;



public class TestServlet extends GenericServlet {

	private static final long serialVersionUID = 1L;		
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		ResponseBuilder responseBuilder = super.createPlainResponseBuilder( request );
		OperationParameters parameters = super.buildOperationParameters( request, response, responseBuilder );
		try {
			super.operationController.execute( "test", parameters );
		} catch (OperationException e) {
			throw new ServletException( e );
		}
	}	
	
}
