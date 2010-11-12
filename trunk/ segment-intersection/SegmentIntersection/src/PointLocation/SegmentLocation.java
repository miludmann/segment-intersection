package PointLocation;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import DCEL.Face;

/**
 * <p>
 * <b>Segment</b>
 * </p>
 * <p>
 * 
 * </p>
 * @author Depoyant Guillaume & Ludmann Michaël
 *
 */
public class SegmentLocation {
	private int id;
	private Point2D p1;
	private Point2D p2;
	private Face upperFace;
	private Face lowerFace;

	
	public void setP1(Point2D p1) {
		this.p1 = p1;
	}
	public Point2D getP1() {
		return p1;
	}
	public void setP2(Point2D p2) {
		this.p2 = p2;
	}
	public Point2D getP2() {
		return p2;
	}
	public void setUpperFace(Face upperFace) {
		this.upperFace = upperFace;
	}
	public Face getUpperFace() {
		return upperFace;
	}
	public void setLowerFace(Face lowerFace) {
		this.lowerFace = lowerFace;
	}
	public Face getLowerFace() {
		return lowerFace;
	}
	
	public boolean isSame(SegmentLocation sl){
		if ( this.getP1().equals(sl.getP1()) && this.getP2().equals(sl.getP2()) 
				||  this.getP1().equals(sl.getP2()) && this.getP2().equals(sl.getP1()) )
			return true;
		
		return false;
	}
	
	public boolean isInList(ArrayList<SegmentLocation> segmentList){
		
		int len = segmentList.size();
		
		for ( int i = 0; i<len; i++){
			if ( this.isSame(segmentList.get(i)) )
				return true;
		}
		
		return false;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	
}
