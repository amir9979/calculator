package dev.log.barinel.activity;

import java.util.ArrayList;

/**
 * The vector is the base of the parts of the activity matrix. This class is
 * extended by ComponentVector and ErrorVector to give some base vector
 * manipulation capabilities to these classes.
 * 
 * @author Joel van den Berg
 *
 * @param <K> The type of the identifier of this vector
 * @param <V> The type of the values of this vector
 */
abstract class Vector<K,V> {
	
	/**
	 * The name of this vector
	 */
	private K header;
	/**
	 * The values of this vector
	 */
	private ArrayList<V> values;

	/**
	 * Create an empty vector with no header of values
	 */
	public Vector() {
		values = new ArrayList<V>();
	}
	
	/**
	 * Create a new instance of Vector with the header and values
	 * @param header The header for this vector
	 * @param values The ArrayList of values for this vector
	 */
	public Vector(K header, ArrayList<V> values) {
		this();
		this.header = header;
		this.values = values;
	}
	
	/**
	 * Get the header for this vector
	 * @return The header for this vector
	 */
	public K getHeader() {
		return this.header;
	}
	
	/**
	 * Set the header for this vector
	 * @param header The header to be set
	 */
	public void setHeader(K header) {
		this.header = header;
	}
	
	/**
	 * Gets the set of values of this vector.
	 * @return The list of values
	 */
	public ArrayList<V> getValues() {
		return this.values;
	}
	
	/**
	 * @see ArrayList#add(Object)
	 */
	public boolean addValue(V value) {
		return this.values.add(value);
	}
	
	/**
	 * @see ArrayList#add(int, Object)
	 */
	public void addValueAt(int index, V value) {
		this.values.add(index,value);
	}
	
	/**
	 * @see ArrayList#remove(Object)
	 */
	public boolean removeValue(V value) {
		return this.values.remove(value);
	}
	
	/**
	 * @see ArrayList#remove(int)
	 */
	public V removeValueAt(int index) {
		return this.values.remove(index);
	}
	
	/**
	 * @see ArrayList#get(int)
	 */
	public V getValue(int index) {
		return this.values.get(index);
	}
	
	/**
	 * @see ArrayList#set(int, Object)
	 */
	public V setValue(int index, V value) {
		return this.values.set(index, value);
	}
}
