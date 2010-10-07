package DCEL;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class DCEL {
	private ArrayList<Vertex> vertexList;
	
	public DCEL(){
		this.setVertexList(new ArrayList<Vertex>());
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
	
		Vertex v = new Vertex(p);
		vertexList.add(v);
		
	}

}
