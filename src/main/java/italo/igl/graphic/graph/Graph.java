package italo.igl.graphic.graph;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public interface Graph {
	
	public BufferedImage getImage();
	
	public Graphics getGraphics();
	
	public boolean isBoundExceeded( int x, int y );
	
	public int getWidth();
	
	public int getHeight();
	
	public void setRGBAlls();
	
	public void setRGBAlls(Color color);
		
	public boolean setRGB(int x, int y);
	
	public boolean setRGB(int x, int y, int rgb);
	
	public boolean setRGB(int x, int y, Color color);
		
	public Color getColor(int x, int y);
	
	public Color getCurrentColor();
	
	public void setCurrentColor(Color color);	
	
}
