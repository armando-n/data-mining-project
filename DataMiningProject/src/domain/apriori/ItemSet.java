package domain.apriori;

import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("serial")
public class ItemSet extends TreeSet<Item> {
    
    private int frequency = 0; // the number of occurrences of this itemset (not the number of items in the set)
    
    public ItemSet() {
    }

    public ItemSet(SortedSet<Item> itemSet) {
        super(itemSet);
    }

    public void incFrequency() {
        frequency++;
    }
    
    public int getFrequency() {
        return frequency;
    }
    
    @Override
    public String toString() {
        String result = "{";
        
        for (Item item : this)
            result += " " + item.toString();
        
        result += " }:" + this.frequency;
        
        return result;
    }
    
}
