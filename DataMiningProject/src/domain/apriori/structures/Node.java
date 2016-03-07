package domain.apriori.structures;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/*
 * This class represents a node in the hash tree. Its bucket (value)
 * will be null if it is a hash node. Otherwise, it will be a set of itemsets.
 * 
 * If this is a hash node, itemsets requested to be added to this node will
 * be hashed and passed down to the appropriate child node.
 * 
 * If this is a bucket node: if the bucket's size limit has not
 * been reached yet and an itemset is requested to be added to this node, the
 * itemset will be added to this node's bucket. However, if the bucket's size
 * limit has been reached, and there is at least one more item in the given
 * itemset to hash, then this node will be converted to a HashNode, and the
 * itemsets from this bucket node will be hashed on their next item and
 * placed into new BucketNodes that are children of this converted node.  If
 * there are no more items to hash in the itemset, then the itemset will be
 * added to this node's bucket despite the bucket size limit being breached.
 */
public class Node {
    private static int numOfChildren = 3; // the number of children per node
    private static int maxBucketSize = 3; // the number of itemsets allowed per bucket (unless there are no more items to hash in the itemset being added)
    
    private int itemSetTargetSize;
    private int level;
    private Set<ItemSet> bucket; // either a set of itemsets, or null
    private Node[] children;

    /** Initializes this node's children to null.
     * @param itemsPerItemSet This is k for a k-item set HashTree (which stores only k-item sets).
     * @param level This Node's level must be specified. (The root is at level 0)
     * @param isBucketNode Indicates whether to set this node up as a bucket node (true) or a hash node (false). **/
    public Node(int itemsPerItemSet, int level, boolean isBucketNode) {
        this.children = new Node[numOfChildren];
        this.itemSetTargetSize = itemsPerItemSet;
        this.level = level;
        this.bucket = isBucketNode ? new HashSet<ItemSet>() : null;
    }
    
    /** @return This node's bucket (set of itemsets), or null if this node has no bucket. **/
    public Set<ItemSet> getBucket() {
        return bucket;
    }
    
    /** @return True if this node has a bucket, or false if this node's value is null **/
    public boolean hasBucket() {
        return bucket != null;
    }
    
    /** Behavior is dependent on the type of node. **/
    public void add(ItemSet itemSet) {
        if (hasBucket())
            bucketNode_add(itemSet);
        else
            hashNode_add(itemSet);
    }
    
    private void hashNode_add(ItemSet itemSet) {
        passToChildren( generateNewItemSets(itemSet) );
    }

    // TODO what if the itemset already exists in the bucket? its frequency must be increased
    private void bucketNode_add(ItemSet itemSet) {
        
        // the passed itemset is the final combination; simply add it to this bucket
        if (this.level == this.itemSetTargetSize)
            this.bucket.add(itemSet);
        
        // we are not at the final level
        else {
            
            // generate new itemsets by choosing each possible next item
            Set<ItemSet> newItemSets = generateNewItemSets(itemSet);
            
            // the new itemsets will fit into this node's bucket, so just add them
            if (this.bucket.size() + newItemSets.size() <= Node.maxBucketSize)
                this.bucket.addAll(newItemSets);   
                
            // itemsets won't fit, and we can go another level deeper, so create child bucket nodes and pass items to them
            else {
                newItemSets.addAll(this.bucket); // collect the existing itemsets in this bucket as well
                passToChildren(newItemSets); // split up the itemsets into the next level
                this.bucket = null; // make this node a hash node
            }
        }
    }
    
    private Set<ItemSet> generateNewItemSets(ItemSet itemSet) {
        Set<ItemSet> newItemSets = new HashSet<ItemSet>();
        SortedSet<Item> chosenItems = new TreeSet<Item>(); // the already chosen set of items that will be part of the final itemsets
        Item[] itemArray = (Item[])itemSet.toArray(); // using an indexed array makes this method much simpler
        int leftToPick = this.itemSetTargetSize - this.level; // the number of items left to pick for the final itemsets
        int maxPickableIndex = itemArray.length - leftToPick;
        
        // create and collect new combinations of itemsets by choosing all possible next items
        for (int i = 0; i <= maxPickableIndex; i++) {
            
            // collect the already-chosen items into a set
            if (i < this.level)
                chosenItems.add(itemArray[i]);
            
            // the current item is an unchosen item
            else {
                
                // create new itemset from a combination of chosen items, the current item, and all items after it
                ItemSet newCombination = new ItemSet(chosenItems);
                newCombination.add(itemArray[i]);
                for (int j = i; j < itemArray.length; j++)
                    newCombination.add(itemArray[j]);
                
                // add it to the set of newCombination itemsets
                newCombination.incFrequency();
                newItemSets.add(newCombination);
            }
        }
        
        return newItemSets;
    }

    private void passToChildren(Set<ItemSet> itemSets) {
        Item[] itemArray;
        for (ItemSet currItemSet : itemSets) {
            
            // hash the current unchosen item to determine which child node to pass the new itemset on to
            itemArray = (Item[])currItemSet.toArray();
            int hashResult = hash(itemArray[level]);
            
            // now pass it down
            if (this.children[hashResult] == null)
                this.children[hashResult] = new Node(this.itemSetTargetSize, this.level+1, true);
            this.children[hashResult].add(currItemSet);
        }
    }
    
    /** The hash function used on the next unchosen item in an itemset in order
     * to determine which child node to pass the itemset to. **/
    private int hash(Item item) {
        return item.getIdForHash() % this.children.length;
    }
}
