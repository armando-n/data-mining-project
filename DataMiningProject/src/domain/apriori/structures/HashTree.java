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
    private int absoluteMinSupport;
    
    /** Initializes this with the given min support and items per itemset. **/
    public HashTree(int itemsPerItemSet, int absoluteMinSupport) {
        this.root = new Node(itemsPerItemSet, 0, false);
        this.numOfItemsPerItemSet = itemsPerItemSet;
        this.absoluteMinSupport = absoluteMinSupport;
    }
    
    public boolean isEmpty() {
        return root == null;
    }

    /** Adds the itemset to this HashTree. If the itemset was already present
     * in the tree, its frequency (occurrence count) will be updated. **/
    public void add(ItemSet itemSet) {
        this.root.add(itemSet);
    }
    
    public void pruneNoMinSupport() {
        throw new UnsupportedOperationException("HashTree.pruneNoMinSupport() not yet implemeneted");
    }
    
}
