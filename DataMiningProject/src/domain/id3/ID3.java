package domain.id3;

import java.util.ArrayList;

public class ID3 {
    private static ID3 id3; // singleton

    private ID3() {
    }
    
    public static ID3 getID3() {
        if (id3 == null)
            id3 = new ID3();
        return id3;
    }
    
    public void run(ArrayList<String[]> tuples, String[] attributeTitles, String positiveAttributeValue, int classLabelIndex) {
        
        Node decisionTree = new Node(tuples, attributeTitles, classLabelIndex);
        
    }

}
