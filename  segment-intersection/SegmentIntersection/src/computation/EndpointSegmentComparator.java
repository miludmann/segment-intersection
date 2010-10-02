package computation;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class EndpointSegmentComparator implements Comparator<EndpointSegment> {

	 public int compare(EndpointSegment obj1, EndpointSegment obj2) {
		 Point2D p1 = obj1.getPoint();
		 Point2D p2 = obj2.getPoint();
		 
	        if (p1.getY() < p2.getY()) 
	         return -1;
	        if (p1.getY() > p2.getY())
	         return 1;
	        if (p1.getX() < p2.getX())
	         return 1;
	        if (p1.getX() > p2.getX())
	         return -1;
	        return 0;
	 }

}
