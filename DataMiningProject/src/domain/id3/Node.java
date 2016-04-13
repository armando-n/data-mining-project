package domain.id3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Node {
    
    protected Integer splittingCriterion; // index of the attribute used to split the data at this node 
    protected String splittingCriterionTitle;
    protected String splitOnValue;
    protected Node[] children; // the size of this array cannot be determined until the splitting criterion is chosen
    protected String whatClass;
    protected InformationGain infoGain;
    
    public Node() {
    }
    
    /** Creates the node as a leaf node labeled with the specified class **/
    public Node(String whatClass, String valueSplitOn) {
        this.whatClass = whatClass;
        this.splitOnValue = valueSplitOn;
    }

    public Node(ArrayList<String[]> tuples, Attribute[] attributes, int classLabelIndex, String valueSplitOn) {
        
        this.splitOnValue = valueSplitOn;
        
        // all tuples are of the same class; label this as a leaf node with class name
        if (isHomogenous(tuples, classLabelIndex))
            whatClass = tuples.get(0)[classLabelIndex];
        
        // no attributes are left to split the data on; use majority voting
        else if (! attributesRemain(attributes))
            whatClass = majorityClass(tuples, classLabelIndex);
        
        // use information gain to determine the splitting criterion
        else {
            infoGain = new InformationGain();
            infoGain.run(tuples, attributes, classLabelIndex);
            splittingCriterion = infoGain.getResult();
            splittingCriterionTitle = infoGain.getResultTitle();
            
            splitData(tuples, attributes, classLabelIndex);
        }
    }
    
    public void classify(String[] tuple, String[] attributeTitles, int classIndex) {
        if (whatClass != null)
            tuple[classIndex] = whatClass;
        else {
            for (Node child : children) {
                if (child.splitOnValue.equals(tuple[splittingCriterion])) {
                    child.classify(tuple, attributeTitles, classIndex);
                    break;
                }
            }
        }
    }
    
    public String toString(int level) {
        String result = "";
        
        for (int i = 0; i < level; i++)
            result += "            ";
        result += "`-- " + splitOnValue + " --> ";
        
        result += (whatClass == null) ? "[" + splittingCriterionTitle + "?]" : whatClass;
        result += String.format("%n");
        
        if (children != null) {
            int nextLevel = level + 1;
            for (Node child : children)
                result += child.toString(nextLevel);
        }
        
        return result;
    }
    
    protected boolean isHomogenous(ArrayList<String[]> tuples, int classLabelIndex) {
        String firstClass = tuples.get(0)[classLabelIndex];
        for (int i = 1; i < tuples.size(); i++)
            if (! tuples.get(i)[classLabelIndex].equals(firstClass))
                return false;
        return true;
    }
    
    /**
     * @param attributes An array of Attribute objects. A removed attribute must be indicated
     *        with a null value.
     * @return True if there are still attributes in the given attribute array, false otherwise.
     */
    protected boolean attributesRemain(Attribute[] attributes) {
        for (Attribute attribute : attributes)
            if (attribute != null)
                return true;
        return false;
    }
    
    protected String majorityClass(ArrayList<String[]> tuples, int classLabelIndex) {
        Map<String, Integer> classFrequencies = countClassFrequencies(tuples, classLabelIndex);
        String mostFrequentClass = null;
        int maxFrequency = 0;
        
        for (String classLabel : classFrequencies.keySet()) {
            if (classFrequencies.get(classLabel) > maxFrequency) {   
                maxFrequency = classFrequencies.get(classLabel);
                mostFrequentClass = classLabel;
            }
        }
        
        return mostFrequentClass;
    }
    
    protected Map<String, Integer> countClassFrequencies(ArrayList<String[]> tuples, int classIndex) {
        Map<String, Integer> classFrequencies = new HashMap<String, Integer>();
        for (String[] tuple : tuples) {
            if (classFrequencies.containsKey(tuple[classIndex]))
                classFrequencies.put(tuple[classIndex], classFrequencies.get(tuple[classIndex])+1);
            else
                classFrequencies.put(tuple[classIndex], 1);
        }
        return classFrequencies;
    }

    protected void splitData(ArrayList<String[]> tuples, Attribute[] attributes, int classIndex) {
        Map<String, ArrayList<String[]>> subsetsByValue;
        Iterator<String> subsetsIter;
        Attribute[] newAttrs;
        
        // create map to contain a list of tuples for each value of the splitting criterion attribute
        subsetsByValue = new HashMap<String, ArrayList<String[]>>();
        
        for (String value : attributes[splittingCriterion].getValues())
            subsetsByValue.put(value, new ArrayList<String[]>());
        
        for (String[] tuple : tuples)
            subsetsByValue.get(tuple[splittingCriterion]).add(tuple);
        
        // remove splitting attribute out from further consideration
        newAttrs = new Attribute[attributes.length];
        System.arraycopy(attributes, 0, newAttrs, 0, attributes.length);
        newAttrs[splittingCriterion] = null;
        
        // create child nodes for each value of the splitting criterion attribute; pass corresponding subsets
        children = new Node[subsetsByValue.values().size()];
        subsetsIter = subsetsByValue.keySet().iterator();
        for (int i = 0; i < children.length; i++) {
            String currentValue = subsetsIter.next();
            ArrayList<String[]> currentSubset = subsetsByValue.get(currentValue);
            if (currentSubset.isEmpty())
                children[i] = new Node(majorityClass(tuples, classIndex), currentValue);
            else
                children[i] = new Node(currentSubset, newAttrs, classIndex, currentValue);
        }
    }
}
