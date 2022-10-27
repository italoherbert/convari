package convari.controller.operation;

import italo.validate.RequestValidatorParam;
import italo.validate.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import convari.controller.operation.captcha.GenCaptchaOperation;
import convari.controller.operation.comment.AddCommentOperation;
import convari.controller.operation.configuration.GetConfigOperation;
import convari.controller.operation.configuration.SaveDataConfigOperation;
import convari.controller.operation.contact.GetContactsOperation;
import convari.controller.operation.contact.GetInvitationNewsOperation;
import convari.controller.operation.contact.GetInvitationStatusOperation;
import convari.controller.operation.contact.GetPendingInvitationsOperation;
import convari.controller.operation.contact.ManageInvitationOperation;
import convari.controller.operation.login.LoginOperation;
import convari.controller.operation.login.LoginRecoveryOperation;
import convari.controller.operation.login.LogoutOperation;
import convari.controller.operation.login.PasswordRedefinitionOperation;
import convari.controller.operation.login.TestOperation;
import convari.controller.operation.menu.GetUserCountsOperation;
import convari.controller.operation.navigation.GetNavigationUserInfoOperation;
import convari.controller.operation.notification.GetNotificationsOperation;
import convari.controller.operation.notification.RemoveNotificationOperation;
import convari.controller.operation.permission.GetPermissionsOperation;
import convari.controller.operation.post.AddPostOperation;
import convari.controller.operation.post.AddTopicOperation;
import convari.controller.operation.post.FindTopicsOperation;
import convari.controller.operation.post.GetPostsByTopicOperation;
import convari.controller.operation.post.GetTopicOperation;
import convari.controller.operation.status.AlterStatusOperation;
import convari.controller.operation.status.GetStatusOperation;
import convari.controller.operation.user.FindUsersOperation;
import convari.controller.operation.user.GetBasicProfileDataOperation;
import convari.controller.operation.user.GetProfileDataOperation;
import convari.controller.operation.user.RegisterOperation;
import convari.controller.operation.user.SaveContactDataOperation;
import convari.controller.operation.user.SaveGeneralDataOperation;
import convari.controller.operation.user.SaveProfessionalDataOperation;
import convari.controller.operation.user.UserImageUploadOperation;
import convari.controller.operation.user.UserImageUploadProgressOperation;
import convari.persistence.Persistence;
import convari.persistence.PersistenceException;
import convari.persistence.bean.InvitationBean;
import convari.persistence.bean.OperationBean;
import convari.persistence.bean.VisibilityBean;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.response.SysCodeConstants;
import convari.weblogic.SessionUserBean;
import convari.weblogic.WebLogic;



public class OperationController {
	
	private Map<String, Operation> operations = new HashMap<String, Operation>();
	private ValidationManager validatorManager = new ValidationManager(); 
	private Map<String, List<RequestValidatorParam>> validatorParameters = new HashMap<String, List<RequestValidatorParam>>(); 
	
	public OperationController() {
		operations.put( "login", new LoginOperation() );
		operations.put( "logout", new LogoutOperation() );
		operations.put( "test", new TestOperation() );
		operations.put( "register", new RegisterOperation() );
		operations.put( "get-permissions", new GetPermissionsOperation() );
		operations.put( "get-basic-user-data", new GetBasicProfileDataOperation() );
		operations.put( "get-user-counts", new GetUserCountsOperation() );
		operations.put( "get-navigation-user-info", new GetNavigationUserInfoOperation() );
		operations.put( "get-status", new GetStatusOperation() );
		operations.put( "alter-status", new AlterStatusOperation() );
		operations.put( "login-recovery", new LoginRecoveryOperation() );
		operations.put( "password-redefinition", new PasswordRedefinitionOperation() );
		operations.put( "gen-captcha", new GenCaptchaOperation() );
		
		operations.put( "find-users", new FindUsersOperation() );
		operations.put( "get-profile-data", new GetProfileDataOperation() );
		operations.put( "save-general-data", new SaveGeneralDataOperation() );
		operations.put( "save-contact-data", new SaveContactDataOperation() );
		operations.put( "save-professional-data", new SaveProfessionalDataOperation() );
		operations.put( "user-image-upload", new UserImageUploadOperation() );
		operations.put( "user-image-upload-progress", new UserImageUploadProgressOperation() );
		
		operations.put( "find-topics", new FindTopicsOperation() );
		operations.put( "get-topic", new GetTopicOperation() );
		operations.put( "add-topic", new AddTopicOperation() );
		operations.put( "add-post", new AddPostOperation() );		
		operations.put( "get-posts", new GetPostsByTopicOperation() );
		
		operations.put( "get-notifications", new GetNotificationsOperation() );
		operations.put( "remove-notification", new RemoveNotificationOperation() );
		
		operations.put( "get-invitation-news", new GetInvitationNewsOperation() );
		operations.put( "get-invitations", new GetPendingInvitationsOperation() );
		operations.put( "get-invitation-status", new GetInvitationStatusOperation() );
		operations.put( "manage-invitation", new ManageInvitationOperation() );
		
		operations.put( "get-config", new GetConfigOperation() );
		operations.put( "save-data-user-config", new SaveDataConfigOperation() );
		
		operations.put( "get-contacts", new GetContactsOperation() );
		operations.put( "add-comment", new AddCommentOperation() );
	}
	
	public void initialize() throws OperationException {
		for( String key:operations.keySet() ) {
			List<RequestValidatorParam> vparams = new ArrayList<RequestValidatorParam>();
			Operation op = operations.get( key );
			op.initialize( this, vparams );
			validatorParameters.put( key, vparams );						
		}
	}
	
	public boolean execute( String key, OperationParameters parameters ) throws OperationException, ValidationOpException {		
		Operation operation = operations.get( key );
		if( operation == null )
			throw new SystemOperationException( "Operação não encontrada. chave="+key );
		if( parameters.getPersistence() == null )			
			throw new SystemOperationException( "Modulo de persistencia necessario para autorizar a execução. chave="+key );

		HttpServletRequest request = parameters.getRequest();
		WebLogic webLogic = parameters.getWebLogic();
		Persistence persistence = parameters.getPersistence();
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
						
		try {
			boolean authorized = false;
			List<OperationBean> publicOperations = persistence.getOperationManager().listByVisibility( VisibilityBean.PUBLIC_VISIBILITY ); 
			for( int i = 0; i < publicOperations.size() && !authorized; i++ ) {
				OperationBean r = publicOperations.get( i );
				authorized = ( r.getName().equals( key ) );
			}

			if( !authorized ) {
				SessionUserBean sessionUser = webLogic.getSessionUser( request );
				if( sessionUser == null ) {
					responseBuilder.setSystemStatus( SysCodeConstants.AUTH_ERROR );
					responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.AUTH_NEED_ERROR );
					throw new SessionOperationException();
				} else {								
					int userId = sessionUser.getUID();
					List<OperationBean> list = sessionUser.getOperations();
					OperationBean opBean = null;
					for( int i = 0; i < list.size() && !authorized; i++ ) {
						OperationBean op = list.get( i );
						if( op.getName().equals( key ) ) {
							opBean = op;
							authorized = true;
						}
					}
					
					if( authorized ) {
						if( userId != parameters.getUID() ) {							
							String visibility = persistence.getOperationManager().getConstraintOperationVisibility( parameters.getUID(), key );
							if( visibility == null ) 
								visibility = opBean.getVisibility();
							if( visibility.equals( VisibilityBean.OWNER_ONLY_VISIBILITY ) ) {
								responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.ACCESS_RESTRICT_OWNER_ERROR ); 
								authorized = false;
							} else if( visibility.equals( VisibilityBean.CONTACTS_ONLY_VISIBILITY ) ) {
								String status = persistence.getContactManager().status( parameters.getUID(), userId );
								if( status != null )
									authorized = ( status.equals( InvitationBean.ACCEPTED_INVITATION ) );								

								if( !authorized )
									responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.ACCESS_RESTRICT_CONTACT_ERROR ); 										
							}						
						}
					} else {
						responseBuilder.processKeyErrorMSG( KeyResponseMessageConstants.ACCESS_RESTRICT_INSUFICIENT_ERROR ); 
					}
				}
			}
			
			if( authorized )
				return validateAndExecute( key, operation, parameters );			
		} catch (PersistenceException e) {
			throw new SystemOperationException( e );			
		}		
		return false;
	}
	
	private boolean validateAndExecute( String key, Operation operation, OperationParameters parameters ) throws OperationException {
		int errorsCount = parameters.getResponseBuilder().getErrorMessagesCount();		
		
		for( RequestValidatorParam param:validatorParameters.get( key ) ) {
			try {
				validatorManager.validate( parameters, param );
			} catch (ValidationException e) {
				throw new ValidationOpException( e );
			}
		}
		
		operation.validate( this, parameters );			
		
		if( errorsCount == parameters.getResponseBuilder().getErrorMessagesCount() ) {		
			operation.execute( this, parameters );
			return true;
		}
		return false;
	}
	
	public boolean authorize( OperationParameters parameters, int uid, String visibility ) throws PersistenceException {
		Persistence persistence = parameters.getPersistence();
		boolean isOwner = this.isOwner( parameters, uid );
		if( isOwner )
			return true;

		if( visibility.equals( VisibilityBean.OWNER_ONLY_VISIBILITY ) ) {		
			return false;			
		} else {
			if( visibility.equals( VisibilityBean.CONTACTS_ONLY_VISIBILITY ) ) {
				SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
				if( sessionUser != null ) {
					InvitationBean contact = persistence.getContactManager().find( sessionUser.getUID(), uid );
					if( contact != null )
						return contact.getStatus().equals( InvitationBean.ACCEPTED_INVITATION );
				}
			} else if( visibility.equals( VisibilityBean.USERS_ONLY_VISIBILITY ) ) {
				return persistence.getUserManager().exists( uid );
			} else if( visibility.equals( VisibilityBean.PUBLIC_VISIBILITY ) ){ 
				return true;
			}
		}
		return false;
	}
	
	public boolean isOwner( OperationParameters parameters, int uid ) {
		if( uid > -1 ) {
			boolean logged = parameters.getWebLogic().isUserLogged( parameters.getRequest() );
			if( logged) {
				SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
				if( sessionUser != null )
					return( sessionUser.getUID() == uid );
			}
		}
		return false;
	}
	
	
	public String getVisibility( OperationParameters parameters, int uid ) throws PersistenceException {
		SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
		if( sessionUser != null ) {
			int userId = sessionUser.getUID();
			if( userId == uid ) {
				return VisibilityBean.OWNER_ONLY_VISIBILITY;
			} else {
				Persistence persistence = parameters.getPersistence(); 				
				InvitationBean contact = persistence.getContactManager().find( userId, uid );
				if( contact != null )
					if( contact.getStatus().equals( InvitationBean.ACCEPTED_INVITATION ) )
						return VisibilityBean.CONTACTS_ONLY_VISIBILITY;
			}
			return VisibilityBean.USERS_ONLY_VISIBILITY;
		}
		return VisibilityBean.PUBLIC_VISIBILITY;
	}
	
	public String getIndexForVisibility( String visibility ) {
		if( visibility != null ) {
			if( visibility.equals( VisibilityBean.OWNER_ONLY_VISIBILITY ) ) {
				return "0";
			} else if( visibility.equals( VisibilityBean.CONTACTS_ONLY_VISIBILITY ) ) {
				return "1";
			} else if( visibility.equals( VisibilityBean.USERS_ONLY_VISIBILITY ) ) {
				return "2";
			} else if( visibility.equals( VisibilityBean.PUBLIC_VISIBILITY ) ) {
				return "3";
			}
		}
		return null;
	}	
	
	public int getWeightForVisibility( String visibility ) {
		if( visibility != null ) {
			if( visibility.equals( VisibilityBean.OWNER_ONLY_VISIBILITY ) ) {
				return 4;
			} else if( visibility.equals( VisibilityBean.CONTACTS_ONLY_VISIBILITY ) ) {
				return 3;
			} else if( visibility.equals( VisibilityBean.USERS_ONLY_VISIBILITY ) ) {
				return 2;
			} else if( visibility.equals( VisibilityBean.PUBLIC_VISIBILITY ) ) {
				return 1;
			}
		}
		return 1;
	}
	
	public String getVisibilityForIndex( String vid ) {
		if( vid != null ) {
			if( vid.equals( "0" ) ) {
				return VisibilityBean.OWNER_ONLY_VISIBILITY;
			} else if( vid.equals( "1" ) ) {
				return VisibilityBean.CONTACTS_ONLY_VISIBILITY;
			} else if( vid.equals( "2" ) ) {
				return VisibilityBean.USERS_ONLY_VISIBILITY;
			} else if( vid.equals( "3" ) ) {
				return VisibilityBean.PUBLIC_VISIBILITY;
			}
		}
		return null;
	}
	
	public String getInvitationStatusForIndex( String sid ) {
		if( sid != null ) {
			if( sid.equals( "0" ) ) {
				return InvitationBean.PENDING_INVITATION;
			} else if( sid.equals( "1" ) ) {
				return InvitationBean.ACCEPTED_INVITATION;
			} else if( sid.equals( "2" ) ) {
				return InvitationBean.REMOVED_INVITATION;
			} else if( sid.equals( "3" ) ) {
				return InvitationBean.CANCELED_INVITATION;
			} else if( sid.equals( "4" ) ) {
				return InvitationBean.REFUSED_INVITATION;
			}
		}
		return null;
	}
	
	public String getIndexForInvitationStatus( String status ) {
		if( status != null ) {
			if( status.equals( InvitationBean.PENDING_INVITATION ) ) {
				return "0";
			} else if( status.equals( InvitationBean.ACCEPTED_INVITATION ) ) {
				return "1";
			} else if( status.equals( InvitationBean.REMOVED_INVITATION ) ) {
				return "2";
			} else if( status.equals( InvitationBean.CANCELED_INVITATION ) ) {
				return "3";
			} else if( status.equals( InvitationBean.REFUSED_INVITATION ) ) {
				return "4";
			}
		}
		return null;
	}

	public ValidationManager getValidationManager() {
		return validatorManager;
	}	
	
}
