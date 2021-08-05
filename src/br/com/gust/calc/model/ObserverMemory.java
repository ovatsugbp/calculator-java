package br.com.gust.calc.model;

@FunctionalInterface
public interface ObserverMemory {
	
	public void updatedValue(String newValue);
}
