package sceneGraph;

import java.util.ArrayList;
import java.awt.geom.Point2D;

import computation.Key;
import computation.RedBlackNode;

/**
 * <p>
 * <b>Segment</b>
 * </p>
 * <p>
 * 
 * </p>
 * @author Depoyant Guillaume & Ludmann Michaël
 *
 */
public class Segment extends Shape implements Key{
	
	private java.awt.Polygon segment;
	private float[] xpoints;
	private float[] ypoints;
	private Point2D.Float upperEndpoint;
	private Point2D.Float lowerEndpoint;
	private Point2D.Float leftEndpoint;
	private Point2D.Float rightEndpoint;
	private float slope;
	private float originOrdinate;
    public static final float MAX_SLOPE = 99999999999999.906F;
    private float value;
    private RedBlackNode node;

	public Segment() {
		segment = new java.awt.Polygon();
		xpoints = null;
		ypoints = null;
		upperEndpoint = new Point2D.Float();
		lowerEndpoint = new Point2D.Float();
		rightEndpoint = new Point2D.Float();
		leftEndpoint = new Point2D.Float();
		value = -1;
	}
	
	public Segment(float[] xpoints, float[] ypoints, int npoints) {
		this();
		setXpoints(xpoints);
		setYpoints(ypoints);

		// Here we need to register the vertices as integer for the drawing part
		int[] xpointsInt = new int[npoints], ypointsInt = new int[npoints];
		for(int i=0; i < npoints; i++)
		{
			xpointsInt[i] = Float.floatToIntBits(xpoints[i]);
			ypointsInt[i] = Float.floatToIntBits(ypoints[i]);

		}
		segment = new java.awt.Polygon(xpointsInt, ypointsInt, npoints);
		computeEndpoints();
	}
	
	public Segment(ArrayList<float[]> points) {
		this();
		setSegment(points);
	}
	
	public void setSegment(float[] xpoints, float[] ypoints, int npoints) {
		segment.reset();
		setXpoints(null);
		setYpoints(null);
		for (int i = 0; i < npoints; i++)
		{
			segment.addPoint((int) xpoints[i], (int) ypoints[i]);
		}
		setXpoints(xpoints);
		setYpoints(ypoints);
		computeEndpoints();
	}
	
	public void setSegment(ArrayList<float[]> points) {
		int npoints = points.size();
		float[] xpoints = new float[npoints];
		float[] ypoints = new float[npoints];
		for (int i = 0; i < npoints; i++)
		{
			float[] point = points.get(i);
			if (point.length == 2)
			{
				xpoints[i] = point[0];
				ypoints[i] = point[1];
			}
		}
		setSegment(xpoints, ypoints, npoints);
	}
	
	public float[] getXpoints() {
		return this.xpoints;
	}
	
	public float[] getYpoints() {
		return this.ypoints;
	}

		
	public java.awt.Polygon getShape() {
		return segment;
	}

	public void setXpoints(float[] xpoints) {
		this.xpoints = xpoints;
	}

	public void setYpoints(float[] ypoints) {
		this.ypoints = ypoints;
	}
	
	public void computeEndpoints() {
		if(xpoints.length == 2 && ypoints.length == 2)
		{
			if((ypoints[0] > ypoints[1]) || (ypoints[0] == ypoints[1] && xpoints[0] <= xpoints[1])){
				upperEndpoint.setLocation(xpoints[0], ypoints[0]);
				lowerEndpoint.setLocation(xpoints[1], ypoints[1]);
			} else {
				upperEndpoint.setLocation(xpoints[1], ypoints[1]);
				lowerEndpoint.setLocation(xpoints[0], ypoints[0]);
			}
			if((xpoints[0] > xpoints[1])){
				rightEndpoint.setLocation(xpoints[0], ypoints[0]);
				leftEndpoint.setLocation(xpoints[1], ypoints[1]);
			} else {
				rightEndpoint.setLocation(xpoints[1], ypoints[1]);
				leftEndpoint.setLocation(xpoints[0], ypoints[0]);
			}
			value = leftEndpoint.x;

			setSlope();
			setOriginOrdinate();
		}	
	}

	public Point2D.Float getLowerEndpoint(){
		return this.lowerEndpoint;
	}
	
	public Point2D.Float getUpperEndpoint(){
		return this.upperEndpoint;
	}
	
	public Point2D.Float getLeftEndpoint(){
		return this.leftEndpoint;
	}
	
	public Point2D.Float getRightEndpoint(){
		return this.rightEndpoint;
	}
	public void printSegment()
	{
		System.out.println("Segment : upperEndPoint = "+upperEndpoint+" lowerEndPoint = "+lowerEndpoint);
	}
	
	public void setSlope()
	{
		if (xpoints[1]-xpoints[0] == 0)
		{
			slope = MAX_SLOPE;
		} else {
			slope = (ypoints[1]-ypoints[0])/(xpoints[1]-xpoints[0]);
		}
	}
	
	public void setOriginOrdinate()
	{
		originOrdinate = ypoints[0] - ((ypoints[1]-ypoints[0])/(xpoints[1]-xpoints[0]))* xpoints[0];
	}
	
	public float getSlope()
	{
		return slope;
	}
	
	public boolean isInBoundingBox(Point2D p)
	{
		boolean c1 = ((p.getX() >= xpoints[0] || p.getX() >= xpoints[1]) && (p.getY() >= ypoints[0] || p.getY() >= ypoints[1]));
		boolean c2 = ((p.getX() <= xpoints[0] || p.getX() <= xpoints[1]) && (p.getY() <= ypoints[0] || p.getY() <= ypoints[1]));
		return c1 && c2;
	}
	
	public boolean containsPoint(Point2D p)
	{
		return (((p.getY()-ypoints[0])/(p.getX()-xpoints[0]) - getSlope()) <= 1.0000000000000001E-009F) && isInBoundingBox(p);	
	}

	
	public void updateValue(float newY)
	{
		value = (newY-originOrdinate)/slope;
	}
	
	public boolean equals(Object obj)
    {
        if(obj instanceof Segment)
        {
            return equals((Key)obj);
        } else
        {
            System.out.println("Bad Object equals");
            return false;
        }
    }
	
	@Override
	public boolean equals(Key key) {
        return this == (Segment)key;
	}

	@Override
	public String getLabel() {
        return "S" + value;
	}

	@Override
	public RedBlackNode getNode() {
		return node;
	}

	@Override
	public float getValue() {
		return value;
	}

	@Override
	public boolean largerThan(Object obj) {
     if(obj instanceof Segment)
        {
            return largerThan((Key)obj);
        } else
        {
            System.out.println("Bad Object larger than");
            return false;
        }
	}

	@Override
	public boolean largerThan(Key key) {
        float d = key.getValue();
        float d1 = getValue();
        return d1 < d;
	}

	@Override
	public boolean lessThan(Object obj) {
		if(obj instanceof Segment)
        {
            return lessThan((Key)obj);
        } else
        {
            System.out.println("Bad Object less than");
            return false;
        }
	}

	@Override
	public boolean lessThan(Key key) {
		float d = key.getValue();
        float d1 = getValue();
        return d1 > d;
	}

	@Override
	public void setNode(RedBlackNode redblacknode) {
		node = redblacknode;
	}

	@Override
	public void setValue(float d) {
		value = d;
	}

	@Override
	public void swapValue(Key key) {
		float d = value;
        setValue(key.getValue());
        key.setValue(d);		
	}
}
