package italo.igl.math.coordinate;


public class SphericCoordinates extends AbstractCoordinates {

	protected double horizontalAngle;
	protected double verticalAngle;
	protected double radial;
	
	public SphericCoordinates(SphericMath sphericMath ) {
		this( 0, 0, 0 );
	}
	
	public SphericCoordinates( double horizontalAngle, double verticalAngle, double radial) {
		super();
		this.horizontalAngle = horizontalAngle;
		this.verticalAngle = verticalAngle;
		this.radial = radial;
	}

	public double[] vector() {
		return new double[] { horizontalAngle, verticalAngle, radial };
	}
	
	public double getHorizontalAngle() {
		return horizontalAngle;
	}

	public void setHorizontalAngle(double horizontalAngle) {
		this.horizontalAngle = horizontalAngle;
	}

	public double getVerticalAngle() {
		return verticalAngle;
	}

	public void setVerticalAngle(double verticalAngle) {
		this.verticalAngle = verticalAngle;
	}

	public double getRadial() {
		return radial;
	}

	public void setRadial(double radial) {
		this.radial = radial;
	}	
	
}
