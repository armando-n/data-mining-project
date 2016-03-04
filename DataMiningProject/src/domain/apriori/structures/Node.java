package domain.apriori.structures;

import java.util.Set;

/*
 * This abstract class represents a node in the hash tree. Its bucket (value)
 * will be null if it is a hash node. Otherwise, it will be a set of itemsets.
 */
public abstract class Node {
    private static int numOfChildren = 3; // the number of children per node
    protected static int bucketSize = 3; // the number of itemsets allowed per bucket (unless there are no more items to hash in the itemset being added)
    
    protected int itemSetTargetSize;
    protected int level;
    protected Set<ItemSet> bucket; // either a set of itemsets, or null
    protected Node[] children;

    /** Initializes this node's children to null.
     * @param itemsPerItemSet This is k for a k-item set HashTree (which stores only k-item sets).
     * @param level This Node's level must be specified. (The root is at level 0)**/
    public Node(int itemsPerItemSet, int level) {
        this.children = new Node[numOfChildren];
        this.itemSetTargetSize = itemsPerItemSet;
        this.level = level;
    }
    
    /** @return This node's bucket (set of itemsets), or null if this node has no bucket. **/
    public Set<ItemSet> getBucket() {
        return bucket;
    }
    
    /** @return True if this node has a bucket, or false if this node's value is null **/
    public boolean hasBucket() {
        return bucket != null;
    }
    
    /** Implemented in concrete child classes. Behavior is dependent on the type of node. **/
    public abstract void add(ItemSet itemSet);

}
