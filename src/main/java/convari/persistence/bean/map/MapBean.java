package convari.persistence.bean.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import convari.persistence.bean.Bean;


public abstract class MapBean<T extends Bean> {
	
	public final static String NO_MAP = "no-map";
	
	protected Map<Object, List<T>> beans = new HashMap<Object, List<T>>();
	protected String mapType = NO_MAP;
			
	public MapBean(String mapType) {
		super();
		this.mapType = mapType;
	}
	
	protected abstract Object getKey( T bean );
	
	public void put( T bean ) {
		Object key;
		if ( mapType == NO_MAP )
			key = "all";	
		else {
			Object k = this.getKey( bean );
			key = ( k != null ? k : "all" );
		}
		
		if( !beans.containsKey( key ) )
			beans.put( key, new ArrayList<T>() );				
		beans.get( key ).add( bean );
	}
	
	public List<T> listAll() {
		if( mapType == NO_MAP ) {
			return beans.get( "all" );
		} else {
			List<T> list = new ArrayList<T>();
			for( List<T> l:beans.values())
				list.addAll( l ); 
			return list;
		}
	}
	
	public List<T> list( Object... keys ) {
		List<T> list = new ArrayList<T>();
		for( Object key: keys ) {
			List<T> l = beans.get( key );
			if( l != null )
				list.addAll( l );
		}
		return list;
	}
	
	public Map<Object, List<T>> getMap() {
		return beans;
	}

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
	}

}

