package convari.controller.operation.post;

import italo.persistence.find.ControlDatesFindBean;
import italo.persistence.find.parameters.FindParameters;
import italo.persistence.find.parameters.TopicFindParameters;
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
import convari.persistence.bean.TopicBean;
import convari.persistence.bean.VisibilityBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.util.DateUtil;



public class FindTopicsOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {				
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam uidP = manager.createParam( "uid" );
		uidP.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );
		
		RequestValidatorParam userIdP = manager.createParam( "userid" );
		userIdP.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );
		
		RequestValidatorParam controlDateP = manager.createParam( "controldate" );
		controlDateP.getValidators().add( manager.createLongValidator( 0, Long.MAX_VALUE ) );

		RequestValidatorParam limitP = manager.createParam( "limit" );
		limitP.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );
		
		RequestValidatorParam pagesCountP = manager.createParam( "pagescount" );
		pagesCountP.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );

		RequestValidatorParam tqueryP = manager.createParam( "tquery" );
		tqueryP.getValidators().add( manager.createLengthValidator( 100 ) ); 
		
		RequestValidatorParam uqueryP = manager.createParam( "uquery" );
		uqueryP.getValidators().add( manager.createLengthValidator( 100 ) ); 
		
		RequestValidatorParam langP = manager.createParam( "lang", "Linguagem" );
		langP.getValidators().add( manager.createLengthValidator( 5 ) );
		
		vparams.add( uidP );
		vparams.add( userIdP );
		vparams.add( controlDateP );
		vparams.add( limitP );
		vparams.add( pagesCountP );
		vparams.add( tqueryP );
		vparams.add( uqueryP );
		vparams.add( langP );
	}
	
	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		HttpServletRequest request = parameters.getRequest();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		Document doc = responseBuilder.getDoc();
		DateUtil dateUtil = parameters.getDateUtil();
		
		String uidP = request.getParameter( "uid" );
		String userIdP = request.getParameter( "userid" );
		String pagesCountP = request.getParameter( "pagescount" );
		String controlDateP = request.getParameter( "controldate" );
		String limitP = request.getParameter( "limit" );
		String tqueryP = request.getParameter( "tquery" );
		String uqueryP = request.getParameter( "uquery" );
		
		String lang = request.getParameter( "lang" );

		int userId = FindParameters.INT_NULL;
		Timestamp controlDate = null;
		int limit = FindParameters.INT_NULL;
		int pagesCount = FindParameters.INT_NULL;
		if( userIdP != null )
			userId = Integer.parseInt( userIdP );
		if( controlDateP != null )
			controlDate = new Timestamp( Long.parseLong( controlDateP ) );
		if( limitP != null )
			limit = Integer.parseInt( limitP );
		if( pagesCountP != null )
			pagesCount = Integer.parseInt( pagesCountP );		

		try {
			String userVisibility = VisibilityBean.PUBLIC_VISIBILITY;
			if( uidP != null ) {
				int uid = Integer.parseInt( uidP );
				userVisibility = controller.getVisibility( parameters, uid );									
			}				
			int vWeight = controller.getWeightForVisibility( userVisibility );
			
			TopicFindParameters findParameters = new TopicFindParameters();
			findParameters.setControlDate( controlDate );
			findParameters.setPagesCount( pagesCount );
			findParameters.setUserId( userId );
			findParameters.setLimit( limit );
			findParameters.setVisibilityWeight( vWeight );
			findParameters.setTQuery( tqueryP );
			findParameters.setUQuery( uqueryP );
			
			List<TopicBean> topics = persistence.getPostManager().findTopics( findParameters );
			Element topicsNode = doc.createElement( "topics" );
			responseBuilder.getBodyNode().appendChild( topicsNode );
			
			if( topics.isEmpty() ) {
				responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.EMPTY_LIST_FIND_INFO );
			} else {				
				if( pagesCount != FindParameters.INT_NULL ) { 
					ControlDatesFindBean findBean = persistence.getPostManager().findControlDatesForTopics( findParameters );
					
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
				
				for( TopicBean topic: topics ) {
					Element topicNode = doc.createElement( "topic" );
					Element idNode = doc.createElement( "id" );
					Element descriptionNode = doc.createElement( "description" );
					Element visibilityNode = doc.createElement( "visibility" );
					Element dateNode = doc.createElement( "date" );
					
					Element userNode = doc.createElement( "user" );
					Element userIdNode = doc.createElement( "id" );
					Element nameNode = doc.createElement( "name" );
					Element imageNode = doc.createElement( "image" );
					
					userNode.appendChild( userIdNode );
					userNode.appendChild( nameNode );
					userNode.appendChild( imageNode );
					
					topicNode.appendChild( idNode );
					topicNode.appendChild( visibilityNode );
					topicNode.appendChild( descriptionNode );
					topicNode.appendChild( dateNode );
					topicNode.appendChild( userNode );
					topicsNode.appendChild( topicNode );					
					
					idNode.appendChild( responseBuilder.createTextNode( String.valueOf( topic.getId() ) ) );
					descriptionNode.appendChild( responseBuilder.createTextNode( topic.getDescription() ) );
					dateNode.appendChild( responseBuilder.createTextNode( dateUtil.formatToDateAndTime( topic.getDate(), lang ) ) );
					visibilityNode.appendChild( responseBuilder.createTextNode( topic.getVisibility() ) );
					
					userIdNode.appendChild( responseBuilder.createTextNode( String.valueOf( topic.getUserId() ) ) );
					nameNode.appendChild( responseBuilder.createTextNode( topic.getUserFirstname() + " " + topic.getUserLastname() ) );
					imageNode.appendChild( responseBuilder.createTextNode( topic.getUserImage() ) ); 
				}	
			}
		} catch (PersistenceException e) {
			throw new OperationException( e );
		}
	}
	

	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {		

	}

}
