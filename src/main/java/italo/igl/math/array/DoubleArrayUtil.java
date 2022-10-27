package italo.igl.math.array;

public class DoubleArrayUtil {

	public void invertSinal(double[] v1, int n) {
		for(int i = 0; i < n; i++)
			v1[i] *= -1;
	}
	
	public void sum(double[] v1, double[] v2, int n) {
		for(int i = 0; i < n; i++)
			v1[i] += v2[i];
	}
	
	public void sub(double[] v1, double[] v2, int n) {
		for(int i = 0; i < n; i++)
			v1[i] -= v2[i];
	}
	
	public void mul(double[] v1, double[] v2, int n) {
		for(int i = 0; i < n; i++)
			v1[i] *= v2[i];
	}
	
	public void sum(double[] v1, double scalar, int n) {
		for(int i = 0; i < n; i++)
			v1[i] += scalar;
	}
	
	public void sub(double[] v1, double scalar, int n) {
		for(int i = 0; i < n; i++)
			v1[i] -= scalar;
	}
	
	public void mul(double[] v1, double scalar, int n) {
		for(int i = 0; i < n; i++)
			v1[i] *= scalar;
	}
	
	public void div(double[] v1, double[] v2, int n) {
		for(int i = 0; i < n; i++)
			v1[i] /= v2[i]; 
	}
	
	public double absGeometricMean(double[] vector) {
		double mean = 0;
		for(int i = 0; i < vector.length; i++)
			mean += Math.pow( vector[i], 2 );
		return Math.sqrt( mean );
	}
	
	public double aritmeticMean(double[] vector) {
		if(vector.length > 0)
			return this.somatorio( vector ) / vector.length;
		return 0;
	}
	
	public double somatorio( double[] vector ) {
		double soma = 0;
		for(int i = 0; i < vector.length; i++)
			soma += vector[i];
		return soma;
	}
	
}
