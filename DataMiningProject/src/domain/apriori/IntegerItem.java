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
        return ((Integer)id).compareTo((Integer)integerItem.getID());
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
