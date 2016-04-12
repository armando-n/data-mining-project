package domain.id3;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {
    
    private Integer splittingCriterion; // index of the attribute used to split the data at this node 
    private Node[] children; // the size of this array cannot be determined until the splitting criterion is chosen
    private HashMap<String, Integer> classFrequencies;
    private String whatClass;
    private InformationGain infoGain;

    public Node(ArrayList<String[]> tuples, ArrayList<Integer> attributeList
            , String[] attributeTitles, String positiveAttributeValue, int classLabelIndex) {
        
        classFrequencies = new HashMap<String, Integer>();
        
        // all tuples are of the same class; label this as a leaf node with class name
        if (isHomogenous(tuples, classLabelIndex))
            whatClass = tuples.get(0)[classLabelIndex];
        
        // no attributes are left to split the data on; use majority voting
        else if (attributeList.isEmpty()) {
            countClassFrequencies(tuples, positiveAttributeValue, classLabelIndex);
            
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
            infoGain = new InformationGain(
                    tuples,
                    attributeList,
                    attributeTitles,
                    classLabelIndex);
            splittingCriterion = infoGain.getResult();
            System.out.println(infoGain.toString());
        }
    }
    
    private boolean isHomogenous(ArrayList<String[]> tuples, int classLabelIndex) {
        
        String firstClass = tuples.get(0)[classLabelIndex];
        
        for (int i = 1; i < tuples.size(); i++)
            if (! tuples.get(i)[classLabelIndex].equals(firstClass))
                return false;
        
        return true;
        
    }
    
    private void countClassFrequencies(ArrayList<String[]> tuples,
            String positiveAttributeValue, int classIndex) {
        
        for (String[] tuple : tuples) {
            if (classFrequencies.containsKey(tuple[classIndex]))
                classFrequencies.put(tuple[classIndex], classFrequencies.get(tuple[classIndex])+1);
            else
                classFrequencies.put(tuple[classIndex], 1);
        }
        
    }

}
