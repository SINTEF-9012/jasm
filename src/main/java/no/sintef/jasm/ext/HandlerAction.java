package no.sintef.jasm.ext;

@FunctionalInterface
public interface HandlerAction {
    void execute(final Event e);
}
