package org.thingml.java.ext;

public final class NullHandlerAction implements IHandlerAction {

    public NullHandlerAction() {
    }

    public boolean check(final Event e, final EventType t) {
        return true;
    }

    public void execute(final Event e) {
        //Do nothing
    }

}
