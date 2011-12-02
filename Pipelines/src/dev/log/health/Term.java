package dev.log.health;

import java.util.ArrayList;
import java.util.HashMap;

public class Term {

	private boolean error;
	private ArrayList<String> components;

	public Term(ArrayList<String> components, boolean error) {
		this.error = error;
		this.components = components;
	}
	
	public ArrayList<String> getComponents() {
		return this.components;
	}
	
	public float calculate(HashMap<String, Float> values) {
		float result = 1.0f;
		
		try {
			for(String component : components) {
				if(!values.containsKey(component)) throw new Exception("Component was not found in the value map.");
				result *= values.get(component);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(error)
			result = 1 - result;
		
		return result;
	}

}