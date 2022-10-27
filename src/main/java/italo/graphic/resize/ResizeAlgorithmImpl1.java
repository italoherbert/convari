package italo.graphic.resize;

import italo.igl.graphic.graph.Graph;

import java.awt.Color;

public class ResizeAlgorithmImpl1 extends AbstractResizeAlgorithm {

	public void resize( Graph graph1, Graph graph2, int w, int h, int w2, int h2, float f1, float f2 ) {
		for( int i = 0; i < h2; i++ ) {
			for( int j = 0; j < w2; j++ ) {
				int x = Math.round( j * f2 );
				int y = Math.round( i * f2 );
				Color color = graph1.getColor( x, y );											
				graph2.setRGB( j, i, color ); 
			}
		}	
	}

}
