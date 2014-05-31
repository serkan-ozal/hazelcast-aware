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

package com.hazelcast.aware.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

/**
 * @author Serkan ÖZAL
 * 
 * Contact Informations:
 * 		GitHub   : https://github.com/serkan-ozal
 * 		LinkedIn : www.linkedin.com/in/serkanozal
 */
public class ClasspathUtil {
	
	private static final ILogger logger = Logger.getLogger(ClasspathUtil.class);
	
	private static Set<URL> classpathUrls;
	
	static {
		init();
	}
	
	private ClasspathUtil() {
		
	}
	
	public static Set<URL> getClasspathUrls() {
		return classpathUrls;
	}
	
	private static void init() {
		classpathUrls = findClasspathUrls();
		logger.log(Level.INFO, "Found classpath URL list: " + classpathUrls);
	}
	
	private static Set<URL> findClasspathUrls() {
		Set<URL> urls = new HashSet<URL>();
		// Start with Current Thread's loader
		ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
		ClassLoader loader = ctxLoader;
		while (loader != null) {
			urls.addAll(findClasspathsByLoader(loader));
			loader = loader.getParent();
		}

		// Also start with this classes's loader, in some environment this can
		// be different than the current thread's one
		ClassLoader sysLoader = ClasspathUtil.class.getClassLoader();
		loader = sysLoader;
		while (loader != null) {
			urls.addAll(findClasspathsByLoader(loader));
			loader = loader.getParent();
		}

		Map<URL, URL> replaceURLs = new HashMap<URL, URL>();
		Set<URL> derivedUrls = new HashSet<URL>();
		for (URL url : urls) {
			if (url.getProtocol().startsWith("vfs")) {
				try {
					URLConnection conn = url.openConnection();
					Object virtualFile = conn.getContent();
					if (virtualFile.getClass().getName().equals("org.jboss.vfs.VirtualFile")) {
						File file = 
								(File) virtualFile.getClass().
											getMethod("getPhysicalFile").
												invoke(virtualFile);
						String fileName = file.getCanonicalPath();
						String name = 
								(String) virtualFile.getClass().
											getMethod("getName").
												invoke(virtualFile);
						name = name.trim().toLowerCase();
						if (	(name.endsWith("jar") || 
								name.endsWith("zip") && 
								fileName.endsWith("/contents"))) {
							fileName = fileName.replace("contents", name);
						}
						URL repURL = new URL("file:/" + fileName);
						replaceURLs.put(url, repURL);
					}
				} 
				catch (Exception e) {
					// We don't expect to trapped here
					e.printStackTrace();
				}
			}
			try {
				if (url.toExternalForm().endsWith("WEB-INF/classes")) {
					derivedUrls.add(
							new URL(
									url.toExternalForm().
										replace("WEB-INF/classes", "WEB-INF/lib")));
				} 
				else if (url.toExternalForm().endsWith("WEB-INF/classes/")) {
					derivedUrls.add(
							new URL(
									url.toExternalForm().
										replace("WEB-INF/classes/", "WEB-INF/lib/")));
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		urls.removeAll(replaceURLs.keySet());
		urls.addAll(replaceURLs.values());
		urls.addAll(derivedUrls);
		replaceURLs.clear();
		//Check contained urls
		for (URL url : urls) {
			for (URL rootUrl : urls) {
				if (url.equals(rootUrl)) {
					continue;
				}
				if (url.toExternalForm().startsWith(rootUrl.toExternalForm())) {
					if (replaceURLs.get(url) != null) {
						URL settledUrl =replaceURLs.get(url);
						if (settledUrl.toExternalForm().startsWith(rootUrl.toExternalForm())) {
							replaceURLs.put(url, rootUrl);	
						}
					}
					else {
						replaceURLs.put(url, rootUrl);						
					}
				}
			}
		}
		urls.removeAll(replaceURLs.keySet());
		return urls;
	}

	
	private static Set<URL> findClasspathsByLoader(ClassLoader loader) {
		Set<URL> urls = new HashSet<URL>();
		if (loader instanceof URLClassLoader) {
			URLClassLoader urlLoader = (URLClassLoader) loader;
			urls.addAll(Arrays.asList(urlLoader.getURLs()));
		} 
		else {
			Enumeration<URL> urlEnum;
			try {
				urlEnum = loader.getResources("");
				while (urlEnum.hasMoreElements()) {
					URL url = urlEnum.nextElement();
					if (url.getProtocol().startsWith("bundleresource")){
						continue;
					}
					urls.add(url);
				}
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return urls;
	}
	
}
