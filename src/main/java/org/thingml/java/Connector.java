package org.thingml.java;

import org.thingml.java.ext.Event;

/**
 * Created by bmori on 29.04.2014.
 */
public class Connector {

    protected final Port required, provided;
    protected final Component client, server;

    public Connector(Port required, Port provided, Component client, Component server) {
        //assert required.type == PortType.REQUIRED && provided.type == PortType.PROVIDED;
        this.required = required;
        this.provided = provided;
        this.client = client;
        this.server = server;
        connect();
    }

    protected void connect() {
        client.connect(required, this);
        server.connect(provided, this);

    }

    public void onProvided(Event e) {
        forward(e, required, client);
    }

    public void onRequired(Event e) {
        forward(e, provided, server);
    }

    public synchronized void forward(Event e, Port p, Component c) {
        //if (c.canReceive(e, p)) {
            c.receive(e);
        //}
    }

}
