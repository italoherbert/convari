package italo.graphic.test;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GraphicPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private BufferedImage image;
		
	public void paintComponent( Graphics g ) {
		super.paintComponent( g );
		if( image != null )	{
			int x = (super.getWidth()-image.getWidth()) / 2;
			int y = (super.getHeight()-image.getHeight()) / 2;
			g.drawImage( image, x, y, image.getWidth(), image.getHeight(), this );
		}
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		super.setPreferredSize( new Dimension( image.getWidth(), image.getHeight() ) );		
	}
	
}
