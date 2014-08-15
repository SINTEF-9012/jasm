package org.thingml.java;

import org.thingml.java.ext.EventType;

import java.util.List;

/**
 * Created by bmori on 29.04.2014.
 */
public class Port {

    final PortType type;
    final String name;
    final List<EventType> in, out;
    //final int ID;

    public Port(final PortType type, final String name, final List<EventType> in, final List<EventType> out/*, final int ID*/) {
        this.type = type;
        this.name = name;
        this.in = in;
        this.out = out;
        //this.ID = ID;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Port) {
            final Port p = (Port) o;
            return p.name.equals(name) && p.type.equals(type) && p.in.equals(in) && p.out.equals(out);
        }
        return false;

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + in.hashCode();
        result = 31 * result + out.hashCode();
        return result;
    }

    public String getName() {
        return name;
    }
}
