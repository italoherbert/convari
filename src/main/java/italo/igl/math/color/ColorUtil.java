package italo.igl.math.color;

import java.awt.Color;

public class ColorUtil {

	public final static Color MEDIAN_COLOR = new Color( 122, 122, 122 );
	
	public Color randomColor() {
		return this.randomColor( Color.BLACK, Color.WHITE );
	}
	
	public Color randomColor( Color color1, Color color2 ) {		
		
		int minR = Math.min( (int)color1.getRed(), (int)color2.getRed() );
		int minG = Math.min( (int)color1.getGreen(), (int)color2.getGreen() );
		int minB = Math.min( (int)color1.getBlue(), (int)color2.getBlue() );

		int maxR = Math.max( (int)color1.getRed(), (int)color2.getRed() );
		int maxG = Math.max( (int)color1.getGreen(), (int)color2.getGreen() );
		int maxB = Math.max( (int)color1.getBlue(), (int)color2.getBlue() );
		
		int r = (int) Math.round( minR + Math.random() * (maxR-minR) );
		int g = (int) Math.round( minG + Math.random() * (maxG-minG) );
		int b = (int) Math.round( minB + Math.random() * (maxB-minB) );
		return new Color( r, g, b );
	}
	
}
