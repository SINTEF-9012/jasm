package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;

import java.util.Map;

public class HelloEventType extends EventType {
        public HelloEventType() {
            name = "hello";
        }

        public Event instantiate(Port port, String who) {
            return new HelloEvent(this, port, who);
        }

    private class HelloEvent extends Event {

        public final String who;

        protected HelloEvent(EventType type, Port port, String who) {
            super(type, port);
            this.who = who;
        }


        @Override
        public Event clone() {
            return instantiate(getPort(), who);
        }
    }

    @Override
    public Event instantiate(Port port, Map<String, Object> params) {
        return instantiate(port, (String) params.get("who"));
    }
    }

