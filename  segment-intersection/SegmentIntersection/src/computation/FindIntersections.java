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
		
		// add all endpoints in the event queue, with the segment if the point is an upper endpoint
		for(int i=0; i<nbSegments; i++){
			EventPointSegment EpS1 = new EventPointSegment(segments.get(i).getUpperEndpoint(), segments.get(i));
			queue.add(EpS1);
			EventPointSegment EpS2 = new EventPointSegment(segments.get(i).getLowerEndpoint());
			queue.add(EpS2);
			
		}
		
		System.out.println();
		while(!queue.isEmpty()){
			EventPointSegment newEvent = queue.getNext();
//			newEvent.printEventPoint();
			HandleEventPoint(newEvent);
		}
		
		
	}
	
	public void HandleEventPoint(EventPointSegment evtPointSeg){	
		// Get event point
		Point2D.Float evtPoint = evtPointSeg.getPoint();
		// Get segments linked to this event point
		ArrayList<Segment> evtSet = evtPointSeg.getSegments();	
		
		ArrayList<Segment> uSet = new ArrayList<Segment>();	
		ArrayList<Segment> lpSet = new ArrayList<Segment>();	
		ArrayList<Segment> lSet = new ArrayList<Segment>();	
		ArrayList<Segment> cSet = new ArrayList<Segment>();	

		
		System.out.println("Handle p : {X = "+evtPoint.getX()+ ", Y = "+evtPoint.getY()+"}");
		
		// Build set U(evtPoint) : set of segments whose upper endpoint is evtPoint
		if(!evtSet.isEmpty())
		{
			for(int i=0; i<evtSet.size(); i++)
			{
				if(evtSet.get(i).getUpperEndpoint() == evtPoint)
				{
					uSet.add(evtSet.get(i));
//					treeInsertBlock(evtSet.get(i));
				}
			}
		}
		
		// Find all segments stored in the tree that contain evtPoint
		lpSet = SegmentsContainingEventPoint(sweepLineStatus, evtPointSeg);
		for(int i = 0; i<lpSet.size(); i++)
		{
			if(lpSet.get(i).getLowerEndpoint().equals(evtPoint))
			{
				// Set of segments whose lowerEndpoint is evtPoint
				lSet.add(lpSet.get(i));
			} else {
				// Set of segments who contain evtPoint in their interior
				cSet.add(lpSet.get(i));
			}
		}
		
		// Report intersection
		if(uSet.size() + lSet.size() + cSet.size() > 1)
		{
			System.out.println("Intersection found here ! : {X = "+evtPoint.getX()+", Y = "+evtPoint.getY()+"}");
			getDrawArea().createIntersection(evtPoint.x, evtPoint.y);
		}
		
		// Delete segments lSet and cSet from tree
		for(int i = 0; i<lSet.size(); i++)
		{
			System.out.println("Delete segment");
			lSet.get(i).printSegment();
			sweepLineStatus.treeDelete(sweepLineStatus.treeSearch(lSet.get(i)));
		}
		for(int i = 0; i<cSet.size(); i++)
		{
			System.out.println("Delete segment");
			cSet.get(i).printSegment();
			sweepLineStatus.treeDelete(sweepLineStatus.treeSearch(cSet.get(i)));
		}
		for(int i = 0; i<uSet.size(); i++)
		{	
			Segment segment = uSet.get(i);
			segment.updateValue(evtPoint.y - 0.02F);
			treeInsertBlock(segment);
		}
		for(int i = 0; i<cSet.size(); i++)
		{
			Segment segment = cSet.get(i);
			segment.updateValue(evtPoint.y - 0.02F);
			treeInsertBlock(segment);
		}
		
		sweepLineStatus.inOrderWalk();
		if(uSet.size() + cSet.size() == 0)
		{
			
		} else {
			
		}
	}
	
	public void FindNewEvent(Segment sl, Segment sr, Point2D.Float p)
	{
		
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
//            	evtPointSeg.printEventPoint();
//            	System.out.println("===");
//            	segment.printSegment();
//            	System.out.println("Segment contains point");
//                eventpoint.addIntersectionSegment(segment);
            	
            	result.add(segment);
            }
        }
        return result;
    }

    public void treeInsertBlock(Segment segment)
    {
//    	System.out.println("Insert in tree");
        RedBlackNode redblacknode = new RedBlackNode(segment);
        if(sweepLineStatus.treeContains(redblacknode))
        {
            System.out.println("Error: Segment must already be in the tree");
            return;
        }
        float d = 0.0F;
        do
        {
            segment.setValue(segment.getValue() + d);
            if(sweepLineStatus.treeInsert(redblacknode))
                break;
            segment.setValue(segment.getValue() - 2F * d);
            if(sweepLineStatus.treeInsert(redblacknode))
                break;
            segment.setValue(segment.getValue() + d);
            d += 0.5F;
        } while(true);
    }

	public DrawArea getDrawArea() {
        return this.drawArea;
    }
    
}
