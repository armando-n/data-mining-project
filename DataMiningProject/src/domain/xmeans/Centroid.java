package domain.xmeans;

import java.awt.Point;

public class Centroid {

	private int id;
	private Point location;
	
	private Point sum;
	private int n;
	
	public Centroid(double x, double y, int newId){
		location = new Point();
		location.setLocation(x, y);
		id = newId;
		sum = new Point();
		n = 0;
	}
	
	public Centroid(Point child1, int newId) {
		location = (Point)child1.clone();
		id = newId;
		sum = new Point();
		n = 0;
	}

	public void addPoint(ClusterPoint newPoint){
		double tmpX = 0, tmpY = 0;
		
		tmpX = newPoint.getX();
		tmpY = newPoint.getY();
		
		sum.setLocation(sum.getX()+tmpX, sum.getY()+tmpY);
		n++;
		
	}
	
	public void finish(){
		double tmpX = sum.getX();
		double tmpY = sum.getY();
		
		tmpX /= n;
		tmpY /= n;
		location.setLocation(tmpX, tmpY);
		
		n = 0;
		sum = new Point();
	}
	
	public Point center(){
		return location;
	}

	public int getID() {
		return id;
	}

	public void setID(int i) {
		id = i;
	}
	
	public String toString(){
		return "(" + location.getX() + ", " + location.getY() + ")";		
	}
}
