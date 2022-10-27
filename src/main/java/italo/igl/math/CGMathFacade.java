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

public class CGMathFacade implements CGMath {

	private MathUtil mathUtil = new MathUtil();
	private DoubleArrayUtil doubleArrayUtil = new DoubleArrayUtil();
	
	private Normalizer normalizer = new Normalizer();
	
	private TrigonometryMath trigonMath = new TrigonometryMath();
	
	private Geometry3DMath geom3dMath = new Geometry3DMath();
	private Geometry2DMath geom2DMath = new Geometry2DMath( this );
	
	private Polar2DMath polar2DMath = new Polar2DMath( this );
	private ProjectionMath projectionMath = new ProjectionMath( this );
	private TransformerMath transformerMath = new TransformerMath();

	private SphericMath sphericMath = new SphericMath();
	private ColorUtil colorUtil = new ColorUtil();
	
	public MathUtil getMathUtil() {
		return mathUtil;
	}

	public Normalizer getNormalizer() {
		return normalizer;
	}

	public TrigonometryMath getTrigonometry() {
		return trigonMath;
	}

	public Geometry3DMath getGeometry3D() {
		return geom3dMath;
	}

	public Geometry2DMath getGeometry2D() {
		return geom2DMath;
	}

	public Polar2DMath getPolar() {
		return polar2DMath;
	}
	
	public ProjectionMath getProjection() {
		return projectionMath;
	}

	public TransformerMath getTransformer() {
		return transformerMath;
	}

	public DoubleArrayUtil getDoubleArrayUtil() {
		return doubleArrayUtil;
	}

	public SphericMath getSphericMath() {
		return sphericMath;
	}
	
	public ColorUtil getColorUtil() {
		return colorUtil;
	}
		
}
