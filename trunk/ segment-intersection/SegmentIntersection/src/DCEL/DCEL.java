package DCEL;

import java.awt.geom.Point2D;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JFileChooser;

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
	
	public void addVertex(int id){
		Vertex v = new Vertex(id);
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
	
	public void addHalfEdge(int id){
		HalfEdge he = new HalfEdge(id);
		this.halfEdgeList.add(he);
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
	
	public void addFaceList(int id){
		Face f = new Face(id);
		
		if ( id == 0 )
			f.setIsOuter(true);
		
		this.faceList.add(f);
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
	
	public void saveDCEL(){
		JFileChooser fc = new JFileChooser();
		
		int saveStatus = fc.showSaveDialog(null);
		
		if ( saveStatus == JFileChooser.APPROVE_OPTION ){
            File savedFile = fc.getSelectedFile();
            try {
				savedFile.createNewFile();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    try {

		        BufferedWriter out = new BufferedWriter(new FileWriter(savedFile.toString()));
		        
		        int nbFaces = this.getFaceList().size();
				int nbHalfEdges = this.getHalfEdgeList().size();
				int nbVertex = this.getVertexList().size();

				Face faceTmp;
				HalfEdge heTmp;
				Vertex vTmp;
				
				//Print Infos
				out.write("INFOS: (nb Vertex, nb Half-Edges, nb Faces)\n");
				out.write(this.getVertexList().size() + " ");
				out.write(this.getHalfEdgeList().size() + " ");
				out.write(this.getFaceList().size() + "\n");
				
				//Print Vertex List
				out.write("\nVERTEX: (id, xCoords, yCoords, incidentEdge)\n");
				
				for(int j=0; j<nbVertex; j++){
					vTmp = this.getVertexList().get(j);
					out.write(vTmp.getId() + " ");
					out.write(vTmp.getP().getX() + " ");
					out.write(vTmp.getP().getY() + " ");
					out.write(vTmp.getHalfEdge().getId() + "\n");
				}
				
				//Print HalfEdge List
				out.write("\nHALF EDGES: (id, origin, twin, incidentFace, next, prev)\n");
				

				for(int j=0; j<nbHalfEdges; j++){
					heTmp = this.getHalfEdgeList().get(j);
					out.write(heTmp.getId() + " ");
					out.write(heTmp.getOrigin().getId() + " ");
					out.write(heTmp.getTwin().getId() + " ");
					out.write(heTmp.getFace().getId() + " ");
					out.write(heTmp.getNext().getId() + " ");
					out.write(heTmp.getPrev().getId() + "\n");
				}
				
				//Print FaceList
				out.write("\nFACES: (id, outerComponent, innerComponent(s))\n");

				for(int j=0; j<nbFaces; j++){
					faceTmp = this.getFaceList().get(j);
					
					out.write(faceTmp.getId() + " ");
					if ( faceTmp.getOuterComponent() == null ){
						out.write("-1 ");
					}
					else{
						out.write(faceTmp.getOuterComponent().getId() + " ");
					}
					
					ArrayList<HalfEdge> HalfEdges2 = faceTmp.getInnerComponent();
					int nbHE = HalfEdges2.size();
					
					if ( nbHE == 0 ){
						out.write("-1");
					}
					
					for(int i=0; i<nbHE; i++){
						out.write(faceTmp.getInnerComponent().get(i).getId() + " ");
					}
					
					out.write("\n");
				}
		        
		        out.close();

		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		}
	}
	
	public void openDCEL(){

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
		
		Point2D pTmp, pTmp2;
		HalfEdge heTmp;
		HalfEdge h0;
		ArrayList<HalfEdge> innerComp;
		ArrayList<int[]> points = new ArrayList<int[]>();
		h0 = face.getOuterComponent();
		heTmp = h0;

		// we add the main points of the polygon
		do{
			pTmp = heTmp.getOrigin().getP();
			int[] point = new int[] { (int) pTmp.getX(), (int) pTmp.getY() };
			points.add(point);
			heTmp = heTmp.getNext();
		}
		while ( !(h0.equals(heTmp)) );
		
		// and from here we "substract" the innerComponents
		pTmp2 = heTmp.getOrigin().getP();
		innerComp = face.getInnerComponent();
		
		for ( int i = 0; i < innerComp.size(); i++ ){
			int[] point = new int[] { (int) pTmp2.getX(), (int) pTmp2.getY() };
			points.add(point);
			
			heTmp = h0 = innerComp.get(i);
			
			do{
				pTmp = heTmp.getOrigin().getP();
				int[] pointIn = new int[] { (int) pTmp.getX(), (int) pTmp.getY() };
				points.add(pointIn);
				heTmp = heTmp.getNext();
			}
			while ( !(h0.equals(heTmp)) );
			
			pTmp = heTmp.getOrigin().getP();
			int[] pointIn = new int[] { (int) pTmp.getX(), (int) pTmp.getY() };
			points.add(pointIn);
		}
		
		
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
