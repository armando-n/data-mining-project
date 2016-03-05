package domain.apriori.structures;

import java.util.SortedSet;
import java.util.TreeSet;

/*
 * A node with a null value. Itemsets requested to be added to this node will
 * be hashed and passed down to the appropriate child node.
 */
public class HashNode extends Node {
    
    /** Initializes this as a node with a null value **/
    public HashNode(int itemsPerItemSet, int level) {
        super(itemsPerItemSet, level);
        this.bucket = null;
    }

    @Override
    public void add(ItemSet itemSet) {
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
    
//    /** The hash function used on the next unchosen item in an itemset in order
//     * to determine which child node to pass the itemset to. **/
//    private int hash(Item item) {
//        return item.getIdForHash() % this.children.length;
//    }

}
