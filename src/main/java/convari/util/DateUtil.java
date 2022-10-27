package convari.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import convari.messages.KeyMessageConstants;
import convari.messages.MessageManager;
import convari.messages.MessageManagerException;

public class DateUtil {
			
	private MessageManager messageManager;
	
	public DateUtil( MessageManager messageManager ) {
		this.messageManager = messageManager;
	}
	
	public String formatToLongDate( Timestamp date, String lang ) {
		if( date != null ) {
			String locale = messageManager.getLocaleForLang( lang );
			DateFormat formatter = DateFormat.getDateInstance( DateFormat.LONG, 
					( locale != null ? new Locale( locale ) : Locale.getDefault() ) );
			return formatter.format( date );
		} else {
			try {
				if( lang != null )
					return messageManager.getMessage( lang, KeyMessageConstants.DATA_UNKNOWN );
			} catch (MessageManagerException e) {
				
			}
			return "desconhecida";
		}						
	}
	
	public String formatToDateAndTime( Timestamp date, String lang ) {
		if( date != null ) {
			DateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss");
			return formatter.format( date );
		} else {
			try {
				if( lang != null )
					return messageManager.getMessage( lang, KeyMessageConstants.DATA_UNKNOWN );
			} catch (MessageManagerException e) {
				
			}
			return "desconhecida";
		}		
	}
	
	public String formatToDate( Timestamp date, String lang ) {
		if( date != null ) {
			DateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy");
			return formatter.format( date );
		} else {
			try {
				if( lang != null )
					return messageManager.getMessage( lang, KeyMessageConstants.DATA_UNKNOWN );
			} catch (MessageManagerException e) {
				
			}
			return "desconhecida";
		}		
	}
	
	public Timestamp parseDate( String date ) {
		if( date.matches( "\\d{2}/\\d{2}/\\d{4}" ) ) {
			String[] d = date.split( "/" );
			String formattedDate = d[2]+"-"+d[1]+"-"+d[0]+" 00:00:00";
			return Timestamp.valueOf( formattedDate );
		}
		return null;
	}
	
	public long getTime( Timestamp date ) {
		if( date != null )
			return date.getTime();
		return 0;
	}	
			
}
