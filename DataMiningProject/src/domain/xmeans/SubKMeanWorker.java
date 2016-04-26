package domain.xmeans;

import java.awt.Point;
import java.util.ArrayList;

public class SubKMeanWorker {
	
	private boolean changed = true;
	private  ArrayList<ClusterPoint> allPoints;
	private ArrayList<Centroid> centers;
	private ArrayList<SubCluster> clusters;
	
	public SubKMeanWorker(SubCluster mySub, Point parent, Point child1, Point child2){
		allPoints = mySub.getPoints();
		centers = new ArrayList<Centroid>();
		centers.add(new Centroid(child1, 0));
		centers.add(new Centroid(child2, 1));
		
		clusters = new ArrayList<SubCluster>();
	}
	
	
	public SubCluster[] runKMeans() {

		while(changed){			
			fixPoints(2);
			computeCentroids();	
		}
		
		createClusters(2);
		return new SubCluster[] {clusters.get(0), clusters.get(1)};
		
	}
	
	
	private void fixPoints(int kVal) {
		
		Double[] distance = new Double[kVal];
		Double min = Double.MAX_VALUE;
		int center = 0, changes = 0;
		
		for(int i = 0; i < allPoints.size(); i++){
			
			for(int j = 0; j < kVal; j++){

				distance[j] = Math.pow((centers.get(j).center().getX() 
						- allPoints.get(i).getX()), 2);
				
				distance[j] += Math.pow((centers.get(j).center().getY() 
						- allPoints.get(i).getY()), 2);
				
				distance[j] = Math.sqrt(distance[j]);
			}
		
			for(int j = 0; j < kVal; j++){
				if(distance[j] < min){
					min = distance[j];
					center = j;
				}
			}
			
			if(allPoints.get(i).getCluster() != center){
				changes++;
			}
			
			
			allPoints.get(i).setCluster(center);						
						
			center = 0;
			min = Double.MAX_VALUE;
			
		}
		
		if(changes > 0)
			changed = true;
		else
			changed = false;
		changes = 0;
		
		
	}

	private void computeCentroids() {
		
		int cluster = 0;
		
		for(ClusterPoint cp: allPoints){
			cluster = cp.getCluster();
			centers.get(cluster).addPoint(cp);
		}
		
		for(Centroid c: centers)
			c.finish();
		
		
	}
	
	private void createClusters(int size) {
		for(Centroid c : centers){
			clusters.add(new SubCluster(c));
		}
		
		//The clusters are put in order of id so just add to the position of cluster
		for(ClusterPoint p: allPoints){
			clusters.get(p.getCluster()).addPoint(p);
		}
		
	}

}
