package dev.log.trace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpectrumBasedFaultLocalization {

	public static final String DATABASE_NAME = "service_logging";
	public static final String TABLE_NAME = "trace_point";
	public static final String ERROR_TABLE_NAME = "trace_point_error";
	
	public static HashMap<String,Double> sJ = new HashMap<String,Double>();
	public static HashMap<String,Double> sT = new HashMap<String,Double>();
	public static HashMap<String,Double> sO = new HashMap<String,Double>();
	
	private static MySQLConnector connector = new MySQLConnector(DATABASE_NAME);
	
	private static Map<String,ArrayList<String>> hitMap = new HashMap<String,ArrayList<String>>();
	private static Map<String,Boolean> errorVector = new HashMap<String,Boolean>();
	public static ArrayList<String> blocks = new ArrayList<String>();
	
	public static void addError(String process_id) {
		connector.update(String.format("INSERT INTO %s VALUES (0, '%s');",ERROR_TABLE_NAME,process_id));
	}

	public static void createMetrics() {
		List results = connector.select(String.format("SELECT a.process_id,a.service_id,b.error FROM %s AS a, %s AS b WHERE a.process_id = b.process_id;",TABLE_NAME,ERROR_TABLE_NAME));

		for(int i = 0; i < results.size(); i++){
			Map row = (Map)results.get(i); // database row
			String process_id = (String)row.get("process_id");
			String service_id = (String)row.get("service_id");
			if(!hitMap.containsKey(process_id))
				hitMap.put(process_id, new ArrayList<String>());
			if(!blocks.contains(service_id))
				blocks.add(service_id);
			hitMap.get(process_id).add(service_id);
			errorVector.put(process_id, (Boolean)row.get("error"));
		}

		connector.close();
		
		System.out.println(hitMap);
		System.out.println(errorVector);
		System.out.println(blocks);
		calculateSJ();
		calculateST();
		calculateSO();
		System.out.println("SJ: "+sJ);
		System.out.println("ST: "+sT);
		System.out.println("SO: "+sO);
		
	}
	
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
	
	private static void calculateSJ() {
		
		for(int i = 0; i < blocks.size(); i++) {

			HashMap<String,Double> counts = countHitsAndErrors(i);

			sJ.put(blocks.get(i), counts.get("a11") / (counts.get("a11") + counts.get("a01") + counts.get("a10")));
		}
	}

	private static void calculateST() {

		for(int i = 0; i < blocks.size(); i++) {

			HashMap<String,Double> counts = countHitsAndErrors(i);
			double a11 = counts.get("a11");
			double a10 = counts.get("a10");
			double a01 = counts.get("a01");
			double a00 = counts.get("a00");
			
			sT.put(blocks.get(i), (a11/(a11+a01)) / ((a11/(a11+a01)) + (a10/(a10+a00))));
		}
	}

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
}
