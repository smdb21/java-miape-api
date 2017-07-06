package org.proteored.miapeapi.util;

import gnu.trove.map.hash.TIntObjectHashMap;

public class HashMapWrapper {

	public static IntegerString[] getArray(TIntObjectHashMap<String> map) {
		if (map == null)
			return null;
		IntegerString[] ret = new IntegerString[map.size()];
		int i = 0;
		for (int integer : map.keys()) {
			IntegerString is = new IntegerString(integer, map.get(integer));
			ret[i] = is;
			i++;
		}
		return ret;
	}

	public static TIntObjectHashMap<String> getHashMap(IntegerString[] array) {
		if (array == null || array.length == 0)
			return null;
		TIntObjectHashMap<String> map = new TIntObjectHashMap<String>();
		for (IntegerString integerString : array) {
			map.put(integerString.getMiapeID(), integerString.getMiapeType());
		}
		return map;
	}
}
