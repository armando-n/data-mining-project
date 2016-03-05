package domain.xmeans.KDTreecontroller;

import java.util.ArrayList;

public class KdLauncher {

	public static void main(String[] args){
		
		ArrayList<KDTreeNode> nodeList = new ArrayList<KDTreeNode>();
		
		KDTreeNode tmp;
		
		//Add tree nodes to fit example
		tmp = new KDTreeNode(2,5);
		nodeList.add(tmp);
		tmp = new KDTreeNode(3,8);
		nodeList.add(tmp);
		tmp = new KDTreeNode(6,3);
		nodeList.add(tmp);
		tmp = new KDTreeNode(8,9);
		nodeList.add(tmp);
		
		//make the tree with the tree nodes
		KDTree tree = new KDTree(nodeList);
		
		System.out.print(tree);
		
		
	}
}
