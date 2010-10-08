package DCEL;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import components.DrawArea;



public class DCEL {
	private ArrayList<Vertex> vertexList;
	private ArrayList<HalfEdge> halfEdgeList;
	private ArrayList<Face> faceList;
	private DrawArea drawArea;
	
	public DCEL(DrawArea drawArea){
		this.setVertexList(new ArrayList<Vertex>());
		this.setHalfEdgeList(new ArrayList<HalfEdge>());
		this.setFaceList(new ArrayList<Face>());
		this.drawArea = drawArea;
	}

	public void setVertexList(ArrayList<Vertex> vertexList) {
		this.vertexList = vertexList;
	}

	public ArrayList<Vertex> getVertexList() {
		return vertexList;
	}
	
	
	public void addVertex(Point2D p){
		
		int nbVertex = this.vertexList.size();

		for(int i=0; i< nbVertex; i++){
			if ( this.vertexList.get(i).getP().distance(p) == 0 ){
				return;
			}
		}
	
		Vertex v = new Vertex(p, vertexList.size());
		vertexList.add(v);
	}
	
	public Vertex pointToVertex(Point2D p){
		int nbVertex = this.vertexList.size();
				
		for(int i=0; i< nbVertex; i++){
			if ( this.vertexList.get(i).pointIsVertex(p) ){
				return this.vertexList.get(i);
			}
		}
		return this.vertexList.get(0);
	}
	
	public void addHalfEdge(HalfEdge halfEdge){
		this.halfEdgeList.add(halfEdge);
	}

	public void setHalfEdgeList(ArrayList<HalfEdge> halfEdgeList) {
		this.halfEdgeList = halfEdgeList;
	}

	public ArrayList<HalfEdge> getHalfEdgeList() {
		return halfEdgeList;
	}
	
	public void addFaceList(Face face){
		this.faceList.add(face);
	}

	public void setFaceList(ArrayList<Face> faceList) {
		this.faceList = faceList;
	}

	public ArrayList<Face> getFaceList() {
		return faceList;
	}
	
	public void printDCEL(){
		int nbFaces = this.getFaceList().size();
		int nbHalfEdges = this.getHalfEdgeList().size();
		int nbVertex = this.getVertexList().size();

		Face faceTmp;
		HalfEdge heTmp;
		Vertex vTmp;
		
		
		//Print Vertex List
		System.out.println("\nVERTEX");
		
		for(int j=0; j<nbVertex; j++){
			vTmp = this.getVertexList().get(j);
			System.out.println("\nid: "+vTmp.getId());
			System.out.println("coordinates: "+vTmp.getP().getX()+" "+vTmp.getP().getY());
			System.out.println("incidentEdge: "+vTmp.getHalfEdge().getId());
		}
		
		//Print HalfEdge List
		System.out.println("\nHALF EDGES");
		
		for(int j=0; j<nbHalfEdges; j++){
			heTmp = this.getHalfEdgeList().get(j);
			System.out.println("\nid: "+heTmp.getId());
			System.out.println("origin: "+heTmp.getOrigin().getId());
			System.out.println("twin: "+heTmp.getTwin().getId());
			System.out.println("incidentFace: "+heTmp.getFace().getId());
			System.out.println("next: "+heTmp.getNext().getId());
			System.out.println("prev: "+heTmp.getPrev().getId());
		}
		
		//Print FaceList
		System.out.println("\nFACES");
		
		for(int j=0; j<nbFaces; j++){
			faceTmp = this.getFaceList().get(j);
			faceTmp.setId(j);
		}
		
		for(int j=0; j<nbFaces; j++){
			faceTmp = this.getFaceList().get(j);
			
			System.out.println("\nid:"+faceTmp.getId());
			if ( faceTmp.getOuterComponent() == null ){
				System.out.println("outerComponent: none");
			}
			else{
				System.out.println("outerComponent: "+faceTmp.getOuterComponent().getId());
			}
			
			ArrayList<HalfEdge> HalfEdges2 = faceTmp.getInnerComponent();
			int nbHE = HalfEdges2.size();
			
			if ( nbHE == 0 ){
				System.out.println("innerComponent: none");
			}
			
			for(int i=0; i<nbHE; i++){
				System.out.println("innerComponent: "+faceTmp.getInnerComponent().get(i).getId());
			}
		}
	}
	
	public void colorDCEL(ArrayList<Face> faceList){
		
		ArrayList<Face> newFaces = new ArrayList<Face>();
		newFaces = (ArrayList<Face>) faceList.clone();

		int nbFaces, index;
		nbFaces = newFaces.size(); 
		newFaces.remove(nbFaces-1);
		
		Face faceTmp;
	
		while ( newFaces.size() > 0 ){
		
			faceTmp = newFaces.get(0);
			index = 0;
			nbFaces = newFaces.size(); 
			
			for(int j=0; j<nbFaces; j++){
				if ( newFaces.get(j).getOuterHalfEdge().getOrigin().getP().getX() < faceTmp.getOuterHalfEdge().getOrigin().getP().getX() ){
					faceTmp = newFaces.get(j);
					index = j;
				}
			}

			colorDCEL(faceTmp);
			newFaces.remove(index);
		}

		return;
	}
	
	public void colorDCEL(Face face){
		
		Point2D pTmp;
		HalfEdge heTmp;
		HalfEdge h0;
		ArrayList<int[]> points = new ArrayList<int[]>();
		h0 = face.getOuterComponent();
		heTmp = h0;

		
		do{
			pTmp = heTmp.getOrigin().getP();
			int[] point = new int[] { (int) pTmp.getX(), (int) pTmp.getY() };
			points.add(point);
			heTmp = heTmp.getNext();
		}
		while ( !(h0.equals(heTmp)) );
		
		
				
		drawArea.drawPolygon(points);
		return;
	}



	public void setDrawArea(DrawArea drawArea) {
		this.drawArea = drawArea;
	}

	public DrawArea getDrawArea() {
		return drawArea;
	}
}
