package domain.apriori;

public class Apriori {

    public Apriori() {
        // scan over entire database, counting occurrences of 1-itemsets. this is C_1
        
        // remove 1-itemsets from consideration that do not meet min_sup
        
        // sort remaining 1-itemsets. this is L_1
        
        // selectively join L_1 with itself
        
        /* prune the result of the join by looking to see
         * if any of its subsets are not in L_1. This is C_2 */
        
        // scan over entire database, counting occurrences of the 2-itemsets in C_2
        
        // remove 2-itemsets from consideration that do not meet min_sup.  This is L_2
        
        // selectively join L_2 with itself
        
        // ... there is a pattern here ...
    }

}
