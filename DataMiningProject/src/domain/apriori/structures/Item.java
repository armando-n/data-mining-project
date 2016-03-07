package domain.apriori.structures;

/*
 * This abstract class represents an item with an ID.
 * Used to form ItemSets.
 */
public abstract class Item implements Comparable<Item> {
    public static int ID_TYPE_INTEGER = 0;
    public static int ID_TYPE_STRING = 1;
    
    private int idType;
    protected Object id;
    
    public Item(Object id, int idType) {
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
    
    public abstract Integer getIdForHash();
    
    @Override
    public abstract int compareTo(Item o);
    
    @Override
    public abstract String toString();
    
    @Override
    public abstract boolean equals(Object o);

}
