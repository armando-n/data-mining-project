package domain.apriori.structures;

import java.util.Set;

/*
 * This abstract class represents a node in the hash tree. Its bucket (value)
 * will be null if it is a hash node. Otherwise, it will be a set of itemsets.
 */
public abstract class Node {
    private static Integer LEFT = 0;
    private static Integer MIDDLE = 1;
    private static Integer RIGHT = 2;
    private static int bucketSize = 3; // the number of itemsets allowed per bucket (unless there are no more items to hash in the itemset being added)
    
    protected Set<Set<Item>> bucket; // either a set of itemsets (a bucket), or null
    private Node left;
    private Node middle;
    private Node right;

    /** Sets this node's children to null **/
    public Node() {
        this.left = null;
        this.middle = null;
        this.right = null;
    }
    
    /** @return This node's bucket (set of itemsets), or null if this node has no bucket. **/
    public Set<Set<Item>> getBucket() {
        return bucket;
    }
    
    /** @return True if this node has a bucket, or false if this node's value is null **/
    public boolean hasBucket() {
        return bucket != null;
    }
    
    /** Implemented in concrete child classes. Behavior is dependent on the type of node. **/
    public abstract void add(Item item);

}
