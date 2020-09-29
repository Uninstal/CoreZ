package org.uninstal.corez.data;

public class Bar {

	private String name;
	
	private int min;
	private int max;

	private int value;

	public Bar(String name, int min, int max, int value) {
		
		this.name = name;
		this.min = min;
		this.max = max;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public int getMin() {
		return min;
	}
	
	public int getMax() {
		return max;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = Math.min(100, Math.max(value, 0));
	}
}
