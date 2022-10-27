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
import convari.persistence.bean.VisibilityBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.util.DateUtil;



public class GetPostsByTopicOperation implements Operation {

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
		HttpServletRequest request = parameters.getRequest();
		Persistence persistence = parameters.getPersistence();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		Document doc = responseBuilder.getDoc();
		DateUtil dateUtil = parameters.getDateUtil();
				
		String uidP = request.getParameter( "uid" );		
		
		String paramId = request.getParameter( "tid" );
		int tid = Integer.parseInt( paramId );
		Timestamp controlDate = null;
		String controlDateP = request.getParameter( "controldate" );
		if( controlDateP != null )
			controlDate = new Timestamp( Long.parseLong( controlDateP ) );
		
		String lang = request.getParameter( "lang" ); 
		
		try {						
			List<PostBean> posts = persistence.getPostManager().getPostsByTopic( tid, controlDate );
			
			Element postsNode = doc.createElement( "posts" );
			responseBuilder.getBodyNode().appendChild( postsNode );
				
			if( posts.isEmpty() ) {				
				if( controlDate == null ) {
					postsNode.setAttribute( "controldate", "-1" );
					responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.EMPTY_LIST_POST_BY_TOPIC_INFO );					
				} else {
					postsNode.setAttribute( "controldate", String.valueOf( controlDate.getTime() ) );
				}								
			} else {
				String userVisibility = VisibilityBean.PUBLIC_VISIBILITY;
				if( uidP != null ) {
					int uid = Integer.parseInt( uidP );
					userVisibility = controller.getVisibility( parameters, uid );					
				}								
				String topicVisibility = persistence.getPostManager().getVisibilityForTopic( tid );
				
				int uVisibilityWeight = controller.getWeightForVisibility( userVisibility );
				int tVisibilityWeight = controller.getWeightForVisibility( topicVisibility );
				
				if( uVisibilityWeight >= tVisibilityWeight ) {				
					int count = 1;
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
						
						if( count == posts.size() ) {
							postsNode.setAttribute( "controldate", String.valueOf( post.getDate().getTime() ) );
						} else count++;					
					}
				} else {
					responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.ACCESS_RESTRICT_INFO );
					responseBuilder.processLN();
					if( topicVisibility.equals( VisibilityBean.OWNER_ONLY_VISIBILITY ) ) {
						responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.ACCESS_RESTRICT_OWNER_ERROR );
					} else if( topicVisibility.equals( VisibilityBean.CONTACTS_ONLY_VISIBILITY ) ) {
						responseBuilder.processKeyInfoMSG( KeyResponseMessageConstants.ACCESS_RESTRICT_CONTACT_ERROR );
					} else if( topicVisibility.equals( VisibilityBean.USERS_ONLY_VISIBILITY ) ) {
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
