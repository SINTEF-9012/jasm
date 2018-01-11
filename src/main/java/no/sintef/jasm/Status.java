package no.sintef.jasm;

public class Status {
    boolean consumed = false;
    AtomicState next;

    Status() {}

    /*public Status update (final Status s) {
        if (!consumed)
            consumed = s.consumed;
        next = s.next;
        return this;
    }*/
}
