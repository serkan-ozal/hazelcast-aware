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

package com.hazelcast.aware.domain.model.config;

import java.util.List;

/**
 * @author Serkan ÖZAL
 * 
 * Contact Informations:
 * 		GitHub   : https://github.com/serkan-ozal
 * 		LinkedIn : www.linkedin.com/in/serkanozal
 */
public class HazelcastAwareClassConfig implements HazelcastAwareMergeableConfig<HazelcastAwareClassConfig> {

	private Class<?> clazz;
	private String instanceName;
	private List<HazelcastAwareFieldConfig> fieldConfigs;
	
	public Class<?> getClazz() {
		return clazz;
	}
	
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public String getInstanceName() {
		return instanceName;
	}
	
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	
	public List<HazelcastAwareFieldConfig> getFieldConfigs() {
		return fieldConfigs;
	}
	
	public void setFieldConfigs(List<HazelcastAwareFieldConfig> fieldConfigs) {
		this.fieldConfigs = fieldConfigs;
	}
	
	@Override
	public HazelcastAwareClassConfig merge(HazelcastAwareClassConfig config) {
		if (clazz == null) {
			clazz = config.clazz;
		}
		if (config.fieldConfigs != null && !config.fieldConfigs.isEmpty()) {
			if (fieldConfigs == null || fieldConfigs.isEmpty()) {
				fieldConfigs = config.fieldConfigs;
			}
			else {
				for (HazelcastAwareFieldConfig fieldConfig : config.fieldConfigs) {
					if (!fieldConfigs.contains(fieldConfig)) {
						fieldConfigs.add(fieldConfig);
					}
				}
			}
		}	
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HazelcastAwareClassConfig) {
			return clazz.equals(((HazelcastAwareClassConfig)obj).clazz);
		}
		else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return clazz.hashCode();
	}
	
}
