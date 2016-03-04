package domain.apriori.structures;

import java.util.HashSet;

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
        throw new UnsupportedOperationException("BucketNode.add(ItemSet) not yet implemeneted");
    }

}
