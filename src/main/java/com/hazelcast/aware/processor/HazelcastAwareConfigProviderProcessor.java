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

package com.hazelcast.aware.processor;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import com.hazelcast.aware.config.manager.ConfigManager;
import com.hazelcast.aware.config.provider.HazelcastAwareConfigProvider;
import com.hazelcast.aware.util.InstanceUtil;
import com.hazelcast.config.Config;
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
public class HazelcastAwareConfigProviderProcessor implements HazelcastAwareProcessor {

	private static final ILogger logger = Logger.getLogger(HazelcastAwareConfigProviderProcessor.class);
	
	@Override
	public int getOrder() {
		return HIGH_ORDER - 1;
	}

	@Override
	public void process(ConfigManager configManager) {
		try {
			Set<Class<? extends HazelcastAwareConfigProvider>> hazelcastAwareConfigProviderClasses = 
					configManager.getHazelcastAwareConfigProviderClasses();
			for (Class<? extends HazelcastAwareConfigProvider> hazelcastAwareConfigProviderClass : 
					hazelcastAwareConfigProviderClasses) {
				HazelcastAwareConfigProvider configProvider = 
						InstanceUtil.getSingleInstance(hazelcastAwareConfigProviderClass);
				if (configProvider != null) {
					List<Config> configs = configProvider.provideConfigs();
					if (configs != null) {
						for (Config config : configs) {
							logger.log(
									Level.INFO, 
									"Creating new Hazelcast instance: " + config.getInstanceName());
							HazelcastInstanceFactory.newHazelcastInstance(config);
						}	
					}
				}
			}	
		} 
		catch (Throwable t) {
			t.printStackTrace();
			logger.log(
					Level.ALL, 
					"Error occured while instrumenting Hazelcast-Aware classes",
					t);
		}
	}
	
}
