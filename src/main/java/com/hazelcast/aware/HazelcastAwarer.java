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

import com.hazelcast.aware.instrument.HazelcastAwareClassTransformer;
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

	protected static final ILogger logger = Logger.getLogger(HazelcastAwarer.class);
	
	static {
		JillegalAgent.init();
	}
	
	private static volatile boolean awared = false;
	
	public synchronized static void makeHazelcastAware() {
		if (!awared) {
			JillegalAgent.getInstrumentation().addTransformer(new HazelcastAwareClassTransformer(), true);

			Class<?>[] hazelcastAwareClasses = 
					HazelcastAwareScannerFactory.getHazelcastAwareScanner().getHazelcastAwareClasses();
			
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
						"These classes will be Hazelcast-Aware: " + hazelcastAwareClassesBuilder.toString()); 
				
				/*
				try {
					JillegalAgent.getInstrumentation().retransformClasses(hazelcastAwareClasses);
				} 
				catch (Throwable t) {
					logger.log(
							Level.ALL, 
							"Error occured while retransforming Hazelcast-Aware classes: " + 
								hazelcastAwareClassesBuilder,
							t);
				}
				*/
			}
			else {
				logger.log(Level.INFO, "There is no Hazelcast-Aware class at classpath"); 
			}
			
			awared = true;
		}	
	}
	
}
