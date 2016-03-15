package domain.apriori;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import domain.apriori.structures.HashTree;
import domain.apriori.structures.ItemSet;

public class Apriori {
    
    private List<HashTree> hashTrees;

    /**
     * Runs the Apriori algorithm on the given set of transactions.
     * @param transactions The transactions to run the algorithm against.
     * @param absoluteMinSupport The minimum number of occurrences an itemset needs to be considered frequent.
     * @param childrenPerNode Each node in the generated hash trees will have this many child nodes.
     * @param maxBucketSize The maximum number of itemsets a bucket node in a hash tree can have before it
     *        is converted to a hash node, assuming the last level has not been reached.
     */
    public Apriori(Set<ItemSet> transactions, int absoluteMinSupport, int childrenPerNode, int maxBucketSize) {
        HashTree kMinusOneItemSetTree;
        HashTree kItemSetTree;
        hashTrees = new ArrayList<HashTree>();
        
        /* count the occurrences of all 1-itemsets in transactions, creating C_1,
         * the set of candidate 1-itemsets. Then remove candidates that do not meet
         * minimum support to create L_1, the set of frequent 1-itemsets. */
        kItemSetTree = new HashTree(1, absoluteMinSupport);
        kItemSetTree.addAll(transactions);
        kItemSetTree.incFrequencies();
        kItemSetTree.removeNoMinSupport();
        hashTrees.add(kItemSetTree);
        
        while (!kItemSetTree.isEmpty()) {
            kMinusOneItemSetTree = kItemSetTree;
            
            // join L_(k-1) with itself and prune resulting k-itemset hash tree to create C_k
            kItemSetTree = kMinusOneItemSetTree.generateNextCandidateTree();
            kItemSetTree.prune(kItemSetTree);
            
            // create L_k by counting candidate k-itemsets and removing those that don't meet minimum support
            kItemSetTree.countCandidates(transactions);
            kItemSetTree.removeNoMinSupport();
            
            hashTrees.add(kItemSetTree);
        }
    }
    
    /** @return A list of frequent k-itemsets (i.e. frequent itemsets of length k) **/
    public List<ItemSet> getFrequentKItemSets(int k) {
        List<ItemSet> itemSets = new ArrayList<ItemSet>();
        itemSets.addAll(Arrays.asList(this.hashTrees.get(k-1).toArray())); // k-itemset tree is at index k-1
        return itemSets;
    }
    
    /** @return A list of all frequent itemsets of any length **/
    public List<ItemSet> getAllFrequentItemSets() {
        List<ItemSet> itemSets = new ArrayList<ItemSet>();
        
        for (HashTree tree : hashTrees)
            itemSets.addAll(Arrays.asList(tree.toArray()));
        
        return itemSets;
    }

}
