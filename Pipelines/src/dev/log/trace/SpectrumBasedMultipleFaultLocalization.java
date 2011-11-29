package dev.log.trace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpectrumBasedMultipleFaultLocalization {

	public static HashMap<String,Double> sO = new HashMap<String,Double>();

	private static Map<String,ArrayList<String>> hitMap = new HashMap<String,ArrayList<String>>();
	private static Map<String,Boolean> errorVector = new HashMap<String,Boolean>();
	public static ArrayList<String> blocks = new ArrayList<String>();
	
	/**
	 * http://photon.isy.liu.se/dx09/papers/dx09_submission_19.pdf
	 * @input matrix (A,e)
	 */
	public void staccato(){
		
	}
	
	/**
	 * @input matrix (A,e)
	 * @input dk
	 */
	public void generatePr(){
		
	}
	
	/**
	 * @input expr
	 * @input (for all j element of dk) gj
	 */
	public void evaluate(){
		
	}
	
	/**
	 * @input error vector
	 * @input hitmap
	 */
	private static void calculateSO() {

		for(int i = 0; i < blocks.size(); i++) {

			HashMap<String,Double> counts = countHitsAndErrors(i);
			double a11 = counts.get("a11");
			double a10 = counts.get("a10");
			double a01 = counts.get("a01");
			double a00 = counts.get("a00");
			
			sO.put(blocks.get(i), a11 / (double)(Math.sqrt((a11+a01)*(a11+a10))));
		}
	}
	
	/**
	 * @input i for ci
	 */	
	private static HashMap<String,Double> countHitsAndErrors(int i) {
		HashMap<String,Double> counts = new HashMap<String,Double>();
		
		counts.put("a11", 0.0);
		counts.put("a10", 0.0);
		counts.put("a01", 0.0);
		counts.put("a00", 0.0);
		
		for(int k = 0; k < hitMap.size(); k++){
			String process_id = (String)hitMap.keySet().toArray()[k];
			boolean hit = hitMap.get(process_id).contains(blocks.get(i));
			boolean error = errorVector.get(process_id);
			if(hit && error) {
				counts.put("a11", counts.get("a11") + 1);
			} else if (hit && !error){
				counts.put("a10", counts.get("a10") + 1);
			} else if (!hit && error) {
				counts.put("a01", counts.get("a01") + 1);
			} else {
				counts.put("a00", counts.get("a00") + 1);
			}
		}
		return counts;
	}
}
