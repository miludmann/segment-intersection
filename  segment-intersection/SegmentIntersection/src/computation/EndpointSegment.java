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
	private Point2D point;
	
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
	
	public Point2D getPoint(){
		return point;
	}
	
	public Segment getSegment(){
		return segment;
	}
}
