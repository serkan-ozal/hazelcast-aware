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

package com.hazelcast.aware.config.provider.annotation;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hazelcast.aware.config.provider.ConfigProvider;
import com.hazelcast.aware.domain.model.config.HazelcastAwareClassConfig;
import com.hazelcast.aware.domain.model.config.HazelcastAwareFieldConfig;

/**
 * @author Serkan ÖZAL
 * 
 * Contact Informations:
 * 		GitHub   : https://github.com/serkan-ozal
 * 		LinkedIn : www.linkedin.com/in/serkanozal
 */
public class AnnotationBasedConfigProvider implements ConfigProvider {

	private Map<Field, HazelcastAwareFieldConfig> fieldConfigMap = 
				new ConcurrentHashMap<Field, HazelcastAwareFieldConfig>();
	private Map<Class<?>, HazelcastAwareClassConfig> classConfigMap = 
				new ConcurrentHashMap<Class<?>, HazelcastAwareClassConfig>();
	
	@Override
	public boolean isAvailable() {
		return true;
	}
	
	@Override
	public HazelcastAwareFieldConfig getHazelcastAwareFieldConfig(Field field) {
		HazelcastAwareFieldConfig fieldConfig = fieldConfigMap.get(field);
		if (fieldConfig == null) {
			fieldConfig = findHazelcastAwareFieldConfig(field);
			fieldConfigMap.put(field, fieldConfig);
		}
		return fieldConfig;
	}
	
	protected HazelcastAwareFieldConfig findHazelcastAwareFieldConfig(Field field) {
		return null;
	}

	@Override
	public HazelcastAwareClassConfig getHazelcastAwareClassConfig(Class<?> clazz) {
		HazelcastAwareClassConfig classConfig = classConfigMap.get(clazz);
		if (classConfig == null) {
			classConfig = findHazelcastAwareClassConfig(clazz);
			classConfigMap.put(clazz, classConfig);
		}
		return classConfig;
	}
	
	protected HazelcastAwareClassConfig findHazelcastAwareClassConfig(Class<?> clazz) {
		return null;
	}
	
}
