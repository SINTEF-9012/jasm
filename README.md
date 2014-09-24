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

JaSM provides the common concepts to define state machines: atomic states, composites, parallel regions, transition and internal transitions.


The [ThingML](http://thingml.org) team.
