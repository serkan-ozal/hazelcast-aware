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

import java.util.Set;
import java.util.logging.Level;

import com.hazelcast.aware.config.manager.ConfigManager;
import com.hazelcast.aware.instrument.HazelcastAwareClassRedefiner;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

/**
 * @author Serkan ÖZAL
 * 
 * Contact Informations:
 * 		GitHub   : https://github.com/serkan-ozal
 * 		LinkedIn : www.linkedin.com/in/serkanozal
 */
public class HazelcastAwareInjectorProcessor implements HazelcastAwareProcessor {

	private static final ILogger logger = Logger.getLogger(HazelcastAwareInjectorProcessor.class);
	private static final HazelcastAwareClassRedefiner classRedefiner = new HazelcastAwareClassRedefiner();
	
	@Override
	public int getOrder() {
		return HIGHEST_ORDER;
	}

	@Override
	public void process(ConfigManager configManager) {
		try {
			Set<Class<?>> hazelcastAwareClasses = configManager.getHazelcastAwareClasses();
			for (Class<?> hazelcastAwareClass : hazelcastAwareClasses) {
				classRedefiner.redefine(hazelcastAwareClass);
				logger.log(
						Level.INFO, 
						"Now " + hazelcastAwareClass.getName() + " is a Hazelcast-Aware class"); 
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
