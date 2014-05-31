What is Hazelcast-Aware?
==============

**Hazelcast-Aware** is a **Java Instrumentation API** based **Hazelcast** extension to use Hazelcast data structures (Distributed maps, lists, sets, queues, objects,  locks, topics, executers, entry listeners, etc ...) without interacting with **`HazelcastInstance`** class. You can specify which classes or fields will be Hazelcast aware by annotation or XML based configurations. Demo application is at [https://github.com/serkan-ozal/hazelcast-aware-demo](https://github.com/serkan-ozal/hazelcast-aware-demo).

Usage
=======

To find and make thses classes or fields Hazelcast aware, there are two ways:

1. You just need to call explicitly making aware method at startup in anywhere of your application.

~~~~~ java
...

com.hazelcast.aware.HazelcastAwarer.makeHazelcastAware();

...
~~~~~

or

2. You can extend your main class from **`com.hazelcast.aware.HazelcastAware`** class.

~~~~~ java
...

public class HazelcastAwareDemo extends HazelcastAware {

	public static void main(String[] args) throws Exception {
	
	...
	
	}
	
}	

...
~~~~~


Installation
=======

In your `pom.xml`, you must add repository and dependency for **Hazelcast-Aware**. 
You can change `hazelcast.aware.version` to any existing **Hazelcast-Aware** library version.
Latest version is `1.0.0-SNAPSHOT`.

~~~~~ xml
...
<properties>
    ...
    <hazelcast.aware.version>1.0.0-SNAPSHOT</hazelcast.aware.version>
    ...
</properties>
...
<dependencies>
    ...
	<dependency>
		<groupId>com.hazelcast</groupId>
		<artifactId>hazelcast.aware</artifactId>
		<version>${hazelcast.aware.version}</version>
	</dependency>
	...
</dependencies>
...
<repositories>
	...
	<repository>
		<id>serkanozal-maven-repository</id>
		<url>https://github.com/serkan-ozal/maven-repository/raw/master/</url>
	</repository>
	...
</repositories>
...
~~~~~

Features
=======

Roadmap
=======

* Distributed lock (**`ILock`**) will be supported.

* Distributed object will be supported.

* Distributed semaphore (**`Semaphore`**) will be supported.

* Entry/Item listeners (for maps, lists, sets, ...) will be supported.

* Message listener for (**`ITopic`**) will be supported.

* Executor service will be supported.

* Caching will be supported.
