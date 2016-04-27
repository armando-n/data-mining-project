/**
 * This is Xmeans. It takes in a dataset with points in either float or int form, but must be the same form on each line 
 * like 8 8 and 3.2 4.2
 * The xmeans keeps adding a center as long as the BIC of the children is better than the BIC
 * of the parent. 
 */

package domain.xmeans;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class MeanLauncher {

	//The entire dataset in points
	private static ArrayList<ClusterPoint> allPoints;
	//The centroids for the entire dataset
	private static ArrayList<Centroid> centers;
	//The clusters made up of centroid related points
	private static ArrayList<SubCluster> clusters;
	
	//indicator for k mean
	private static boolean changed = true;
	
	//verbose flag
	private static boolean verbose = false;
	
	//For randomly adding centroids, the max for x and y to set
	private static double maxX = 0.0, maxY = 0.0;
	private static File in;
	private static FileOutputStream myOut;
	
	/**
	 * This is the main driver for xmeans
	 * @param dataSet The Data set to run xMeans on
	 * @param kLow the lower bound for K
	 * @param kHigh the upperbound for K
	 */
	public static void runXMeans(String dataSet, int kLow, int kHigh, boolean toVerbose, String outputName){
		clusters = new ArrayList<SubCluster>();
		centers	= new ArrayList<Centroid>();
		allPoints = new ArrayList<ClusterPoint>();
		verbose = toVerbose;
		
		
			try {
				if(outputName != null && !outputName.equals(""))
					myOut = new FileOutputStream(outputName);
				else
					myOut = null;
				
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		if(kHigh - kLow < 0)
			throw new IllegalArgumentException();
		
		in = new File(dataSet);
		int currK = kLow, actualK = kLow;
		
		try{
			addDataFromSet(dataSet);
		}catch(InputMismatchException e){
			System.out.println("Dataset invalid");
			return;
		}
		randomlyAddCentroids(kLow);
		
				
		do{
			kMeans(currK);				
			createClusters(currK);
			if(!addCentroid()){
				actualK = currK;
				currK = kHigh+1;
			}else{
				currK++;
				changed = true;
				clusters = new ArrayList<SubCluster>();
			}
		}
		while(currK <= kHigh);
		
		printResult(actualK);
	}	
	

	
	private static void printResult(int actualK) {
		int count = 0;
		
		if(myOut!= null)
			System.setOut(new PrintStream(myOut));
		
		if(verbose){
			for(SubCluster sb : clusters){
				Centroid c = centers.get(count);
				System.out.println("Cluster " + c.getID()  + " has the points");
				for(int i = 0; i < sb.getPoints().size() && i < 10; i++){
					System.out.println(sb.getPoints().get(i).getX() + " " + sb.getPoints().get(i).getX());					
				}
				
				System.out.println("...");
				
				count++;
			}
			System.out.println("");
		}
		System.out.println("The dataset has an estimated K of " + actualK + ".\n");
		System.out.println("With the estimated centroids: ");
		for(Centroid c : centers){
			System.out.println("\t" + c);
		}
		
		
	}

	
	/**
	 * This calculates the bic
	 */
	private static double bic(SubCluster[] sub) {
		
		double num_points, num_dims, logL, freeParam, bic;
		
		if(sub[1] == null){
			num_points = sub[0].size();
			num_dims = 2;
			logL = logLikely(sub, num_points);
			
			//freeparam is simmilar to degrees of freedom
			freeParam = 1 * (num_dims + 1);
			bic = logL - ((freeParam / 2.0) * Math.log(num_points));
			
		}else{
			num_points = sub[0].size() + sub[1].size();
			num_dims = 2;
			logL = logLikely(sub, num_points);
			freeParam = 2 * (num_dims + 1);
			bic = logL - ((freeParam / 2.0) * Math.log(num_points));
		}
		
		
		return bic;

			
		
	}

	/**
	 * Compute the log likely hood
	 */
	private static double logLikely(SubCluster[] sub, double num_points) {
		double total = 0.0, subTotal = 0, bigR, variance;
		
		for(SubCluster sc : sub){
			if(sc != null){
				bigR = sc.size();
				subTotal = (bigR*Math.log(bigR)) - (bigR*Math.log(sc.size()));
				variance = sc.var();
				subTotal -= ((bigR * 2) / 2) * Math.log(2*Math.PI*variance);
				subTotal -=((bigR-1)/2);	
				
				total += subTotal;					
			}
		}
		
		return total;
	}


	private static boolean addCentroid() {
		boolean toRet = false;
		
		for(SubCluster sub : clusters){
			toRet = split(sub) || toRet;
		}

		if(toRet){
			commitRemoveAdd();
		}
		
		return toRet;
	}

	private static boolean split(SubCluster sub) {
		double bicParent = bic(new SubCluster[] {sub, null});
		
		double vecX = Math.random();
		double vecY = Math.random();
		SubCluster[] children;
		
		vecX *= sub.getSplitDistance();
		vecY *= sub.getSplitDistance();
		
		Point parentPoint = sub.myCentroid.center();
		Point child1 = new Point(), child2 = new Point();
		
		child1.setLocation(parentPoint.getX()+vecX , parentPoint.getY()+vecY);
		child2.setLocation(parentPoint.getX()-vecX , parentPoint.getY()-vecY);
		
		SubKMeanWorker subWork = new SubKMeanWorker(sub, parentPoint, child1, child2);
		children = subWork.runKMeans();

		double bicChildren = bic(new SubCluster[] {children[0], children[1]});
		
		
		if(bicParent <  bicChildren)
			removeAddInit((bicChildren - bicParent), clusters.indexOf(sub), child1, child2);
		
		
		return (bicParent <  bicChildren);
				
}

	private static Point newChild1, newChild2;
	private static double newDiff = 0;
	private static int pIndex;
	
	private static void removeAddInit(double diff, int indexOf, Point child1, Point child2) {		

		if(diff > newDiff){			
			newDiff = diff;
			pIndex = indexOf;
			newChild1 = child1;
			newChild2 = child2;
		}
		
	}

	private static void commitRemoveAdd() {
		int offset = 0;
		for(int i = 0; i < centers.size(); i++){
			if(i == pIndex){
				centers.set(i, new Centroid(newChild1, i));
				centers.add(i+1, new Centroid(newChild2, i+1));
				offset++;
			}else{
				centers.get(i).setID(centers.get(i).getID() + offset);
			}
		}
		
	}

	private static void kMeans(int k) {

		while(changed){			
			fixPoints(k);
			computeCentroids();	
		}
		
	}

	private static void createClusters(int size) {
		for(Centroid c : centers){
			clusters.add(new SubCluster(c));
		}
		
		//The clusters are put in order of id so just add to the position of cluster
		for(ClusterPoint p: allPoints){
			clusters.get(p.getCluster()).addPoint(p);
		}
		
	}

	private static void randomlyAddCentroids(int k) {

		Random ran = new Random();
		
		for(int i = 0; i < k; i++){
			double x = ran.nextInt((int)maxX);
			double y = ran.nextInt((int)maxY);
			
			centers.add(new Centroid((int)x, (int)y, i));
			
		}
		
		
	}

	private static void fixPoints(int kVal) {
		
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

	private static void computeCentroids() {
		
		int cluster = 0;
		
		for(ClusterPoint cp: allPoints){
			cluster = cp.getCluster();
			centers.get(cluster).addPoint(cp);
		}
		
		for(Centroid c: centers)
			c.finish();
		
		
	}

	public static void parseData(String line) throws InputMismatchException{
		
		int x = 0, y = 0;
		double dx = 0, dy = 0;
		Scanner lineData = new Scanner(line);
		ClusterPoint point;
		
		if(lineData.hasNextInt()){
			x = lineData.nextInt();
			y = lineData.nextInt();
			point = new ClusterPoint(x, y, 0);			
		}else{
			dx = lineData.nextDouble();
			dy = lineData.nextDouble();
			point = new ClusterPoint(dx, dy, 0);
		}
		
		if(x > maxX)
			maxX = x;
		
		if(y > maxY)
			maxY = x;
		
		if(dx > maxX)
			maxX = dx;
		
		if(dy > maxY)
			maxY = dy;
		
		allPoints.add(point);		
		lineData.close();
	}
	
	@SuppressWarnings("resource")
	private static void addDataFromSet(String dataSet) throws InputMismatchException{
		
		Scanner data = new Scanner(System.in);
		
		try {
			data = new Scanner(in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		while(data.hasNextLine()){
			parseData((String)data.nextLine());
		}
		
	}


	
}




