package org.thingml.java;

import org.thingml.java.ext.Event;

public interface IState {

    void onEntry();

    void onExit();

    IState dispatch(final Event e, final HandlerHelper helper);

}
