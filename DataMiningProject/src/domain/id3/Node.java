package domain.id3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Node {
    
    protected Integer splittingCriterion; // index of the attribute used to split the data at this node 
    protected String splittingCriterionTitle;
    private Node[] children; // the size of this array cannot be determined until the splitting criterion is chosen
    protected String whatClass;
    protected InformationGain infoGain;

    public Node(ArrayList<String[]> tuples, String[] attributeTitles, int classLabelIndex) {
        
        // all tuples are of the same class; label this as a leaf node with class name
        if (isHomogenous(tuples, classLabelIndex))
            whatClass = tuples.get(0)[classLabelIndex];
        
        // no attributes are left to split the data on; use majority voting
        else if (! attributesRemain(attributeTitles)) {
            Map<String, Integer> classFrequencies = countClassFrequencies(tuples, classLabelIndex);
            String mostFrequentClass = null;
            int maxFrequency = 0;
            
            for (String classLabel : classFrequencies.keySet()) {
                if (classFrequencies.get(classLabel) > maxFrequency) {   
                    maxFrequency = classFrequencies.get(classLabel);
                    mostFrequentClass = classLabel;
                }
            }
            
            whatClass = mostFrequentClass;
        }
        
        // use information gain to determine the splitting criterion
        else {
            infoGain = new InformationGain(tuples, attributeTitles, classLabelIndex);
            splittingCriterion = infoGain.getResult();
            splittingCriterionTitle = infoGain.getResultTitle();
            
            splitData(tuples, attributeTitles, classLabelIndex, infoGain.getAttributeInfo());
        }
    }
    
    protected boolean isHomogenous(ArrayList<String[]> tuples, int classLabelIndex) {
        String firstClass = tuples.get(0)[classLabelIndex];
        for (int i = 1; i < tuples.size(); i++)
            if (! tuples.get(i)[classLabelIndex].equals(firstClass))
                return false;
        return true;
    }
    
    /**
     * @param attributes An array of attribute names. A removed attribute must be indicated
     *        with a null value.
     * @return True if there are still attributes in the given attribute array, false otherwise.
     */
    protected boolean attributesRemain(String[] attributeTitles) {
        for (String attribute : attributeTitles)
            if (attribute != null)
                return true;
        return false;
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

    protected void splitData(ArrayList<String[]> tuples, String[] attributeTitles
            , int classIndex, Map<String, String[]>[] attrInfo) {
        Map<String, ArrayList<String[]>> subsetsByValue;
        Iterator<ArrayList<String[]>> subsetsIter;
        String[] newAttrTitles;
        
        // create map to contain a list of tuples for each value of the splitting criterion attribute
        subsetsByValue = new HashMap<String, ArrayList<String[]>>();
        
        for (String[] tuple : tuples) {
            if (subsetsByValue.containsKey(tuple[splittingCriterion]))
                subsetsByValue.get(tuple[splittingCriterion]).add(tuple);
            else {
                subsetsByValue.put(tuple[splittingCriterion], new ArrayList<String[]>());
                subsetsByValue.get(tuple[splittingCriterion]).add(tuple);
            }
        }
        
        // remove splitting attribute out from further consideration
        newAttrTitles = new String[attributeTitles.length];
        System.arraycopy(attributeTitles, 0, newAttrTitles, 0, attributeTitles.length);
        newAttrTitles[splittingCriterion] = null;
        
        // create child nodes for each value of the splitting criterion attribute; pass corresponding subsets
        children = new Node[subsetsByValue.values().size()];
        subsetsIter = subsetsByValue.values().iterator();
        for (int i = 0; i < children.length; i++) {
            children[i] = new Node(subsetsIter.next(), newAttrTitles, classIndex);
        }
    }
}
