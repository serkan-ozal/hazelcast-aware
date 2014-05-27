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

package com.hazelcast.aware.domain.builder.config;

import java.util.List;

import com.hazelcast.aware.domain.builder.Builder;
import com.hazelcast.aware.domain.model.config.HazelcastAwareClassConfig;
import com.hazelcast.aware.domain.model.config.HazelcastAwareFieldConfig;

/**
 * @author Serkan ÖZAL
 * 
 * Contact Informations:
 * 		GitHub   : https://github.com/serkan-ozal
 * 		LinkedIn : www.linkedin.com/in/serkanozal
 */
public class HazelcastAwareClassConfigBuilder implements Builder<HazelcastAwareClassConfig> {

	private Class<?> clazz;
	private List<HazelcastAwareFieldConfig> fieldConfigs;
	
	public HazelcastAwareClassConfigBuilder clazz(Class<?> clazz) {
		this.clazz = clazz;
		return this;
	}
	
	public HazelcastAwareClassConfigBuilder fieldConfigs(List<HazelcastAwareFieldConfig> fieldConfigs) {
		this.fieldConfigs = fieldConfigs;
		return this;
	}
	
	@Override
	public HazelcastAwareClassConfig build() {
		HazelcastAwareClassConfig config = new HazelcastAwareClassConfig();
		config.setClazz(clazz);
		config.setFieldConfigs(fieldConfigs);
		return config;
	}
	
}
