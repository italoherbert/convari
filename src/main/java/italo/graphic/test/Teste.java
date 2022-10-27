package italo.graphic.test;

import italo.graphic.GraphicUtil;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Teste {

	public static void main(String[] args) throws IOException {
		//showImage( "Penguins.jpg" );
		//showImage( "Desert.gif" );
		showImage( "eu.jpg" );		
	}
	
	public static void showImage( String name ) throws IOException {
		BufferedImage image = ImageIO.read( Teste.class.getResourceAsStream( name ) );				
		BufferedImage imageResized = ImageIO.read( Teste.class.getResourceAsStream( name.replace( ".", "150.") ) ); 				
		
		GraphicUtil graphicUtil = new GraphicUtil();		
		showImages( image, 
			imageResized, 
			graphicUtil.resizeWidth3( image, 150, 1 ), 
			graphicUtil.resizeWidth2( image, 150, 1 ), 
			graphicUtil.resizeWidth3( image, 150, 2 ), 
			graphicUtil.resizeWidth3( image, 150, 3 ),
			graphicUtil.resizeWidth3( image, 150, 0 )
		);		
	}	
	
	public static void showImages( BufferedImage originalImage, BufferedImage... copies ) {		
		int xgrid = (int) Math.round( Math.sqrt( copies.length ) ) + 1;
		int ygrid = (int) Math.round( Math.sqrt( copies.length ) );
		
		GraphicPanel leftPanel = new GraphicPanel();
		leftPanel.setImage( originalImage );
		leftPanel.repaint();
		
		JPanel rightPanel = new JPanel( new GridLayout( ygrid, xgrid, 5, 5 ) );				
		for( int i = 0; i < copies.length; i++ ) {
			GraphicPanel gpanel = new GraphicPanel();
			gpanel.setImage( copies[i] );
			gpanel.repaint();
			rightPanel.add( gpanel );
		}
		
		JPanel contentPanel = new JPanel();				
		contentPanel.add( "West", leftPanel );
		contentPanel.add( "Center", rightPanel );		
		
		JFrame f = new JFrame( "Redimensionamento" );
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		f.setContentPane( contentPanel );							
		f.pack();
		f.setLocationRelativeTo( f );				
		f.setVisible( true );
	}
	
	public static void showImage( BufferedImage image, String title ) {
		GraphicPanel gpanel = new GraphicPanel();
		
		JFrame f = new JFrame( title );
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		f.setContentPane( gpanel );
		
		gpanel.setImage( image );
		gpanel.repaint();
		f.pack();
		f.setLocationRelativeTo( f );				
		f.setVisible( true );
	}
	
}

