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

import java.util.logging.Level;

import com.hazelcast.aware.instrument.HazelcastAwareClassRedefiner;
import com.hazelcast.aware.scanner.HazelcastAwareScannerFactory;
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
	private static final HazelcastAwareClassRedefiner classRedefiner = new HazelcastAwareClassRedefiner();
	
	private static volatile boolean awared = false;
	
	static {
		JillegalAgent.init();
	}

	public synchronized static void makeHazelcastAware() {
		if (!awared) {
			
			logger.log(
					Level.INFO, 
					"Scanning started for Hazelcast-Aware classes ..."); 
			long start = System.currentTimeMillis();
			Class<?>[] hazelcastAwareClasses = 
					HazelcastAwareScannerFactory.
						getHazelcastAwareScanner().getHazelcastAwareClasses();
			long finish = System.currentTimeMillis();
			logger.log(
					Level.INFO, 
					"Scanning finished for Hazelcast-Aware classes in " + 
							(finish - start) + " milliseconds"); 
			
			if (hazelcastAwareClasses != null && hazelcastAwareClasses.length > 0) { 
				StringBuilder hazelcastAwareClassesBuilder = new StringBuilder();
				for (int i = 0; i < hazelcastAwareClasses.length; i++) {
					if (i > 0) {
						hazelcastAwareClassesBuilder.append(", ");
					}
					hazelcastAwareClassesBuilder.append(hazelcastAwareClasses[i].getName());
				}
				
				logger.log(
						Level.INFO, 
						"These classes will be Hazelcast-Aware: " + 
								hazelcastAwareClassesBuilder.toString()); 
				
				try {
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
							"Error occured while instrumenting Hazelcast-Aware classes: " + 
								hazelcastAwareClassesBuilder,
							t);
				}
			}
			else {
				logger.log(Level.INFO, "There is no Hazelcast-Aware class at classpath"); 
			}
			
			awared = true;
		}	
	}
	
}
