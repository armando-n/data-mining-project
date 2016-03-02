package domain.apriori.structures;

/*
 * A node with a null value. Itemsets requested to be added to this node will
 * be hashed and passed down to the appropriate child node.
 */
public class HashNode extends Node {
    
    /** Initializes this as a node with a null value **/
    public HashNode() {
        this.bucket = null;
    }

    @Override
    public void add(Item item) {
        throw new UnsupportedOperationException("HashNode.add(Item) not yet implemeneted");
    }
    
    private int hash(Item item) {
        throw new UnsupportedOperationException("HashNode.hash(Item) not yet implemeneted");
    }

}
