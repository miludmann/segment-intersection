package computation;

import java.awt.geom.Point2D;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JFileChooser;

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
		setDcel(new DCEL(drawArea));
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
				this.getDcel().addVertex(segments.get(i).getSplit().get(j));
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
				v1 = this.getDcel().pointToVertex(segments.get(i).getSplit().get(j));
				v2 = this.getDcel().pointToVertex(segments.get(i).getSplit().get(j+1));
				
				h1 = new HalfEdge(v1, this.getDcel().getHalfEdgeList().size());
				this.getDcel().addHalfEdge(h1);

				h2 = new HalfEdge(v2, this.getDcel().getHalfEdgeList().size());
				this.getDcel().addHalfEdge(h2);
				
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
		int nbHalfEdges = getDcel().getHalfEdgeList().size();
		ArrayList<HalfEdge> tmpHalfEdges = new ArrayList<HalfEdge>();
		Vertex v3, v4, vtmp;
		HalfEdge he1, he2, heTmp;
		double angle, oldAngle;
	
		
		for(int i=0; i<nbHalfEdges; i++){
			tmpHalfEdges.clear();
			
			he1 = this.getDcel().getHalfEdgeList().get(i);
			
			v1 = he1.getTwin().getOrigin();
			v2 = he1.getOrigin();
			
			he2 = null;
			vtmp = null;
			oldAngle = 0;
			angle = 0;
			
			for(int j=0; j<nbHalfEdges; j++){
				
				v3 = this.getDcel().getHalfEdgeList().get(j).getOrigin();
				
				if ( v1.equals(v3) ){
					
					heTmp = this.getDcel().getHalfEdgeList().get(j);
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
		ArrayList<HalfEdge> HalfEdgesBis = (ArrayList<HalfEdge>) getDcel().getHalfEdgeList().clone();
		Face face;
		
		while ( HalfEdgesBis.size() > 0 ){

			he1 = HalfEdgesBis.get(0);
			face = new Face(he1, getDcel().getFaceList().size());
			getDcel().addFaceList(face);

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
		int nbFaces = getDcel().getFaceList().size();
		Face faceTmp;
		
		for(int j=0; j<nbFaces; j++){
			faceTmp = getDcel().getFaceList().get(j);
			faceTmp.analyseFace();

			//System.out.println("id:"+faceTmp.getId()+"_outerComponent:"+faceTmp.getOuterComponent().getId()+"_outer:"+faceTmp.getIsOuter());
		}
		
		//System.out.println("Nombre de faces : "+dcel.getFaceList().size());
		
		
		// Split inner and outer
		ArrayList<Face> outerFace = new ArrayList<Face>();
		nbFaces = getDcel().getFaceList().size();
		
		Face f0 = new Face(null, nbFaces); // the infinite Face


		for(int j=0; j<nbFaces; j++){
			faceTmp = getDcel().getFaceList().get(j);
			
			if ( faceTmp.getIsOuter() ){
				outerFace.add(faceTmp);
				getDcel().getFaceList().remove(faceTmp);
				nbFaces = getDcel().getFaceList().size();
			}
		}
		
		//System.out.println("Inner : "+dcel.getFaceList().size()+" - Outer : "+outerFace.size());
		//System.out.println("===");
		
		// Fill the innerComponent in the Faces
		int nbFacesIn = getDcel().getFaceList().size();
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
				faceIn = getDcel().getFaceList().get(j);
				h1 = faceIn.getOuterComponent();
				
				heTmp = h1;
				nbEdgesCrossed = 0;
				if ( v1.crossHorizontal(heTmp) ){
					distTmp2 = v1.horizontalDistance(heTmp);
					
					if ( distTmp2 >= 0 )
					{
						if ( distTmp == 0 || distTmp2 < distTmp )
							distTmp = distTmp2;
						
						nbEdgesCrossed++;
					}
				}
				heTmp = heTmp.getNext();
				
				while ( !(heTmp.equals(h1)) ){
					if ( v1.crossHorizontal(heTmp) ){
						distTmp2 = v1.horizontalDistance(heTmp);
						
						if ( distTmp2 >= 0 )
						{
							if ( distTmp == 0 || distTmp2 < distTmp )
								distTmp = distTmp2;
							
							nbEdgesCrossed++;
						}
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
			
			nbHalfEdges = getDcel().getHalfEdgeList().size();
			for(int j=0; j<nbHalfEdges; j++){
				he1 = getDcel().getHalfEdgeList().get(j);
				if ( he1.getFace().equals(faceOut) ){
					he1.setFace(faceRes);
				}
			}
			
			// Fill the inner components
			faceRes.addInnerComponent(faceOut.getOuterComponent());
		}
		
		//add the infinite face to the Face List
		getDcel().addFaceList(f0);
		
		//Re-arrange the IDs of the faces
		for(int j=0; j<nbFaces+1; j++){
			faceTmp = getDcel().getFaceList().get(j);
			faceTmp.setId(j);
		}
			
		/*
		nbHalfEdges = dcel.getHalfEdgeList().size();
		for(int j=0; j<nbHalfEdges; j++){
			heTmp = dcel.getHalfEdgeList().get(j);
			System.out.println("id:"+heTmp.getId()+"_face:"+heTmp.getFace().getId()+"_prev:"+heTmp.getPrev().getId()+"_next:"+heTmp.getNext().getId());
		}
		*/
		
		//dcel.colorDCEL(dcel.getFaceList());
	}
	
	public void printDCEL(){
		this.getDcel().printDCEL();
		this.colorDCEL();
	}
	
	public void colorDCEL(){
		this.getDcel().colorDCEL(this.getDcel().getFaceList());
	}
	
	public void saveDCEL(){
		this.getDcel().saveDCEL();
	}
	
	public void openDCEL(){
		
		this.setDcel(new DCEL(drawArea));
		
		JFileChooser fc = new JFileChooser();
		
		int openStatus = fc.showOpenDialog(null);


		if ( openStatus == JFileChooser.APPROVE_OPTION ){
            File openedFile = fc.getSelectedFile();

            try {
            	FileInputStream fis = new FileInputStream(openedFile);

                // Here BufferedInputStream is added for fast reading.
            	BufferedInputStream bis = new BufferedInputStream(fis);
            	BufferedReader d = new BufferedReader(new InputStreamReader(bis));

                String line = null;
                String[] lineSplit ;
                int id, incidentEdge, origin, twin, incidentFace, next, prev, outer, inner;
                Float xcoord, ycoord;
                int nbVertex, nbHalfEdge, nbFaces;
                
                
                // First while loop, we initiate the vertex, half edges and faces 
                // We simply input the IDs
                while ((line = d.readLine()) != null) {
                	
                	lineSplit = line.split(" ");
                	
                	if ( lineSplit.length == 1){
                		d.readLine();
                		break;
                	}
                	
                	if ( lineSplit.length == 3){
                		nbVertex = (int) Float.parseFloat(lineSplit[0]);
                		nbHalfEdge = (int) Float.parseFloat(lineSplit[1]);
                		nbFaces = (int) Float.parseFloat(lineSplit[2]);
                		
                    	for ( int i = 0; i < nbVertex; i++){
                    		this.getDcel().addVertex(i);
                    	}
                    	
                    	for ( int i = 0; i < nbHalfEdge; i++){
                    		this.getDcel().addHalfEdge(i);
                    	}
                    	
                    	for ( int i = 0; i < nbFaces; i++){
                    		this.getDcel().addFaceList(i);
                    	}
                    	
                    	//System.out.println("nb Vertex in DCEL " + this.dcel.getVertexList().size());
                    	//System.out.println("nb hEdges in DCEL " + this.dcel.getHalfEdgeList().size());
                    	//System.out.println("nb  Faces in DCEL " + this.dcel.getFaceList().size());
                	}
                }
                
                // Second while loop, we set the vertex fields 
                while ((line = d.readLine()) != null) {
                	
                	lineSplit = line.split(" ");
                	
                	if ( lineSplit.length == 1){
                		d.readLine();
                		break;
                	}

                	id = (int) Float.parseFloat(lineSplit[0]);
                	xcoord = Float.parseFloat(lineSplit[1]);
                	ycoord = Float.parseFloat(lineSplit[2]);
                	incidentEdge = (int) Float.parseFloat(lineSplit[3]);
                	
                	Point2D p = new Point2D.Float(xcoord, ycoord);
                	
                	this.getDcel().getVertexList().get(id).setP(p);
                	this.getDcel().getVertexList().get(id).setHalfEdge(this.getDcel().getHalfEdgeList().get(incidentEdge));
                }
                
                // Second while loop, we set the half edges fields 
                while ((line = d.readLine()) != null) {
                	
                	lineSplit = line.split(" ");
                	
                	if ( lineSplit.length == 1){
                		d.readLine();
                		break;
                	}
                	
                	id = (int) Float.parseFloat(lineSplit[0]);
                	origin = (int) Float.parseFloat(lineSplit[1]);
                	twin = (int) Float.parseFloat(lineSplit[2]);
                	incidentFace = (int) Float.parseFloat(lineSplit[3]);
                	next = (int) Float.parseFloat(lineSplit[4]);
                	prev = (int) Float.parseFloat(lineSplit[5]);
                	
                	this.getDcel().getHalfEdgeList().get(id).setOrigin(this.getDcel().getVertexList().get(origin));
                	this.getDcel().getHalfEdgeList().get(id).setTwin(this.getDcel().getHalfEdgeList().get(twin));
                	this.getDcel().getHalfEdgeList().get(id).setFace(this.getDcel().getFaceList().get(incidentFace));
                	this.getDcel().getHalfEdgeList().get(id).setNext(this.getDcel().getHalfEdgeList().get(next));
                	this.getDcel().getHalfEdgeList().get(id).setPrev(this.getDcel().getHalfEdgeList().get(prev));
                }
                
                while ((line = d.readLine()) != null) {
                	
                	lineSplit = line.split(" ");
                	
                	if ( lineSplit.length == 1)
                		break;
                	
                	id = (int) Float.parseFloat(lineSplit[0]);
                	outer = (int) Float.parseFloat(lineSplit[1]);
                	
                	int lng = lineSplit.length;
                	
                	if ( outer != -1 ){
                    	this.getDcel().getFaceList().get(id).setOuterComponent(this.getDcel().getHalfEdgeList().get(outer));
                    	this.getDcel().getFaceList().get(id).analyseFace();
                	}
                	
                	for ( int i=2; i<lng; i++ ){
                		inner = (int) Float.parseFloat(lineSplit[i]);
                    	if ( inner != -1 ){
                    		this.getDcel().getFaceList().get(id).getInnerComponent().add(this.getDcel().getHalfEdgeList().get(inner));
                    	}
                	}
                }

            // dispose all the resources after using them.
            fis.close();
            bis.close();
            d.close();

          } catch (FileNotFoundException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
		}
		
		//this.dcel.printDCEL();
		//colorDCEL();
	}

	public void setDcel(DCEL dcel) {
		this.dcel = dcel;
	}

	public DCEL getDcel() {
		return dcel;
	}
}