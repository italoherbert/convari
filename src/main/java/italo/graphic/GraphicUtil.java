package italo.graphic;

import italo.graphic.resize.ResizeAlgorithmImpl1;
import italo.graphic.resize.ResizeAlgorithmImpl2;
import italo.graphic.resize.ResizeAlgorithmImpl3;

import java.awt.image.BufferedImage;

public class GraphicUtil {

	private ResizeAlgorithmImpl1 algorithm1 = new ResizeAlgorithmImpl1();
	private ResizeAlgorithmImpl2 algorithm2 = new ResizeAlgorithmImpl2();
	private ResizeAlgorithmImpl3 algorithm3 = new ResizeAlgorithmImpl3();
	
	public BufferedImage resizeWidth1( BufferedImage image, int newWidth ) {		
		return algorithm1.resizeWidth( image, newWidth );
	}
	
	public BufferedImage resizeWidth2( BufferedImage image, int newWidth, int radial ) {
		algorithm2.setRadial( radial );
		return algorithm2.resizeWidth( image, newWidth );
	}
	
	public BufferedImage resizeWidth3( BufferedImage image, int newWidth, int radial ) {
		algorithm3.setRadial( radial );
		return algorithm3.resizeWidth( image, newWidth );
	}
	
}
