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

package com.hazelcast.aware.instrument;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Serkan ÖZAL
 * 
 * Contact Informations:
 * 		GitHub   : https://github.com/serkan-ozal
 * 		LinkedIn : www.linkedin.com/in/serkanozal
 */
public class HazelcastAwareClassTransformer extends HazelcastAwareClassInstrumenter implements ClassFileTransformer {

	protected static final Set<String> transformedClasses = new HashSet<String>();

	protected volatile boolean active = false;
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, 
			ProtectionDomain domain, byte[] bytes) throws IllegalClassFormatException {
		if (!active) {
			return bytes;
		}
		if (transformedClasses.contains(className)) {
			return bytes;
		}
        try {
        	byte[] instrumentedBytes = instrument(cp.makeClass(new ByteArrayInputStream(bytes), false));
        	if (instrumentedBytes != null) {
        		return instrumentedBytes;
        	}
        }
        catch (Throwable t) {
        	t.printStackTrace();
        }
        return bytes;
    }

}
