package computation;

import java.util.ArrayList;

import components.DrawArea;

import DCEL.DCEL;
import DCEL.Face;
import DCEL.HalfEdge;
import DCEL.Vertex;

import sceneGraph.Intersection;
import sceneGraph.Segment;

public class FindDCEL {
	private DCEL dcel;
	private DrawArea drawArea;

	
	public FindDCEL(ArrayList<Segment> segments, ArrayList<Intersection> intersections, DrawArea drawArea){

		// Initialize DCEL
		dcel = new DCEL(drawArea);
		this.drawArea = drawArea;
		
		int nbIntersections = intersections.size();
		int nbSegments;
		ArrayList<Segment> tmp;

		nbSegments = segments.size();

		// Initialize split segments
		for(int i=0; i<nbSegments; i++){
			segments.get(i).initSplitSegment();
		}
	
		// Complete splitting operation
		for(int i=0; i<nbIntersections; i++){
			//System.out.println(intersections.get(i).getPoint().toString());
			
			tmp = intersections.get(i).getSegments();
			nbSegments = tmp.size();
			for(int j=0; j<nbSegments; j++){
				tmp.get(j).addSplit(intersections.get(i).getPoint());
			}
		}
		
		nbSegments = segments.size();
		
		// Creation of Vertex List
		for(int i=0; i<nbSegments; i++){
			//segments.get(i).printSegment();
			//System.out.println(segments.get(i).getSplit().size());
			
			int nbSplit = segments.get(i).getSplit().size();
			
			for(int j=0; j<nbSplit; j++){
				this.dcel.addVertex(segments.get(i).getSplit().get(j));
			}
		}
		
		// Create Half Edge List
		// Stores Vertex and Twin
		// Links a Vertex to a Half Edge
		//System.out.println("===");
		Vertex v1, v2;
		HalfEdge h1, h2;

		for(int i=0; i<nbSegments; i++){
			int nbSplit = segments.get(i).getSplit().size();
			
			for(int j=0; j<(nbSplit-1); j++){
				v1 = this.dcel.pointToVertex(segments.get(i).getSplit().get(j));
				v2 = this.dcel.pointToVertex(segments.get(i).getSplit().get(j+1));
				
				h1 = new HalfEdge(v1, this.dcel.getHalfEdgeList().size());
				this.dcel.addHalfEdge(h1);

				h2 = new HalfEdge(v2, this.dcel.getHalfEdgeList().size());
				this.dcel.addHalfEdge(h2);
				
				h1.setTwin(h2);
				h2.setTwin(h1);
				
				v1.setHalfEdge(h1);
				v2.setHalfEdge(h2);
				
			}
		}
		
		// Check Half Edge List
		//System.out.println("===");
		//System.out.println(this.dcel.getHalfEdgeList().size());
		//System.out.println("===");

		//int nbHalfEdges = this.dcel.getHalfEdgeList().size();
		
		//for(int i=0; i<nbHalfEdges; i++){
			//System.out.println(this.dcel.getHalfEdgeList().get(i).getId());
			//System.out.println(this.dcel.getHalfEdgeList().get(i).toString());
			//System.out.println(this.dcel.getHalfEdgeList().get(i).getTwin().toString());
		//}
		
		// Check Vertex list
		//System.out.println("===");
		//System.out.println(dcel.getVertexList().size());
		//System.out.println("===");

		// Stores next and prev in Half Edge List
		int nbHalfEdges = dcel.getHalfEdgeList().size();
		ArrayList<HalfEdge> tmpHalfEdges = new ArrayList<HalfEdge>();
		Vertex v3, v4, vtmp;
		HalfEdge he1, he2, heTmp;
		double angle, oldAngle;
	
		
		for(int i=0; i<nbHalfEdges; i++){
			tmpHalfEdges.clear();
			
			he1 = this.dcel.getHalfEdgeList().get(i);
			
			v1 = he1.getTwin().getOrigin();
			v2 = he1.getOrigin();
			
			he2 = null;
			vtmp = null;
			oldAngle = 0;
			angle = 0;
			
			for(int j=0; j<nbHalfEdges; j++){
				
				v3 = this.dcel.getHalfEdgeList().get(j).getOrigin();
				
				if ( v1.equals(v3) ){
					
					heTmp = this.dcel.getHalfEdgeList().get(j);
					v4 = heTmp.getTwin().getOrigin();
					tmpHalfEdges.add(heTmp);
					angle = v1.getAngle(v2, v4);
					// System.out.println( angle +" - "+v4.getP().toString() );
					
					if ( vtmp == null || angle >= oldAngle ){
						vtmp = v4;
						oldAngle = angle;
						he2 = heTmp;
					}
				}
			}
			
			he1.setNext(he2);
			he2.setPrev(he1);
			
			//System.out.println("RESULT : "+vtmp.getP().toString());
			//System.out.println(this.dcel.getHalfEdgeList().get(i).getTwin().getOrigin().getId());
			//System.out.println(tmpHalfEdges.size());
		}
		
		
		// Check Half Edge List
		/*
		nbHalfEdges = dcel.getHalfEdgeList().size();
		for(int j=0; j<nbHalfEdges; j++){
			heTmp = dcel.getHalfEdgeList().get(j);
			System.out.println("id:"+heTmp.getId()+"_vertex:"+heTmp.getOrigin().getP().toString()+"_prev:"+heTmp.getPrev().getId()+"_next:"+heTmp.getNext().getId());
		}
		*/
		
		// Face part (rt+lm)
		ArrayList<HalfEdge> HalfEdgesBis = (ArrayList<HalfEdge>) dcel.getHalfEdgeList().clone();
		Face face;
		
		while ( HalfEdgesBis.size() > 0 ){
			
			he1 = HalfEdgesBis.get(0);
			face = new Face(he1, dcel.getFaceList().size());
			dcel.addFaceList(face);
			
			he1.setFace(face);
			HalfEdgesBis.remove(he1);
			heTmp = he1.getNext();
			
			while ( !(he1.equals(heTmp)) ){
				heTmp.setFace(face);
				HalfEdgesBis.remove(heTmp);
				heTmp = heTmp.getNext();
			}
		}
		
		// Check Half Edge List with Faces label
		/*
		nbHalfEdges = dcel.getHalfEdgeList().size();
		for(int j=0; j<nbHalfEdges; j++){
			heTmp = dcel.getHalfEdgeList().get(j);
			System.out.println("id:"+heTmp.getId()+"_face:"+heTmp.getFace().getId()+"_prev:"+heTmp.getPrev().getId()+"_next:"+heTmp.getNext().getId());
		}
		*/
		int nbFaces = dcel.getFaceList().size();
		Face faceTmp;
		
		for(int j=0; j<nbFaces; j++){
			faceTmp = dcel.getFaceList().get(j);
			faceTmp.analyseFace();

			//System.out.println("id:"+faceTmp.getId()+"_outerComponent:"+faceTmp.getOuterComponent().getId()+"_outer:"+faceTmp.getIsOuter());
		}
		
		//System.out.println("Nombre de faces : "+dcel.getFaceList().size());
		
		
		// Split inner and outer
		ArrayList<Face> outerFace = new ArrayList<Face>();
		nbFaces = dcel.getFaceList().size();
		
		Face f0 = new Face(null, nbFaces); // the infinite Face


		for(int j=0; j<nbFaces; j++){
			faceTmp = dcel.getFaceList().get(j);
			
			if ( faceTmp.getIsOuter() ){
				outerFace.add(faceTmp);
				dcel.getFaceList().remove(faceTmp);
				nbFaces = dcel.getFaceList().size();
			}
		}
		
		//System.out.println("Inner : "+dcel.getFaceList().size()+" - Outer : "+outerFace.size());
		//System.out.println("===");
		
		// Fill the innerComponent in the Faces
		int nbFacesIn = dcel.getFaceList().size();
		int nbFacesOut = outerFace.size();
		int nbEdgesCrossed;
		
		Face faceIn, faceOut;
		Face faceRes;
		double distRes, distTmp, distTmp2;

		for(int i=0; i<nbFacesOut; i++){
			faceOut = outerFace.get(i);
			v1 = faceOut.getOuterHalfEdge().getOrigin();
			nbEdgesCrossed = 0;
			distTmp = 0;
			distRes = 0;
			faceRes = null;
			
			for(int j=0; j<nbFacesIn; j++){
				faceIn = dcel.getFaceList().get(j);
				h1 = faceIn.getOuterComponent();
				
				heTmp = h1;
				nbEdgesCrossed = 0;
				if ( v1.crossHorizontal(heTmp) ){
					distTmp2 = v1.horizontalDistance(heTmp);
					
					if ( distTmp == 0 || distTmp2 < distTmp )
						distTmp = distTmp2;
					
					nbEdgesCrossed++;
				}
				heTmp = heTmp.getNext();
				
				while ( !(heTmp.equals(h1)) ){
					if ( v1.crossHorizontal(heTmp) ){
						distTmp2 = v1.horizontalDistance(heTmp);
						
						if ( distTmp == 0 || distTmp2 < distTmp )
							distTmp = distTmp2;
						
						nbEdgesCrossed++;
					}
					heTmp = heTmp.getNext();
				}
				//System.out.println(faceOut.getId()+"->"+faceIn.getId()+" - times... "+nbEdgesCrossed%2);
				
				// here is the trick: if there are an odd edges crossed
				// faceOut is included in faceIn
				// in order to know which one is the tinier, we just use 
				// distance between a vertex and a Half Edge
				if ( nbEdgesCrossed%2 == 1 ){
					if ( distRes == 0 ){
						faceRes = faceIn;
						distRes = distTmp;
					}
					else
					{
						if ( distTmp < distRes ){
							faceRes = faceIn;
							distRes = distTmp;
						}
					}
				}
			}
			if ( distRes == 0 ){
				faceRes = f0;
			}
			
			//System.out.println("Face "+faceOut.getId()+" points to face "+faceRes.getId());
			
			// Link faces with holes
			nbHalfEdges = dcel.getHalfEdgeList().size();
			for(int j=0; j<nbHalfEdges; j++){
				he1 = dcel.getHalfEdgeList().get(j);
				if ( he1.getFace().equals(faceOut) ){
					he1.setFace(faceRes);
				}
			}
			
			// Fill the inner components
			faceRes.addInnerComponent(faceOut.getOuterComponent());
		}
		
		//add the infinite face to the Face List
		dcel.addFaceList(f0);
		
		/*
		nbHalfEdges = dcel.getHalfEdgeList().size();
		for(int j=0; j<nbHalfEdges; j++){
			heTmp = dcel.getHalfEdgeList().get(j);
			System.out.println("id:"+heTmp.getId()+"_face:"+heTmp.getFace().getId()+"_prev:"+heTmp.getPrev().getId()+"_next:"+heTmp.getNext().getId());
		}
		*/
		
		dcel.colorDCEL(dcel.getFaceList());
	}
	
	public void printDCEL(){
		this.dcel.printDCEL();
	}
}