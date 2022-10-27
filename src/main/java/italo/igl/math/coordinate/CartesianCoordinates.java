package italo.igl.math.coordinate;


public class CartesianCoordinates extends AbstractCoordinates {

	protected double x;
	protected double y;
	protected double z;
	
	public CartesianCoordinates() {
		this( 0, 0, 0 );
	}
	
	public CartesianCoordinates( double x, double y, double z ) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double[] vector() {
		return new double[] { x, y, z };
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
}
