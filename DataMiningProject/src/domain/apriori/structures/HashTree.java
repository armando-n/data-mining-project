package domain.apriori.structures;

/*
 * This class represents a hash tree in which the interior nodes are hash
 * nodes, which have a null value, and the leaf nodes are bucket nodes,
 * with a set of itemsets as value. Itemsets are assumed to be
 * self-ordering sets (e.g. a TreeSet).
 */
public class HashTree {
    
    private Node root;
    private int count;
    private int absoluteMinSupport;
    private int level;
    
    /** Initializes this with the given min support and level. **/
    public HashTree(int absoluteMinSupport, int level) {
        this.root = null;
        this.count = 0;
        this.absoluteMinSupport = absoluteMinSupport;
        this.level = level;
    }
    
    /** Initializes this with its level set to 1 and with the given min support. **/
    public HashTree(int absoluteMinSupport) {
        this(absoluteMinSupport, 1);
    }
    
    public boolean isEmpty() {
        return count == 0;
    }

    public void add(ItemSet itemSet) {
        throw new UnsupportedOperationException("HashTree.add(ItemSet) not yet implemeneted");
    }
    
    public void pruneNoMinSupport() {
        throw new UnsupportedOperationException("HashTree.pruneNoMinSupport() not yet implemeneted");
    }
    
}
