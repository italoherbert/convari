package italo.igl.graphic.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class GraphImpl implements Graph {

	private Color currentColor = Color.WHITE;
	private ImageGraph imageGraph;
	
	public GraphImpl( final BufferedImage image ) {
		this( new ImageGraph() {

			public BufferedImage getImage() {
				return image;
			}
			
		} );
	}
		
	public GraphImpl(ImageGraph imageGraph) {
		super();
		this.imageGraph = imageGraph;		
	}
		
	public boolean isBoundExceeded( int x, int y ) {
		int w = imageGraph.getImage().getWidth();
		int h = imageGraph.getImage().getHeight();		
		return ( x < 0 || x >= w || y < 0 || y >= h );
	}
	
	public void setRGBAlls() {
		this.setRGBAlls( currentColor );
	}
	
	public void setRGBAlls(Color color) {
		BufferedImage image = imageGraph.getImage();
		int w = image.getWidth();
		int h = image.getHeight();
		for(int i = 0; i < w; i++)
			for(int j = 0; j < h; j++)
				image.setRGB( i, j, color.getRGB() );
	}
	
	public Color getCurrentColor() {
		return currentColor;
	}

	public void setCurrentColor(Color color) {
		this.currentColor = color;
	}
	
	public BufferedImage getImage() {
		return imageGraph.getImage();
	}

	public Graphics getGraphics() {
		return imageGraph.getImage().getGraphics();
	}
	
	public int getWidth() {
		return imageGraph.getImage().getWidth();
	}
	
	public int getHeight() {
		return imageGraph.getImage().getHeight();
	}

	public Color getColor( int x, int y ) {
		int rgb = imageGraph.getImage().getRGB( x, y );
		return this.getColor( rgb ); 		
	}
	
	public Color getColor( int rgb ) {
		int red   = (rgb & 0x00FF0000) >> 16;
		int green = (rgb & 0x0000FF00) >> 8;
		int blue  = (rgb & 0x000000FF);
		
		return new Color( red, green, blue );
	}
		
	public boolean setRGB(int x, int y) {
		return this.setRGB(x, y, currentColor); 
	}
	
	public boolean setRGB(int x, int y, Color color) {
		return this.setRGB(x, y, color.getRGB() );
	}
	
	public boolean setRGB(int x, int y, int rgb) {
		if( !isBoundExceeded( x, y ) ) {
			imageGraph.getImage().setRGB( x, y, rgb );
			return true;
		}
		return false;
	}		
		
}
