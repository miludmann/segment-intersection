package PointLocation;

import java.util.ArrayList;

import DCEL.DCEL;
import DCEL.HalfEdge;

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
public class FindLocation {
	
	private DCEL dcel;
	private ArrayList<SegmentLocation> segmentList;
	

	public FindLocation(DCEL dcel2) {
		this.dcel = dcel2;
	}

	public void setSegmentList(ArrayList<SegmentLocation> segmentList) {
		this.segmentList = segmentList;
	}

	public ArrayList<SegmentLocation> getSegmentList() {
		return segmentList;
	}

	public void setDcel(DCEL dcel) {
		this.dcel = dcel;
	}

	public DCEL getDcel() {
		return dcel;
	}
	

	public void createSegmentList() {

		int nbFaces = dcel.getFaceList().size();
		segmentList = new ArrayList<SegmentLocation>();
		SegmentLocation sl;
		HalfEdge heTmp, heInit;

		// We look at all the faces
		for ( int i = 0; i < (nbFaces-1); i++){
			heInit = heTmp = dcel.getFaceList().get(i).getOuterComponent();
			
			// And try to store the segments from the half edges without storing doubles
			if ( heInit != null ){
				
				sl = new SegmentLocation();
				sl.setP1(heInit.getOrigin().getP());
				sl.setP2(heInit.getTwin().getOrigin().getP());
				
				if ( ! sl.isInList(segmentList) ){
					
					if ( sl.getP1().getX() >= sl.getP2().getX() ){
						sl.setLowerFace(heInit.getFace());
						sl.setUpperFace(heInit.getTwin().getFace());
					}
					else{
						sl.setLowerFace(heInit.getTwin().getFace());
						sl.setUpperFace(heInit.getFace());
					}
					
					sl.setId(segmentList.size());
					segmentList.add(sl);
					//System.out.println("seg info: " + sl.getUpperFace().getId() + " / " + sl.getLowerFace().getId());
				}
		
				
				while ( ! (heTmp = heTmp.getNext()).equals(heInit) ){
					
					sl = new SegmentLocation();
					sl.setP1(heTmp.getOrigin().getP());
					sl.setP2(heTmp.getTwin().getOrigin().getP());
					
					if ( ! sl.isInList(segmentList) ){
						if ( sl.getP1().getX() >= sl.getP2().getX() ){
							sl.setLowerFace(heTmp.getFace());
							sl.setUpperFace(heTmp.getTwin().getFace());
						}
						else{
							sl.setLowerFace(heTmp.getTwin().getFace());
							sl.setUpperFace(heTmp.getFace());
						}
						
						sl.setId(segmentList.size());
						segmentList.add(sl);
						//System.out.println("seg info: " + sl.getUpperFace().getId() + " / " + sl.getLowerFace().getId());
					}
				}
			}
		}
		//System.out.println("Nb Segments: " + segmentList.size());
	}
}
