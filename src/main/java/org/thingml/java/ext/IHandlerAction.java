package org.thingml.java.ext;

public interface IHandlerAction {

    boolean check(final Event e, final EventType t);

    void execute(final Event e);

}
