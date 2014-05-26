/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.aware.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Serkan ÖZAL
 * 
 * Contact Informations:
 * 		GitHub   : https://github.com/serkan-ozal
 * 		LinkedIn : www.linkedin.com/in/serkanozal
 */
public class ReflectionUtil {
	
	private ReflectionUtil() {
		
	}
	
	public static Field getField(Class<?> cls, String fieldName) {
		if (cls == null || cls.equals(Object.class)) {
			return null;
		}
		try {
			return cls.getDeclaredField(fieldName);
		} 
		catch (Exception e) {
			return getField(cls.getSuperclass(), fieldName);
		} 
	}
	
	public static Class<?> getFieldType(Class<?> cls, String fieldName) {
		if (cls == null || cls.equals(Object.class)) {
			return null;
		}
		Field field = getField(cls, fieldName);
		if (field == null) {
			return null;
		}
		else {
			return field.getType();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(Object obj, String fieldName) 
			throws 	SecurityException, NoSuchFieldException, 
					IllegalArgumentException, IllegalAccessException {
		Class<?> cls = obj.getClass();
		Field field = cls.getDeclaredField(fieldName);
		field.setAccessible(true);
		return (T)field.get(obj);
	}
	
	public static List<Field> getAllFields(Class<?> cls) {
		List<Field> fields = new ArrayList<Field>();
		createFields(cls, fields, null);
		return fields;
	}
	
	public static List<Field> getAllFields(Class<?> cls, Class<? extends Annotation> annotationFilter) {
		List<Field> fields = new ArrayList<Field>();
		createFields(cls, fields, annotationFilter);
		return fields;
	}
	
	private static void createFields(Class<?> cls, 
			List<Field> fields, Class<? extends Annotation> annotationFilter) {
		if (cls == null || cls.equals(Object.class)) {
			return;
		}
		
		Class<?> superCls = cls.getSuperclass();
		createFields(superCls, fields, annotationFilter);
		
		for (Field f : cls.getDeclaredFields()) {
			f.setAccessible(true);
			if (annotationFilter == null) {
				fields.add(f);
			}	
			else {
				if (f.getAnnotation(annotationFilter) != null) {
					fields.add(f);
				}	
			}
		}	
	}
	
	public static List<Method> getAllMethods(Class<?> cls) {
		List<Method> methods = new ArrayList<Method>();
		createMethods(cls, methods, null);
		return methods;
	}
	
	public static List<Method> getAllMethods(Class<?> cls, Class<? extends Annotation> annotationFilter) {
		List<Method> methods = new ArrayList<Method>();
		createMethods(cls, methods, annotationFilter);
		return methods;
	}
	
	private static void createMethods(Class<?> cls, List<Method> methods, 
			Class<? extends Annotation> annotationFilter) {
		if (cls == null || cls.equals(Object.class)) {
			return;
		}
		
		Class<?> superCls = cls.getSuperclass();
		createMethods(superCls, methods, annotationFilter);
		
		for (Method m : cls.getDeclaredMethods()) {	
			if (annotationFilter == null) {
				methods.add(m);
			}	
			else {
				if (m.getAnnotation(annotationFilter) != null) {
					methods.add(m);
				}	
			}
		}	
	}
	
	public static interface Filter<T> {
		public boolean allow(T objToFilter);
	}
	
	public static interface FieldFilter extends Filter<Field> {
		// Override public boolean allow(Field fieldToFilter) ...
	}
	
	public static interface MethodFilter extends Filter<Method> {
		// Override public boolean allow(Method methodToFilter) ...
	}
	
	public static interface ClassFilter extends Filter<Class<?>> {
		// Override public boolean allow(Method clsToFilter) ...
	}
	
	public static abstract class AnnotatedFilter {
		
		protected Class<? extends Annotation> annotation;
		
		public AnnotatedFilter(Class<? extends Annotation> annotation) {
			this.annotation = annotation;
		}

	}
	
	public static class AnnotatedFieldFilter extends AnnotatedFilter implements FieldFilter {

		public AnnotatedFieldFilter(Class<? extends Annotation> annotation) {
			super(annotation);
		}

		@Override
		public boolean allow(Field fieldToFilter) {
			return fieldToFilter.getAnnotation(annotation) != null;
		}
		
	}
	
	public static class AnnotatedMethodFilter extends AnnotatedFilter implements MethodFilter {

		public AnnotatedMethodFilter(Class<? extends Annotation> annotation) {
			super(annotation);
		}

		@Override
		public boolean allow(Method methodToFilter) {
			return methodToFilter.getAnnotation(annotation) != null;
		}
		
	}
	
	public static class AnnotatedClassFilter extends AnnotatedFilter implements ClassFilter {

		public AnnotatedClassFilter(Class<? extends Annotation> annotation) {
			super(annotation);
		}

		@Override
		public boolean allow(Class<?> clsToFilter) {
			return clsToFilter.getAnnotation(annotation) != null;
		}
		
	}
	
	public static List<Field> getFilteredFields(Class<?> cls, 
			ClassFilter clsFilter, FieldFilter fieldFilter) {
		List<Field> fields = new ArrayList<Field>();
		createFilteredFields(cls, fields, clsFilter, fieldFilter);
		return fields;
	}
	
	private static void createFilteredFields(Class<?> cls, List<Field> fields, 
			ClassFilter clsFilter, FieldFilter filter) {
		if (cls == null || cls.equals(Object.class)) {
			return;
		}
		
		Class<?> superCls = cls.getSuperclass();
		if (clsFilter == null || clsFilter.allow(superCls)) {
			createFilteredFields(superCls, fields, clsFilter, filter);
		}
		
		for (Field f : cls.getDeclaredFields()) {
			f.setAccessible(true);
			if (filter == null || filter.allow(f)) {
				fields.add(f);
			}	
		}	
	}
	
	public static List<Method> getFilteredMethods(Class<?> cls, 
			ClassFilter clsFilter, MethodFilter methodFilter) {
		List<Method> methods = new ArrayList<Method>();
		createFilteredMethods(cls, methods, clsFilter, methodFilter);
		return methods;
	}
	
	private static void createFilteredMethods(Class<?> cls, List<Method> methods, 
			ClassFilter clsFilter, MethodFilter methodFilter) {
		if (cls == null || cls.equals(Object.class)) {
			return;
		}
		
		Class<?> superCls = cls.getSuperclass();
		if (clsFilter == null || clsFilter.allow(superCls)) {
			createFilteredMethods(superCls, methods, clsFilter, methodFilter);
		}
		
		for (Method m : cls.getDeclaredMethods()) {	
			m.setAccessible(true);
			if (methodFilter == null || methodFilter.allow(m)) {
				methods.add(m);
			}	
		}	
	}
	
}
