package domain.apriori;

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
        int result = -1;
        try {
            result = ((String)id).compareTo((String)stringItem.getID());
        } catch (ClassCastException e) {
            System.err.println("Error: both integer and string inputs found. Make sure you specify a delimiter if the default delimiter does not apply to your input.");
            System.exit(1);
        }
        return result;
    }
    
    @Override
    public String toString() {
        return (String)id;
    }
    
    @Override
    public boolean equals(Object item) {
        if (!(item instanceof StringItem))
            throw new IllegalArgumentException("StringItem.equals: invalid item object");
        StringItem strItem = (StringItem)item;
        String itemID = (String)strItem.getID();
        
        return ((String)this.id).equals(itemID);
    }
    
    @Override
    public int hashCode() {
        return ((String)this.id).hashCode();
    }

}
