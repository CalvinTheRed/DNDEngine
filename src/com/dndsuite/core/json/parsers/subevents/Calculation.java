package com.dndsuite.core.json.parsers.subevents;

public interface Calculation {

	public abstract void addBonus(int bonus);

	public abstract void setTo(int set);

	public abstract int get();

}
