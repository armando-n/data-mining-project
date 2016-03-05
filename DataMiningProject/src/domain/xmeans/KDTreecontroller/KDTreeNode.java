package domain.xmeans.KDTreecontroller;

import java.awt.Point;

public class KDTreeNode {

	private Point value;
	private KDTreeNode leftNode;
	private KDTreeNode rightNode;
	
	public KDTreeNode(){
		value = new Point(0,0);
		leftNode = null;
		rightNode = null;
	}
	
	
	public KDTreeNode(int x, int y){
		value = new Point(x, y);
		leftNode = null;
		rightNode = null;
	}
	
	public int getXValue(){
		return (int) value.getX();
	}
	
	public int getYValue(){
		return (int) value.getY();
	}
	
	public void setLeft(KDTreeNode to_set){
		leftNode = to_set;
	}
	
	public KDTreeNode getLeft(){
		return leftNode;
	}
	
	public void setRight(KDTreeNode r_to_set){
		rightNode = r_to_set;
	}
	
	public KDTreeNode getRight(){
		return rightNode;
	}
	
	public String toString(){
		return "(" + (int)value.getX() + "," + (int)value.getY() + ")";
		
	}
	
}
