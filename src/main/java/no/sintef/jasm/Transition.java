package no.sintef.jasm;

import no.sintef.jasm.ext.Event;
import no.sintef.jasm.ext.HandlerAction;

public class Transition extends Handler {



    public Transition() {
        super();
        this.action = (final Event event) -> {
            source.onExit.execute();
            target.onEntry.execute();
        };
    }

    public Transition to(final AtomicState target) {
        this.target = target;
        return this;
    }

    public Handler action(final HandlerAction action) {
        this.action = (Event event) -> {
          source.onExit.execute();
          action.execute(event);
          target.onEntry.execute();
        };
        return this;
    }

}
