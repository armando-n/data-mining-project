package domain.apriori.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 * This class is not correct or finished yet ...
 */
public class HashTree<T extends Comparable<? super T>> {
    private static Map<ItemID, Node> hash = new HashMap<ItemID, Node>();
    
    private Node root;
    private int count;

    public HashTree(Node node) {
        if (node == null)
            throw new IllegalArgumentException("Cannot create a HashTree from a null node.");
        
        this.root = node;
        this.count = 1;
    }
    
    public HashTree(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Cannot create a HashTree from a null element.");
        
        this.root = new BucketNode(item);
        this.count = 1;
    }
    
    public HashTree() {
        this.root = null;
        this.count = 0;
    }
    
    public HashTree(Set<ItemID> identifiers) {
        ArrayList<ItemID> ids = new ArrayList<ItemID>(identifiers);
        Collections.sort(ids);
        
        Node.setIdentifiers(ids);
    }
    
    public boolean isEmpty() {
        return count == 0;
    }

    public void add(Item item) {
        
    }
    
}
