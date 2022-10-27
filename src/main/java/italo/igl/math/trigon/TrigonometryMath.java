package italo.igl.math.trigon;



public class TrigonometryMath {
						
	public double adjacentSide( double radial, double angular ) {
		double firstQAngle = firstQuadrantAngle( angular );
		return Math.cos( firstQAngle ) * radial;
	}
	
	public double oppositeSide( double radial, double angular ) {
		double firstQAngle = firstQuadrantAngle( angular );
		return Math.sin( firstQAngle ) * radial;
	}
	
	public double angle0x360( double angle ) {
		double ang = Math.abs( angle ) % (2*Math.PI);	
		if(angle < 0)
			return 2*Math.PI - ang;
		return ang;
	}
	
	public int quadrant( double angle ) {
		double angle0x360 = angle0x360( angle );
		return (int)( angle0x360 / (Math.PI*0.5f) ) + 1;
	}
	
	public double firstQuadrantAngle( double angle ) {
		double angle0x360 = angle0x360( angle );
		int quadrant = quadrant( angle0x360 );		
		if( quadrant % 2 == 0 )
			return ( quadrant * (Math.PI*0.5f) ) - angle0x360;					
		return  angle0x360 - ( (quadrant-1) * (Math.PI*0.5f) );
	}
	
}
