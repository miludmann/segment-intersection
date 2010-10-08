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
public class Polygon extends Shape {
	
	private java.awt.Polygon polygon;


	public Polygon() {
		polygon = new java.awt.Polygon();
	}
	
	public Polygon(int[] xpoints, int[] ypoints, int npoints) {
		polygon = new java.awt.Polygon(xpoints, ypoints, npoints);
	}
	
	public Polygon(ArrayList<int[]> points) {
		this();
		setPolygon(points);
	}
	
	public void setPolygon(int[] xpoints, int[] ypoints, int npoints) {
		polygon.reset();
		for (int i = 0; i < npoints; i++)
		{
			polygon.addPoint(xpoints[i], ypoints[i]);
		}
	}
	
	public void setPolygon(ArrayList<int[]> points) {
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
		setPolygon(xpoints, ypoints, npoints);
	}
	
		
	public java.awt.Polygon getShape() {
		return polygon;
	}

	public void addPoint(int i, int j) {
		polygon.addPoint(i, j);
	}
}
