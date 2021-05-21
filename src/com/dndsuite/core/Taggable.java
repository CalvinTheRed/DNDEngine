package com.dndsuite.core;

/**
 * Taggable is an iterface which represents an object which can be given
 * arbitrary tags to assist in being sorted at a later time.
 * 
 * @author Calvin Withun
 *
 */
public interface Taggable {

	public abstract void addTag(String tag);

	public abstract void removeTag(String tag);

	public abstract boolean hasTag(String tag);

}
