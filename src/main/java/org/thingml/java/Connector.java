package org.thingml.java;

import org.thingml.java.ext.Event;

/**
 * Created by bmori on 29.04.2014.
 */
public class Connector {

    private final Port required, provided;
    private final Component client, server;

    public Connector(Port required, Port provided, Component client, Component server) {
        assert required.type == PortType.REQUIRED && provided.type == PortType.PROVIDED;
        this.required = required;
        this.provided = provided;
        this.client = client;
        this.server = server;
    }

    public void onProvided(Event e) {
        forward(e, required, client);
    }

    public void onRequired(Event e) {
        forward(e, provided, server);
    }

    private synchronized void forward(Event e, Port p, Component c) {
        if (c.canReceive(e, p)) {
            c.receive(e, p);
        }
    }

}
