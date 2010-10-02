package sceneGraph;

import java.util.ArrayList;
import java.awt.geom.Point2D;

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
@SuppressWarnings("serial")
public class Segment extends Shape {
	
	private java.awt.Polygon segment;
	private float[] xpoints;
	private float[] ypoints;
	private Point2D.Float upperEndpoint;
	private Point2D.Float lowerEndpoint;


	public Segment() {
		segment = new java.awt.Polygon();
		xpoints = null;
		ypoints = null;
		upperEndpoint = new Point2D.Float();
		lowerEndpoint = new Point2D.Float();
	}
	
	public Segment(float[] xpoints, float[] ypoints, int npoints) {
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
		}	
	}
	
	public Point2D.Float getLowerEndpoint(){
		return this.lowerEndpoint;
	}
	
	public Point2D.Float getUpperEndpoint(){
		return this.upperEndpoint;
	}
}
