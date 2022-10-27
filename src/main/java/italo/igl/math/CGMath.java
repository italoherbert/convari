package italo.igl.math;

import italo.igl.math.array.DoubleArrayUtil;
import italo.igl.math.color.ColorUtil;
import italo.igl.math.coordinate.SphericMath;
import italo.igl.math.m2d.Geometry2DMath;
import italo.igl.math.m2d.Polar2DMath;
import italo.igl.math.m3d.Geometry3DMath;
import italo.igl.math.m3d.ProjectionMath;
import italo.igl.math.m3d.TransformerMath;
import italo.igl.math.normal.Normalizer;
import italo.igl.math.trigon.TrigonometryMath;
import italo.igl.math.util.MathUtil;

public interface CGMath {	
	
	public final static double GOLDEN_RATIO = (1.0d + Math.sqrt( 5 ) )/ 2;
	
	public MathUtil getMathUtil();
	
	public Normalizer getNormalizer();

	public TrigonometryMath getTrigonometry();

	public Geometry3DMath getGeometry3D();

	public Geometry2DMath getGeometry2D();
	
	public Polar2DMath getPolar();
	
	public ProjectionMath getProjection();
	
	public TransformerMath getTransformer();
	
	public DoubleArrayUtil getDoubleArrayUtil();
	
	public SphericMath getSphericMath();
	
	public ColorUtil getColorUtil();
	
}
