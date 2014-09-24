package org.thingml.java;

/**
 * Created by bmori on 29.04.2014.
 */
public class Connector {

    protected final Port required, provided;
    protected final Component client, server;

    public Connector(Port required, Port provided, Component client, Component server) {
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
}
