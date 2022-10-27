package convari.messages;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MessageManagerImpl implements MessageManager {

	private final String FILE_PATH = "messages/config.xml";
		
	private Map<String, Map<String, String>> propertiesMap = new HashMap<String, Map<String, String>>();
	private Map<String, Map<String, String>> textsMap = new HashMap<String, Map<String, String>>();
	private Map<String, Map<String, String>> htmlMap = new HashMap<String, Map<String, String>>();
	
	private List<String> supportedLanguages = new ArrayList<String>();
	private Map<String, String> languageLocaleMap = new HashMap<String, String>();
	private List<Node> messageFileNodes = new ArrayList<Node>();
	
	private String defaultLang = null;
	private String basedir = "";
	private String htmlbasedir = null;
		
	public void processConfigFile() throws MessageManagerException {   		
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse( MessageManagerImpl.class.getResourceAsStream( FILE_PATH ) );		

			Node root = doc.getDocumentElement();
			NodeList list = root.getChildNodes();	
			for( int i = 0; i < list.getLength(); i++ ) {
				Node node = list.item( i );
				if( node.getNodeType() == Node.ELEMENT_NODE ) {
					String nodeName = node.getNodeName(); 
					NamedNodeMap attrs = node.getAttributes();
					if( nodeName.equals( "langs" ) ) {
						this.defaultLang = attrs.getNamedItem( "default" ).getNodeValue();
						
						NodeList languageNodes = node.getChildNodes();
						for( int j = 0; j < languageNodes.getLength(); j++ ) {
							Node languageNode = languageNodes.item( j );
							if( languageNode.getNodeType() == Node.ELEMENT_NODE ) {							
								if( languageNode.getNodeName().equals( "lang" ) ) {
									String language = languageNode.getAttributes().getNamedItem( "name" ).getNodeValue();
									this.propertiesMap.put( language, new HashMap<String, String>() );
									this.htmlMap.put( language, new HashMap<String, String>() );
									this.textsMap.put( language, new HashMap<String, String>() );									
									this.supportedLanguages.add( language );
									
									Node localeAttrNode = languageNode.getAttributes().getNamedItem( "locale" );
									if( localeAttrNode != null )
										languageLocaleMap.put( language, localeAttrNode.getNodeValue() );
								}
							}
						}
					} else if( nodeName.equals( "messageFiles" ) ) {
						this.messageFileNodes.add( node );
					}
				}
			}						
			
			for( String lang:this.supportedLanguages )
				for( Node node:this.messageFileNodes )
					this.processMessageFiles( node, lang );				
		} catch (ParserConfigurationException e) {
			throw new MessageManagerException( e );
		} catch (SAXException e) {
			throw new MessageManagerException( e );
		} catch (IOException e) {
			throw new MessageManagerException( e );
		}			
	}
	
	public void processMessageFiles( Node node, String lang ) throws MessageManagerException {
		NamedNodeMap attrs = node.getAttributes();
			
		Node basedirAttrNode = attrs.getNamedItem( "basedir" );
		if( basedirAttrNode != null )
			this.basedir = basedirAttrNode.getNodeValue();
		Node htmlbasedirAttrNode = attrs.getNamedItem( "htmlbasedir" );
		if( htmlbasedirAttrNode != null )
			this.htmlbasedir = htmlbasedirAttrNode.getNodeValue();
		String basedir = this.basedir.replace( "{lang}", lang );		
		
		NodeList nodes = node.getChildNodes();
		for( int i = 0; i < nodes.getLength(); i++ ) {
			Node item = nodes.item( i );
			if( item.getNodeType() == Node.ELEMENT_NODE ) {
				String nodeName = item.getNodeName(); 

				if( nodeName.equals( "messageFile" ) )
					this.processMessageFileNode( item, basedir, lang );
			}
		}
	}
	
	public void processMessageFileNode( Node node, String basedir, String lang ) throws MessageManagerException {
		NamedNodeMap attrs = node.getAttributes();
		String src = basedir + attrs.getNamedItem( "path" ).getNodeValue();
		src = src.replace( "{lang}", lang );
		
		String type = attrs.getNamedItem( "type" ).getNodeValue();
		if( type.equals( "properties" ) ) {
			this.processPropertiesMessageFile( this.propertiesMap, src, lang );
		} else if( type.equals( "htmlmap" ) ) { 
			this.processPropertiesMessageFile( this.htmlMap, src, lang );
		} else if( type.equals( "text" ) ) { 
			this.processTextMessageFile( src, lang );
		} else {
			throw new MessageManagerException( "Tipo de arquivo inválido, TYPE="+type+"; SRC="+src );
		}
	}
	
	public void processPropertiesMessageFile( Map<String, Map<String, String>> map, String src, String lang ) throws MessageManagerException {
		Properties p = new Properties();
		try {
			p.load( MessageManagerImpl.class.getResourceAsStream( src ) );
			Map<String, String> properties = map.get( lang );
			if( properties == null )
				throw new MessageManagerException( "Mapa de propriedades por linguagem não carregado, LANG="+lang );
			for( Object pKey:p.keySet() ) {
				String key = String.valueOf( pKey );
				String value = p.getProperty(key);
				properties.put( key, String.valueOf( value ) );
			}
		} catch ( IOException e ) {
			throw new MessageManagerException( e );
		}
	}
		
	public void processTextMessageFile( String src, String lang ) throws MessageManagerException {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse( MessageManagerImpl.class.getResourceAsStream( src ) );		
			
			Node root = doc.getDocumentElement();
			NodeList list = root.getChildNodes();	
			for( int i = 0; i < list.getLength(); i++ ) {
				Node node = list.item( i );
				if( node.getNodeType() == Node.ELEMENT_NODE ) {
					String nodeName = node.getNodeName(); 
					NamedNodeMap attrs = node.getAttributes();
					if( nodeName.equals( "text" ) ) {
						String key = attrs.getNamedItem( "key" ).getNodeValue();						
						String value = node.getNodeValue();
						Map<String, String> texts = this.textsMap.get( lang );
						if( texts == null )
							throw new MessageManagerException( "Mapa de texto por linguagem não carregado, LANG="+lang );
						texts.put( key, value );
					}
				}
			}											
		} catch (ParserConfigurationException e) {
			throw new MessageManagerException( e );
		} catch (SAXException e) {
			throw new MessageManagerException( e );
		} catch (IOException e) {
			throw new MessageManagerException( e );
		}	
	}
	
	public String getMessage( String lang, String key, String... params ) throws MessageManagerException {
		String value = this.getMessageMapValue( lang, key );
		if( value == null )
			value = this.getMessageMapValue( this.defaultLang, key );
		if( value == null )
			throw new MessageManagerException( "Mensagem não encontrada. LANG="+lang+"; KEY="+key );
		int i = 1;
		for( String param:params )
			value = value.replace( "{"+(i++)+"}", param );
		return value;
	}		
	
	public String getHTMLText( String lang, String key, String... params ) throws MessageManagerException {
		String value = null;
		if( lang != null ) {
			Map<String, String> map = this.htmlMap.get( lang );
			if( map == null )
				map = this.htmlMap.get( this.defaultLang );
			if( map != null )
				value = map.get( key );
		}
		
		if( value == null )
			throw new MessageManagerException( "Caminho de Arquivo HTML não encontrado. LANG="+lang+"; KEY="+key );

		value = ( this.htmlbasedir != null ? this.htmlbasedir : this.basedir ).replace( "{lang}", lang ) + value;		
		
		String html = "";
		try {
			BufferedReader input = new BufferedReader( 
					new InputStreamReader( MessageManagerImpl.class.getResourceAsStream( value ) ) );
		
			String line = input.readLine();
			while( line != null ) {
				html += line + "\n";
				line = input.readLine();
			}
			input.close();
		} catch ( FileNotFoundException e ) {
			throw new MessageManagerException( "Arquivo HTML não encontrado. LANG="+lang+"; KEY="+key, e);
		} catch ( IOException e ) {
			throw new MessageManagerException( "Falha na leitura de arquivo HTML. LANG="+lang+"; KEY="+key, e );
		}
		
		int i = 1;
		for( String param:params )
			html = html.replace( "{"+(i++)+"}", param );
		return html;
	}
	
	public String getLocaleForLang( String lang ) {
		String locale = this.languageLocaleMap.get( lang );
		if( locale == null )
			locale = this.languageLocaleMap.get( this.defaultLang );
		return locale;
	}
	
	private String getMessageMapValue( String lang, String key ) { 
		if( lang == null )
			return null;
		Map<String, String> map = this.propertiesMap.get( lang );
		if( map == null )
			map = this.textsMap.get( lang );					
		return ( map != null ? map.get( key ) : null );
	}

	public List<String> getSupportedLanguages() {
		return supportedLanguages;
	}

	public String getDefaultLang() {
		return defaultLang;
	}
				
}
