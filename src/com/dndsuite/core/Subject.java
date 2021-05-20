package com.dndsuite.core;

public interface Subject {

	public abstract void addObserver(Observer o);

	public abstract void removeObserver(Observer o);

	public abstract void updateObservers();

}
