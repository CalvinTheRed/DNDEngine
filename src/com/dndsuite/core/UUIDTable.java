package com.dndsuite.core;

import java.util.concurrent.ConcurrentHashMap;

import com.dndsuite.exceptions.UUIDDoesNotExistException;

/**
 * This class contains all objects in the library which possess a UUID value.
 * All such objects, upon being loaded, should be passed to this class, and all
 * objects which are saved when a save is created will be from this class.
 * 
 * @author Calvin Withun
 *
 */
public final class UUIDTable {

	private static ConcurrentHashMap<Long, UUIDTableElement> table = new ConcurrentHashMap<Long, UUIDTableElement>();

	public static void addToTable(UUIDTableElement element) {
		long key = element.getUUID();
		if (key == -1L) {
			do {
				key = (long) (Math.random() * Long.MAX_VALUE);
			} while (table.containsKey(key));
			element.assignUUID(key);
		}
		table.put(key, element);
	}

	public static UUIDTableElement get(long uuid) throws UUIDDoesNotExistException {
		if (!table.containsKey(uuid)) {
			throw new UUIDDoesNotExistException(uuid);
		}
		return table.get(uuid);
	}

	public static void clear() {
		table.clear();
	}

	public static boolean containsKey(long uuid) {
		return table.containsKey(uuid);
	}

	public static int size() {
		return table.size();
	}

}
