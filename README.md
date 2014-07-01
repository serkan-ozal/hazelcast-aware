1. What is Hazelcast-Aware?
==============

**Hazelcast-Aware** is a **Java Instrumentation API** based **Hazelcast** extension to use Hazelcast data structures (Distributed maps, lists, sets, queues, objects,  locks, topics, executers, entry listeners, etc ...) without interacting with **`HazelcastInstance`** class. You can specify which classes or fields will be Hazelcast aware by annotation or XML based configurations. **Hazelcast-Aware** scans classpath (classpath directories, dependent jar files and web application directories) of your application and finds Hazelcast aware classes and fields, then instruments them. Demo application is avaiable at [https://github.com/serkan-ozal/hazelcast-aware-demo](https://github.com/serkan-ozal/hazelcast-aware-demo).

2. Usage
=======

To find and make these classes or fields Hazelcast aware, there are two ways:

1) You just need to call explicitly making aware method at startup in anywhere of your application.

~~~~~ java
...

com.hazelcast.aware.HazelcastAwarer.makeHazelcastAware();

...
~~~~~

or

2) You can extend your main class from **`com.hazelcast.aware.HazelcastAware`** class.

~~~~~ java
...

public class HazelcastAwareDemo extends HazelcastAware {

	public static void main(String[] args) {
	
		...
	
	}
	
}	

...
~~~~~


3. Installation
=======

In your `pom.xml`, you must add repository and dependency for **Hazelcast-Aware**. 
You can change `hazelcast.aware.version` to any existing **Hazelcast-Aware** library version.
Latest version is `1.0.0`.

~~~~~ xml
...
<properties>
    ...
    <hazelcast.aware.version>1.0.0</hazelcast.aware.version>
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

4. Features
=======

4.1. Default Configurations
-------

You can access object which holds default configurations by **`DefaultConfigs getDefaultConfigs()`** method of **`com.hazelcast.aware.config.manager.ConfigManager`** class. If you don't have configuration manager instance, you can access it by **`com.hazelcast.aware.config.manager.ConfigManagerFactory.getConfigManager()`**.

Here are default configurations:

* **Default Instance name:** If there is no specified instance name for Hazelcast aware class or field, this instance name is used to get default Hazelcast instance. If there is no specified instance name for Hazelcast aware class or field and default instance name is empty in default configurations, the first instance is returned from **`com.hazelcast.instance.HazelcastInstanceFactory.getAllHazelcastInstances()`**.

4.2. Hazelcast-Aware Class
-------

You can make any class Hazelcast aware by annotating it with **`com.hazelcast.aware.config.provider.annotation.HazelcastAwareClass`** annotation.

~~~~~ java
@HazelcastAwareClass
public class HazelcastAwareBean {

	...
}
~~~~~

* **Instance name:**  You can associate Hazelcast aware class with specific Hazelcast instance by using **`instanceName`** attribute of **`com.hazelcast.aware.config.provider.annotation.HazelcastAwareClass`** annotation. If you don't specified instance name, default Hazelcast instance is used.

~~~~~ java
@HazelcastAwareClass(instanceName = "myHazelcastInstance")
public class HazelcastAwareBean {

	...
}
~~~~~

4.3. Hazelcast-Aware Field
-------

You can make general configurations of any field defined in any Hazelcast aware class by annotating it with **`com.hazelcast.aware.config.provider.annotation.HazelcastAwareField`** annotation.

* **Instance name:** You can associate Hazelcast aware field with specific Hazelcast instance by using **`instanceName`** attribute of **`com.hazelcast.aware.config.provider.annotation.HazelcastAwareField`** annotation. If you don't specify instance name, Hazelcast instance of owner class is used. If instance name is not defined for class also, default Hazelcast instance is used.

~~~~~ java
@HazelcastAwareClass
public class HazelcastAwareBean {

	@HazelcastAwareField(instanceName = "myHazelcastInstance")
	private Map<Long, String> myMap;
	
	...
}	
~~~~~


4.4. Hazelcast-Aware Map Typed Field
-------

You can make any **`java.util.Map`** typed (or sub-typed) field, defined in any Hazelcast aware class, Hazelcast aware by annotating it with **`com.hazelcast.aware.config.provider.annotation.HazelcastAwareMapField`** annotation.

~~~~~ java
@HazelcastAwareClass
public class HazelcastAwareBean {

	@HazelcastAwareMapField
	private Map<Long, String> myMap;
	
	...
}	
~~~~~

* **Map name:**  You can associate this map field to any distributed map on Hazelcast cluster by using **`name`** attribute of **`com.hazelcast.aware.config.provider.annotation.HazelcastAwareMapField`** annotation. If you don't specify map name, default map name is generated by using **`<class_name>_<field_name>`** format.

~~~~~ java
@HazelcastAwareClass
public class HazelcastAwareBean {

	@HazelcastAwareMapField(name = "myMap")
	private Map<Long, String> myMap;
	
	...
}	
~~~~~

4.5. Hazelcast-Aware List Typed Field
-------

You can make any **`java.util.List`** typed (or sub-typed) field, defined in any Hazelcast aware class, Hazelcast aware by annotating it with **`com.hazelcast.aware.config.provider.annotation.HazelcastAwareListField`** annotation.

~~~~~ java
@HazelcastAwareClass
public class HazelcastAwareBean {

	@HazelcastAwareListField
	private List<Long> myList;
	
	...
}	
~~~~~

* **List name:**  You can associate this list field to any distributed list on Hazelcast cluster by using **`name`** attribute of **`com.hazelcast.aware.config.provider.annotation.HazelcastAwareListField`** annotation. If you don't specify list name, default list name is generated by using **`<class_name>_<field_name>`** format.

~~~~~ java
@HazelcastAwareClass
public class HazelcastAwareBean {

	@HazelcastAwareListField(name = "myList")
	private List<Long> myList;
	
	...
}	
~~~~~

4.6. Hazelcast-Aware Set Typed Field
-------

You can make any **`java.util.Set`** typed (or sub-typed) field, defined in any Hazelcast aware class, Hazelcast aware by annotating it with **`com.hazelcast.aware.config.provider.annotation.HazelcastAwareSetField`** annotation.

~~~~~ java
@HazelcastAwareClass
public class HazelcastAwareBean {

	@HazelcastAwareSetField
	private Set<Long> mySet;
	
	...
}	
~~~~~

* **Set name:**  You can associate this set field to any distributed set on Hazelcast cluster by using **`name`** attribute of **`com.hazelcast.aware.config.provider.annotation.HazelcastAwareSetField`** annotation. If you don't specify set name, default set name is generated by using **`<class_name>_<field_name>`** format.

~~~~~ java
@HazelcastAwareClass
public class HazelcastAwareBean {

	@HazelcastAwareSetField(name = "mySet")
	private Set<Long> mySet;
	
	...
}	
~~~~~

4.7. Hazelcast-Aware Queue Typed Field
-------

You can make any **`java.util.Queue`** typed (or sub-typed) field, defined in any Hazelcast aware class, Hazelcast aware by annotating it with **`com.hazelcast.aware.config.provider.annotation.HazelcastAwareQueueField`** annotation.

~~~~~ java
@HazelcastAwareClass
public class HazelcastAwareBean {

	@HazelcastAwareQueueField
	private Queue<Long> myQueue;
	
	...
}	
~~~~~

* **Queue name:**  You can associate this queue field to any distributed queue on Hazelcast cluster by using **`name`** attribute of **`com.hazelcast.aware.config.provider.annotation.HazelcastAwareQueueField`** annotation. If you don't specify queue name, default queue name is generated by using **`<class_name>_<field_name>`** format.

~~~~~ java
@HazelcastAwareClass
public class HazelcastAwareBean {

	@HazelcastAwareQueueField(name = "myQueue")
	private Queue<Long> myQueue;
	
	...
}	
~~~~~

4.8. Hazelcast-Aware Topic Typed Field
-------

You can make any **`com.hazelcast.core.ITopic`** typed (or sub-typed) field, defined in any Hazelcast aware class, Hazelcast aware by annotating it with **`com.hazelcast.aware.config.provider.annotation.HazelcastAwareTopicField`** annotation.

~~~~~ java
@HazelcastAwareClass
public class HazelcastAwareBean {

	@HazelcastAwareTopicField
	private ITopic<Long> myTopic;
	
	...
}	
~~~~~

* **Topic name:**  You can associate this topic field to any distributed topic on Hazelcast cluster by using **`name`** attribute of **`com.hazelcast.aware.config.provider.annotation.HazelcastAwareTopicField`** annotation. If you don't specify topic name, default topic name is generated by using **`<class_name>_<field_name>`** format.

~~~~~ java
@HazelcastAwareClass
public class HazelcastAwareBean {

	@HazelcastAwareTopicField(name = "myTopic")
	private ITopic<Long> myTopic;
	
	...
}	
~~~~~

4.9. Hazelcast-Aware Initializer
-------

You can specify your custom initializer classes by implementing **`com.hazelcast.aware.initializer.HazelcastAwareInitializer`** interface to do some stuff before all operations such as setting default configurations in **`com.hazelcast.aware.config.DefaultConfigs`** class. Your custom initializer classes are found while scanning claspath and registered automatically. 

You can specify execution order of your initializer by implementing **`int getOrder()`** method. There are three pre-defined execution orders such as **`HIGHEST_ORDER`**, **`ORDER_DOESNT_MATTER`** and **`LOWEST_ORDER`**. 

~~~~~ java
@HazelcastAwareClass
public class MyHazelcastAwareInitializer implements HazelcastAwareInitializer {

	@Override
	public int getOrder() {
		return ORDER_DOESNT_MATTER;
	}
	
	
	@Override
	public void init(ConfigManager configManager) {
		configManager.getDefaultConfigs().setDefaultInstanceName("myInstance");
	}
	
}	
~~~~~

4.10. Hazelcast-Aware Injector
-------

You can specify your custom injector classes by implementing **`com.hazelcast.aware.injector.HazelcastAwareInjector`** interface to do some stuff (injecting or whatever you want) on instances of Hazelcast aware classes. Your custom injector classes are found while scanning claspath and registered automatically. 

Instance of injector class is called for every object creation of related class. 

You must specify type of your related class to inject by implementing **`Class<T> getType()`** method. If you are interested in all types, you can return **`Object.class`** or pre-defined expression of it as **`TYPE_DOESNT_MATTER`** as type in  **`getType()`** method.

You can specify execution order of your injector by implementing **`int getOrder()`** method. There are three pre-defined execution orders such as **`HIGHEST_ORDER`**, **`ORDER_DOESNT_MATTER`** and **`LOWEST_ORDER`**. 

~~~~~ java
@HazelcastAwareClass
public class MyHazelcastAwareInjector implements HazelcastAwareInjector<MyEntity> {

	@Override
	public int getOrder() {
		return ORDER_DOESNT_MATTER;
	}
	
	@Override
	Class<MyEntity> getType() {
		return MyEntity.class;
	}
	
	@Override
	public void inject(MyEntity myEntity) {
		// Do some stuff on MyEntity object
	}
	
}	
~~~~~

4.11. Hazelcast-Aware Processor
-------

You can specify your custom processor classes by implementing **`com.hazelcast.aware.processor.HazelcastAwareProcessor`** interface to do some stuff after instrumentation operations are done. Your custom processor classes are found while scanning claspath and registered automatically. 

You can specify execution order of your processor by implementing **`int getOrder()`** method. There are three pre-defined execution orders such as **`HIGHEST_ORDER`**, **`ORDER_DOESNT_MATTER`** and **`LOWEST_ORDER`**. 

~~~~~ java
@HazelcastAwareClass
public class MyHazelcastAwareProcessor implements HazelcastAwareprocessor {

	@Override
	public int getOrder() {
		return ORDER_DOESNT_MATTER;
	}
	
	
	@Override
	public void process(ConfigManager configManager) {
		// Do some stuff after instrumenting
	}
	
}	
~~~~~

4.12. Hazelcast-Aware Config Provider
-------

You can specify your custom configuration provider classes by implementing **`com.hazelcast.aware.config.provider.HazelcastAwareConfigProvider`** interface to provide Hazelcast cluster configurations (**`com.hazelcast.config.Config`**) to build Hazelcast cluster programmatically. Your custom configuration provider classes are found while scanning claspath and registered automatically.  

~~~~~ java
@HazelcastAwareClass
public class HazelcastInstanceConfigProvider implements HazelcastAwareConfigProvider {

	private List<Config> configs = new ArrayList<Config>();
	
	public HazelcastInstanceConfigProvider() {
		init();
	}
	
	/**
	<hz:config>
       <!-- Hazelcast Instance Name -->
       <hz:instance-name>hazelcast-aware-demo-instance</hz:instance-name>
       <!-- Hazelcast Group Name and Password -->
       <hz:group name="hazelcast-aware-demo" password="$$$_hazelcast-aware-demo_$$$" />
       <!-- Hazelcast Network Configuration -->
       <hz:network port="5701" port-auto-increment="true">
           <hz:join>
               <hz:multicast enabled="true"/>
               <hz:tcp-ip enabled="false"/>
           </hz:join>
       </hz:network>
   	</hz:config>
	*/
	private void init() {
		Config config = new Config();
		config.setInstanceName("hazelcast-aware-demo-instance");
		config.setGroupConfig(
				new GroupConfig(
						"hazelcast-aware-demo", 
						"$$$_hazelcast-aware-demo_$$$"));
		config.setNetworkConfig(
				new NetworkConfig().
						setPort(5701).
						setPortAutoIncrement(true).
						setJoin(
								new JoinConfig().
										setMulticastConfig(
												new MulticastConfig().
														setEnabled(true))));
		configs.add(config);
	}
	
	@Override
	public List<Config> provideConfigs() {
		return configs;
	}

}	
~~~~~

5. Lifecycle
=======

Lifecycle of operations done by Hazelcast-Aware are as follows:

**1.** Find all Hazelcast aware classes (annotated by **`com.hazelcast.aware.config.provider.annotation.HazelcastAwareClass`** annotation and specificed at XML or properties configuration files).

**2.** Find all Hazelcast aware initializer classes between Hazelcast aware classes and execute them. 

**3.** Do instrumentation over found Hazelcast aware classes.

**4.** Find all Hazelcast aware configuration provider classes between Hazelcast aware classes, get Hazelcast cluster configurations from them and create Hazelcast clusters with these configurations. 

**5.** Find all Hazelcast aware processor classes between Hazelcast aware classes and execute them. 

6. Roadmap
=======

* Distributed lock (**`ILock`**) will be supported.

* Distributed object will be supported.

* Distributed semaphore (**`Semaphore`**) will be supported.

* Entry/Item listeners (for maps, lists, sets, ...) will be supported.

* Message listener for (**`ITopic`**) will be supported.

* Executor service will be supported.

* XML based configuration will be supported.

* Properties file based configuration will be supported.

* Caching will be supported.
