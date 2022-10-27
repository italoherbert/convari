package convari.response;

public interface KeyResponseMessageConstants {

	public final static String SYSTEM_ERROR = "error.system";
	public final static String OPERATION_ERROR = "error.operation";
	public final static String ACCESS_RESTRICT_OWNER_ERROR = "error.access.restrict.owner";
	public final static String ACCESS_RESTRICT_CONTACT_ERROR = "error.access.restrict.contact";
	public final static String ACCESS_RESTRICT_USER_ERROR = "error.access.restrict.user";
	public final static String ACCESS_RESTRICT_INSUFICIENT_ERROR = "error.access.restrict.insuficient";
			
	public final static String AUTH_NEED_ERROR = "error.auth.need";
		
	public final static String NOTUNIQUE_USERNAME_ERROR = "error.notunique.username";
			
	public final static String NULL_REQUEST_PARAM_ERROR = "error.request.param.null";
	public final static String REQUIRED_REQUEST_PARAM_ERROR = "error.request.param.required";
	public final static String NOTNUMBER_REQUEST_PARAM_ERROR = "error.request.param.notnumber";
	public final static String INTERVAL_EXCEED_REQUEST_PARAM_ERROR = "error.request.param.interval.exceed";
	public final static String UNKNOWN_REQUEST_PARAM_ERROR = "error.request.param.unknown";
	public final static String LENGTH_REQUEST_PARAM_ERROR = "error.request.param.length";
	public final static String MAIL_REQUEST_PARAM_ERROR = "error.request.param.mail";
	public final static String PASSWORD_REQUEST_PARAM_ERROR = "error.request.param.password";
	public final static String DATE_REQUEST_PARAM_ERROR = "error.request.param.date";
	
	public final static String USER_NOT_FOUND_ERROR = "error.notfound.user";
	public final static String USER_NOT_FOUND_BY_ID_ERROR = "error.notfound.user.id";
	public final static String USER_NOT_FOUND_BY_USERNAME_ERROR = "error.notfound.user.username";
	
	public final static String TOPIC_NOT_FOUND_ERROR = "error.notfound.topic";
	public final static String TOPIC_NOT_FOUND_BY_ID_ERROR = "error.notfound.topic.id";
	
	public final static String PASSWORD_REDEFINITION_SEND_LINK_ERROR = "error.password.redefinition.send.link";
	public final static String PASSWORD_REDEFINITION_EXPIRED_ERROR = "error.password.redefinition.expired";
	public final static String PASSWORD_REDEFINITION_INVALID_CODE_ERROR = "error.password.redefinition.code.invalid";

	public final static String INVALID_LOGIN_ERROR= "error.login.invalid";
	public final static String INVALID_VERIFICATION_CODE_ERROR= "error.verification.code.invalid";

	public final static String SAVE_USER_IMAGE_FORMAT_ERROR = "error.save.user.image.format";
	public final static String SAVE_USER_IMAGE_LENGTH_ERROR = "error.save.user.image.length";
	public final static String SAVE_USER_IMAGE_CONTENT_TYPE_ERROR = "error.save.user.image.content.type";
	public final static String SAVE_USER_IMAGE_FAIL_ERROR = "error.save.user.image.fail";

	public final static String SAVE_INDISPONIBLE_USERNAME = "error.save.indisponible.username";
	

	public final static String SYSTEM_INFO = "info.system";
	public final static String ACCESS_RESTRICT_INFO = "info.access.restrict";
	
	public final static String INVALID_VERIFICATION_CODE_INFO = "info.verification.code.invalid";
	public final static String PASSWORD_REDEFINITION_SEND_LINK_INFO = "info.password.redefinition.send.link";
	public final static String PASSWORD_REDEFINITION_EXPIRED_INFO = "info.password.redefinition.expired";
	public final static String PASSWORD_REDEFINITION_SUCCESS_INFO = "info.password.redefinition.success";	
	
	public final static String SAVE_COMMENT_INFO = "info.save.comment";
	public final static String SAVE_POST_INFO = "info.save.post";
	public final static String SAVE_TOPIC_INFO = "info.save.topic";
	public final static String SAVE_USER_INFO = "info.save.user";
	public final static String SAVE_USER_IMAGE_FORMAT_INFO = "info.save.user.image.format";
	public final static String SAVE_USER_IMAGE_LENGTH_INFO = "info.save.user.image.length";
	public final static String SAVE_USER_IMAGE_CONTENT_TYPE_INFO = "info.save.user.image.content.type";
	public final static String SAVE_USER_IMAGE_FINISHED_INFO = "info.save.user.image.finished";
	public final static String SAVE_USER_IMAGE_CANCELED_INFO = "info.save.user.image.canceled";
	
	public final static String SET_STATUS_INFO = "info.set.status";
	public final static String SET_PERSONAL_DATA_INFO = "info.set.user.personalinfo";
	public final static String SET_PROFESSIONAL_DATA_INFO = "info.set.user.professionalinfo";
	public final static String SET_CONFIG_OPS_INFO = "info.set.config.ops";
	public final static String SET_CONFIG_DATAS_INFO = "info.set.config.datas";
	
	public final static String REMOVE_NOTIFICATION_INFO = "info.remove.notification";
				
	public final static String EMPTY_LIST_FIND_INFO = "info.empty.list.find";
	public final static String EMPTY_LIST_CONTACT_INFO = "info.empty.list.contact";
	public final static String EMPTY_LIST_NOTIFICATION_INFO = "info.empty.list.notification";
	public final static String EMPTY_LIST_POST_INFO = "info.empty.list.post";
	public final static String EMPTY_LIST_POST_BY_TOPIC_INFO = "info.empty.list.post.by.topic";
	public final static String EMPTY_LIST_INVITE_INFO = "info.empty.list.invite";
	
}
