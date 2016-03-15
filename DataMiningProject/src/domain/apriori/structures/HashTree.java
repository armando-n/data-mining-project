package domain.apriori.structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/*
 * This class represents a hash tree in which the interior nodes are hash
 * nodes, which have a null value, and the leaf nodes are bucket nodes,
 * with a set of itemsets as value. Itemsets are assumed to be
 * self-ordering sets (e.g. a TreeSet).
 */
public class HashTree {
    private static int numOfChildren = 3; // the number of children per node
    
    private Node root;
    private int numOfItemsPerItemSet; // this is k for a k-itemset hash tree
    private int absoluteMinimumSupport;
    
    /** Initializes this with the given min support and items per itemset. **/
    public HashTree(int itemsPerItemSet, int absoluteMinSupport) {
        this.numOfItemsPerItemSet = itemsPerItemSet;
        this.absoluteMinimumSupport = absoluteMinSupport;
        this.root = new Node(itemsPerItemSet, 0, absoluteMinSupport);
    }
    
    public int getNumberOfItemsPerItemSet() {
        return numOfItemsPerItemSet;
    }
    
    public boolean isEmpty() {
        return this.root.hasChildren();
    }

    /** Adds the itemset to this HashTree. If the itemset was already present
     * in the tree, its frequency (occurrence count) will be updated. Otherwise, its
     * frequency is set to 0. **/
    public void add(ItemSet itemSet) {
        if (itemSet.size() >= this.numOfItemsPerItemSet)
            this.root.add(itemSet);
    }
    
    public void addAll(Set<ItemSet> itemSets) {
        for (ItemSet itemSet : itemSets)
            add(itemSet);
    }
    
    /** Finds all candidate itemsets in transaction and increases their frequency counts in this hash tree. **/
    public void countCandidates(ItemSet transaction) {
        if (transaction.size() >= this.numOfItemsPerItemSet)
            this.root.countCandidates(transaction);
    }
    
    public void countCandidates(Set<ItemSet> transactions) {
        for (ItemSet transaction : transactions)
            countCandidates(transaction);
    }
    
    /** Removes all itemsets that have a frequency count lower than minimum support
     * @return True if removal results in an empty hash tree. False otherwise. **/
    public boolean removeNoMinSupport() {
        return this.root.removeNoMinSupport();
    }
    
    /** Using the hash tree generated previously for frequent (k-1)-itemsets, prune (remove) any k-itemsets
     * existing in this hash tree that contain subsets that are not present in the (k-1)-itemset tree, and
     * are therefore not frequent. **/
    public void prune(HashTree previousTree) {
        if (previousTree.getNumberOfItemsPerItemSet() != this.numOfItemsPerItemSet-1)
            throw new IllegalArgumentException("To prune the hash tree, it must be given the hash tree for frequent (k-1)-itemsets");
        
        this.root.prune(previousTree);
    }
    
    /** @return True if all itemsets of length this.numOfItemsPerItemSet in the given transaction
     * are present in this hash tree. False otherwise. **/
    public boolean areAllSubsetsFrequent(ItemSet transaction) {
        return this.root.areAllSubsetsPresent(transaction);
    }
    
    /** @return A new hash tree of (k+1)-itemsets ((this.numOfItemsPerItemSet-1)-itemsets) generated
     * by a self-join of the itemsets in this hash tree. **/
    public HashTree generateNextCandidateTree() {
        ItemSet[] itemSetArray = this.toArray();
        HashTree resultTree = new HashTree(this.numOfItemsPerItemSet+1, this.absoluteMinimumSupport);
        
        for (int i = 0; i < itemSetArray.length-1; i++) {
            
            // check if the current itemset is joinable with any of the other itemsets, and join them if they are
            for (int j = i+1; j < itemSetArray.length; j++) {
                boolean joinableItemSets = true;
                Item[] firstItemArray = (Item[])itemSetArray[i].toArray();
                Item[] secondItemArray = (Item[])itemSetArray[j].toArray();
                
                // all items before the last items in each item array must be identical for them to be joinable
                for (int k = 0; k < firstItemArray.length-1; k++) {
                    if (firstItemArray[k] != secondItemArray[k]) {
                        joinableItemSets = false;
                        break; // these two items should not be joined
                    }
                }
                
                // perform the join and add resulting itemset to the new tree
                if (joinableItemSets) {
                    ItemSet newItemSet = new ItemSet();
                    newItemSet.addAll(new ArrayList<Item>(Arrays.asList(firstItemArray)));
                    newItemSet.add(secondItemArray[secondItemArray.length-1]);
                    
                    resultTree.add(newItemSet);
                }
                
            }
        }
        
        return resultTree;
    }
    
    /** @return An ordered array of all itemsets contained in this hash tree. **/
    public ItemSet[] toArray() {
        List<ItemSet> result = new ArrayList<ItemSet>();
        
        this.root.toArray(result);
        
        return result.toArray(new ItemSet[result.size()]);
    }
    
    /** Increments the frequency count of all itemsets in this hash tree by 1 **/
    public void incFrequencies() {
        this.root.increaseFrequencies();
    }
    
}
