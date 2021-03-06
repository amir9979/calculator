package dev.log.barinel.activity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A ComponentVector represents a component in the activity matrix. Since
 * such component are used for the calculation of the ochiai value this is
 * done for each component, rather than for the whole activity matrix. Once
 * the ComponentVector is initiated, a hit or miss can be added with the
 * value of the error for that execution. By doing this the counts are
 * updated and the ochiai value is adapted accordingly.
 * 
 * A vector's value at a location is 1 when it is hit and 0 when it is not
 * hit.
 * 
 * @author Joel van den Berg
 */
public class ComponentVector extends Vector<String,Integer> {
	
	/**
	 * An hashmap that register the counts for this component.
	 * The elements are "n01", "n11", "n10".
	 */
	private HashMap<String,Integer> counts;
	private Double ochiaiValue;

	/**
	 * initiate a ComponentVector with empty counts and an ochiai value of 0.0
	 */
	public ComponentVector() {
		super();
		counts = new HashMap<String,Integer>();
		this.setCounts(0, 0, 0);
		ochiaiValue = 0.0;
	}

	/**
	 * Insert a hit or miss for this component. The error is used to update
	 * the counts of this component.
	 * @param hit 1 is a hit, 0 is a miss
	 * @param error 1 is the execution resulted in failure, 0 is the execution succeeded.
	 */
	public void insertHit(Integer hit, Integer error) {
		this.addValue(hit);
		if((int)hit == 1) {
			if((int)error == 1)
				counts.put("n11", counts.get("n11") + 1);
			else
				counts.put("n10", counts.get("n10") + 1);				
		} else if((int)error == 1)
			counts.put("n01", counts.get("n01") + 1);
	}
	
	/**
	 * Update the values of the local counts variable in respect to the given error
	 * vector.
	 * @param errorVector The error vector that is used for determining the counts
	 */
	public void updateCounts(ErrorVector errorVector) {
		
		int i = 0,
		    n11 = 0,
			n10 = 0,
			n01 = 0;
		
		for(Integer value : getValues() ) {
			int ei = errorVector.getValues().get(i);
			
			int aij = value;
			
			if(aij == 1 && ei == 1) n11++;
			if(aij == 1 && ei == 0) n10++;
			if(aij == 0 && ei == 1) n01++;
			
			i++;
		}
		
		counts.put("n11", n11);
		counts.put("n10", n10);
		counts.put("n01", n01);
		
		/// updateOchiai();
	}
	
	/**
	 * @deprecated
	 * The getOchiaiValue will return the calculated value, since we will more often
	 * add values than get the ochiai value for calculations. This means that 
	 * getOchiaiValue will now perform the calculations.
	 */
	private void updateOchiai() {

	    int n11 = counts.get("n11"),
			n10 = counts.get("n10"),
			n01 = counts.get("n01");
	    
		ochiaiValue = n11 / (double)(Math.sqrt((n11+n01)*(n11+n10)));
	}
	
	/**
	 * Calculate the ochiai value for the registered counts.
	 * @return The ochiai value for this component
	 */
	public Double getOchiaiValue() {
		
	    int n11 = counts.get("n11"),
			n10 = counts.get("n10"),
			n01 = counts.get("n01");
	    
		return n11 / (double)(Math.sqrt((n11+n01)*(n11+n10)));	
		
	}
	
	/**
	 * Sets the counts to the given values.
	 * @param n11 New value for n11
	 * @param n10 New value for n10
	 * @param n01 New value for n01
	 */
	private void setCounts(Integer n11, Integer n10, Integer n01) {
		counts.put("n11", n11);
		counts.put("n10", n10);
		counts.put("n01", n01);
	}
	
	/**
	 * Get the counts variable for this component.
	 * @return The list of counts
	 */
	public HashMap<String,Integer> getCounts() {
		return this.counts;
	}

	/**
	 * Return a deep copy of this component vector.
	 * @return The deep copy of the component
	 */
	public ComponentVector copy() {

		ComponentVector newVector = new ComponentVector();
		
		
		newVector.setHeader(new String(this.getHeader()));
		
		for(Integer value : this.getValues()) {
			newVector.addValue(value);
		}

		newVector.setCounts(this.counts.get("n11"), this.counts.get("n10"), this.counts.get("n01"));
		
		return newVector;
	}
	
	/**
	 * Important function for the {@link Matrix#strip(String)} method.
	 * @param obj The other object that we compare this with.
	 * @return True when equal, false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if(obj instanceof ComponentVector) {
			ComponentVector vector = (ComponentVector) obj;
			if(vector.getValues().size() != this.getValues().size())
				return false;
			for(int i = 0; i < vector.getValues().size(); i++) {
				if(this.getValue(i) != vector.getValue(i))
					return false;
			}
//			if(	this.counts.get("n11") != vector.counts.get("n11") ||
//				this.counts.get("n10") != vector.counts.get("n10") ||
//				this.counts.get("n01") != vector.counts.get("n01")){
//				return false;
//			}
			if(!vector.getHeader().equals(this.getHeader()))
				return false;
			
			return true;
			
		}
		return false;
	}
	
	/**
	 * Create a string representation of this component vector in a more
	 * human readable manner.
	 * 
	 * @return String representation of this component vector
	 */
	@Override
	public String toString() {
		ArrayList<Object> output = new ArrayList<Object>();
		output.add(this.getHeader());
		output.addAll(this.getValues());
		output.addAll(this.getCounts().values());
		output.add(this.getOchiaiValue());
		return output.toString();
	}
}
