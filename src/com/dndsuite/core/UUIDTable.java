package com.dndsuite.core;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.dndsuite.core.json.JSONLoader;
import com.dndsuite.exceptions.UUIDDoesNotExistException;
import com.dndsuite.exceptions.UUIDKeyMissingException;

public final class UUIDTable {

	private static ConcurrentHashMap<Integer, JSONLoader> table = new ConcurrentHashMap<Integer, JSONLoader>();

	@SuppressWarnings("unchecked")
	public static void addToTable(JSONLoader element) {
		int key;
		try {
			key = element.getUUID();
		} catch (UUIDKeyMissingException e) {
			Random r = new Random();
			do {
				key = r.nextInt(Integer.MAX_VALUE);
			} while (table.containsKey(key));
			element.getJSONData().put("uuid", key);
		}
		table.put(key, element);
	}

	public static JSONLoader get(int uuid) throws UUIDDoesNotExistException {
		if (!table.containsKey(uuid)) {
			throw new UUIDDoesNotExistException(uuid);
		}
		return table.get(uuid);
	}

	public static void clear() {
		table.clear();
	}

	public static boolean containsKey(int uuid) {
		return table.containsKey(uuid);
	}

	public static int size() {
		return table.size();
	}

}
