package convari.controller.operation.user;

import italo.persistence.find.ControlDatesFindBean;
import italo.persistence.find.parameters.FindParameters;
import italo.persistence.find.parameters.UserFindParameters;
import italo.validate.RequestValidatorParam;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import convari.persistence.bean.FindUserBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;



public class FindUsersOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
				
		RequestValidatorParam controlDateP = manager.createParam( "controldate" );
		controlDateP.getValidators().add( manager.createLongValidator( 0, Long.MAX_VALUE ) );

		RequestValidatorParam limitP = manager.createParam( "limit" );
		limitP.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );
		
		RequestValidatorParam pagesCountP = manager.createParam( "pagescount" );
		pagesCountP.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );
		
		RequestValidatorParam namequeryP = manager.createParam( "namequery" );
		namequeryP.getValidators().add( manager.createLengthValidator( 100 ) ); 
		
		vparams.add( controlDateP );
		vparams.add( limitP );
		vparams.add( pagesCountP );
		vparams.add( namequeryP );
	}
	
	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		HttpServletRequest request = parameters.getRequest();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		Document doc = responseBuilder.getDoc();
		
		String pagesCountP = request.getParameter( "pagescount" );
		String controlDateP = request.getParameter( "controldate" );
		String limitP = request.getParameter( "limit" );
		String nameQuery = request.getParameter( "namequery" );
                                
		Timestamp controlDate = null;
		int limit = FindParameters.INT_NULL;
		int pagesCount = FindParameters.INT_NULL;
		if( controlDateP != null )
			controlDate = new Timestamp( Long.parseLong( controlDateP ) );
		if( limitP != null )
			limit = Integer.parseInt( limitP );
		if( pagesCountP != null )
			pagesCount = Integer.parseInt( pagesCountP );		

		try {			
			UserFindParameters findParameters = new UserFindParameters();
			findParameters.setControlDate( controlDate );
			findParameters.setPagesCount( pagesCount );
			findParameters.setLimit( limit );
			findParameters.setNameQuery( nameQuery );
			
			List<FindUserBean> users = persistence.getUserManager().find( findParameters ); 
			Element usersNode = doc.createElement( "users" );
			responseBuilder.getBodyNode().appendChild( usersNode );
			
			if( users.isEmpty() ) {
				responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.EMPTY_LIST_FIND_INFO );
			} else {				
				if( pagesCount != FindParameters.INT_NULL ) { 
					ControlDatesFindBean findBean = persistence.getUserManager().findControlDates( findParameters );
					
					Element controlDateNode = doc.createElement( "controldates" );
					for( Timestamp date:findBean.getControlDates() ) {						
						Element msDateNode = doc.createElement( "msdate" );
						controlDateNode.appendChild( msDateNode );						
						msDateNode.appendChild( responseBuilder.createTextNode( String.valueOf( date.getTime() ) ) );
					}
					controlDateNode.setAttribute( "count", String.valueOf( findBean.getControlDates().size() ) );
					controlDateNode.setAttribute( "hasMore", String.valueOf( findBean.getHasMore() ) );
					responseBuilder.getBodyNode().appendChild( controlDateNode );
				}
				
				for( FindUserBean user: users ) {
					Element userNode = doc.createElement( "user" );
					Element idNode = doc.createElement( "id" );
					Element nameNode = doc.createElement( "name" );
					Element imagePathNode = doc.createElement( "image" );
					
					userNode.appendChild( idNode );
					userNode.appendChild( nameNode );
					userNode.appendChild( imagePathNode );
					
					usersNode.appendChild( userNode );	
					
					idNode.appendChild( responseBuilder.createTextNode( String.valueOf( user.getId() ) ) );
					nameNode.appendChild( responseBuilder.createTextNode( user.getName()+" "+user.getLastname() ) );
					imagePathNode.appendChild( responseBuilder.createTextNode( user.getImagePath() ) );
				}	
			}
		} catch (PersistenceException e) {
			throw new OperationException( e );
		}
	}
	

	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {		
		
	}

}
