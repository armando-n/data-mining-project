package id3;

import java.util.ArrayList;

public class gainCalculator {

	//pIndex is the index of the classifier field
	private int pIndex, mySize;
	private ArrayList<Gain> gList = new ArrayList<Gain>();
	private ArrayList<Double> gains = new ArrayList<Double>();
	private EntropyCalculator ec;
	private String pKey;
	public gainCalculator(int positiveIndex, int size, String key){
		pIndex = positiveIndex;
		mySize = size;	
		pKey = key;
		
		for(int i = 0; i < size; i++){
			gList.add(new Gain());
			gains.add(0.0);
		}
		 
		
	}
	
	
	public void addInfo(String[] datas) {
		Gain g;
		for(int i = 0; i < mySize; i++){
			
			g = gList.get(i);
			g.addAtribute(datas[i]);		
			
			if(datas[pIndex].equals(pKey))
				g.addPositive(datas[i]);
		}			
		
	}
	
	/**
	 * 
	 * @return The total entropy for the set
	 */
	public double totalEnt(){
		ec = new EntropyCalculator(gList.get(pIndex), gList.get(pIndex).size());
		return ec.getResult();
	}
	
	
	public void calculateGains() {
		
		double gain = 0, toSub = 0, theGain = 0;
		
		for(int i = 0; i < mySize; i++){

			if(i != pIndex){
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
