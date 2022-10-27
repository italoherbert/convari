package italo.igl.math.vector3d;

public class DoubleVector3D {

	private double x = 0.0d;
	private double y = 0.0d;
	private double z = 0.0d;
	
	public DoubleVector3D(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double[] vector() {
		return new double[] {x, y, z};
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
