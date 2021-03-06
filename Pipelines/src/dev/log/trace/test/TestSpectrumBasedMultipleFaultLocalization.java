package dev.log.trace.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import dev.log.barinel.Barinel;
import dev.log.barinel.activity.Matrix;

public class TestSpectrumBasedMultipleFaultLocalization {

	public static void main(String[] args) throws Exception {
		
		System.out.println(Barinel.calculate(readFile()));
//		ComponentVector component1 = new ComponentVector();
//		ComponentVector component2 = new ComponentVector();
//		ComponentVector component3 = new ComponentVector();
//		ErrorVector error = new ErrorVector();
//
//		error.setHeader("ei");error.addValue(1);error.addValue(1);error.addValue(0);
//		
//		component1.setHeader("c1");component1.addValue(1);component1.addValue(0);component1.addValue(1);component1.updateCounts(error);
//		component2.setHeader("c2");component2.addValue(0);component2.addValue(1);component2.addValue(0);component2.updateCounts(error);
//		component3.setHeader("c3");component3.addValue(1);component3.addValue(1);component3.addValue(1);component3.updateCounts(error);
//
//		ArrayList<ComponentVector> components = new ArrayList<ComponentVector>();
//		
//		components.add(component1);
//		components.add(component2);
//		components.add(component3);
//		
//		Matrix matrix = new Matrix(components,error);
		
//		Matrix matrix = new Matrix();
//		ArrayList<String> execution = new ArrayList<String>();
//		execution.add("c1");
//		execution.add("c2");
//		matrix.addExecution(execution, 1);
//		execution = new ArrayList<String>();
//		execution.add("c2");
//		execution.add("c3");
//		matrix.addExecution(execution, 1);
//		execution = new ArrayList<String>();
//		execution.add("c1");
//		matrix.addExecution(execution, 1);
//		execution.add("c1");
//		execution.add("c3");
//		matrix.addExecution(execution, 0);
//		
//		SpectrumBasedMultipleFaultLocalization local = new SpectrumBasedMultipleFaultLocalization();
//		System.out.println("END: "+local.calculate(matrix));
	}
	
	public static Matrix readFile() throws IOException {
		String file = "matrix_even_smaller.txt";
		BufferedReader reader = new BufferedReader(new FileReader(file));
		Matrix matrix = new Matrix();
		String line;
		ArrayList<String> execution;
		while((line = reader.readLine()) != null){
			execution = new ArrayList<String>();
			
			String[] parts = line.split("\t");
			int error = 0;
			if(parts[1].equals("-")) error = 1;
			
			parts = parts[0].split(" ");
			
			for(int i = 0; i < parts.length; i++)
				if(Integer.parseInt(parts[i]) == 1)
					execution.add("c"+i);
			
			matrix.addExecution(execution, error);
		}
		return matrix;
	}
}
