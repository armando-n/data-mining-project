package domain.apriori.structures;

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

}
