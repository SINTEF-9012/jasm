package no.sintef.jasm;

import no.sintef.jasm.ext.Event;

/**
 * Created by bmori on 25.02.2016.
 */
public class FinalState extends AtomicState {

    public FinalState(final String name) {
        super(name);
    }

    @Override
    protected final void handle(final Event e, final Status s) {}
}
