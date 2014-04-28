package org.thingml.java.sample;

import org.thingml.java.ext.Event;
import org.thingml.java.ext.IHandlerAction;

import java.util.logging.Logger;

public final class HelloHandlerAction implements IHandlerAction {

    private static final Logger log = Logger.getLogger(HelloHandlerAction.class.getName());

    public HelloHandlerAction() {
    }

    public void execute(final Event e) {
        System.out.println("hello " + e.get("who") + "!");
    }

}
