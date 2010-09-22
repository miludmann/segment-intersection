package sceneGraph;

import java.util.ArrayList;

/**
 * <p>
 * <b>Polygon</b>
 * </p>
 * <p>
 * Polygon est la classe implémentant les polygons
 * </p>
 * @author Depoyant Guillaume & Ludmann Michaël
 *
 */
@SuppressWarnings("serial")
public class Segment extends Shape {
	
	private java.awt.Polygon segment;


	public Segment() {
		segment = new java.awt.Polygon();
	}
	
	public Segment(int[] xpoints, int[] ypoints, int npoints) {
		segment = new java.awt.Polygon(xpoints, ypoints, npoints);
	}
	
	public Segment(ArrayList<int[]> points) {
		this();
		setSegment(points);
	}
	
	public void setSegment(int[] xpoints, int[] ypoints, int npoints) {
		segment.reset();
		for (int i = 0; i < npoints; i++)
		{
			segment.addPoint(xpoints[i], ypoints[i]);
		}
	}
	
	public void setSegment(ArrayList<int[]> points) {
		int npoints = points.size();
		int[] xpoints = new int[npoints];
		int[] ypoints = new int[npoints];
		for (int i = 0; i < npoints; i++)
		{
			int[] point = points.get(i);
			if (point.length == 2)
			{
				xpoints[i] = point[0];
				ypoints[i] = point[1];
			}
		}
		setSegment(xpoints, ypoints, npoints);
	}
	
	public int[] getXpoints() {
		return segment.xpoints;
	}
	
	public int[] getYpoints() {
		return segment.ypoints;
	}

		
	public java.awt.Polygon getShape() {
		return segment;
	}
}
