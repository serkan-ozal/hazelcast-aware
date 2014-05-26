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
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import com.hazelcast.aware.util.HazelcastUtil;

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
public class HazelcastAwareClassTransformer implements ClassFileTransformer {
	
	protected ClassPool cp = ClassPool.getDefault();
    
	protected CtClass buildClass(byte[] bytes) throws IOException, RuntimeException {
        return cp.makeClass(new ByteArrayInputStream(bytes));
    }
	
	protected boolean isHazelcastAware(CtClass clazz) {
		return false;
	}
	
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, 
			ProtectionDomain domain, byte[] bytes) throws IllegalClassFormatException {
        try {
        	CtClass ct = buildClass(bytes);
        	if (isHazelcastAware(ct)) {
	            System.out.println("[INFO] : " + "Class " + className + " is being instrumented ...");
	            
	            cp.importPackage(HazelcastUtil.class.getPackage().getName());
	            cp.appendClassPath(new ClassClassPath(HazelcastUtil.class));
	            
	            // Ensure that there will be at least one class initializer (or constructor)
	            ct.makeClassInitializer();
	            
	            CtConstructor[] constructors = ct.getConstructors();
	            
	            for (CtConstructor c : constructors) {
	            	c.insertAfter("HazelcastUtil.injectHazelcast(this);");
	            }
	            
	            return ct.toBytecode();
        	}
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
        
        return bytes;
    }
	
}
