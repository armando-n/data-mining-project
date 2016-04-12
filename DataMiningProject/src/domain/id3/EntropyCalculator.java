package domain.id3;

import java.util.ArrayList;
import java.util.Map;

public class EntropyCalculator {
    private static EntropyCalculator entropyCalculator;

	private double result;
	
	private EntropyCalculator() {
	}
	
	public static EntropyCalculator getCalculator() {
	    if (entropyCalculator == null)
	        entropyCalculator = new EntropyCalculator();
	    return entropyCalculator;
	}
	
	// Info(D) = expected information required to classify a tuple in D
    // Info(D) = -sum_i=1-m( (|C_i,D| / |D|) * log_2(|C_i,D| / |D|) ) = entropy of D
//	public void calculateEntropy(Gain gain, int j){
	public void calculateEntropy(ArrayList<ArrayList<String>> tuples, ArrayList<Integer> attributeList,
	        ArrayList<ArrayList<String>> positiveTuples, ArrayList<ArrayList<String>> negativeTuples/*,
	        ArrayList<String> classList*/) {
//		Map<String, Integer> attributes = gain.getAttr();
//		ArrayList<String> tmp = new ArrayList<String>(attributes.keySet());
//		int total = j;
		double classProbability; // the probability that an tuple belongs to the current class
		double logOfProbability; // the log-base-2 of the above probability
		double summation = 0;
		
		// in English
		for (int i = 0; i < classList.size(); i++) {
//            classProbability = (double)attributes.get(tmp.get(i))/total;
		    // probability of current class is # of tuples of current class in D divided by # of tuples in D
		    /* TODO the problem as I see it: I was hoping to use the previous counts of positive and
		     * negative classes to get ahead at this point. However, though the counts are useful
		     * here, I need to recount for each attribute. The problem comes when there are more than
		     * two classes, I need to consider this here. My thinking is to have the initial count not
		     * be a full count, but just a scan over the data that stops as soon as it encounters a second
		     * class. After all, that is the only point of the initial count.
		     */
		    classProbability = classList.get(i);
            logOfProbability = Math.log(classProbability) / Math.log(2);
            summation += classProbability * logOfProbability;
        }
		
		// in cryptic speak
		for(int i = 0; i < tmp.size(); i++){
			classProbability = (double)attributes.get(tmp.get(i))/total;
			logOfProbability = Math.log(classProbability)/Math.log(2);
			summation += -(classProbability*logOfProbability);
			
		}
		
		result = summation;		
	}

	// Info_A(D) = expected information required to classify a tuple from D based on partitioning by A
    // Info_A(D) = sum_j=1-v( (|D_j| / |D|) * Info(D_j) )
	public void calculateEntropyAfterSplit(Gain gain, int size, int pos) {
		Map<String, Integer> attributes = gain.getAttr();
		Map<String, Integer> positives = gain.getPos();
		ArrayList<String> tmp = new ArrayList<String>(attributes.keySet());

		
		double p1, p2, p4, p5, positive, negative;
		Integer p3;
		Double calc = 0.0;	
		
		for(int i = 0; i < tmp.size(); i++){
			p1 = (double)attributes.get(tmp.get(i));
			p3 = positives.get(tmp.get(i));
			
			if(p3 != null){
				p1 = (double)p3/p1;
			}else{
				p1 = 0;
			}

			
			p4 = 1-p1;
			p2 = Math.log(p1)/Math.log(2);
			p5 = Math.log(p4)/Math.log(2);
			
			positive = -(p1*p2);			
			negative = -(p4*p5);			
			calc = positive+negative;
			
			
			if(!calc.toString().equals("NaN")){
				calc *= (attributes.get(tmp.get(i))/(double)size);
				result += calc;
			}
			
		}	
		
	}
	
	private void countClasses(ArrayList<ArrayList<String>> tuples,
            String positiveAttributeValue, int classLabelIndex) {
        
        for (ArrayList<String> tuple : tuples) {
            if (tuple.get(classLabelIndex).equals(positiveAttributeValue))
                positiveTuples.add(tuple);
            else
                negativeTuples.add(tuple);
        }
        
    }
	
//	public EntropyCalculator(Gain gain, int size) {
//		Map<String, Integer> attributes = gain.getAttr();
//		Map<String, Integer> positives = gain.getPos();
//		ArrayList<String> tmp = new ArrayList<String>(attributes.keySet());
//		
//		
//		double p1, p2, p4, p5, positive, negative;
//		Integer p3;
//		Double calc = 0.0;	
//		
//		for(int i = 0; i < tmp.size(); i++){
//			
//			p1 = (double)attributes.get(tmp.get(i));			
//			p3 = positives.get(tmp.get(i));
//			
//			
//			if(p3 != null){
//				p1 = (double)p3/p1;
//			}else{
//				p1 = 0;
//			}
//
//			
//			p4 = 1-p1;
//			p2 = Math.log(p1)/Math.log(2);
//			p5 = Math.log(p4)/Math.log(2);
//			
//			positive = -(p1*p2);			
//			negative = -(p4*p5);			
//			calc = positive+negative;
//			
//			if(!calc.toString().equals("NaN")){
//				calc *= (attributes.get(tmp.get(i))/(double)size);
//				
//				result += calc;
//			}
//			
//			
//		}	
////		System.out.println(result + " <<\n");
//		
//	}

	public double getResult() {
		return result;
		
	}
	

	
	
}
