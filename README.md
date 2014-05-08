ThingML JaSM
============

A Java framework with reduced footprint (compiles in Java 8 compact profile 1) and no dependency (100% self-contained Java code) to implement and execute state machines. The source code is in Java 6 to enable JaSM to run on Android without hassle.

Java packages used by JaSM (according to jdeps):
```
      -> java.lang                                          compact1
      -> java.lang.invoke                                   compact1
      -> java.util                                          compact1
      -> java.util.function                                 compact1
      -> java.util.logging                                  compact1
      -> java.util.stream                                   compact1
```

JaSM provides the common concepts to define state machines: atomic states, composites, parallel regions, transition and internal transitions. Regarding the management of parallel regions (available in tag `v0.1-java8`), we provide two implementation of ```CompositeState``` (where regions are managed):

- ```CompositeMT```, which should be used on multi-threaded platforms _and_ if you have many regions within a composite. This implementation leverages the ```parallelStream().forEach``` feature of Java 8.
- ```CompositeST```, which should be used on single-threaded platforms _or_ if you have only the default region (or very few regions) in your composite.

> Be aware that parallelism induces [some overhead](https://blogs.oracle.com/jtc/entry/an_embedded_java_8_lambda), which can be compensated or not depending on your usages. 

- The [ThingML](http://thingml.org) team.