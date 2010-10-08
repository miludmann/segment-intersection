package DCEL;

import java.util.ArrayList;

public class Face {
	private int id;
	private HalfEdge innerComponent;
	private ArrayList<HalfEdge> outerComponent;
	private boolean isOuter;
	private Face outerFace;
	private HalfEdge outerHalfEdge;
	

	public Face(HalfEdge inner, int id){
		this(id);
		this.setInnerComponent(inner);
	}
	
	public Face(int id){
		this.setId(id);
		this.setIsOuter(false);
	}

	public void setId(int id) {
		this.id = id;
	}


	public int getId() {
		return id;
	}


	public void setInnerComponent(HalfEdge innerComponent) {
		this.innerComponent = innerComponent;
	}


	public HalfEdge getInnerComponent() {
		return innerComponent;
	}


	public void setOuterComponent(ArrayList<HalfEdge> outerComponent) {
		this.outerComponent = outerComponent;
	}


	public ArrayList<HalfEdge> getOuterComponent() {
		return outerComponent;
	}

	public void setIsOuter(boolean isOuter) {
		this.isOuter = isOuter;
	}

	public boolean getIsOuter() {
		return isOuter;
	}

	public void setOuterFace(Face outerFace) {
		this.outerFace = outerFace;
	}

	public Face getOuterFace() {
		return outerFace;
	}

	public void setOuterHalfEdge(HalfEdge outerHalfEdge) {
		this.outerHalfEdge = outerHalfEdge;
	}

	public HalfEdge getOuterHalfEdge() {
		return outerHalfEdge;
	}
	
	public void analyseFace(){
		Vertex v;
		HalfEdge he, heTmp;
		double angle;
		
		heTmp = this.innerComponent.getNext();
		he = this.innerComponent;
		v = he.getOrigin();
		
		while ( !(heTmp.equals(this.innerComponent)) ){
			if ( v.getP().getX() > heTmp.getOrigin().getP().getX() ){
				he = heTmp;
				v = he.getOrigin();
			}
			heTmp = heTmp.getNext();
		}
		
		angle = v.getAngle(he.getNext().getOrigin(), he.getPrev().getOrigin());
		
		if ( angle == 0 || angle > 3.141592646952213 ){
			this.setIsOuter(true);
			this.setOuterHalfEdge(he);
		}
	}

}
