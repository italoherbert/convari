package convari.response;

import javax.servlet.http.HttpServletRequest;

public interface ResponseMessageListener {

	public void message( HttpServletRequest request, ResponseMessage message );
		
}
