package domain.apriori.structures;

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
    
    /** Initializes this with the given min support and items per itemset. **/
    public HashTree(int itemsPerItemSet, int absoluteMinSupport) {
        this.numOfItemsPerItemSet = itemsPerItemSet;
        this.root = new Node(itemsPerItemSet, 0, absoluteMinSupport);
    }
    
    public int getNumberOfItemsPerItemSet() {
        return numOfItemsPerItemSet;
    }
    
    public boolean isEmpty() {
        return root == null;
    }

    /** Adds the itemset to this HashTree. If the itemset was already present
     * in the tree, its frequency (occurrence count) will be updated. **/
    public void add(ItemSet itemSet) {
        if (itemSet.size() >= this.numOfItemsPerItemSet)
            this.root.add(itemSet);
    }
    
    /** Finds all candidate itemsets in transaction and increases their frequency counts. **/
    public void countCandidates(ItemSet transaction) {
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
        if (previousTree.getNumberOfItemsPerItemSet() != this.numOfItemsPerItemSet-1)
            throw new IllegalArgumentException("To prune the hash tree, it must be given the hash tree for frequent (k-1)-itemsets");
        
        this.root.prune(previousTree);
    }
    
    /** @return True if all itemsets of length this.numOfItemsPerItemSet in the given transaction
     * are present in this hash tree. False otherwise. **/
    public boolean areAllSubsetsFrequent(ItemSet transaction) {
        return this.root.areAllSubsetsPresent(transaction);
    }
    
}
