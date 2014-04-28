package org.thingml.java.ext;

import java.util.logging.Logger;

public final class DebugHandlerAction implements IHandlerAction {

    private static final Logger log = Logger.getLogger(DebugHandlerAction.class.getName());

    public DebugHandlerAction() {
    }

    public void execute(final Event e) {
        log.info("on " + e.getType().getName() + "(" + e.getType().getClass().getName() + ")");
    }

}
