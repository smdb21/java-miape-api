package org.proteored.miapeapi.util;

import java.util.HashMap;

public class HashMapWrapper {

	public static IntegerString[] getArray(HashMap<Integer, String> map) {
		if (map == null)
			return null;
		IntegerString[] ret = new IntegerString[map.size()];
		int i = 0;
		for (Integer integer : map.keySet()) {
			IntegerString is = new IntegerString(integer, map.get(integer));
			ret[i] = is;
			i++;
		}
		return ret;
	}

	public static HashMap<Integer, String> getHashMap(IntegerString[] array) {
		if (array == null || array.length == 0)
			return null;
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		for (IntegerString integerString : array) {
			map.put(integerString.getMiapeID(), integerString.getMiapeType());
		}
		return map;
	}
}
