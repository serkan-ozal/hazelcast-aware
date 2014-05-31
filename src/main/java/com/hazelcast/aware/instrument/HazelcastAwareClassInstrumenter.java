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

import com.hazelcast.aware.HazelcastAwarer;
import com.hazelcast.aware.config.provider.annotation.HazelcastAwareClass;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;

/**
 * @author Serkan ÖZAL
 * 
 * Contact Informations:
 * 		GitHub   : https://github.com/serkan-ozal
 * 		LinkedIn : www.linkedin.com/in/serkanozal
 */
public class HazelcastAwareClassInstrumenter {

	protected ClassPool cp = ClassPool.getDefault();
	
	public HazelcastAwareClassInstrumenter() {
		init();
	}
	
	protected void init() {
		 cp.importPackage(HazelcastAwarer.class.getPackage().getName());
         cp.appendClassPath(new ClassClassPath(HazelcastAwarer.class));
	}
    
	protected boolean isHazelcastAware(CtClass clazz) {
		return clazz.hasAnnotation(HazelcastAwareClass.class);
	}
	
	public byte[] instrument(CtClass ct) {
		try {
	    	if (isHazelcastAware(ct)) {
	            System.out.println("[INFO] : " + "Class " + ct.getName() + " is being instrumented ...");
	            
	            CtConstructor[] constructors = ct.getConstructors();
	            
	            for (CtConstructor c : constructors) {
	            	c.insertAfter("HazelcastAwarer.injectHazelcast(this);");
	            }
	            
	            return ct.toBytecode();
	    	}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		
		return null;
	}
	
}
