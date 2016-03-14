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
    private int numOfItemsPerItemSet;
    
    /** Initializes this with the given min support and items per itemset. **/
    public HashTree(int itemsPerItemSet, int absoluteMinSupport) {
        this.numOfItemsPerItemSet = itemsPerItemSet;
        this.root = new Node(itemsPerItemSet, 0, absoluteMinSupport);
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
    
    /** Removes all itemsets that have a frequency count lower than minimum support
     * @return True if removal results in an empty hash tree. False otherwise. **/
    public boolean removeNoMinSupport() {
        return this.root.removeNoMinSupport();
    }
    
    /** Finds all candidate itemsets in transaction and increases their frequency counts. **/
    public void countCandidates(ItemSet transaction) {
        if (transaction.size() >= this.numOfItemsPerItemSet)
            this.root.countCandidates(transaction);
    }
    
}
