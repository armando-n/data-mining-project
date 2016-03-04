package domain.apriori.structures;

/*
 * Represents an item with a String ID.
 * Comparisons to other items assumes both IDs are Strings.
 */
public class StringItem extends Item {

    public StringItem(String id) {
        super(id, Item.ID_TYPE_STRING);
    }
    
    @Override
    public Integer getIdForHash() {
        return ((String)id).hashCode();
    }

    @Override
    public int compareTo(Item stringItem) {
        return ((String)id).compareTo((String)stringItem.getID());
    }
    
    @Override
    public String toString() {
        return (String)id;
    }

}
