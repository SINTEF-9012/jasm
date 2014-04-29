package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;

import java.util.Collections;
import java.util.List;

/**
 * Created by bmori on 29.04.2014.
 */
public class Port {

    final PortType type;
    final String name;
    final List<EventType> in, out;

    public Port(final PortType type, final String name, final List<EventType> in, final List<EventType> out) {
        this.type = type;
        this.name = name;
        this.in = Collections.unmodifiableList(in);
        this.out = Collections.unmodifiableList(out);
    }

}
