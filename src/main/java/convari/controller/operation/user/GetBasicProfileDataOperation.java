package convari.controller.operation.user;

import italo.validate.RequestValidatorParam;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import convari.controller.operation.Operation;
import convari.controller.operation.OperationController;
import convari.controller.operation.OperationException;
import convari.controller.operation.OperationParameters;
import convari.controller.operation.ValidationManager;
import convari.controller.operation.ValidationOpException;
import convari.persistence.Persistence;
import convari.persistence.PersistenceException;
import convari.persistence.bean.BasicUserDataBean;
import convari.response.ResponseBuilder;


public class GetBasicProfileDataOperation implements Operation {
	
	public void initialize(OperationController controller, List<RequestValidatorParam> vparams) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam param = manager.createParam( "uid" );
		param.getValidators().add( manager.createNotNullValidator() );
		param.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );

		vparams.add( param );
	}
	
	public void execute( OperationController controller, OperationParameters parameters )	throws OperationException {
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		HttpServletRequest request = parameters.getRequest();
		Persistence persistence = parameters.getPersistence();
		
		String uidParam = request.getParameter( "uid" );		
		int uid = Integer.parseInt( uidParam );
		
		BasicUserDataBean bdata = null;
		try {					
			bdata = persistence.getUserManager().findBasicUserData( uid );										
		} catch (PersistenceException e) {
			throw new OperationException( e );
		}
		
		Document doc = responseBuilder.getDoc();
		Element userNode = doc.createElement( "user" );
		Attr foundAttr = doc.createAttribute( "found" );
		userNode.setAttributeNode( foundAttr );
		
		if( bdata != null ) {
			Element imagePathNode = doc.createElement( "image" );		
			Element nameNode = doc.createElement( "name" );
			Element lastnameNode = doc.createElement( "lastname" );
			Element sexNode = doc.createElement( "sex" );			
			Element professionalDataNode = doc.createElement( "professionaldata" ); 
						
			userNode.appendChild( imagePathNode );
			userNode.appendChild( nameNode );
			userNode.appendChild( lastnameNode );
			userNode.appendChild( sexNode );
			
			userNode.appendChild( professionalDataNode );
			

			foundAttr.setNodeValue( "true" );
			imagePathNode.appendChild( responseBuilder.createTextNode( bdata.getImagepath() ) );
			nameNode.appendChild( responseBuilder.createTextNode( bdata.getName() ) );
			lastnameNode.appendChild( responseBuilder.createTextNode( bdata.getLastname() ) );
			sexNode.appendChild( responseBuilder.createTextNode( bdata.getSex() ) );	
			
			String professionalData = null;
			if( bdata.getOccupation() != null ) {
				professionalData = bdata.getOccupation();
			} else if( bdata.getAcademic() != null ) {
				professionalData = bdata.getAcademic();
			}			
			
			professionalDataNode.appendChild( responseBuilder.createTextNode( professionalData ) );
			
		} else {
			foundAttr.setNodeValue( "false" );
		}
		responseBuilder.getBodyNode().appendChild( userNode );
										
	}
	
	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		
	}

}
