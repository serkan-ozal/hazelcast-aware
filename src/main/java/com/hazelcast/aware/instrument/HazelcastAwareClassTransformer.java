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

import com.hazelcast.aware.config.provider.annotation.HazelcastAwareClass;
import com.hazelcast.aware.util.HazelcastAwareUtil;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

/**
 * @author Serkan ÖZAL
 * 
 * Contact Informations:
 * 		GitHub   : https://github.com/serkan-ozal
 * 		LinkedIn : www.linkedin.com/in/serkanozal
 */
public class HazelcastAwareClassTransformer implements ClassFileTransformer {

	protected static final Set<String> alreadyInstrumentedClasses = new HashSet<String>();
	
	protected ClassPool cp = ClassPool.getDefault();
	
	protected volatile boolean active = false;
	
	public HazelcastAwareClassTransformer() {
		init();
	}
	
	protected void init() {
		 cp.importPackage(HazelcastAwareUtil.class.getPackage().getName());
         cp.appendClassPath(new ClassClassPath(HazelcastAwareUtil.class));
	}

	protected boolean isHazelcastAware(CtClass clazz) {
		return clazz.hasAnnotation(HazelcastAwareClass.class);
	}
	
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
		if (alreadyInstrumentedClasses.contains(className)) {
			return bytes;
		}
        try {
        	byte[] instrumentedBytes = instrumentInternal(cp.makeClass(new ByteArrayInputStream(bytes), false));
        	if (instrumentedBytes != null) {
        		return instrumentedBytes;
        	}
        }
        catch (Throwable t) {
        	t.printStackTrace();
        }
        
        return bytes;
    }
	
	public byte[] instrument(Class<?> classBeingRedefined) {
		try {
			return instrumentInternal(cp.get(classBeingRedefined.getName()));
		} 
		catch (NotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected byte[] instrumentInternal(CtClass ct) {
		System.out.println(ct);
		try {
	    	if (isHazelcastAware(ct)) {
	            System.out.println("[INFO] : " + "Class " + ct.getName() + " is being instrumented ...");
	            
	            CtConstructor[] constructors = ct.getConstructors();
	            
	            for (CtConstructor c : constructors) {
	            	c.insertAfter("HazelcastAwareUtil.injectHazelcast(this);");
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
