package com.dndsuite.core;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.dndsuite.exceptions.UUIDDoesNotExistException;
import com.dndsuite.exceptions.UUIDNotAssignedException;

public final class UUIDTable {

	private static ConcurrentHashMap<Long, UUIDTableElement> table = new ConcurrentHashMap<Long, UUIDTableElement>();

	public static void addToTable(UUIDTableElement element) {
		long key;
		try {
			key = element.getUUID();
		} catch (UUIDNotAssignedException e) {
			Random r = new Random();
			do {
				key = r.nextInt(Integer.MAX_VALUE);
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
