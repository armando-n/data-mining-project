package domain.apriori.structures;

/*
 * This abstract class represents a generic item ID. 
 */
public abstract class ItemID implements Comparable<ItemID> {
    
    public static int ID_TYPE_INTEGER = 0;
    public static int ID_TYPE_STRING = 1;
    
    private int idType;
    protected Object id;

    public ItemID(Object id, int idType) {
        if (id == null)
            throw new IllegalArgumentException("Item ID cannot be null");
        
        if (idType == ID_TYPE_INTEGER) {
            if (!(id instanceof Integer))
                throw new IllegalArgumentException("Item ID type mismatch");
        }
        else if (idType == ID_TYPE_STRING) {
            if (!(id instanceof String))
                throw new IllegalArgumentException("Item ID type mismatch");
        }
        else
            throw new IllegalArgumentException("Invalid item ID type");
        
        this.id = id;
        this.idType = idType;
    }
    
    public Object getID() {
        return id;
    }
    
    public int getIdType() {
        return idType;
    }

    @Override
    public int compareTo(ItemID o) {
        throw new UnsupportedOperationException("ItemID.compareTo should be overwritten in child classes");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("ItemID.toString should be overwritten in child classes");
    }
    
}
