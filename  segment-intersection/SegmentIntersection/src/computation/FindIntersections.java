/**
 * 
 */
package computation;

import java.awt.geom.Point2D;
import java.util.PriorityQueue;
import java.util.Comparator;

/**
 * @author parapampa
 *
 */
public class FindIntersections {

	/**
	 * 
	 */
	private PriorityQueue queue;
	
	
	public FindIntersections() {
		Comparator <EndpointSegment> comparator = new EndpointSegmentComparator();
		queue = new PriorityQueue(10, comparator);
		
	}
	


}
