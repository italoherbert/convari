package italo.igl.math.m2d;

import italo.igl.math.CGMath;

public class Geometry2DMath {

	private CGMath math;
	
	public Geometry2DMath(CGMath math) {
		super();
		this.math = math;
	}

	public double distance( double x1, double y1, double x2, double y2 ){
		return math.getGeometry3D().distance( new double[]{x1, y1}, new double[]{x2, y2} );
	}
	
	public boolean isLeftPoint( double x, double y,  double x1, double y1, double x2, double y2 ) {
		double ang1 = math.getPolar().angular0x360(x1, y1, x2, y2);
		double ang2 = math.getPolar().angular0x360(x1, y1, x, y);		
		if(ang1 < Math.PI) {
			ang2 -= ang1;
		} else {			
			ang2 += 2*Math.PI-ang1;
		}
		ang1 = 0;
		ang2 = math.getTrigonometry().angle0x360( ang2 );		
		return !(ang1 >= Math.PI && ang2 < Math.PI || ang1 < Math.PI && ang2 >= Math.PI);
	}
	
	public boolean crossSegments( double x1, double y1, double x2, double y2,
			double x3, double y3, double x4, double y4) {
		boolean isLeftP1 = isLeftPoint(x1, y1, x3, y3, x4, y4);
		boolean isLeftP2 = isLeftPoint(x2, y2, x3, y3, x4, y4);		
		boolean isLeftP3 = isLeftPoint(x3, y3, x1, y1, x2, y2);
		boolean isLeftP4 = isLeftPoint(x4, y4, x1, y1, x2, y2);						
		return ( (isLeftP1 != isLeftP2) && (isLeftP3 != isLeftP4) );
	}
		
}
