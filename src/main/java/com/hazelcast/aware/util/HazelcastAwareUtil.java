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

import java.util.Set;

import com.hazelcast.aware.config.DefaultConfigs;
import com.hazelcast.aware.config.manager.ConfigManager;
import com.hazelcast.aware.config.manager.ConfigManagerFactory;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.HazelcastInstanceFactory;

/**
 * @author Serkan ÖZAL
 * 
 * Contact Informations:
 * 		GitHub   : https://github.com/serkan-ozal
 * 		LinkedIn : www.linkedin.com/in/serkanozal
 */
public class HazelcastAwareUtil {
	
	private static final ConfigManager configManager = ConfigManagerFactory.getConfigManager();
	
	private HazelcastAwareUtil() {
		
	}
	
	public static HazelcastInstance getDefaultHazelcastInstance() {
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
	
	public static HazelcastInstance getHazelcastInstance(String instanceName) {
		return HazelcastInstanceFactory.getHazelcastInstance(instanceName);
	}
	
}
