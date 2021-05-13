package com.dndsuite.core.json.parsers.subevents;

public interface Calculation {

	public abstract void addBonus(long bonus);

	public abstract void setTo(long set);

	public abstract long get();

	public abstract long getRaw();

}
