package dev.log.activity;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

public class Matrix {

	private ArrayList<ComponentVector> components = new ArrayList<ComponentVector>();
	private ErrorVector error = new ErrorVector();
	
	private Matrix() {
		
	}
	
	public Matrix(ArrayList<ComponentVector> components, ErrorVector error_vector) {
		this.components = components;
		this.error = error_vector;
	}
	
	public Matrix copy() {
		Matrix new_matrix = new Matrix();
		
		for(ComponentVector component : components) {
			ComponentVector new_component = component.copy();
			new_matrix.addComponent(new_component);
		}
		
		ErrorVector new_error = error.copy();
		new_matrix.setError(new_error);

		return new_matrix;
	}
	
	public void addComponent(ComponentVector component) {
		components.add(component);
	}
	
	public void setError(ErrorVector error) {
		this.error = error;
	}
	
	public ErrorVector getError() {
		return this.error;
	}

	/**
	 * Removes component j from the ActivityMatrix and also removes the rows
	 * where the component is hit and the error_vector contains a 1 on that
	 * position.
	 * @param j String representing the component
	 * @throws Exception from removeVector(Object)
	 * @see {@link Matrix#removeComponent(Object)};
	 */
	public void strip(String header) throws Exception {
		ComponentVector removed_column = this.removeComponent(header);

		for(int i = removed_column.getValues().size()-1; i >= 0; i--) {
			if(removed_column.getValue(i) == 1 && error.getValue(i) == 1)
				this.removeRow(i);
		}
	}
	
	/**
	 * Remove the column from the ActivityMatrix with a key as it's identifier.
	 * @param key String representing the key of the vector.
	 * @return vector that was found or null if no vector with key was found.
	 * @throws Exception When the key is not a String.
	 */
	public ComponentVector removeComponent(String key) throws Exception {
		
		for(ComponentVector vector : this.components) {
			String header = vector.getHeader();
			if(header.equals(key)) {
				components.remove(vector);
				return vector;
			}
		}
		
		return null;
	}
	
	public ArrayList<String> getComponentHeaders() {
		ArrayList<String> headers = new ArrayList<String>();
		for(ComponentVector vector : this.components)
			headers.add(vector.getHeader());
		return headers;
	}
	
	public ComponentVector getComponent(String key) throws Exception {
		
		for(ComponentVector vector : this.components) {
			String header = vector.getHeader();
			if(header.equals(key)) {
				return vector;
			}
		}
		
		return null;
	}
	
//	public Integer getVectorElement(Object j, int i) throws Exception {
//		return (Integer)this.getComponent(j).getValue(i);
//	}
	
	public ArrayList<Object> removeRow(int index) {
		
		ArrayList<Object> return_list = new ArrayList<Object>();
		
		Integer errorValue = error.removeValueAt(index);
		
		for(ComponentVector component : this.components) {
			return_list.add(component.removeValueAt(index));
			component.updateCounts(error);
		}
		
		return_list.add(errorValue);	
		
		return return_list;
	}
	
	public ArrayList<Object> getRow(int index) {
		
		index++;
		
		ArrayList<Object> return_list = new ArrayList<Object>();
		
		for(ComponentVector component : this.components) {
			return_list.add(component.getValue(index));
		}
		
		return_list.add(error.getValue(index));		
		
		return return_list;
	}

	public int getComponentCount() {
		return this.components.size();		
	}
	
	@Override
	public String toString() {
		
		ArrayList<ArrayList<String>> matrix = new ArrayList<ArrayList<String>>();
		ArrayList<String> headers = this.getComponentHeaders();
		headers.add(this.error.getHeader());
		matrix.add(headers);

		int valueCount = this.error.getValues().size();
		ArrayList<String> n11 = new ArrayList<String>();
		ArrayList<String> n10 = new ArrayList<String>();
		ArrayList<String> n01 = new ArrayList<String>();
		ArrayList<String> o = new ArrayList<String>();
		
		for(int k = 0; k < valueCount; k++) {
			matrix.add(new ArrayList<String>());
		}

		for(ComponentVector component : components) {
			for(int i = 1; i <= valueCount; i++) {
				matrix.get(i).add(""+component.getValue(i - 1));
			}
			n11.add(""+component.getCounts().get("n11"));
			n10.add(""+component.getCounts().get("n10"));
			n01.add(""+component.getCounts().get("n01"));
			o.add(""+component.getOchiaiValue());
		}

		for(int i = 1; i <= valueCount; i++) {
			matrix.get(i).add(""+error.getValue(i - 1));
		}
		
		matrix.add(n11);matrix.add(n10);matrix.add(n01);matrix.add(o);
		
		String output = "";
		for(ArrayList<String> string : matrix)
			output += string.toString() +"\n";
		
		return output;
	}
	
	public ArrayList<String> getRank() {
		ArrayList<String> rank = new ArrayList<String>();
		
		SortedMap<String,Double> map = new TreeMap<String,Double>();
		
		for(ComponentVector component : this.components) {
			map.put(component.getHeader(),component.getOchiaiValue());
		}

		for(int i = 0; i < map.size(); i++) {
			String key = (String) map.keySet().toArray()[i];
			rank.add(0,key);
		}
		
		return rank;
	}

	public ArrayList<ComponentVector> getComponents() {
		return this.components;
	}

}