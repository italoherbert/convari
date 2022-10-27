package italo.captcha;

import italo.igl.graphic.graph.Graph;
import italo.igl.graphic.graph.GraphImpl;
import italo.igl.math.color.ColorUtil;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class CaptchaGenerator {

	private ColorUtil colorUtil = new ColorUtil();
	private int captchaLength = 4;
	
	public String genRandomCaptcha( String validRandomCharacters ) {
		String randomCaptcha = "";
		for( int i = 0; i < captchaLength; i++ ) {
			int n = (int) Math.round( Math.random() * ( validRandomCharacters.length()-1) );
			randomCaptcha += validRandomCharacters.charAt( n );
		}
		return randomCaptcha;
	}
	
	public BufferedImage genCaptchaImage( String captcha ) {
		BufferedImage image = new BufferedImage( 120, 40, BufferedImage.TYPE_INT_RGB );
		Graph graph = new GraphImpl( image );
		
		Graphics2D g = (Graphics2D)image.getGraphics();
		g.setColor( Color.BLACK );
		g.fillRect( 0, 0, image.getWidth(), image.getHeight() );
				
		Font font = new Font( "serif", Font.BOLD+Font.ITALIC, 30 );
		FontRenderContext frc = g.getFontRenderContext();
		FontMetrics metrics = g.getFontMetrics( font );						
		g.setFont( font );
		
		int lineQuant = 6;
		int xL1 = (int) Math.round( Math.random() * (image.getWidth()-1) );
		int yL1 = (int) Math.round( Math.random() * (image.getHeight()-1) );
		int quant = (image.getWidth()+image.getHeight());
		int factor = (int)(((float)quant) / ((float)lineQuant));
		for( int i = 0; i < quant; i++ ) {
			int x = (int) Math.round( Math.random() * (image.getWidth()-1) );
			int y = (int) Math.round( Math.random() * (image.getHeight()-1) );
			g.setColor( colorUtil.randomColor( ColorUtil.MEDIAN_COLOR, Color.WHITE ) );
			graph.setRGB( x, y, g.getColor() );
			if( i % factor == 0 ) {
				Line2D.Float line = new Line2D.Float( xL1, yL1, x, y );
				g.setStroke( new BasicStroke( 1.0f ) );
				g.draw( line );
				xL1 = x;
				yL1 = y;
			}
		}
		
		for( int i = 0; i < captcha.length(); i++ ) {
			String ch = String.valueOf( captcha.charAt( i ) );
						
			int x = 12 + ( metrics.stringWidth( ch ) + 10 ) * i;
			int y = (int) Math.round( ( (image.getHeight()-metrics.getHeight() )*.5f) + metrics.getHeight()*.75f );
			
			GlyphVector vect = font.createGlyphVector( frc, ch );
			AffineTransform at = AffineTransform.getTranslateInstance( x, y );
			at.rotate( Math.toRadians( -30 + Math.random() * 60 ) );
			
			Shape glyph = vect.getGlyphOutline( 0 );
			Shape transformedShape = at.createTransformedShape( glyph );
			g.setColor( colorUtil.randomColor( ColorUtil.MEDIAN_COLOR, Color.WHITE ) );
			g.fill( transformedShape );
		}
						
		return image;
	}
	
}
