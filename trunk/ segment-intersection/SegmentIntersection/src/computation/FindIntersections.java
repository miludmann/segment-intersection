/**
 * 
 */
package computation;

import java.awt.geom.Point2D;
import java.util.PriorityQueue;
import java.util.Comparator;

import sceneGraph.Segment;

/**
 * @author parapampa
 *
 */
public class FindIntersections {

	/**
	 * 
	 */
	private PriorityQueue <EndpointSegment> queue;
	//private AVLNode <Segment> tree;
	
	public FindIntersections() {
		Comparator <EndpointSegment> comparator = new EndpointSegmentComparator();
		queue = new PriorityQueue<EndpointSegment>(10, comparator);
		
	}


	public FindIntersections(Segment[] segments) {
		
		this();
		
		int nbSegments = segments.length;
		
		// add all endpoints in the event queue, with the segment if the point is an upper endpoint
		for(int i=0; i<nbSegments; i++){
			EndpointSegment EpS1 = new EndpointSegment(segments[i].getUpperEndpoint(), segments[i]);
			queue.add(EpS1);
			EndpointSegment EpS2 = new EndpointSegment(segments[i].getLowerEndpoint());
			queue.add(EpS2);
		}
		
		System.out.println();
		int size = queue.size();
		for(int i=0; i<size; i++){
			EndpointSegment newEvent = queue.poll();
			System.out.println("iter"+i+" - X = "+newEvent.getX()+ " Y = "+newEvent.getY());
		}
		
		
	}
	


}
