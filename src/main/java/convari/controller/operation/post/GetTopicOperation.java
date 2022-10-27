package convari.controller.operation.post;

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
import convari.persistence.bean.PostBean;
import convari.persistence.bean.TopicBean;
import convari.persistence.bean.VisibilityBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.util.DateUtil;



public class GetTopicOperation implements Operation {

	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {		
		ValidationManager manager = controller.getValidationManager();
		
		RequestValidatorParam uidP = manager.createParam( "uid" );
		uidP.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );
				
		RequestValidatorParam tidP = manager.createParam( "tid" );
		tidP.getValidators().add( manager.createNotNullValidator() );
		tidP.getValidators().add( manager.createIntValidator( 0, Integer.MAX_VALUE ) );
		
		RequestValidatorParam controlDateP = manager.createParam( "controldate" );
		controlDateP.getValidators().add( manager.createLongValidator( 0, Long.MAX_VALUE ) );
		
		RequestValidatorParam langP = manager.createParam( "lang" );
		langP.getValidators().add( manager.createLengthValidator( 5 ) );

		vparams.add( uidP );
		vparams.add( tidP );
		vparams.add( controlDateP );
		vparams.add( langP );
	}
	
	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		HttpServletRequest request = parameters.getRequest();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		Document doc = responseBuilder.getDoc();
		DateUtil dateUtil = parameters.getDateUtil();
				
		String uidP = parameters.getRequest().getParameter( "uid" );
		
		String lang = parameters.getRequest().getParameter( "lang" );
		
		String paramId = request.getParameter( "tid" );
		int tid = Integer.parseInt( paramId );
								
		try {									
			TopicBean topic = persistence.getPostManager().findTopic( tid );	
			if( topic == null ) {
				responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.TOPIC_NOT_FOUND_ERROR );
			} else {				
				String userVisibility = VisibilityBean.PUBLIC_VISIBILITY;
				if( uidP != null ) {
					int uid = Integer.parseInt( uidP );
					userVisibility = controller.getVisibility( parameters, uid );					
				}
				
				int tVisibilityWeight = controller.getWeightForVisibility( topic.getVisibility() );
				int uVisibilityWeight = controller.getWeightForVisibility( userVisibility );
				
				if( uVisibilityWeight >= tVisibilityWeight ) {				
					Timestamp controlDate = null;
					String controlDateP = parameters.getRequest().getParameter( "controldate" );
					if( controlDateP != null )
						controlDate = new Timestamp( Long.parseLong( controlDateP ) );
					
					List<PostBean> posts = persistence.getPostManager().getPostsByTopic( tid, controlDate );
							
					Element topicNode = doc.createElement( "topic" );
					Element idNode = doc.createElement( "id" );
					Element descriptionNode = doc.createElement( "description" );
					Element dateNode = doc.createElement( "date" );
					Element visibilityNode = doc.createElement( "visibility" );
					Element postsNode = doc.createElement( "posts" );


					Element userNode = doc.createElement( "user" );
					Element userIdNode = doc.createElement( "id" );
					Element nameNode = doc.createElement( "name" );
					Element imageNode = doc.createElement( "image" );
					
					userNode.appendChild( userIdNode );
					userNode.appendChild( nameNode );
					userNode.appendChild( imageNode );
					
					topicNode.appendChild( idNode );
					topicNode.appendChild( descriptionNode );
					topicNode.appendChild( dateNode );
					topicNode.appendChild( visibilityNode );
					topicNode.appendChild( userNode );
					topicNode.appendChild( postsNode );					
		
					responseBuilder.getBodyNode().appendChild( topicNode );
								
					idNode.appendChild( responseBuilder.createTextNode( String.valueOf( topic.getId() ) ) );
					descriptionNode.appendChild( responseBuilder.createTextNode( topic.getDescription() ) );
					dateNode.appendChild( responseBuilder.createTextNode( dateUtil.formatToDateAndTime( topic.getDate(), lang ) ) ); 
					visibilityNode.appendChild( responseBuilder.createTextNode( topic.getVisibility() ) );

					userIdNode.appendChild( responseBuilder.createTextNode( String.valueOf( topic.getUserId() ) ) );
					nameNode.appendChild( responseBuilder.createTextNode( topic.getUserFirstname() + " " + topic.getUserLastname() ) );
					imageNode.appendChild( responseBuilder.createTextNode( topic.getUserImage() ) ); 
					
					if( posts.isEmpty() ) {
						postsNode.setAttribute( "controldate", "-1" );
						responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.EMPTY_LIST_POST_BY_TOPIC_INFO );
					} else {
						for( PostBean post: posts ) {
							Element postNode = doc.createElement( "post" );
							Element postIdNode = doc.createElement( "id" );
							Element postMessageNode = doc.createElement( "message" );
							Element postDateNode = doc.createElement( "date" );
			
							Element postUserNode = doc.createElement( "user" );
							Element postUserIdNode = doc.createElement( "id" );
							Element postUserNameNode = doc.createElement( "name" );
							Element postUserImageNode = doc.createElement( "image" );
							
							postUserNode.appendChild( postUserIdNode );
							postUserNode.appendChild( postUserNameNode );
							postUserNode.appendChild( postUserImageNode );
							
							postNode.appendChild( postIdNode );
							postNode.appendChild( postMessageNode );
							postNode.appendChild( postDateNode );
							postNode.appendChild( postUserNode );
							postsNode.appendChild( postNode );
							
							postIdNode.appendChild( responseBuilder.createTextNode( String.valueOf( post.getId() ) ) );
							postMessageNode.appendChild( responseBuilder.createTextNode( post.getMessage() ) );
							postDateNode.appendChild( responseBuilder.createTextNode( dateUtil.formatToDateAndTime( post.getDate(), lang ) ) );
							
							postUserIdNode.appendChild( responseBuilder.createTextNode( String.valueOf( post.getUserId() ) ) );
							postUserNameNode.appendChild( responseBuilder.createTextNode( post.getUserFirstname() + " " + post.getUserLastname() ) );
							postUserImageNode.appendChild( responseBuilder.createTextNode( post.getUserImage() ) ); 							
						}
						PostBean lastPost = posts.get( posts.size() - 1 ); 
						postsNode.setAttribute( "controldate", String.valueOf( lastPost.getDate().getTime() ) );
					}				
				} else {
					responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.ACCESS_RESTRICT_INFO );
					responseBuilder.processLN();
					if( topic.getVisibility().equals( VisibilityBean.OWNER_ONLY_VISIBILITY ) ) {
						responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.ACCESS_RESTRICT_OWNER_ERROR );
					} else if( topic.getVisibility().equals( VisibilityBean.CONTACTS_ONLY_VISIBILITY ) ) {
						responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.ACCESS_RESTRICT_CONTACT_ERROR );
					} else if( topic.getVisibility().equals( VisibilityBean.USERS_ONLY_VISIBILITY ) ) {
						responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.ACCESS_RESTRICT_USER_ERROR );
					} else {
						throw new OperationException( "Falha no controle de pesmiss�es por t�picos do sistema." );
					}
				}
			}
		} catch (PersistenceException e) {
			throw new OperationException( e );
		}
	}
	

	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		
	}

}
