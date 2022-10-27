package italo.graphic.resize;

import italo.igl.graphic.graph.Graph;

import java.awt.Color;

public class ResizeAlgorithmImpl3 extends AbstractResizeAlgorithm {

	private int radial = 2;
	
	public ResizeAlgorithmImpl3() {}
	
	public ResizeAlgorithmImpl3(int radial) {
		this.radial = radial;
	}

	public void resize( Graph graph1, Graph graph2, int w, int h, int w2, int h2, float f1, float f2 ) {		
		for( int i = 0; i < h2; i++ ) {
			for( int j = 0; j < w2; j++ ) {
				int r = 0, g = 0, b = 0;
				int count = 0;
				for( int k = -radial; k <= radial; k++ ) {
					int x = Math.round( j * f2 ) + 0;
					int y = Math.round( i * f2 ) + k;
					if( x >= 0 && x < w && y >= 0 && y < h ) {
						Color color = graph1.getColor( x, y );
						r += color.getRed();
						g += color.getGreen();
						b += color.getBlue();															
						count++;
					}						 
				}	
				if( count > 0 ) {
					r /= count;
					g /= count;
					b /= count;
				} else {
					r = g = b = 0;
				}
				
				graph2.setRGB( j, i, new Color( r, g, b ) ); 
			}
		}			
	}

	public int getRadial() {
		return radial;
	}

	public void setRadial(int radial) {
		this.radial = radial;
	}

}
