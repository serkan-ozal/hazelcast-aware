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

package com.hazelcast.aware.config.provider.xml;

import java.lang.reflect.Field;
import java.util.Set;

import com.hazelcast.aware.config.provider.ConfigProvider;
import com.hazelcast.aware.config.provider.HazelcastAwareConfigProvider;
import com.hazelcast.aware.domain.model.config.HazelcastAwareClassConfig;
import com.hazelcast.aware.domain.model.config.HazelcastAwareFieldConfig;
import com.hazelcast.aware.initializer.HazelcastAwareInitializer;
import com.hazelcast.aware.injector.HazelcastAwareInjector;
import com.hazelcast.aware.processor.HazelcastAwareProcessor;

/**
 * @author Serkan ÖZAL
 * 
 * Contact Informations:
 * 		GitHub   : https://github.com/serkan-ozal
 * 		LinkedIn : www.linkedin.com/in/serkanozal
 */
public class XmlBasedConfigProvider implements ConfigProvider {

	@Override
	public boolean isAvailable() {
		return false;
	}
	
	@Override
	public Set<Class<?>> getHazelcastAwareClasses() {
		return null;
	}
	
	@Override
	public Set<Class<? extends HazelcastAwareConfigProvider>> getHazelcastAwareConfigProviderClasses() {
		return null;
	}
	
	@Override
	public Set<Class<? extends HazelcastAwareProcessor>> getHazelcastAwareProcessorClasses() {
		return null;
	}
	
	@Override
	public Set<Class<? extends HazelcastAwareInjector<?>>> getHazelcastAwareInjectorClasses() {
		return null;
	}
	
	@Override
	public Set<Class<? extends HazelcastAwareInitializer>> getHazelcastAwareInitializerClasses() {
		return null;
	}

	@Override
	public HazelcastAwareFieldConfig getHazelcastAwareFieldConfig(Field field) {
		return null;
	}

	@Override
	public HazelcastAwareClassConfig getHazelcastAwareClassConfig(Class<?> clazz) {
		return null;
	}

}
