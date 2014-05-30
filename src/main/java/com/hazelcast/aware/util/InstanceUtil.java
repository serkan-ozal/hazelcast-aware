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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

/**
 * @author Serkan ÖZAL
 * 
 * Contact Informations:
 * 		GitHub   : https://github.com/serkan-ozal
 * 		LinkedIn : www.linkedin.com/in/serkanozal
 */
public class InstanceUtil {

	private static final ILogger logger = Logger.getLogger(InstanceUtil.class);
	
	private static final Map<Class<?>, Object> singleInstances = new ConcurrentHashMap<Class<?>, Object>();
	
	private InstanceUtil() {
		
	}
	
	public static <T> T getPrototypeInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		}
		catch (Throwable t) {
			logger.log(Level.ALL, "Unable to create instance of class " + clazz.getSimpleName(), t);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getSingleInstance(Class<T> clazz) {
		try {
			T instance = (T) singleInstances.get(clazz);
			if (instance == null) {
				instance = clazz.newInstance();
				singleInstances.put(clazz, instance);
			}
			return instance;
		}
		catch (Throwable t) {
			logger.log(Level.ALL, "Unable to create instance of class " + clazz.getSimpleName(), t);
			return null;
		}
	}
	
	public static <T> T getInstance(Class<T> clazz, boolean single) {
		if (single) {
			return getSingleInstance(clazz);
		}
		else {
			return getPrototypeInstance(clazz);
		}
	}
	
}
