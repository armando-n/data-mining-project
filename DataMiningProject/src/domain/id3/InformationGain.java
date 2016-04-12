package domain.id3;

import java.util.ArrayList;

public class InformationGain {
    
    private int result = -1; // The index of the attribute that the data should be split on **/
    private ArrayList<ArrayList<String>> positiveTuples;
    private ArrayList<ArrayList<String>> negativeTuples;
    private static ArrayList<String> CLASS_LIST; // TODO expand this algorithm to handle more than two classes

    public InformationGain(ArrayList<ArrayList<String>> tuples, ArrayList<Integer> attributeList
            , ArrayList<String> attributeTitles, String positiveAttributeValue, int classLabelIndex
        , ArrayList<ArrayList<String>> positiveTuples, ArrayList<ArrayList<String>> negativeTuples) {
        
        CLASS_LIST = new ArrayList<String>();
        CLASS_LIST.add("No");
        CLASS_LIST.add("Yes");
        
        EntropyCalculator.getCalculator().calculateEntropy(
                tuples, attributeList, positiveTuples, negativeTuples/*, NUM_CLASSES*/);
        
        // Gain(A) = how much would be gained by branching on A -> we want to pick the attribute that has
        //     the highest value for this
        // Gain(A) = Info(D) - Info_A(D)
        
        // D = tuples at current node N
        // m = number of classes
        // C_i = class i, 1 <= i <= m
        
        // Info(D) = expected information required to classify a tuple in D
        // Info(D) = -sum_i=1-m( (|C_i,D| / |D|) * log_2(|C_i,D| / |D|) ) = entropy of D
        
        // Info_A(D) = expected information required to classify a tuple from D based on partitioning by A
        // Info_A(D) = sum_j=1-v( (|D_j| / |D|) * Info(D_j) )
    }

    /** @return The index of the attribute that the data should be split on **/
    public int getResult() {
        return result;
    }
    
}
