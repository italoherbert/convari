package convari.controller.operation.user;

import italo.validate.RequestValidatorParam;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import convari.controller.operation.Operation;
import convari.controller.operation.OperationController;
import convari.controller.operation.OperationException;
import convari.controller.operation.OperationParameters;
import convari.controller.operation.ValidationOpException;
import convari.persistence.Persistence;
import convari.persistence.PersistenceException;
import convari.response.KeyResponseMessageConstants;
import convari.response.ResponseBuilder;
import convari.upload.ImageUploadProcess;
import convari.upload.ImageUploader;
import convari.upload.ImageUploaderCBException;
import convari.upload.ImageUploaderCallback;
import convari.upload.ImageUploaderVO;
import convari.weblogic.SessionUserBean;



public class UserImageUploadOperation implements Operation {
	
	public void initialize( OperationController controller, List<RequestValidatorParam> vparams ) throws OperationException {		
		
	}
	
	public void execute( OperationController controller, OperationParameters parameters ) throws OperationException {
		Persistence persistence = parameters.getPersistence();
		HttpServletRequest request = parameters.getRequest();
		ServletContext context = parameters.getServlet().getServletContext();		
		ImageUploader imageUploader = parameters.getImageUploader();
				
		SessionUserBean suser = parameters.getWebLogic().getSessionUser( request );
		int userId = suser.getUID();
		ImageUploadProcess uploadProcess = suser.getImageUploadProcess(); 				
		
		boolean isMultpart = ServletFileUpload.isMultipartContent( request );
		if( isMultpart ) {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold( 524288 );
			
			ServletFileUpload upload = new ServletFileUpload( factory );
			upload.setSizeMax( 524288 );	
						
			try {
				List<?> itens = upload.parseRequest( request );
				for( Object obj: itens ) {
					FileItem item = (FileItem)obj;
					if( !item.isFormField() ) {
						String contentType = item.getContentType();
						if( imageUploader.isSupportedType( contentType ) ) {
							ImageUploaderVO vo = new ImageUploaderVO();
							vo.setServletContext( context );
							vo.setInputStream( item.getInputStream() );
							vo.setSize( item.getSize() );
							vo.setContentType( contentType ); 
							vo.setUserId( userId );
							vo.setCallback( new ImageUploaderCallbackImpl( persistence, uploadProcess, userId ) );
							imageUploader.process( vo );
						} else {
							uploadProcess.addErrorMessageKey( KeyResponseMessageConstants.SAVE_USER_IMAGE_FORMAT_ERROR, imageUploader.outputFormatType( contentType ) ); 
							uploadProcess.addInfoMessageKey( KeyResponseMessageConstants.SAVE_USER_IMAGE_FORMAT_INFO );
							uploadProcess.setStatus( ImageUploadProcess.Status.FAILED );
						}
					}
				}
			} catch (FileUploadException e) {
				uploadProcess.addErrorMessageKey( KeyResponseMessageConstants.SAVE_USER_IMAGE_LENGTH_ERROR );
				uploadProcess.addInfoMessageKey( KeyResponseMessageConstants.SAVE_USER_IMAGE_LENGTH_INFO );
				uploadProcess.setStatus( ImageUploadProcess.Status.FAILED );
			} catch (IOException e) {
				uploadProcess.setStatus( ImageUploadProcess.Status.FAILED );
				throw new OperationException( e );
			} catch (ImageUploaderCBException e) {
				uploadProcess.setStatus( ImageUploadProcess.Status.FAILED );
				throw new OperationException( e );
			}
		} else {
			uploadProcess.addErrorMessageKey( KeyResponseMessageConstants.SAVE_USER_IMAGE_CONTENT_TYPE_ERROR );
			uploadProcess.addInfoMessageKey( KeyResponseMessageConstants.SAVE_USER_IMAGE_CONTENT_TYPE_INFO );
			uploadProcess.setStatus( ImageUploadProcess.Status.FAILED );
		}
	}

	public void validate( OperationController controller, OperationParameters parameters ) throws ValidationOpException {
		ResponseBuilder responseBuilder = parameters.getResponseBuilder();
		
		SessionUserBean sessionUser = parameters.getWebLogic().getSessionUser( parameters.getRequest() );
		if( sessionUser == null )
			responseBuilder.processTextErrorMSG( "Autenticação necessária." );		
	}

	private class ImageUploaderCallbackImpl implements ImageUploaderCallback {

		private ImageUploadProcess uploadProcess;
		private Persistence persistence;
		private int userId;		
				
		public ImageUploaderCallbackImpl(Persistence persistence, ImageUploadProcess uploadProcess, int userId) {
			super();
			this.persistence = persistence;
			this.uploadProcess = uploadProcess;
			this.userId = userId;
		}

		public void started() {
			uploadProcess.setStatus( ImageUploadProcess.Status.PROCESSING );
		}

		public void progress( float percent ) {
			uploadProcess.setProgress( Math.round( percent ) ); 
		}

		public void finishing() {
			uploadProcess.setStatus( ImageUploadProcess.Status.FINISHING );
		}

		public void finished( String imagePath ) throws ImageUploaderCBException {
			try {
				persistence.getUserManager().setImagepath( userId, imagePath );
				uploadProcess.setStatus( ImageUploadProcess.Status.FINISHED );
			} catch (PersistenceException e) {
				throw new ImageUploaderCBException( e );
			}					
		}

		public void canceled() {
			uploadProcess.setStatus( ImageUploadProcess.Status.CANCELED );
		}

		public boolean cancelCondition() {
			return uploadProcess.getStatus() == ImageUploadProcess.Status.CANCELING || 
					uploadProcess.getStatus() == ImageUploadProcess.Status.FAILED;
		}
		
	}
	
}
