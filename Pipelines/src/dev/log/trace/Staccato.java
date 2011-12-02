package dev.log.trace;

import java.util.ArrayList;

import dev.log.activity.ComponentVector;
import dev.log.activity.Matrix;

/**
 * Staccato algorithm described by Rui Abreu and Arjan J.C. van Gemund
 * for finding a minimal hitting set.
 * 
 * @author Joel van den Berg
 *
 */

public class Staccato {	
	
	static public ArrayList<ArrayList<String>> calculate(Matrix matrix, double lambda, int l) throws Exception{
		
		int m = matrix.getComponentCount();
		
		/**
		 * Count conflicting sets
		 */
		int Tf = 0;
		for(Integer i : matrix.getError().getValues()) {
			Tf += i;
		}	
		
		ArrayList<String> r = matrix.getRank();
		
		/**
		 * Initiate empty conflict set
		 */
		ArrayList<ArrayList<String>> d = new ArrayList<ArrayList<String>>();
		
		/**
		 * Initiate the part of the matrix we have seen
		 */
		double seen = 0.0;
		
		Matrix matrix_copy = matrix.copy();
		for(ComponentVector component : matrix_copy.getComponents()) {
			if (component.getCounts().get("n11") == Tf) {
				ArrayList<String> set = new ArrayList<String>();
				set.add(component.getHeader());
				d.add(set);
				/* Strip_Component(A,j) */
				matrix.removeComponent(component.getHeader());
				r.remove(component.getHeader());
				seen += 1/m;
			}
		}
		
		//System.out.println("Matrix after 5-12: " + matrix);
		
		while(!r.isEmpty() && seen <= lambda && d.size() <= l) {
			String j = r.remove(0);
			seen += 1/m;
			
			Matrix new_matrix = matrix.copy();
			new_matrix.strip(j);

			//System.out.println("recursion" + new_matrix);
			ArrayList<ArrayList<String>> dprime = Staccato.calculate(new_matrix, lambda, l);
			//System.out.println("end recursion" + new_matrix);
			
			while(!dprime.isEmpty()) {
				ArrayList<String> jprime = dprime.remove(0);
				jprime.add(j);
				if(!isSubsumed(d,jprime))
					d.add(jprime);
			}
		}
		
		return d;
	}

	/**
	 * Returns true if the an element in d contains the elements in jprime.
	 * Take for example d = [[1,2],[2,3,4],[1,4]]
	 * 					jprime = [1,2,3,4]
	 * Then this should return true.
	 * @param d
	 * @param jprime
	 * @return true when jprime is subsumed by d
	 */
	static public boolean isSubsumed(ArrayList<ArrayList<String>> d,
			ArrayList<String> jprime) {
		
		for(ArrayList<String> item : d) {
			boolean subsumed = true;
			for(String c : item)
				if(!jprime.contains(c)) 
					subsumed = false;
			if(subsumed)
				return true;
		}

		return false;
	}

}