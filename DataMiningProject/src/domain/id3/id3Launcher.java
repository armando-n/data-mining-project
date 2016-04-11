/*
 * http://www.cise.ufl.edu/~ddd/cap6635/Fall-97/Short-papers/2.htm
 */
package domain.id3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class id3Launcher {

	private static String 			line, outlook, temp, humid, wind,
									play, positiveKey;
	
	private static int 				positives, fields, positiveIndex;
	private static gainCalculator 	gain;
	
	public static void main(String[] args){
		int[] ignore = {5};
		kickoff("Yes", 4, ignore);
		
	}
	public static void kickoff(String positiveAttributeName, int positiveAttrCol, int[] ignore) {
		File data = new File("training");
		Scanner scanData = new Scanner(System.in);
		int decisionIndex; 
		
		positiveKey = positiveAttributeName;
		positiveIndex = positiveAttrCol;
		fields = 5;
		
		gain = new gainCalculator(positiveIndex, fields, positiveKey);
		
		int 	numEntries = 0, positive = 0, negative = 0;		
		double 	entropy = 0.0;
		
		try {
			scanData = new Scanner(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();			
		}

		while(scanData.hasNextLine()){
			numEntries++;
			line = scanData.nextLine();
			parseLine(line, positiveIndex);			
		}
		
		gain.calculateGains();

		decisionIndex = gain.getDecision();
		System.out.println(decisionIndex);
				
		
	}
	
	private static void parseLine(String line, int pindex){
		
		Scanner lineScan = new Scanner(line);
		int pos = 0; 
		String[]  datas = new String[fields];
		boolean[] afirm = new boolean[fields];
		
		while(lineScan.hasNext()){
			datas[pos] = lineScan.next();
			pos++;
		}		
		
		if(datas[positiveIndex].equals(positiveKey))
			positives++;
		
		gain.addInfo(datas);
	}
	
	

}
