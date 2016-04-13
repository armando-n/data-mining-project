package domain.id3;

import java.util.ArrayList;
import java.util.Map;

public class DecisionTree extends Node {

    public DecisionTree(ArrayList<String[]> tuples, String[] attributeTitles, int classLabelIndex) {
        
        // all tuples are of the same class; label this as a leaf node with class name
        if (isHomogenous(tuples, classLabelIndex))
            whatClass = tuples.get(0)[classLabelIndex];
        
        // no attributes are left to split the data on; use majority voting
        else if (! attributesRemain(attributeTitles))
            whatClass = majorityClass(tuples, classLabelIndex);
        
        // use information gain to determine the splitting criterion
        else {
            infoGain = new InformationGain();
            infoGain.run(tuples, attributeTitles, classLabelIndex);
            splittingCriterion = infoGain.getResult();
            splittingCriterionTitle = infoGain.getResultTitle();
            
            splitData(tuples, infoGain.getAttributeInfo(), classLabelIndex);
        }
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
    
    @Override
    public String toString() {
        String result = "";
        
        if (whatClass != null)
            result += whatClass;
        else
            result += "[" + splittingCriterionTitle + "?]";
        result += String.format("%n");
        
        for (Node child : children)
            result += child.toString(1);
        
        return result;
    }

}
