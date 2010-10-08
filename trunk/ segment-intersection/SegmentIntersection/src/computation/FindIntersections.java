/**
 * 
 */
package computation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Vector;

import components.DrawArea;

import sceneGraph.Intersection;
import sceneGraph.Segment;

/**
 * @author parapampa
 *
 */
public class FindIntersections {

	/**
	 * 
	 */
	private DrawArea drawArea;
	
	private PriorityQueueAdv queue;
	//private AVLNode <Segment> tree;
	
	private RedBlackTree sweepLineStatus;
	
	public FindIntersections(DrawArea drawArea) {
		Comparator <EventPointSegment> comparator = new EventPointSegmentComparator();
		queue = new PriorityQueueAdv(10, comparator);
		
		sweepLineStatus = new RedBlackTree();
		
		this.drawArea = drawArea;
		
	}


	public FindIntersections(DrawArea drawArea, ArrayList<Segment> segments) {
		
		this(drawArea);
		
		int nbSegments = segments.size();
		
		long timerStart = System.currentTimeMillis();
		
		// add all endpoints in the event queue, with the segment if the point is an upper endpoint
		for(int i=0; i<nbSegments; i++){
			EventPointSegment EpS1 = new EventPointSegment(segments.get(i).getUpperEndpoint(), segments.get(i));
			queue.add(EpS1);
			EventPointSegment EpS2 = new EventPointSegment(segments.get(i).getLowerEndpoint());
			queue.add(EpS2);
			
		}
		
		while(!queue.isEmpty()){
			EventPointSegment newEvent = queue.getNext();
			HandleEventPoint(newEvent);
		}
		
		long timerEnd = System.currentTimeMillis();
		long runningTime = Math.abs(timerEnd - timerStart);

		System.out.println("Running time to find intersections : "+runningTime+" ms");
		
	}
	
	public void HandleEventPoint(EventPointSegment evtPointSeg){	
		// Get event point
		Point2D.Float evtPoint = evtPointSeg.getPoint();
		// Get segments linked to this event point
		ArrayList<Segment> evtSet = evtPointSeg.getSegments();	
		
		ArrayList<Segment> uSet = new ArrayList<Segment>();	
		ArrayList<Segment> lcSet = new ArrayList<Segment>();	
		ArrayList<Segment> lSet = new ArrayList<Segment>();	
		ArrayList<Segment> cSet = new ArrayList<Segment>();	

//		System.out.println("Handling p : {X = "+evtPoint.getX()+ ", Y = "+evtPoint.getY()+"}");
		
		// Build set U(evtPoint) : set of segments whose upper endpoint is evtPoint
		if(!evtSet.isEmpty())
		{
			for(int i=0; i<evtSet.size(); i++)
			{
				if(evtSet.get(i).getUpperEndpoint().equals(evtPoint))
				{
					uSet.add(evtSet.get(i));
				}
			}
		}
		
		// Find all segments stored in the tree that contain evtPoint
		lcSet = SegmentsContainingEventPoint(sweepLineStatus, evtPointSeg);
		for(int i = 0; i<lcSet.size(); i++)
		{
			if(lcSet.get(i).getLowerEndpoint().equals(evtPoint))
			{
				// Set of segments whose lowerEndpoint is evtPoint
				lSet.add(lcSet.get(i));
			} else {
				// Set of segments who contain evtPoint in their interior
				cSet.add(lcSet.get(i));
			}
		}
		
		// Report intersection
		if(uSet.size() + lSet.size() + cSet.size() > 1)
		{
			evtPointSeg.isIntersection();
			Intersection newIntersection = getDrawArea().createIntersection(evtPoint.x, evtPoint.y);			
			ArrayList<Segment> segments = new ArrayList<Segment>();
			segments.addAll(uSet);
			segments.addAll(lSet);
			segments.addAll(cSet);
			newIntersection.setSegments(segments);
		}
		
		// Delete segments lSet and cSet from tree
		for(int i = 0; i<lSet.size(); i++)
		{
//			lSet.get(i).printSegment();
			sweepLineStatus.treeDelete(sweepLineStatus.treeSearch(lSet.get(i)));
		}
		for(int i = 0; i<cSet.size(); i++)
		{
//			cSet.get(i).printSegment();
			sweepLineStatus.treeDelete(sweepLineStatus.treeSearch(cSet.get(i)));
		}
		
		// Make sure the segments in the tree are in good order (order of intersection with the sweep line just below the event point)
		float minVal = -1;

		try {
			for(int i = 0; i<sweepLineStatus.allNodes().size() ; i++)
			{
				Segment segment = ((Segment) sweepLineStatus.allNodes().get(i).getKey());
				if (segment.getValue() < minVal || minVal == -1)
				{
					minVal = segment.getValue();
				}
				sweepLineStatus.treeDelete(sweepLineStatus.treeSearch(segment));
				if(segment.isHorizontal())
				{
					segment.updateValue(minVal + 0.0002F);
				} else {
					segment.updateValue(evtPoint.y - 0.02F);
				}
				treeInsertBlock(segment);
				
				
			}
		} catch (NullPointerException ex){}
		
		// Insert segments from uSet and cSet in tree
		Segment horizontalSegment = null;
		for(int i = 0; i<uSet.size(); i++)
		{	
			Segment segment = uSet.get(i);
			segment.updateValue(evtPoint.y - 0.02F);
			if (segment.getValue() < minVal || minVal == -1)
			{
				minVal = segment.getValue();
			}
			if(segment.isHorizontal())
			{
				horizontalSegment = segment;
			} else {
				treeInsertBlock(segment);
			}
		}
		for(int i = 0; i<cSet.size(); i++)
		{
			Segment segment = cSet.get(i);
			segment.updateValue(evtPoint.y - 0.02F);
			if (segment.getValue() < minVal || minVal == -1)
			{
				minVal = segment.getValue();
			}
			if(segment.isHorizontal())
			{
				horizontalSegment = segment;
			} else {
				treeInsertBlock(segment);
			}	
		}
		if(horizontalSegment != null)
		{
			horizontalSegment.updateValue(minVal + 0.0002F);
			treeInsertBlock(horizontalSegment);
		}
		
		if(uSet.size() + cSet.size() == 0)
		{
			// Find sl and sr, left and right segments neighbors of evtPoint in tree
//			System.out.println("---CASE 9---");
	
			try {
				Segment sTmp = new Segment(new float[]{evtPoint.x, evtPoint.x}, new float[]{evtPoint.y, evtPoint.y +50}, 2);
				treeInsertBlock(sTmp);
				Segment sl = (Segment) sweepLineStatus.treeSearch(sTmp).getNext().getKey();
				Segment sr = (Segment) sweepLineStatus.treeSearch(sTmp).getPrev().getKey();
				sweepLineStatus.treeDelete(sweepLineStatus.treeSearch(sTmp));
				if(sr.getId() != -1 && sl.getId() != -1)
				{
					FindNewEvent(sl, sr, evtPoint);
				}

			} catch (NullPointerException ex) {}
			
		} else {
//			System.out.println("---CASE 11---");
			ArrayList<Segment> unionSet = new ArrayList<Segment>();
			unionSet.addAll(uSet);
			unionSet.addAll(cSet);
			
			Segment sPrime = unionSet.get(0);

			// find the leftmost segment in unionSet :
			for(int i = 1; i<unionSet.size(); i++)
			{
				if(unionSet.get(i).getValue() < sPrime.getValue())
				{
					sPrime = unionSet.get(i);
				}
			}
		
			// find left neighbor of sPrime in tree :
			try {
				Segment sl = (Segment) sweepLineStatus.treeSearch(sPrime).getNext().getKey();
				FindNewEvent(sl, sPrime, evtPoint);
				
			} catch (NullPointerException ex) {}
			
			
			Segment sSecond = unionSet.get(0);
			// find the rightmost segment in unionSet :
			for(int j = 1; j<unionSet.size(); j++)
			{
				if(unionSet.get(j).getValue() > sSecond.getValue())
				{
					sSecond = unionSet.get(j);
				}
			}			
			// find right neighbor of sSecond in tree :          
			try {
				Segment sr = (Segment) sweepLineStatus.treeSearch(sSecond).getPrev().getKey();
				FindNewEvent(sSecond, sr, evtPoint);
				
			} catch (NullPointerException ex) {}
		}
	}
	
	public void FindNewEvent(Segment sl, Segment sr, Point2D.Float p)
	{

		Point2D.Float inter = inter2Segments(sl, sr);
		if ( inter != null && (inter.y < p.y || (Math.abs(inter.y - p.y) <= 1.000001E-002F && inter.x > p.x)))
				{			
					EventPointSegment newPoint = new EventPointSegment(inter);
					if(!queue.contains(newPoint))
					{
						queue.add(newPoint);
					}
				}
	}
	
	public ArrayList<Segment> SegmentsContainingEventPoint(RedBlackTree redblacktree, EventPointSegment evtPointSeg)
    {
		ArrayList<Segment> result = new ArrayList<Segment>();
		
        if(redblacktree == null)
            return result;
        Vector vector = new Vector();
        vector = redblacktree.allNodes();
        if(vector == null)
            return result;
        for(Enumeration enumeration = vector.elements(); enumeration.hasMoreElements();)
        {
            RedBlackNode redblacknode = (RedBlackNode)enumeration.nextElement();
            Segment segment = (Segment)redblacknode.getKey();
            if(segment.containsPoint(evtPointSeg.getPoint()))
            {
//            	segment.printSegment();
//                eventpoint.addIntersectionSegment(segment);
            	
            	result.add(segment);
            }
        }
        return result;
    }

    public void treeInsertBlock(Segment segment)
    {
        RedBlackNode redblacknode = new RedBlackNode(segment);
        if(sweepLineStatus.treeContains(redblacknode))
        {
            System.out.println("Error: Segment must already be in the tree");
            return;
        }
        float d = 0.0F;
        if (segment.isVertical())
        {
        	sweepLineStatus.treeInsert(redblacknode);
        	return;
        }
        do
        {
            segment.setValue(segment.getValue() - d);
            if(sweepLineStatus.treeInsert(redblacknode))
                break;
            segment.setValue(segment.getValue() + 2F * d);
            if(sweepLineStatus.treeInsert(redblacknode))
                break;
            segment.setValue(segment.getValue() - d);
            d += 0.5F;
        } while(true);
    }

	public DrawArea getDrawArea() {
        return this.drawArea;
    }
	
	public static Point2D.Float inter2Segments(Segment s1, Segment s2)
	{
		Point2D.Float A = s1.getLowerEndpoint();
		Point2D.Float B = s1.getUpperEndpoint();
		Point2D.Float C = s2.getLowerEndpoint();
		Point2D.Float D = s2.getUpperEndpoint();
		
		float Ax = A.x;
		float Ay = A.y;
		float Bx = B.x;
		float By = B.y;
		float Cx = C.x;
		float Cy = C.y;
		float Dx = D.x;
		float Dy = D.y;
		
		double Sx;
		double Sy;
 
		if(Ax==Bx)
		{
			if(Cx==Dx) return null;
			else
			{
				double pCD = (Cy-Dy)/(Cx-Dx);
				Sx = Ax;
				Sy = pCD*(Ax-Cx)+Cy;
			}
		}
		else
		{
			if(Cx==Dx)
			{
				double pAB = (Ay-By)/(Ax-Bx);
				Sx = Cx;
				Sy = pAB*(Cx-Ax)+Ay;
			}
			else
			{
				double pCD = (Cy-Dy)/(Cx-Dx);
				double pAB = (Ay-By)/(Ax-Bx);
				double oCD = Cy-pCD*Cx;
				double oAB = Ay-pAB*Ax;
				Sx = (oAB-oCD)/(pCD-pAB);
				Sy = pCD*Sx+oCD;
			}
		}
		if((Sx<Ax && Sx<Bx)|(Sx>Ax && Sx>Bx) | (Sx<Cx && Sx<Dx)|(Sx>Cx && Sx>Dx)
				| (Sy<Ay && Sy<By)|(Sy>Ay && Sy>By) | (Sy<Cy && Sy<Dy)|(Sy>Cy && Sy>Dy))
			{
				return null;
			} else {
				return new Point2D.Float((float)Sx,(float)Sy);
			}
	}
    
}
