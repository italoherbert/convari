package italo.igl.math.m3d;

import italo.igl.math.CGMath;

public class ProjectionMath {

	private CGMath math;
	
	public ProjectionMath(CGMath math) {
		super();
		this.math = math;
	}

	public double[] representIn2D( double[] point3d, double[] angulars ) {
		return this.representIn2D( 0, 0, point3d, angulars, true );
	}		
	
	public double[] representIn2D( double xB, double yB, double[] point3d, double[] angulars) {
		return this.representIn2D( xB, yB, point3d, angulars, true );
	}
	
	public double[] representIn2D( double[] point3d, double[] angulars, boolean zCalculate ) {
		return this.representIn2D( 0, 0, point3d, angulars, zCalculate );
	}
	
	public double[] representIn2D( double xB, double yB, double[] point3d, double[] angulars, boolean zCalculate) {
		double x = xB;
		double y = yB;
		for(int i = 0; i < (zCalculate ? 3 : 2); i++) {
			x = math.getPolar().calculateX2( x, point3d[i], angulars[i] );
			y = math.getPolar().calculateY2( y, point3d[i], angulars[i] ); 			
		}
		return new double[]{ x, y };			
	}
	
}
