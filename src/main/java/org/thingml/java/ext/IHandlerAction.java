package org.thingml.java.ext;

public interface IHandlerAction {

    default boolean check(final Event e, final EventType t) {return e.getType() == t;}

    void execute(final Event e);

}
