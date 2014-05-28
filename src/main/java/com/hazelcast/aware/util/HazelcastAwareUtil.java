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

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import com.hazelcast.aware.config.DefaultConfigs;
import com.hazelcast.aware.config.manager.ConfigManager;
import com.hazelcast.aware.config.manager.ConfigManagerFactory;
import com.hazelcast.aware.domain.model.config.HazelcastAwareClassConfig;
import com.hazelcast.aware.domain.model.config.HazelcastAwareFieldConfig;
import com.hazelcast.aware.domain.model.config.HazelcastAwareMapFieldConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.HazelcastInstanceFactory;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

/**
 * @author Serkan ÖZAL
 * 
 * Contact Informations:
 * 		GitHub   : https://github.com/serkan-ozal
 * 		LinkedIn : www.linkedin.com/in/serkanozal
 */
public class HazelcastAwareUtil {
	
	private static final ILogger logger = Logger.getLogger(HazelcastAwareUtil.class);
	
	private static final ConfigManager configManager = ConfigManagerFactory.getConfigManager();
	
	private HazelcastAwareUtil() {
		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> void injectHazelcast(T obj) {
		if (obj != null) {
			Class<T> clazz = (Class<T>) obj.getClass();
			HazelcastAwareClassConfig classConfig = 
					configManager.getHazelcastAwareClassConfig(clazz);
			if (classConfig != null) {
				HazelcastInstance hazelcastInstance = null;
				if (!StringUtil.isEmpty(classConfig.getInstanceName())) {
					hazelcastInstance = getHazelcastInstance(classConfig.getInstanceName());
				}
				if (hazelcastInstance == null) {
					hazelcastInstance = getDefaultHazelcastInstance();
				}
				if (hazelcastInstance != null) {
					List<HazelcastAwareFieldConfig> fieldConfigs = classConfig.getFieldConfigs();
					if (fieldConfigs != null) {
						for (HazelcastAwareFieldConfig fieldConfig : fieldConfigs) {
							Field field = fieldConfig.getField();
							if (field != null) {
								HazelcastInstance fieldHazelcastInstance = null;
								if (!StringUtil.isEmpty(fieldConfig.getInstanceName())) {
									fieldHazelcastInstance = getHazelcastInstance(fieldConfig.getInstanceName());
								}
								if (fieldHazelcastInstance == null) {
									fieldHazelcastInstance = hazelcastInstance;
								}
								
								processMapFieldConfig(
										fieldHazelcastInstance, 
										fieldConfig, 
										fieldConfig.getMapFieldConfig(),
										obj,
										field);
								
								// TODO Process other field configurations such as Lists, Locks, Objects, ...
							}
						}
					}
				}
				else {
					logger.log(Level.ALL, "There is no Hazelcast instance to inject");
				}
			}
			else {
				logger.log(Level.ALL, "Class config is not exist for class " + clazz.getName());
			}
		}
		else {
			logger.log(Level.ALL, "Object to inject Hazelcast is null");
		}
	}
	
	private static <T> void processMapFieldConfig(HazelcastInstance hazelcastInstance, 
			HazelcastAwareFieldConfig fieldConfig,
			HazelcastAwareMapFieldConfig mapFieldConfig,
			T obj, Field field) {
		if (mapFieldConfig != null) {
			String mapName = mapFieldConfig.getMapName();
			if (StringUtil.isEmpty(mapName)) {
				mapName = obj.getClass().getName() + "_" + field.getName();
			}
			try {
				field.set(obj, hazelcastInstance.getMap(mapName));
			} 
			catch (Throwable t) {
				logger.log(
					Level.ALL, 
					"Unable to set Hazelcast map for field " + field.getName() + 
						" in class " + obj.getClass().getName(), 
					t);
			} 
		}
	}
	
	private static HazelcastInstance getDefaultHazelcastInstance() {
		DefaultConfigs defaultConfigs = configManager.getDefaultConfigs();
		if (defaultConfigs != null && !StringUtil.isEmpty(defaultConfigs.getDefaultInstanceName())) {
			HazelcastInstance hazelcastInstance = getHazelcastInstance(defaultConfigs.getDefaultInstanceName());
			if (hazelcastInstance != null) {
				return hazelcastInstance;
			}
		}
		Set<HazelcastInstance> hazelcastInstances = HazelcastInstanceFactory.getAllHazelcastInstances();
		if (hazelcastInstances == null || hazelcastInstances.isEmpty()) {
			return null;
		}
		else {
			return hazelcastInstances.iterator().next();
		}	
	}
	
	private static HazelcastInstance getHazelcastInstance(String instanceName) {
		return HazelcastInstanceFactory.getHazelcastInstance(instanceName);
	}
	
}
