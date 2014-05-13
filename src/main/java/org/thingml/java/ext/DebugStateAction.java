package org.thingml.java.ext;

import java.util.logging.Logger;

public final class DebugStateAction implements IStateAction {

    private static final Logger log = Logger.getLogger(DebugStateAction.class.getName());
    private final String name;

    public DebugStateAction(String name) {
        this.name = name;
    }

    public void onEntry() {
        log.info(name + ". on entry");
    }

    public void onExit() {
        log.info(name + ". on exit");
    }

}
