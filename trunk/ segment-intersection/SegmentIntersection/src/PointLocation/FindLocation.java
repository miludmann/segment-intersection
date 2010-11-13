package PointLocation;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import DCEL.DCEL;
import DCEL.Face;
import DCEL.HalfEdge;
import DCEL.Vertex;

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
		
		dcel.getDrawArea().setDcel(dcel);
		dcel.getDrawArea().setFl(this);
		
		//System.out.println("Nb Segments: " + segmentList.size());
	}
	
	
	public void locateFace(int x, int y) {
		
		int nbFaces;
		Vertex v = new Vertex(new Point2D.Float(x,y), 0);
		double dist, distTmp, distMes;
		int nbcross;
		Face f, fres;
		HalfEdge h0, heTmp;
		
		fres = null;
		dist = 0;
		nbFaces = dcel.getFaceList().size();
		
		for( int i=0; i<(nbFaces-1); i++ ){
			f = dcel.getFaceList().get(i);
			h0 = heTmp = f.getOuterComponent();
			nbcross = 0;
			distTmp = 0;
			
			do {
				if ( v.crossHorizontal(heTmp) ){
					distMes = v.horizontalDistance(heTmp);
					
					if ( distMes > 0 ){
						if ( nbcross == 0){
							distTmp = distMes;
						}
						else{
							if ( distTmp > distMes )
								distTmp = distMes;
						}
						nbcross++;
					}
				}
			}
			while( !(heTmp = heTmp.getNext()).equals(h0) );
			
			if ( nbcross % 2 == 1 ){
				if ( fres == null ){
					fres = f;
					dist = distTmp;
				}
				else{
					if ( dist > distTmp ){
						fres = f;
						dist = distTmp;
					}
				}
			}
		}
		
		dcel.getDrawArea().getSceneGraphArea().removePolygons();
		if ( !(fres == null) ){
			dcel.colorDCEL(fres);
		}
		else{
			dcel.getDrawArea().redrawAll();
		}
	}
	
	public static void delay (int n){
        long t0,t1;
        t0=System.currentTimeMillis();
        do{
            t1=System.currentTimeMillis();
        }
        while (t1-t0<1000);
}
}
