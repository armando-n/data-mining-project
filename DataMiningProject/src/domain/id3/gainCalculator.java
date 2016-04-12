package domain.id3;

import java.util.ArrayList;

public class gainCalculator {

	//classIndex is the index of the classifier field
	private int classIndex, numFields;
	private ArrayList<Gain> gList = new ArrayList<Gain>();
	private ArrayList<Double> gains = new ArrayList<Double>();
	private EntropyCalculator ec;
	private String positiveAttributeName;
	
	public gainCalculator(int classLabelIndex, int numOfFields, String posAttrName){
		classIndex = classLabelIndex;
		numFields = numOfFields;	
		positiveAttributeName = posAttrName;
		
		for (int i = 0; i < numOfFields; i++){
			gList.add(new Gain());
			gains.add(0.0);
		}
		 
		
	}
	
	/**
	 * @param datas An array of field values representing a single tuple of data.
	 */
	public void addInfo(String[] datas) {
		Gain g;
		for(int i = 0; i < numFields; i++){
			
			g = gList.get(i);
			g.addAtribute(datas[i]);
			
			if (datas[classIndex].equals(positiveAttributeName))
				g.addPositive(datas[i]);
		}
		
	}
	
	/**
	 * 
	 * @return The total entropy for the set
	 */
	public double totalEnt(){
		ec = new EntropyCalculator(gList.get(classIndex), gList.get(classIndex).size());
		return ec.getResult();
	}
	
	
	public void calculateGains() {
		
		double gain = 0, toSub = 0, theGain = 0;
		
		for(int i = 0; i < numFields; i++){

			if(i != classIndex){
				ec = new EntropyCalculator(gList.get(i), gList.get(i).size(), i);			
				theGain = ec.getResult();
				gain = totalEnt() - theGain;				
				gains.set(i, gain);
			}else{
				gains.set(i, 0.0);
			}
			
		}
				
	}


	public int getDecision() {
		
		int 	decision = -1;
		double 	value = 0;
		for(int i = 0; i < gains.size(); i++){
			System.out.println(gains.get(i));
			if(value < gains.get(i)){				
				value = gains.get(i);
				decision = i;
			}
		}
		
		return decision;
		
	}

	
}
