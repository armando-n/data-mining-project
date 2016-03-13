package domain.apriori.structures;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.math3.util.CombinatoricsUtils;

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
    private int level; // the level of this node in the hash tree (root is at level 0)
    private Map<ItemSet, ItemSet> bucket; // contains itemsets (bucket node), or is null (hash node)
    private Node[] children;

    /** Initializes this node's children to null.
     * @param itemsPerItemSet This is k for a k-item set HashTree (which stores only k-item sets).
     * @param level This Node's level must be specified. (The root is at level 0)
     * @param isBucketNode Indicates whether to set this node up as a bucket node (true) or a hash node (false). **/
    public Node(int itemsPerItemSet, int level, boolean isBucketNode) {
        this.children = new Node[numOfChildren];
        this.itemSetTargetSize = itemsPerItemSet;
        this.level = level;
        this.bucket = isBucketNode ? new HashMap<ItemSet, ItemSet>() : null;
    }
    
    /** @return This node's bucket (set of itemsets), or null if this node has no bucket. **/
    public Collection<ItemSet> getBucket() {
        return bucket.values();
    }
    
    /** @return True if this node has a bucket, indicating this node is a bucket node. False if this node's value
     * is null, indicating this node is a hash node. **/
    public boolean hasBucket() {
        return bucket != null;
    }
    
    /** Add or pass on all itemsets of length this.itemSetTargetSize contained in the given itemSet **/
    public void add(ItemSet itemSet) {
        add(new ItemSet(), itemSet);
    }
    
    /** Add or pass on all itemsets of length this.itemSetTargetSize that can be made from the chosen and remaining items **/
    public void add(ItemSet chosenItems, ItemSet remainingItems) {
        if (hasBucket())
            bucketNode_add(chosenItems, remainingItems);
        else
            chooseNextAndPass(chosenItems, remainingItems);
    }

    /** Either adds all itemsets of length this.itemSetTargetSize that can be made from the chosen and remaining items,
     * or converts this node into a hash node and passes everything on if the max bucket size would have been breached. **/
    private void bucketNode_add(ItemSet chosenItems, ItemSet remainingItems) {
        // there are no more items to choose; simply put it in this bucket
        if (chosenItems.size() == this.itemSetTargetSize) {
            putInBucket(chosenItems);
            return;
        }
        
        long numOfItemSetsLeft = CombinatoricsUtils.binomialCoefficient(remainingItems.size(), this.itemSetTargetSize - chosenItems.size());
        
        // the new itemsets will fit into this node's bucket; generate them and put them in the bucket
        if (this.bucket.size() + numOfItemSetsLeft <= Node.maxBucketSize)
            putInBucket(generateFinalItemSets(chosenItems, remainingItems));
            
        // itemsets won't fit, and we can go another level deeper
        else {
            chooseNextAndPass(chosenItems, remainingItems); // choose all possible next items, and pass it on to child nodes
            passBucketItemSets(); // pass all itemsets already in bucket and make this a hash node 
        }
    }
    
    /** Generate new itemsets by choosing each possible next item, and pass them to the appropriate children **/
    private void chooseNextAndPass(ItemSet chosenItems, ItemSet remainingItems) {
        int numItemsLeftToPick = this.itemSetTargetSize - chosenItems.size();
        int maxPickableIndex = remainingItems.size() - numItemsLeftToPick;
        Iterator<Item> remainingIter = remainingItems.iterator();
        
        for (int i = 0; i <= maxPickableIndex; i++) {
            Item nextItemChoice = remainingIter.next();
            
            ItemSet newRemaining = new ItemSet(remainingItems.tailSet(nextItemChoice, false));
            ItemSet newChosen = new ItemSet(chosenItems);
            newChosen.add(nextItemChoice);
            
            // hash the current item to determine which child node to pass the new itemset on to
            int hashResult = hash(nextItemChoice);
            
            // now pass it down
            if (this.children[hashResult] == null)
                this.children[hashResult] = new Node(this.itemSetTargetSize, this.level+1, true);
            this.children[hashResult].add(newChosen, newRemaining);
        }
    }
    
    /** Take all itemsets in the bucket, hash on their appropriate items, and pass them down to child nodes.
     * Also converts this node into a hash node. **/
    private void passBucketItemSets() {
        Item[] itemArray;
        int hashResult;
        for (ItemSet itemSet : this.bucket.values()) {
            // hash on appropriate item
            itemArray = (Item[])itemSet.toArray();
            hashResult = hash(itemArray[this.level]);
            
            // pass itemset down
            if (this.children[hashResult] == null)
                this.children[hashResult] = new Node(this.itemSetTargetSize, this.level+1, true);
            this.children[hashResult].add(itemSet, null);
        }
        
        // since we are hashing and passing now, this is a hash node
        this.bucket = null;
    }
    
    /** Generate and return all itemsets of length this.itemSetTargetSize that can be made from the chosen and remaining items **/
    private Set<ItemSet> generateFinalItemSets(ItemSet chosenItems, ItemSet remainingItems) {
        Set<ItemSet> result = new HashSet<ItemSet>();
        if (chosenItems.size() == this.itemSetTargetSize) {
            result.add(chosenItems);
            return result;
        }
        
        int numItemsLeftToPick = this.itemSetTargetSize - chosenItems.size();
        int maxPickableIndex = remainingItems.size() - numItemsLeftToPick;
        Iterator<Item> remainingIter = remainingItems.iterator();
        
        // choose all possible next items recursively
        for (int i = 0; i <= maxPickableIndex; i++) {
            Item nextItemChoice = remainingIter.next();
            
            ItemSet newRemainingItems = new ItemSet(remainingItems.tailSet(nextItemChoice, false));
            ItemSet newChosenItems = new ItemSet(chosenItems);
            newChosenItems.add(nextItemChoice);
            
            result.addAll(generateFinalItemSets(newChosenItems, newRemainingItems));
        }
        
        return result;
    }
    
    /** The hash function used on the next unchosen item in an itemset in order
     * to determine which child node to pass the itemset to. **/
    private int hash(Item item) {
        return item.getIdForHash() % this.children.length;
    }
    
    /** Adds the itemset to the bucket, or increases its frequency count if it already exists **/
    private void putInBucket(ItemSet itemSet) {
        if (this.bucket.containsKey(itemSet))
            this.bucket.get(itemSet).incFrequency();
        else
            this.bucket.put(itemSet, itemSet);
    }
    /** Adds all itemsets to the bucket, or increases their frequency counts if they already exists **/
    private void putInBucket(Set<ItemSet> itemSets) {
        for (ItemSet itemSet : itemSets)
            putInBucket(itemSet);
    }
}
