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

import java.util.ArrayList;
import java.util.List;

import com.hazelcast.aware.config.DefaultConfigs;
import com.hazelcast.aware.config.provider.ConfigProvider;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

/**
 * @author Serkan ÖZAL
 * 
 * Contact Informations:
 * 		GitHub   : https://github.com/serkan-ozal
 * 		LinkedIn : www.linkedin.com/in/serkanozal
 */
public abstract class BaseConfigManager implements ConfigManager {

	protected final ILogger logger = Logger.getLogger(getClass());
	
	protected List<ConfigProvider> configProviderList = new ArrayList<ConfigProvider>();
	protected DefaultConfigs defaultConfigs = new DefaultConfigs();
	
	public BaseConfigManager() {
		init();
	}
	
	abstract protected void init();
	
	protected void addConfigProviderIfAvailable(ConfigProvider configProvider) {
		if (configProvider.isAvailable()) {
			configProviderList.add(configProvider);
		}
	}

	@Override
	public void addConfigProvider(ConfigProvider configProvider) {
		configProviderList.add(configProvider);
	}
	
	@Override
	public void removeConfigProvider(ConfigProvider configProvider) {
		configProviderList.remove(configProvider);
	}
	
	@Override
	public List<ConfigProvider> getAllConfigProviders() {
		return configProviderList;
	}
	
	@Override
	public DefaultConfigs getDefaultConfigs() {
		return defaultConfigs;
	}
	
}
