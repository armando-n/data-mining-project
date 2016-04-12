package domain.id3;

import java.util.ArrayList;

public class Node {
    
    private Integer splittingCriterion; // index of the attribute used to split the data at this node 
    private Node[] children; // the size of this array cannot be determined until the splitting criterion is chosen
    private ArrayList<ArrayList<String>> positiveTuples;
    private ArrayList<ArrayList<String>> negativeTuples;
    private String whatClass;
    private InformationGain infoGain;

    public Node(ArrayList<ArrayList<String>> tuples, ArrayList<Integer> attributeList
            , ArrayList<String> attributeTitles, String positiveAttributeValue, int classLabelIndex) {
        
        positiveTuples = new ArrayList<ArrayList<String>>();
        negativeTuples = new ArrayList<ArrayList<String>>();
        
        isHomogenous(tuples, positiveAttributeValue, classLabelIndex);
//        countClasses(tuples, positiveAttributeValue, classLabelIndex);
        
        // all tuples are of the same positive class; label this as a leaf node with class name
        if (negativeTuples.isEmpty() && !positiveTuples.isEmpty())
            whatClass = positiveTuples.get(0).get(classLabelIndex);
        
        // all tuples are of the same negative class; label this as a leaf node with class name
        else if (positiveTuples.isEmpty() && !negativeTuples.isEmpty())
            whatClass = negativeTuples.get(0).get(classLabelIndex);
        
        // no attributes are left to split the data on; use majority voting
        else if (attributeList.isEmpty()) {
            if (positiveTuples.size() >= negativeTuples.size())
                whatClass = positiveTuples.get(0).get(classLabelIndex);
            else
                whatClass = negativeTuples.get(0).get(classLabelIndex);
        }
        
        // use information gain to determine the splitting criterion
        infoGain = new InformationGain(
                tuples,
                attributeList,
                attributeTitles,
                positiveAttributeValue,
                classLabelIndex,
                positiveTuples,
                negativeTuples);
        splittingCriterion = infoGain.getResult();
    }
    
    private boolean isHomogenous(ArrayList<ArrayList<String>> tuples
            , String positiveAttributeValue, int classLabelIndex) {
        
        String firstClass = tuples.get(0).get(classLabelIndex);
        
        for (int i = 1; i < tuples.size(); i++)
            if (! tuples.get(i).get(classLabelIndex).equals(firstClass))
                return false;
        
        return true;
        
    }

}
