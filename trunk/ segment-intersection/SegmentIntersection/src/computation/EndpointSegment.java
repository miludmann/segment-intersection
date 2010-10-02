/**
 * 
 */
package computation;

import java.awt.geom.Point2D;

import sceneGraph.Segment;

/**
 * @author parapampa
 *
 */
public class EndpointSegment {

	/**
	 * 
	 */
	private Segment segment;
	private Point2D.Float point;
	
	public EndpointSegment() {
		segment = new Segment();
		point = new Point2D.Float();
	}

	public EndpointSegment(Point2D.Float pt) {
		this.point = pt;
		this.segment = null;
	}
	
	public EndpointSegment(Point2D.Float pt, Segment seg){
		this.point = pt;
		this.segment = seg;
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
	
	public Segment getSegment(){
		return segment;
	}
}
