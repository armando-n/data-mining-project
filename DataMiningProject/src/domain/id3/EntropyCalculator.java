package domain.id3;

import java.util.ArrayList;
import java.util.Map;

public class EntropyCalculator {

	private double result;
	
	public EntropyCalculator(Gain gain, int j){
		Map<String, Integer> attributes = gain.getAttr();
		ArrayList<String> tmp = new ArrayList<String>(attributes.keySet());
		int total = j;
		double p1, p2;
		double calc = 0;	
		
		
		
		for(int i = 0; i < tmp.size(); i++){
			p1 = (double)attributes.get(tmp.get(i))/total;
			p2 = Math.log(p1)/Math.log(2);
			calc += -(p1*p2);
			
		}
		
		result = calc;		
	}

	public EntropyCalculator(Gain gain, int size, int pos) {
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
