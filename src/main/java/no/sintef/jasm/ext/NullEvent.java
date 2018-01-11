package no.sintef.jasm.ext;

public final class NullEvent extends Event {

    public NullEvent(EventType type) {
        super(type);
    }

    @Override
    public Event clone() {
        return ((NullEventType)getType()).instantiate();
    }


}
