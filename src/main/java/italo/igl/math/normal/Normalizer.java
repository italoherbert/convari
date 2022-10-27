package italo.igl.math.normal;


public class Normalizer {
			
	public double value( double normal, double min, double max, double maxValue ) {
		return ( ( normal - min ) * maxValue ) / (max-min) ;
	}
	
	public double[] values( double[] normals, double min, double max, double maxValue ) {
		double[] newVector = new double[ normals.length ];				
		for(int i = 0; i < newVector.length; i++)
			newVector[i] = value( normals[i], min, max, maxValue ); 		
		return newVector;
	}
	
	public double normalize( double value, double min, double max, double maxValue ) {
		return min + ( ( value / maxValue ) * (max-min) ); 
	}
	
	public double[] normalize( double[] values, double min, double max, double maxValue ) {
		double[] newVector = new double[ values.length ];				
		for(int i = 0; i < newVector.length; i++)
			newVector[i] = normalize( values[i], min, max, maxValue); 		
		return newVector;		
	}		
		
}
