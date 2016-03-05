package domain.apriori.structures;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/*
 * This abstract class represents a node in the hash tree. Its bucket (value)
 * will be null if it is a hash node. Otherwise, it will be a set of itemsets.
 */
public abstract class Node {
    private static int numOfChildren = 3; // the number of children per node
    protected static int maxBucketSize = 3; // the number of itemsets allowed per bucket (unless there are no more items to hash in the itemset being added)
    
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
//    public abstract void add(ItemSet itemSet);
    
    public void add(ItemSet itemSet) {
        if (hasBucket())
            bucketNode_add(itemSet);
        else
            hashNode_add(itemSet);
    }
    
    private void hashNode_add(ItemSet itemSet) {
        SortedSet<Item> chosenItems = new TreeSet<Item>(); // the already chosen set of items that will be part of the final itemsets
        Item[] itemSetArray = (Item[])itemSet.toArray(); // using an indexed array makes this method much simpler
        int leftToPick = this.itemSetTargetSize - this.level; // the number of items left to pick for the final itemsets
        int maxPickableIndex = itemSetArray.length - leftToPick;
        
        for (int i = 0; i <= maxPickableIndex; i++) {
            
            // collect the already-chosen items into a set
            if (i < this.level)
                chosenItems.add(itemSetArray[i]);
            
            // the current item is an unchosen item
            else {
                
                // hash the current unchosen item to determine which child node to pass the new itemset on to
                int hashResult = hash(itemSetArray[i]);
                
                // create new itemset from a combination of chosen items, the current item, and all items after it
                ItemSet newCombination = new ItemSet(chosenItems);
                newCombination.add(itemSetArray[i]);
                for (int j = i; j < itemSetArray.length; j++)
                    newCombination.add(itemSetArray[j]);
                
                // now pass it down
                if (this.children[hashResult] == null)
                    this.children[hashResult] = new BucketNode(this.itemSetTargetSize, this.level+1);
                this.children[hashResult].add(newCombination);
            }
        }
    }

    private void bucketNode_add(ItemSet itemSet) {
        // the passed itemset is the final combination; simply add it to this bucket
        if (this.level == this.itemSetTargetSize)
            this.bucket.add(itemSet);
        
        else if (this.bucket.size() < Node.maxBucketSize) {
            // find all final combinations and add them to this bucket (I think)
            // TODO while the bucket size limit has not been reached
            // TODO need while loop or something...gotta add it to this node's bucket, not child node
            // TODO I think what I'm a do is, have the loop collect the chosen items and newCombination itemsets, but not pass to children
            // TODO instead, that will be done after the loop, but not before checking to make sure the bucket can hold all the new itemsets
            // --------------------------------- COPY-PASTED FROM HASHNODE: CANNABALIZE IT ---------------------------------------- \\
            SortedSet<Item> chosenItems = new TreeSet<Item>(); // the already chosen set of items that will be part of the final itemsets
            Item[] itemSetArray = (Item[])itemSet.toArray(); // using an indexed array makes this method much simpler
            int leftToPick = this.itemSetTargetSize - this.level; // the number of items left to pick for the final itemsets
            int maxPickableIndex = itemSetArray.length - leftToPick;
            
            for (int i = 0; i <= maxPickableIndex; i++) {
                
                // collect the already-chosen items into a set
                if (i < this.level)
                    chosenItems.add(itemSetArray[i]);
                
                // the bucket size limit has been reached
                else if (this.bucket.size() == Node.maxBucketSize)
                    break;
                
                // the current item is an unchosen item
                else {
                    
                    // hash the current unchosen item to determine which child node to pass the new itemset on to
                    int hashResult = hash(itemSetArray[i]);
                    
                    // create new itemset from a combination of chosen items, the current item, and all items after it
                    ItemSet newCombination = new ItemSet(chosenItems);
                    newCombination.add(itemSetArray[i]);
                    for (int j = i; j < itemSetArray.length; j++)
                        newCombination.add(itemSetArray[j]);
                    
                    // now pass it down
                    if (this.children[hashResult] == null)
                        this.children[hashResult] = new BucketNode(this.itemSetTargetSize, this.level+1);
                    this.children[hashResult].add(newCombination);
                }
            }
            
         // --------------------------------- END: COPY-PASTED FROM HASHNODE: CANNABALIZE IT ---------------------------------------- \\
        }
        
        else {
            // we are at the bucket size limit; keep track of the current itemsets
            // in this bucket, hash them on the next item (as well as the passed itemset),
            // convert this node to a hash node (hm how to do that), and put the itemsets
            // in newly created child nodes
        }
        
        throw new UnsupportedOperationException("BucketNode.add(ItemSet) not yet implemeneted");
    }

    /** The hash function used on the next unchosen item in an itemset in order
     * to determine which child node to pass the itemset to. **/
    protected int hash(Item item) {
        return item.getIdForHash() % this.children.length;
    }

}
