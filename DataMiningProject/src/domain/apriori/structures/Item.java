package domain.apriori.structures;

/*
 * Represents an item along with its counted frequency (occurrences).
 */
public class Item implements Comparable<Item> {
    
    private ItemID id;
    private int frequency;

    public Item(Integer id) {
        this.id = new IntegerItemID(id);
    }
    public Item(String id) {
        this.id = new StringItemID(id);
    }

    public ItemID getID() {
        return id;
    }
    
    public int getFrequency() {
        return frequency;
    }
    
    @Override
    public int compareTo(Item o) {
        return id.compareTo(o.getID());
    }

}
