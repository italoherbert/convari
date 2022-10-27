package italo.igl.math.m2d;

import italo.igl.math.CGMath;

public class Polar2DMath {

	private CGMath math;
	
	public Polar2DMath(CGMath math) {
		super();
		this.math = math;
	}

	public double radial( double x1, double y1, double x2, double y2 ) {
		return math.getGeometry2D().distance( x1, x2, y1, y2 );
	}
		
	public double angular( double x1, double y1, double x2, double y2 ) {
		return -Math.atan2( y2-y1, x2-x1 );
	}
	
	public double angular0x360( double x1, double y1, double x2, double y2 ) {
		double angular = angular(x1, y1, x2, y2);
		if(angular < 0)
			angular = 2*Math.PI + angular;
		return angular;
	}
	
	public double calculateX2( double x1, double radial, double angular ) {
		double angle0x360 = math.getTrigonometry().angle0x360(angular); 
		double x = math.getTrigonometry().adjacentSide(radial, angle0x360);		
		if( angle0x360 < Math.PI*0.5f) {
			return x1 + x;
		} else if( angle0x360 < Math.PI) {
			return x1 - x;
		} else if( angle0x360 < Math.PI + Math.PI*0.5f) {
			return x1 - x;
		} else {
			return x1 + x;
		}
	}
	
	public double calculateY2( double y1, double radial, double angular ) {		
		double angle0x360 = math.getTrigonometry().angle0x360(angular); 
		double y = math.getTrigonometry().oppositeSide(radial, angle0x360);		
		if( angle0x360 < Math.PI*0.5f) {
			return y1 - y;
		} else if( angle0x360 < Math.PI) {
			return y1 - y;
		} else if( angle0x360 < Math.PI + Math.PI*0.5f) {
			return y1 + y;
		} else {
			return y1 + y;
		}
	}
	
}
