package domain.xmeans;

public class ClusterPoint {

	private double x;
	private double y;
	private int myCluster;
	
	public ClusterPoint(int newX, int newY, int newCluster) {
		
		x = newX;
		y = newY;
		myCluster = newCluster;
		
	}
	
	public ClusterPoint(double newX, double newY, int newCluster) {
		
		x = newX;
		y = newY;
		myCluster = newCluster;
		
	}

	public double getX() {
		return x;
		
	}

	public double getY() {
		return y;
		
	}

	public int getCluster() {
		return myCluster;
	}

	public void setCluster(int center) {
		myCluster = center;
		
	}

	
}
