package domain.apriori;

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
    private Node root;
    private int numOfItemsPerItemSet; // this is k for a k-itemset hash tree
    private int absoluteMinimumSupport;
    private int maxBucketSize;
    private int numOfChildren;
    private String depth = ""; // helps with recursive printTree(Node) method
    
    /** Initializes this with the given min support and items per itemset. **/
    public HashTree(int itemsPerItemSet, int absoluteMinSupport, int maxBucketSize, int numOfChildren) {
        this.numOfItemsPerItemSet = itemsPerItemSet;
        this.absoluteMinimumSupport = absoluteMinSupport;
        this.maxBucketSize = maxBucketSize;
        this.numOfChildren = numOfChildren;
        this.root = new Node(itemsPerItemSet, 0, absoluteMinSupport, maxBucketSize, numOfChildren);
    }
    
    /** @return k for this k-itemset hash tree. **/
    public int getNumberOfItemsPerItemSet() {
        return numOfItemsPerItemSet;
    }
    
    /** @return True if there is only a root (hash) node. False otherwise. **/
    public boolean isEmpty() {
        return !this.root.hasChildren();
    }

    /** Adds the itemset to this HashTree. If the itemset was already present
     * in the tree, its frequency (occurrence count) will be updated. Otherwise, its
     * frequency is set to 0. **/
    public void add(ItemSet itemSet) {
        if (itemSet.size() >= this.numOfItemsPerItemSet)
            this.root.add(itemSet);
    }
    
    /** Adds all itemsets to this HashTree. If the itemsets were already present
     * in the tree, their frequencies (occurrence counts) will be updated. Otherwise, their
     * frequencies are set to 0. **/
    public void addAll(Set<ItemSet> itemSets) {
        for (ItemSet itemSet : itemSets)
            add(itemSet);
    }
    
    /** Finds all candidate itemsets in the given transactions and increases their frequency counts in this hash tree. **/
    public void countCandidates(Set<ItemSet> transactions) {
        for (ItemSet transaction : transactions)
            if (transaction.size() >= this.numOfItemsPerItemSet)
                this.root.countCandidates(transaction);
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
        if (previousTree.getNumberOfItemsPerItemSet() != this.numOfItemsPerItemSet-1) {
            String str = "To prune the hash tree, it must be given the hash tree for frequent (k-1)-itemsets.\n";
            str += "previous tree k: " + previousTree.getNumberOfItemsPerItemSet() + "\n";
            str += "current tree k: " + this.numOfItemsPerItemSet + "\n";
            throw new IllegalArgumentException(str);
        }
        
        this.root.prune(previousTree);
    }
    
    /** @return True if all itemsets of length this.numOfItemsPerItemSet in the given transaction
     * are present in this hash tree. False otherwise. **/
    public boolean areAllSubsetsFrequent(ItemSet transaction) {
        return this.root.areAllSubsetsPresent(transaction);
    }
    
    /** @return A new hash tree of (k+1)-itemsets ((this.numOfItemsPerItemSet+1)-itemsets) generated
     * by a self-join of the itemsets in this hash tree. **/
    public HashTree generateNextCandidateTree() {
        List<ItemSet> itemSets = this.toArray();
        HashTree resultTree = new HashTree(this.numOfItemsPerItemSet+1, this.absoluteMinimumSupport, this.maxBucketSize, this.numOfChildren);
        
        for (int i = 0; i < itemSets.size()-1; i++) {
            
            // check if the current itemset is joinable with any of the other itemsets, and join them if they are
            for (int j = i+1; j < itemSets.size(); j++) {
                boolean joinableItemSets = true;
                Item[] firstItemArray = itemSets.get(i).toArray(new Item[itemSets.get(i).size()]);
                Item[] secondItemArray = itemSets.get(j).toArray(new Item[itemSets.get(j).size()]);
                
                // all items before the last items in each item array must be identical for them to be joinable
                if (firstItemArray.length > 1) {
                    for (int k = 0; k < firstItemArray.length-1; k++) {
                        if (firstItemArray[k] != secondItemArray[k]) {
                            joinableItemSets = false;
                            break; // these two items should not be joined
                        }
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
    public List<ItemSet> toArray() {
        List<ItemSet> result = new ArrayList<ItemSet>();
        this.root.toArray(result);
        return result;
    }
    
    /** Increments the frequency count of all itemsets in this hash tree by 1 **/
    public void incFrequencies() {
        this.root.incFrequencies();
    }
    
    @Override
    public String toString() {
        return printTree(this.root);
    }
    
    /** Helper for toString() method **/
    private String printTree(Node node) {
        String result = "";
        
        if (node.hasBucket()) {
            for (ItemSet itemSet : node.getBucket())
                result += " " + itemSet.toString();
        } else
            result += " #";
        result += "\n";
        
        Node[] children = node.getChildren();
        Node next = null;
        
        for (int i = 0; i < children.length; i++) {
            if (children[i] != null) {
                next = (i < children.length-1) ? children[i+1] : null;
                result += depth + " `--";
                depth += " " + ((next == null) ? " " : "|") + "  ";
                result += printTree(children[i]);
                depth = depth.substring(0, (depth.length()-4 > 0) ? depth.length()-4 : 0);
            }
        }
        
        return result;
    }
    
}
