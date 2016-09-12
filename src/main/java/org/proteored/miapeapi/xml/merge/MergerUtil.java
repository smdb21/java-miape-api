package org.proteored.miapeapi.xml.merge;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MergerUtil {
	/**
	 * Given two objects, gives the returned value of invoking a method (given a
	 * name) of the object that returns a non null value.<br>
	 * if the two objects return a non null value, the method will return the
	 * value from the objectOverriting
	 * 
	 * @param objectOriginal
	 * @param objectOverriting
	 * @param methodName
	 * @param name
	 * @return
	 */
	public static Object getNonNullValue(Object objectOriginal, Object objectOverriting,
			String methodName) {
		Object returnedObject2 = null;
		Object returnedObject1 = null;
		try {
			if (objectOriginal != null) {
				Class class1 = Class.forName(objectOriginal.getClass().getName());
				Method method1 = class1.getMethod(methodName, null);
				returnedObject1 = method1.invoke(objectOriginal, null);
			}
			if (objectOverriting != null) {
				Class class2 = Class.forName(objectOverriting.getClass().getName());
				Method method2 = class2.getMethod(methodName, null);
				returnedObject2 = method2.invoke(objectOverriting, null);
			}
			if (returnedObject2 != null)
				return returnedObject2;
			else if (returnedObject1 != null) {
				return returnedObject1;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
