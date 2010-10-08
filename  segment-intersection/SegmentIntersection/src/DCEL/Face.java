package DCEL;

import java.util.ArrayList;

public class Face {
	private int id;
	private HalfEdge innerComponent;
	private ArrayList<HalfEdge> outerComponent;
	private int isOuter;
	

	public Face(HalfEdge inner, int id){
		this(id);
		this.setInnerComponent(inner);
	}
	
	public Face(int id){
		this.setId(id);
		this.setIsOuter(0);
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

	public void setIsOuter(int isOuter) {
		this.isOuter = isOuter;
	}

	public int getIsOuter() {
		return isOuter;
	}
	
}
