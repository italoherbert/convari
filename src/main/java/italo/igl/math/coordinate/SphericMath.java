package italo.igl.math.coordinate;

public class SphericMath {

	public CartesianCoordinates createCartesianCoordinates( double x, double y, double z ) {
		return new CartesianCoordinates( x, y, z );
	}
	
	public SphericCoordinates createSphericCoordinates( double hAngle, double vAngle, double radial ) {
		return new SphericCoordinates( hAngle, vAngle, radial );
	}
	
	public CartesianCoordinates toCartesianCoordinates( SphericCoordinates coords ) {
		return toCartesianCoordinates( 
			coords.getHorizontalAngle(), 
			coords.getVerticalAngle(), 
			coords.getRadial() 
		);
	}
	
	public SphericCoordinates toSphericCoordinates( CartesianCoordinates coords ) {
		return toSphericCoordinates( 
			coords.getX(), 
			coords.getY(), 
			coords.getZ() 
		);
	}
		
	public SphericCoordinates toSphericCoordinates( double x, double y, double z ) {				
		double radial = Math.sqrt( Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2) );				
		double radial2 = Math.sqrt( Math.pow(x, 2) + Math.pow(z, 2) );
		double phi, theta;	
				 
		if(z == 0) {
			phi = Math.PI*.5d;
			if(x == 0) {
				theta = Math.PI*.5d;
			} else {
				theta = Math.atan( y / radial2 );						
			}
		} else {
			phi = Math.atan( x / z );
			theta = Math.atan( y / radial2 );
		}	
								
		return this.createSphericCoordinates( phi, theta, radial );
	}
	
	public CartesianCoordinates toCartesianCoordinates( double phi, double theta, double r ) {
		return this.createCartesianCoordinates(
			Math.sin( phi ) * Math.cos( theta ) * r,
			Math.sin( theta ) * r,
			Math.cos( phi ) * Math.cos( theta ) * r
		);
	}
	
}
