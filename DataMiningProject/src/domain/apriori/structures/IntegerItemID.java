package domain.apriori.structures;

/*
 * Represents an item ID in Integer form.
 * Comparisons to other IDs assumes both IDs are Integers.
 */
public class IntegerItemID extends ItemID {

    public IntegerItemID(Integer id) {
        super(id, ItemID.ID_TYPE_INTEGER);
    }
    
    @Override
    public int compareTo(ItemID integerID) {
        return ((Integer)id).compareTo((Integer)integerID.getID());
    }
    
    @Override
    public String toString() {
        return ((Integer)id).toString();
    }

}
