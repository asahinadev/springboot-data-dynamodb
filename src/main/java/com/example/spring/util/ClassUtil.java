package com.example.spring.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassUtil {

	public static Type[] getGenericType(Object obj, Class<?> searchType) {

		Class<?> clazz = obj.getClass();

		while (true) {
			List<Type> types = new ArrayList<>();
			types.add(clazz.getGenericSuperclass());
			types.addAll(Arrays.asList(clazz.getGenericInterfaces()));

			for (Type type : types) {

				if (!(type instanceof ParameterizedType)) {
					continue;
				}

				if (!searchType.equals(((ParameterizedType) type).getRawType())) {
					continue;
				}

				Type[] res = ((ParameterizedType) type).getActualTypeArguments();
				if (res != null) {
					return res;
				}
			}

			clazz = clazz.getSuperclass();
			if (clazz.getSimpleName().equals("Object")) {
				break;
			}
		}
		return new Type[0];
	}

	@SuppressWarnings("unchecked")
	public static <E> Class<E> getGenericType(Object obj, Class<?> searchType, int index) {

		return (Class<E>) getGenericType(obj, searchType)[index];
	}

}
