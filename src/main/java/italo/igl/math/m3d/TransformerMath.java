package italo.igl.math.m3d;

public class TransformerMath {

	public double[] scale(double[] vector, double[] scalars) {
		double[] vscalar = vector.clone();
		for(int i = 0; i < vscalar.length; i++)
			vscalar[i] *= scalars[i];
		return vscalar;
	}	
					
	public double[] rotate( double[] vector, double[] angulars ) {		
		double x = vector[0];
		double y = vector[1];
		double z = vector[2];
		
		double xAng = angulars[0];
		double yAng = angulars[1];
		double zAng = angulars[2];
			
		double rx = x, ry = y, rz = z;
		
		ry = y * Math.cos( xAng ) - z * Math.sin( xAng );
		rz = y * Math.sin( xAng ) + z * Math.cos( xAng );
		
		x = rx;
		y = ry;
		z = rz;
		
		rx = x * Math.cos( yAng ) + z * Math.sin( yAng );
		rz = x * -Math.sin( yAng ) + z * Math.cos( yAng );
		
		x = rx;
		y = ry;
		z = rz;
		
		rx = x * Math.cos( zAng ) - y * Math.sin( zAng );
		ry = x * Math.sin( zAng ) + y * Math.cos( zAng );								
		
		return new double[]{rx, ry, rz};
	}
	
	public double[] perspective( double[] vector, double d ) {
		double x = vector[0];
		double y = vector[1];
		double z = vector[2];
		
		d = -1/d;
		
		x = x / (d*z + 1);
		y = y / (d*z + 1);
		z = 0;
		
		return new double[]{ x, y, z };
	}
	
}
