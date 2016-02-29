package domain.apriori.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * This class is not correct or finished yet ...
 */
public abstract class Node {
    private static Map<ItemID, Integer> hash = new HashMap<ItemID, Integer>();
    private static Integer LEFT = 0;
    private static Integer MIDDLE = 1;
    private static Integer RIGHT = 2;
    
    private Item item;
    private Node left;
    private Node middle;
    private Node right;
    
    public Node() {
        this.item = null;
        this.left = null;
        this.middle = null;
        this.right = null;
    }

    public Node(Item item) {
        this.item = item;
        this.left = null;
        this.middle = null;
        this.right = null;
    }
    
    public static void addLeftHash(ItemID identifer) {
        hash.put(identifer, LEFT);
    }
    
    public static void addMiddleHash(ItemID identifer) {
        hash.put(identifer, MIDDLE);
    }
    
    public static void addRightHash(ItemID identifer) {
        hash.put(identifer, RIGHT);
    }

    public static void setIdentifiers(ArrayList<ItemID> ids) {
        
        for (int i = 0; i < ids.size(); i++) {
            if (i % 3 == 0)
                hash.put(ids.get(i), LEFT);
            else if (i % 3 == 1)
                hash.put(ids.get(i), MIDDLE);
            else if (i % 3 == 2)
                hash.put(ids.get(i), RIGHT);
            else
                System.err.println("Bad");
        }
    }
    
    public Item getItem() {
        return item;
    }
    
    public void setItem(Item newItem) {
        this.item = newItem;
    }
    
    public void setLeft(Item element) {
        throw new UnsupportedOperationException("Node.setLeft not yet implemented");
    }
    
    public void setMiddle(Item element) {
        throw new UnsupportedOperationException("Node.setMiddle not yet implemented");
    }
    
    public void setRight(Item element) {
        throw new UnsupportedOperationException("Node.setRight not yet implemented");
    }

}
