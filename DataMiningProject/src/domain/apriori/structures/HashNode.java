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
        SortedSet<Item> chosenItems = new TreeSet<Item>(); // the number of items in the set chosen to be a part of the final itemsets
        Item[] itemSetArray = (Item[])itemSet.toArray(); // using an indexed array makes this method much simpler
        int leftToPick = this.itemSetTargetSize - this.level; // number of items left to pick for the final itemsets
        
        for (int i = 0; i < itemSetArray.length; i++) {
            
            // collect the already-chosen items into a set
            if (i < level)
                chosenItems.add(itemSetArray[i]);
            
            // the current item is an unchosen item. take it, all (unchosen) items after it, and combine with chosen items, then pass it down
            else if (i <= itemSetArray.length - leftToPick) {
                
                // hash the current unchosen item to determine which child node to pass the new itemset on to
                int hashResult = hash(itemSetArray[i]);
                
                // create new itemset by choosing the current item and keeping all unchosen items after the current item
                ItemSet newCombination = new ItemSet(chosenItems);
                newCombination.add(itemSetArray[i]);
                for (int j = i; j < itemSetArray.length; j++)
                    newCombination.add(itemSetArray[j]);
                
                // now pass it down
                if (children[hashResult] == null)
                    children[hashResult] = new BucketNode(this.itemSetTargetSize, this.level+1);
                children[hashResult].add(newCombination);
                
            }
        }
    }
    
    /** The hash function used on the next unchosen item in an itemset in order
     * to determine which child node to pass the itemset to. **/
    private int hash(Item item) {
        return item.getIdForHash() % this.children.length;
    }

}
