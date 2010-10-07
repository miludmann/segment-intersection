package sceneGraph;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * <p>
 * <b>Circle</b>
 * </p>
 * <p>
 * Circle est la classe implémentant le cercle
 * </p>
 * @author Depoyant Guillaume & Ludmann Michaël
 *
 */
@SuppressWarnings("serial")
public class Intersection extends Ellipse {
	
	private float radius;
	protected float RADIUS = 10F;
	private float x, y;
	private ArrayList<Segment> segments;
	/**
	 * <p>
	 * Constructeur de la classe
	 * </p>
	 * <p>
	 * Instancie un cercle
	 * </p>
	 */
	public Intersection(float x, float y, float radius) {
		super();
		this.x = x;
		this.y = y;
		setFrame(x, y, radius);
		segments = new ArrayList<Segment>();
	}
	
	public Intersection(float x, float y) {
		super();
		this.x = x;
		this.y = y;
		setFrame(x, y, RADIUS);
		segments = new ArrayList<Segment>();
	}
	
	/**
	 * Getter du rayon
	 * @return le rayon
	 */
	public float getRadius() {
		return radius;
	}
	
	/**
	 * Mise à jour des coordonnées du cercle
	 * @param x nouvelle abscisse
	 * @param y nouvelle ordonnée
	 * @param radius nouveau rayon
	 */
	public void setFrame(float x, float y, float radius) {
		setFrame(x - radius, y - radius, x + radius, y + radius);
		this.radius = radius;
	}
	
	public String toString()
	{
		return "Intersection : {"+this.x+", "+this.y+"}";
	}
	
	public void printIntersection()
	{
		System.out.println("Intersection : "+this.x+" ; "+this.y);
	}	
	/**
	 * Set the list of segments that are part of the intersection
	 * @param list
	 */
	public void setSegments(ArrayList<Segment> list)
	{
		this.segments = list;
	}
	
	public ArrayList<Segment> getSegments()
	{
		return this.segments;
	}
	
	public Point2D getPoint(){
		Point2D res = null;
		res = new Point2D.Float();
		res.setLocation((double) this.x, (double) this.y);
		
		return res;
	}
}
