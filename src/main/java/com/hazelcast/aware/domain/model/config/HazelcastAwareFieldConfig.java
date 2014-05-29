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

import java.lang.reflect.Field;

import com.hazelcast.aware.util.StringUtil;

/**
 * @author Serkan ÖZAL
 * 
 * Contact Informations:
 * 		GitHub   : https://github.com/serkan-ozal
 * 		LinkedIn : www.linkedin.com/in/serkanozal
 */
public class HazelcastAwareFieldConfig implements HazelcastAwareMergeableConfig<HazelcastAwareFieldConfig> {

	private Class<?> ownerClass;
	private Field field;
	private String instanceName;
	private HazelcastAwareMapFieldConfig mapFieldConfig;
	private HazelcastAwareListFieldConfig listFieldConfig;
	private HazelcastAwareSetFieldConfig setFieldConfig;
	private HazelcastAwareQueueFieldConfig queueFieldConfig;
	private HazelcastAwareTopicFieldConfig topicFieldConfig;
	
	public Class<?> getOwnerClass() {
		return ownerClass;
	}
	
	public void setOwnerClass(Class<?> ownerClass) {
		this.ownerClass = ownerClass;
	}
	
	public Field getField() {
		return field;
	}
	
	public void setField(Field field) {
		this.field = field;
	}
	
	public String getInstanceName() {
		return instanceName;
	}
	
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	
	public HazelcastAwareMapFieldConfig getMapFieldConfig() {
		return mapFieldConfig;
	}
	
	public void setMapFieldConfig(HazelcastAwareMapFieldConfig mapFieldConfig) {
		this.mapFieldConfig = mapFieldConfig;
	}
	
	public HazelcastAwareListFieldConfig getListFieldConfig() {
		return listFieldConfig;
	}

	public void setListFieldConfig(HazelcastAwareListFieldConfig listFieldConfig) {
		this.listFieldConfig = listFieldConfig;
	}

	public HazelcastAwareSetFieldConfig getSetFieldConfig() {
		return setFieldConfig;
	}

	public void setSetFieldConfig(HazelcastAwareSetFieldConfig setFieldConfig) {
		this.setFieldConfig = setFieldConfig;
	}

	public HazelcastAwareQueueFieldConfig getQueueFieldConfig() {
		return queueFieldConfig;
	}

	public void setQueueFieldConfig(HazelcastAwareQueueFieldConfig queueFieldConfig) {
		this.queueFieldConfig = queueFieldConfig;
	}

	public HazelcastAwareTopicFieldConfig getTopicFieldConfig() {
		return topicFieldConfig;
	}

	public void setTopicFieldConfig(HazelcastAwareTopicFieldConfig topicFieldConfig) {
		this.topicFieldConfig = topicFieldConfig;
	}

	@Override
	public HazelcastAwareFieldConfig merge(HazelcastAwareFieldConfig config) {
		if (ownerClass == null) {
			ownerClass = config.ownerClass;
		}
		if (field == null) {
			field = config.field;
		}
		if (StringUtil.isEmpty(instanceName)) {
			instanceName = config.instanceName;
		}
		if (mapFieldConfig == null) {
			mapFieldConfig = config.mapFieldConfig;
		}
		if (listFieldConfig == null) {
			listFieldConfig = config.listFieldConfig;
		}
		if (setFieldConfig == null) {
			setFieldConfig = config.setFieldConfig;
		}
		if (queueFieldConfig == null) {
			queueFieldConfig = config.queueFieldConfig;
		}
		if (topicFieldConfig == null) {
			topicFieldConfig = config.topicFieldConfig;
		}
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HazelcastAwareFieldConfig) {
			return field.equals(((HazelcastAwareFieldConfig)obj).field);
		}
		else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return field.hashCode();
	}
	
}
