package dev.log.barinel;

import java.util.ArrayList;
import java.util.HashMap;

import dev.log.barinel.activity.ErrorVector;
import dev.log.barinel.activity.Matrix;
import dev.log.barinel.health.Function;
import dev.log.barinel.health.Term;
import dev.log.barinel.mhs.HittingSet;

/**
 * This algorithm calculates the order in which to analyze the different solutions
 * to the AIM algorithm. It does so by calculating for the diagnosis set the maximum
 * likelihood estimations and comparing them with each other.
 * 
 * This algorithm is purely used to save time during the analysis, as components in
 * a combination might be more likely to cause the error than others.
 * 
 * @author Joel van den Berg
 */
public class Barinel {

	public static ArrayList<HittingSet> calculate(Matrix a) {
		ArrayList<HittingSet> hittingSets = a.getHittingSets();
		ErrorVector errorVector = a.getError();
		double p = 0.01; // from c
		double pre = errorVector.getChance();
		for(HittingSet set : hittingSets) {
			Function function = null;
			ArrayList<Term> terms = new ArrayList<Term>();
			int i = 0;
			for(Integer error : errorVector.getValues()) {
				ArrayList<String> components = new ArrayList<String>();
				for(String c : set.getComponents()) {
					if(a.getComponent(c).getValue(i).equals(1)) {
						components.add(c);
					}
				}
				Term t = new Term(components, error);
				terms.add(t);
				i++;
			}
			function = new Function(terms);
			HashMap<String,Float> healthValues = function.maximize();
			double prf = (function.calculate(healthValues)) / pre * hittingSets.size();  
			set.setError(prf);
		}
		a.sortHittingSetsByError();
		return hittingSets;
	}
	
}
