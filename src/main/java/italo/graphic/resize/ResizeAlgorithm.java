package italo.graphic.resize;

import java.awt.image.BufferedImage;

public interface ResizeAlgorithm {

	public BufferedImage resizeWidth( BufferedImage image, int newWidth );
	
}
