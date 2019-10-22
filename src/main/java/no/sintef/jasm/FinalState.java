package no.sintef.jasm;

import no.sintef.jasm.ext.Event;

public class FinalState extends AtomicState {

    public FinalState(final String name) {
        super(name);
    }

    @Override
    protected final void handle(final Event e, final Status s) {}
}
