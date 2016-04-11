package domain.id3;

import java.util.HashMap;
import java.util.Map;

public class Gain {

	//The attributes and counts
	private HashMap<String, Integer> atributes = new HashMap<String, Integer>();
	
	//attributes and affermatives
	private HashMap<String, Integer> positives = new HashMap<String, Integer>();
	
	private int size = 0, index;
	
	public void addAtribute(String toAdd) {
		
		size++;
		Integer atributeCount = atributes.get(toAdd);
		
		if(atributeCount == null){
			atributes.put(toAdd, 1);
		}else{
			atributes.put(toAdd, atributeCount+1);
		}
					
	}

	public void printInfo() {
		System.out.println(atributes);
		System.out.println(positives);
		
	}

	public Map<String, Integer> getAttr() {
		return atributes;
		
	}
	
	public Map<String, Integer> getPos() {
		return positives;
		
	}

	public int size() {
		return size;
	}

	public void addPositive(String string) {
		
		Integer count = positives.get(string); 
		if(count == null){
			positives.put(string, 1);
		}else{
			positives.put(string, count+1);
		}
	}


}
