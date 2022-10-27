package convari.response;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;



public class ResponseBuilder {
	
	private Document doc;
	private Element responseNode;
	private Element headNode;
	private Element bodyNode;
	
	private Attr statusAttr;
	private Attr debugAttr;
	private Element messagesNode;
	private Element systemNode;
	
	private Transformer transformer;
	
	private int errorMessagesCount = 0;
	private int infoMessagesCount = 0;
	
	private String contentType;
	private PrintWriter writer;
	private HttpServletRequest request;
	private ResponseMessageListener messageListener;
	
	public ResponseBuilder(HttpServletRequest request) {
		this( request, null, null );
	}
	
	public ResponseBuilder( HttpServletRequest request, String contentType ) {
		this( request, contentType, null );
	}
	
	public ResponseBuilder( HttpServletRequest request, PrintWriter writer ) {
		this( request, null, writer );
	}
	
	public ResponseBuilder( HttpServletRequest request, String contentType, PrintWriter writer ) {
		super();
		this.request = request;
		this.contentType = contentType;
		this.writer = writer;
	}
	
	public void initializeXMLResponse() throws ResponseBuilderException {
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty( OutputKeys.METHOD, "xml" );
			transformer.setOutputProperty( OutputKeys.ENCODING, "UTF-8" );
			
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = builder.newDocument();
			
			responseNode = doc.createElement( "response" );
			statusAttr = doc.createAttribute( "status" );
			debugAttr = doc.createAttribute( "debug" );
			
			headNode = doc.createElement( "head" );			
			bodyNode = doc.createElement( "body" );
			messagesNode = doc.createElement( "messages" );
			
			systemNode = doc.createElement( "system" );			
		
			headNode.appendChild( systemNode );
			headNode.appendChild( messagesNode );			

			responseNode.setAttributeNode( debugAttr );
			responseNode.setAttributeNode( statusAttr );
			responseNode.appendChild( headNode );
			responseNode.appendChild( bodyNode );
			
			doc.appendChild( responseNode );
			
			debugAttr.setNodeValue( "false" );
		} catch( ParserConfigurationException e ) {
			throw new ResponseBuilderException( e );
		} catch (TransformerConfigurationException e) {
			throw new ResponseBuilderException( e );
		} catch (TransformerFactoryConfigurationError e) {
			throw new ResponseBuilderException( e );
		}
	}
	
	public String build() throws ResponseBuilderException { 
		DOMSource source = new DOMSource( doc );
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult( writer );
		
		try {
			transformer.transform( source, result );
		} catch (TransformerException e) {
			throw new ResponseBuilderException( e );
		}
		
		return writer.toString();
	}
	
	public void setSystemStatus( int status ) {
		systemNode.setAttribute( "status", String.valueOf( status ) ); 
	}
		
	public Text createTextNode( String text ) {
		return doc.createTextNode( text == null  ? "" : text );
	}
		
	public void processLN() {
		if( contentType != null && writer != null )
			if( contentType.equals( "html" ) )
				writer.println( "<br />" );								
	}
	
	public void processTextInfoMSG( String text, String... params ) {
		this.processInfoMSG( null, text, params );
	}
	
	public void processTextErrorMSG( String text, String... params ) {
		this.processErrorMSG( null, text, params );
	}
	
	public void processKeyInfoMSG( String key, String... params ) {
		this.processInfoMSG( key, null, params );
	}		
	
	public void processKeyErrorMSG( String key, String... params ) {
		this.processErrorMSG( key, null, params );
	}
	
	public void processInfoMSG( String key, String text, String... params ) {
		if( contentType != null ) {
			if( contentType.equals( "xml" ) ) { 
				addMessageNode( key, text, ResponseMessage.INFO_TYPE, params );
			} else {
				if( writer != null ) {
					if( contentType.equals( "html" ) ) {			
						writer.println( "<span style='color:blue'>"+text+"</span>" );
					} else {
						writer.println( key );
					}
				}
			}
		} else {
			if( writer != null )
				writer.println( key );
		}
		infoMessagesCount++;
	}		
	
	public void processErrorMSG( String key, String text, String... params ) {
		if( contentType != null ) {
			if( contentType.equals( "xml" ) ) { 
				addMessageNode( key, text, ResponseMessage.ERROR_TYPE, params );				
			} else {
				if( writer != null ) {
					if( contentType.equals( "html" ) ) {			
						writer.println( "<span style='color:red'>"+text+"</span>" );
					} else {
						writer.println( text );
					}
				}
			}
		} else {
			if( writer != null )
				writer.println( text );
		}		
		errorMessagesCount++;		
	}
	
	private void addMessageNode( String key, String msg, String type, String... params ) {
		Element messageNode = doc.createElement( "message" );		
		messageNode.setAttribute( "type", type );
		if( key != null ) {
			messageNode.setAttribute( "key", key );
			int index = 1;
			for( String param: params ) {
				Element paramNode = doc.createElement( "param" );
				paramNode.setAttribute( "index", String.valueOf( index ) );
				messageNode.appendChild( paramNode );
				
				paramNode.appendChild( this.createTextNode( param ) );
				index++;
			}
		} else {
			messageNode.appendChild( this.createTextNode( msg ) );
		}
		messagesNode.appendChild( messageNode );
		if( messageListener != null )
			messageListener.message( request, new ResponseMessage( type, msg, key, params ) );
	}		
	
	public void setMessageListener(ResponseMessageListener messageListener) {
		this.messageListener = messageListener;
	}

	public Document getDoc() {
		return doc;
	}

	public Element getBodyNode() {
		return bodyNode;
	}

	public void setBodyNode(Element bodyNode) {
		this.bodyNode = bodyNode;
	}

	public Element getResponseNode() {
		return responseNode;
	}

	public Element getSystemNode() {
		return systemNode;
	}

	public Element getHeadNode() {
		return headNode;
	}

	public Attr getDebugAttr() {
		return debugAttr;
	}

	public Attr getStatusAttr() {
		return statusAttr;
	}

	public Element getMessagesNode() {
		return messagesNode;
	}

	public int getErrorMessagesCount() {
		return errorMessagesCount;
	}

	public int getInfoMessagesCount() {
		return infoMessagesCount;
	}

}
