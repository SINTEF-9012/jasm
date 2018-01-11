package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.EventType;

import java.util.Map;

public class HelloEventType extends EventType {
        public HelloEventType() {
            super("hello", (short)1);
        }

        public Event instantiate(String who) {
            return new HelloEvent(this, who);
        }

    private class HelloEvent extends Event {

        public final String who;

        protected HelloEvent(EventType type, String who) {
            super(type);
            this.who = who;
        }


        @Override
        public Event clone() {
            return instantiate(who);
        }
    }

    @Override
    public Event instantiate(Map<String, Object> params) {
        return instantiate((String) params.get("who"));
    }
    }

