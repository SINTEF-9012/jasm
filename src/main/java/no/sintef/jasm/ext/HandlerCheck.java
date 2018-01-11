package no.sintef.jasm.ext;

@FunctionalInterface
public interface HandlerCheck {
    boolean check(final Event e);
}
