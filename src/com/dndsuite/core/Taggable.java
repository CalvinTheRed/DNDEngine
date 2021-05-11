package com.dndsuite.core;

public interface Taggable {

	public abstract void addTag(String tag);

	public abstract void removeTag(String tag);

	public abstract boolean hasTag(String tag);

}
