package italo.igl.math.color;

import java.awt.Color;

public class RGB {

	private int r = 0;
	private int g = 0;
	private int b = 0;
	
	public RGB() {}
	
	public RGB(int r, int g, int b) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Color color() {
		return new Color( r, g, b );
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}	
	
}
