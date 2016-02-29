package domain.apriori.structures;

/*
 * Represents an item ID in String form.
 * Comparisons to other IDs assumes both IDs are Strings.
 */
public class StringItemID extends ItemID {

    public StringItemID(String id) {
        super(id, ItemID.ID_TYPE_STRING);
    }

    @Override
    public int compareTo(ItemID stringID) {
        return ((String)id).compareTo((String)stringID.getID());
    }
    
    @Override
    public String toString() {
        return (String)id;
    }

}
