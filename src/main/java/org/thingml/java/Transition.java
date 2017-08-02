package org.thingml.java;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.HandlerAction;

public class Transition extends Handler {



    public Transition() {
        super();
        this.action = (Event event) -> {
            source.onExit.execute();
            target.onEntry.execute();
        };
    }

    public Transition to(AtomicState target) {
        this.target = target;
        return this;
    }

    public Handler action(HandlerAction action) {
        this.action = (Event event) -> {
          source.onExit.execute();
          action.execute(event);
          target.onEntry.execute();
        };
        return this;
    }

}
