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

package com.hazelcast.aware.config.manager;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import com.hazelcast.aware.config.DefaultConfigs;
import com.hazelcast.aware.config.provider.ConfigProvider;
import com.hazelcast.aware.config.provider.HazelcastAwareConfigProvider;
import com.hazelcast.aware.domain.model.config.HazelcastAwareClassConfig;
import com.hazelcast.aware.domain.model.config.HazelcastAwareFieldConfig;

/**
 * @author Serkan ÖZAL
 * 
 * Contact Informations:
 * 		GitHub   : https://github.com/serkan-ozal
 * 		LinkedIn : www.linkedin.com/in/serkanozal
 */
public interface ConfigManager {

	void addConfigProvider(ConfigProvider configProvider);
	void removeConfigProvider(ConfigProvider configProvider);
	List<ConfigProvider> getAllConfigProviders();
	
	DefaultConfigs getDefaultConfigs();
	
	Set<Class<?>> getHazelcastAwareClasses();
	Set<Class<? extends HazelcastAwareConfigProvider>> getHazelcastAwareConfigProviderClasses();
	
	HazelcastAwareFieldConfig getHazelcastAwareFieldConfig(Field field);
	HazelcastAwareClassConfig getHazelcastAwareClassConfig(Class<?> clazz);
	
}
