package domain.id3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InformationGain {
    private Double[] gains; // each cell corresponds to an attribute in attributes array, and represents the info gained by splitting on that attribute
    Attribute[] attributes;
    private int attributeIndex = -1; // The index of the attribute that the data should be split on
    private String attributeTitle; // the title of the chosen attribute
    private int classLabelIndex;
    
    public InformationGain() {
    }

    public void run(ArrayList<String[]> tuples, String[] attrTitles, int classIndex) {
        Map<String, Integer>[] attributeFrequencies;
        
        // create array of value-to-frequency maps; each cell in the array corresponds to the attributes in attributes array
        attributeFrequencies = countAttributeFrequencies(tuples);
        
        attributes = new Attribute[attrTitles.length];
        for (int i = 0; i < attributes.length; i++)
            attributes[i] = new Attribute(attrTitles[i], attributeFrequencies[i].keySet());
        
        run(tuples, attributes, classIndex);
    }
    
    public void run(ArrayList<String[]> tuples, Attribute[] attrs, int classIndex) {
        Map<String, Integer>[] attributeFrequencies;
        double entropy;
        double entropyAfterSplit;
        double maxGain;
        
        gains = new Double[attrs.length];
        classLabelIndex = classIndex;
        attributes = attrs;
        
        // create array of value-to-frequency maps; each cell in the array corresponds to the attributes in attributes array
        attributeFrequencies = countAttributeFrequencies(tuples);
        
        // calculate the expected information required to classify a tuple in D (i.e. in all tuples)
        entropy = calculateEntropy(attributeFrequencies, classIndex, tuples.size());
        
        // calculate the expected information required to classify a tuple from D based on partitioning by each attribute A
        for (int i = 0; i < attrs.length; i++) {
            
            // null attribute titles mean that attribute has already been split on, and we never split on the class label attribute
            if (attrs[i] == null || i == classIndex)
                continue;
            
            entropyAfterSplit = calculateEntropyAfterSplit(tuples, attributeFrequencies, i, classIndex);
            gains[i] = entropy - entropyAfterSplit; // store the information gain for the current attribute
        }
        
        // whichever attribute has the highest information gain is the chosen result of the information gain algorithm
        maxGain = 0.0;
        for (int i = 0; i < gains.length; i++) {
            if (gains[i] != null && gains[i].doubleValue() > maxGain) {
                maxGain = gains[i];
                attributeIndex = i;
                attributeTitle = attrs[i].getTitle();
            }
        }
    }
    
    /** @return The index of the attribute that the data should be split on **/
    public int getResult() {
        return attributeIndex;
    }
    
    /** @return The title of the attribute that the data should be split on **/
    public String getResultTitle() {
        return attributeTitle;
    }
    
    /** @return An array of Attribute objects, each of which contains an attribute title
     * along with all corresponding values discovered during the course of the algorithm. */
    public Attribute[] getAttributeInfo() {
        return attributes;
    }
    
    @Override
    public String toString() {
        String result = String.format("<attribute> : <information gain>%n--------------------------------%n");
        
        for (int i = 0; i < gains.length; i++) {
            if (i == classLabelIndex)
                continue;
            result += String.format("%s : %s%n", attributes[i].getTitle(), gains[i]);
        }
        
        return result;
    }
    
    /** For each attribute in the given list of tuples, the counts of each of their values is
     * found and returned as an array of Maps, with each Map containing the values and
     * frequencies for a particular attribute. The attribute Map indexes correspond to the
     * indexes of the attributes in the given tuple arrays. **/
    @SuppressWarnings("unchecked")
    private Map<String, Integer>[] countAttributeFrequencies(ArrayList<String[]> tuples) {
        
        // this array contains a mapping of values to their counts for each attribute
        HashMap<String, Integer>[] attrFreq = (HashMap<String, Integer>[])new HashMap[tuples.get(0).length];
        for (int i = 0; i < attrFreq.length; i++)
            attrFreq[i] = new HashMap<String, Integer>();
        
        // go through each tuple and count occurrences of each value
        for (String[] tuple : tuples) {
            for (int i = 0; i < tuple.length; i++) {
                if (attrFreq[i].containsKey(tuple[i]))
                    attrFreq[i].put(tuple[i], attrFreq[i].get(tuple[i])+1);
                else
                    attrFreq[i].put(tuple[i], 1);
            }
        }
        
        return attrFreq;
    }
    
    // Info(D) = expected information required to classify a tuple in D
    // Info(D) = -sum_i=1-m( (|C_i,D| / |D|) * log_2(|C_i,D| / |D|) ) = entropy of D
    private double calculateEntropy(Map<String, Integer>[] attrFreq, int classIndex, int totalTuples) {
        double classProbability; // the probability that an tuple belongs to the current class
        double logOfProbability; // the log-base-2 of the above probability
        double summation = 0.0;
        
        // for each class
        for (String className : attrFreq[classIndex].keySet()) {
            // probability of current class is # of tuples of current class in D divided by # of tuples in D
            classProbability = (double)attrFreq[classIndex].get(className) / totalTuples;
            logOfProbability = Math.log(classProbability) / Math.log(2);
            summation += -(classProbability * logOfProbability);
        }
        
        return summation;
    }
    
    // Info_A(D) = expected information required to classify a tuple from D based on partitioning by A
    // Info_A(D) = sum_j=1-v( (|D_j| / |D|) * Info(D_j) )
    private double calculateEntropyAfterSplit(ArrayList<String[]> tuples, Map<String, Integer>[] attrFreq
            , int attrIndex, int classIndex) {
        ArrayList<String[]> tuplesSubset;
        Map<String, Integer>[] subAttrFreq;
        double summation;
        
        // determine the weighted entropy of each subset created by splitting on the specified attribute
        summation = 0.0;
        for (String value : attrFreq[attrIndex].keySet()) {
            
            // create a subset for each possible value of the current attribute, containing only tuples having that value
            tuplesSubset = new ArrayList<String[]>();
            for (String[] tuple : tuples) {
                if (tuple[attrIndex].equals(value))
                    tuplesSubset.add(tuple);
            }
            
            // calculate entropy for each subset
            subAttrFreq = countAttributeFrequencies(tuplesSubset);
            summation += (double)tuplesSubset.size() / tuples.size() * calculateEntropy(subAttrFreq, classIndex, tuplesSubset.size());
        }
        
        return summation;
    }
    
}
