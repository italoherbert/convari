package italo.graphic.resize;

import italo.igl.graphic.graph.Graph;
import italo.igl.graphic.graph.GraphImpl;

import java.awt.image.BufferedImage;

public abstract class AbstractResizeAlgorithm implements ResizeAlgorithm {
	
	public abstract void resize( Graph graph1, Graph graph2, int w, int h, int w2, int h2, float f1, float f2 );
	
	public BufferedImage resizeWidth( BufferedImage image, int newWidth ) {
		int w = image.getWidth();
		int h = image.getHeight();
		
		float f1 = ((float)newWidth) / ((float)w);
		float f2 = ( (float)w ) / ((float)newWidth);
				
		int w2 = Math.round( f1 * w );
		int h2 = Math.round( f1 * h );
		
		if( h2 > 2*w2 )
			h2 = 2*w2;		
		
		BufferedImage image2 = new BufferedImage( w2, h2, image.getType() );
		Graph originalGraph = new GraphImpl( image );
		Graph resizedGraph = new GraphImpl( image2 );
				
		this.resize( originalGraph, resizedGraph, w, h, w2, h2, f1, f2 );		
		return image2;
	}
		
}
