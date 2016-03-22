package domain.apriori;

/*
 * Represents an item with an Integer ID.
 * Comparisons to other items assumes both IDs are Integers.
 */
public class IntegerItem extends Item {

    public IntegerItem(Integer id) {
        super(id, Item.ID_TYPE_INTEGER);
    }
    
    @Override
    public Integer getIdForHash() {
        return (Integer)id;
    }
    
    @Override
    public int compareTo(Item integerItem) {
        int result = -1;
        try {
            result = ((Integer)id).compareTo((Integer)integerItem.getID());
        } catch (ClassCastException e) {
            System.err.println("Error: both integer and string inputs found. \nMake sure you specify a delimiter if the default delimiter does not apply to your input.");
            System.exit(1);
        }
        return result;
    }
    
    @Override
    public String toString() {
        return ((Integer)id).toString();
    }
    
    @Override
    public boolean equals(Object item) {
        if (!(item instanceof IntegerItem))
            throw new IllegalArgumentException("IntegerItem.equals: invalid item object");
        IntegerItem intItem = (IntegerItem)item;
        Integer itemID = (Integer)intItem.getID();
        
        return ((Integer)this.id).equals(itemID);
    }
    
    @Override
    public int hashCode() {
        return ((Integer)this.id).hashCode();
    }

}
