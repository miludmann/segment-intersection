package DCEL;

import java.awt.geom.Point2D;

public class Vertex {
	private Point2D p;
	//private Edges edges;

	public Vertex(Point2D p){
		this.setP(p);
	}

	public void setP(Point2D p) {
		this.p = p;
	}

	public Point2D getP() {
		return p;
	}
	
}
