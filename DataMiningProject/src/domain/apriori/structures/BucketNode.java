package domain.apriori.structures;

import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

/*
 * A node with a bucket (a set of itemsets). If the bucket's size limit has not
 * been reached yet and an itemset is requested to be added to this node, the
 * itemset will be added to this node's bucket. However, if the bucket's size
 * limit has been reached, and there is at least one more item in the given
 * itemset to hash, then this node will be converted to a HashNode, and the
 * itemsets from this bucket node will be hashed on their next item and
 * placed into new BucketNodes that are children of this converted node.  If
 * there are no more items to hash in the itemset, then the itemset will be
 * added to this node's bucket despite the bucket size limit being breached.
 */
public class BucketNode extends Node {
    
    /** Initializes an empty bucket **/
    public BucketNode(int itemsPerItemSet, int level) {
        super(itemsPerItemSet, level);
        this.bucket = new HashSet<ItemSet>();
    }

    @Override
    public void add(ItemSet itemSet) {
        
        // the passed itemset is the final combination; simply add it to this bucket
        if (this.level == this.itemSetTargetSize)
            this.bucket.add(itemSet);
        
        else if (this.bucket.size() < Node.maxBucketSize) {
            // find all final combinations and add them to this bucket (I think)
            // while the bucket size limit has not been reached
            // TODO need while loop or something...gotta add it to this node's bucket, not child node
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
    
//    /** The hash function used on the next unchosen item in an itemset in order
//     * to determine which child node to pass the itemset to. **/
//    private int hash(Item item) {
//        return item.getIdForHash() % this.children.length;
//    }

}
