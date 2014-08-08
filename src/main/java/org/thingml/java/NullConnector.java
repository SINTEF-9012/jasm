package org.thingml.java;

import org.thingml.java.ext.Event;

public class NullConnector extends Connector {

    public NullConnector(Port required, Port provided, Component client, Component server) {
        super(null, null, null, null);
    }

    @Override
    public void onProvided(Event e) {
    }

    @Override
    public void onRequired(Event e) {
    }

    @Override
    protected void connect() {
    }

}
