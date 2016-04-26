package domain.xmeans;

import java.util.ArrayList;

public class SubCluster {
	
	ArrayList<ClusterPoint> myPoints;
	Centroid myCentroid;
	double sumX, sumY, maxDistance = 0.0;
	
	public SubCluster(Centroid c){
		myPoints = new ArrayList<ClusterPoint>();
		myCentroid = c;
		sumX = 0.0;
		sumY = 0.0;
	}
	
	public void addPoint(ClusterPoint p){
		sumX += p.getX();
		sumY += p.getY();
		myPoints.add(p);
	}

	public int size() {
		return myPoints.size();
	}

	public double var() {
		double avgX = sumX/myPoints.size();
		double avgY = sumY/myPoints.size();
		double distance = 0.0;
		
		for(ClusterPoint p : myPoints){
			distance = Math.pow((avgX
					- p.getX()), 2);
			
			distance += Math.pow((avgY 
					- p.getY()), 2);
			
			distance = Math.sqrt(distance);
			
			if(distance > maxDistance)
				maxDistance = distance;
		}
		
		return (distance / myPoints.size());
	}

	public double getSplitDistance() {
		return maxDistance;
		
	}
	
	public ArrayList<ClusterPoint> getPoints(){
		return myPoints;
	}
	

}
