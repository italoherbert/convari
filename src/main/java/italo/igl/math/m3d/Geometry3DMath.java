package italo.igl.math.m3d;

public class Geometry3DMath {

	public double distance(double[] p1, double[] p2){
		double sum = 0;
		for(int j = 0; j < p1.length; j++)
			sum += Math.pow(Math.abs(p1[j]-p2[j]), 2);
		return Math.sqrt(sum);
	}
	
}
