/**
 * 
 */
package computation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import sceneGraph.Segment;

/**
 * @author parapampa
 *
 */
public class EventPointSegment {

	/**
	 * 
	 */
	private ArrayList<Segment> segments;
	private Point2D.Float point;
	private boolean isIntersection;
	
	public EventPointSegment() {
		segments = new ArrayList<Segment>();
		point = new Point2D.Float();
		isIntersection = false;
	}

	public EventPointSegment(Point2D.Float pt) {
		this();
		this.point = pt;
		segments.clear();
	}
	
	public EventPointSegment(Point2D.Float pt, Segment seg){
		this();
		this.point = pt;
		segments.add(seg);
	}
	
	public Point2D.Float getPoint(){
		return point;
	}
	
	public Float getX(){
		return point.x;
	}
	
	public Float getY(){
		return point.y;
	}
	
	public void addSegment(Segment seg)
	{
		segments.add(seg);
	}
	
	public ArrayList<Segment> getSegments(){
		return segments;
	}
	
	public void setIsIntersection()
	{
		isIntersection = true;
	}
	
	public boolean isIntersection()
	{
		return isIntersection;
	}
	
	public void printEventPoint()
	{
		System.out.println("EventPoint : "+point);
		for(int i=0; i<segments.size(); i++)
		{
			segments.get(i).printSegment();
		}
	}

	public void addIntersectionSegment(Segment segment) {
		// TODO Auto-generated method stub
		
	}
}
