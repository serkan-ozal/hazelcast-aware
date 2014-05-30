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

package com.hazelcast.aware;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

import com.hazelcast.aware.config.manager.ConfigManager;
import com.hazelcast.aware.config.manager.ConfigManagerFactory;
import com.hazelcast.aware.processor.HazelcastAwareConfigProviderProcessor;
import com.hazelcast.aware.processor.HazelcastAwareInjectorProcessor;
import com.hazelcast.aware.processor.HazelcastAwareProcessor;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

import tr.com.serkanozal.jillegal.agent.JillegalAgent;

/**
 * @author Serkan ÖZAL
 * 
 * Contact Informations:
 * 		GitHub   : https://github.com/serkan-ozal
 * 		LinkedIn : www.linkedin.com/in/serkanozal
 */
public class HazelcastAwarer {

	private static final ILogger logger = Logger.getLogger(HazelcastAwarer.class);
	
	private static final Set<HazelcastAwareProcessor> processors = 
			Collections.synchronizedSet(
					new TreeSet<HazelcastAwareProcessor>(
							new HazelcastAwareProcessorComparator()));
	
	private static volatile boolean awared = false;
	
	private static final ConfigManager configManager = ConfigManagerFactory.getConfigManager();
	
	private static class HazelcastAwareProcessorComparator implements Comparator<HazelcastAwareProcessor> {

		@Override
		public int compare(HazelcastAwareProcessor o1, HazelcastAwareProcessor o2) {
			// Sort as reverse order
			if (o1.getOrder() >= o2.getOrder()) {
				return -1;
			}
			else {
				return +1;
			}
		}
		
	}
	
	static {
		init();
	}
	
	private static void init() {
		JillegalAgent.init();
		registerDefaultHazelcastAwareProcessors();
	}
	
	private static void registerDefaultHazelcastAwareProcessors() {
		addHazelcastAwareProcessor(new HazelcastAwareInjectorProcessor());
		addHazelcastAwareProcessor(new HazelcastAwareConfigProviderProcessor());
	}
	
	public static void addHazelcastAwareProcessor(HazelcastAwareProcessor processor) {
		processors.add(processor);
	}
	
	public static void removeHazelcastAwareProcessor(HazelcastAwareProcessor processor) {
		processors.remove(processor);
	}

	public synchronized static void makeHazelcastAware() {
		if (!awared) {
			for (HazelcastAwareProcessor processor : processors) {
				try {
					logger.log(
							Level.INFO, 
							"Executing processor " + processor.getClass().getName() + " ..."); 
					processor.process(configManager);
				}
				catch (Throwable t) {
					logger.log(
							Level.ALL, 
							"Error occured while executing processor " + 
									processor.getClass().getName()); 
				}
			}
		}	
		else {
			logger.log(Level.INFO, "There is no Hazelcast-Aware class at classpath"); 
		}
			
		awared = true;	
	}
	
}
