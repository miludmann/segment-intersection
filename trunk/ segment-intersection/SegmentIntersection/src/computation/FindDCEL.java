package computation;

import java.util.ArrayList;

import DCEL.DCEL;

import sceneGraph.Intersection;
import sceneGraph.Segment;

public class FindDCEL {
	DCEL dcel;

	
	public FindDCEL(ArrayList<Segment> segments, ArrayList<Intersection> intersections){

		dcel = new DCEL();
		
		int nbIntersections = intersections.size();
		int nbSegments;
		ArrayList<Segment> tmp;
		
		
		
		nbSegments = segments.size();
		
		for(int i=0; i<nbSegments; i++){
			segments.get(i).initSplitSegment();
			
		}
	
		
		for(int i=0; i<nbIntersections; i++){
			System.out.println(intersections.get(i).getPoint().toString());
			
			tmp = intersections.get(i).getSegments();
			nbSegments = tmp.size();
			for(int j=0; j<nbSegments; j++){
				tmp.get(j).addSplit(intersections.get(i).getPoint());
			}
		}
		
		nbSegments = segments.size();
		
		for(int i=0; i<nbSegments; i++){
			segments.get(i).printSegment();
			System.out.println(segments.get(i).getSplit().size());
			
			int nbSplit = segments.get(i).getSplit().size();
			
			for(int j=0; j<nbSplit; j++){
				this.dcel.addVertex(segments.get(i).getSplit().get(j));
			}
		}
		
		System.out.println(dcel.getVertexList().size());
		int nbVertex = dcel.getVertexList().size();
		
		for(int i=0; i<nbVertex; i++){
			System.out.println(this.dcel.getVertexList().get(i).getP().toString());
		}
	}

}
