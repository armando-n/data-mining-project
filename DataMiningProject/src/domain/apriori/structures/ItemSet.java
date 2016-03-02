package domain.apriori.structures;

import java.util.TreeSet;

@SuppressWarnings("serial")
public class ItemSet extends TreeSet<Item> {
    
    private int frequency; // the number of occurrences of this itemset (not the number of items in the set)
    
    public ItemSet() {
        super();
        this.frequency = 0;
    }

    public void incFrequency() {
        frequency++;
    }
    
    public int getFrequency() {
        return frequency;
    }
    
}
