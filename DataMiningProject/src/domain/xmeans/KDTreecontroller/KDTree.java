package domain.xmeans.KDTreecontroller;

import java.util.ArrayList;


/**
 * This class is a kd tree with a 2d splitting based off of pre-selected values 5 and 2 for depth 0 and 1 respectively 
 * @author John
 *
 */
public class KDTree {

	
	//The pre-selected split points
	int[] splits = {5,2};
	
	//The head of the binary tree
	private KDTreeNode head;
	
	/**
	 * The constructor with list of nodes to add
	 * @param nodeList List to add
	 */
	public KDTree(ArrayList<KDTreeNode> nodeList) {

		KDTreeNode tmp;
		int depth = 0;
		head = nodeList.get(0);
		
		for(int i = 1; i < nodeList.size(); i++){
			tmp = nodeList.get(i);			
			addToTree(head, tmp, depth);
		}
		
	}
 
	/**
	 * This is a recursive method to add to trees
	 * @param currNode the current node when going down the tree
	 * @param toAdd the node we are trying to add
	 * @param depth the current depth of the currNode node
	 */
	private void addToTree(KDTreeNode currNode, KDTreeNode toAdd, int depth) {
		
//		System.out.println("Adding " + toAdd + " depth: " + depth);
		if(depth%2 == 0){
			//if the depth is 0 or even we split on the y axis
			if(toAdd.getYValue() > splits[depth]){
				//if the adding node value is greater than the currnode go right
				
				if(currNode.getRight() == null){
					//if there is no node to the right then set it
					currNode.setRight(toAdd);
				}else{
					//else continue on to the next node and split
					addToTree(currNode.getRight(), toAdd, depth+1);
				}
				
			}else{
				
				//else if it is less we work with the left side of the tree
				if(currNode.getLeft() == null){
					currNode.setLeft(toAdd);
				}else{

					addToTree(currNode.getLeft(), toAdd, depth+1);
				}
					
			}
		}else{
			//if the depth is odd we split on the x axis
			if(toAdd.getXValue() > splits[depth]){
				if(currNode.getRight() == null){
					currNode.setRight(toAdd);
				}else{
					addToTree(currNode.getRight(), toAdd, depth+1);
				}
			}else{
				if(currNode.getLeft() == null){
					currNode.setLeft(toAdd);
				}else{
					addToTree(currNode.getLeft(), toAdd, depth+1);
				}
			}
		}
	}

	/**
	 * helper of toString
	 * @param printing the current node to print
	 * @return string to be used by toString
	 */
	private String makeString(KDTreeNode printing){
		if(printing.getLeft() == null || printing.getRight() == null){
			return "\n" + printing.toString() + " left: " + printing.getLeft() + " right: " + printing.getRight();
		}else {
			return printing.toString() + " left: " + printing.getLeft() + " right: " + printing.getRight() 
			+ makeString(printing.getLeft()) +  makeString(printing.getRight());
		}
	}
	public String toString(){
		return makeString(head);
	}
}
