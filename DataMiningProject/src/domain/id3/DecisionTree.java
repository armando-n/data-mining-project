package domain.id3;

import java.util.ArrayList;
import java.util.Map;

public class DecisionTree extends Node {

    public DecisionTree(ArrayList<String[]> tuples, String[] attributeTitles, int classLabelIndex) {
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

}
