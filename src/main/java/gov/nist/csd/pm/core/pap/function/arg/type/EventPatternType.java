package gov.nist.csd.pm.core.pap.function.arg.type;

import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;

public final class EventPatternType extends Type<EventPattern> {

    @Override
    public EventPattern cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof EventPattern r)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to EventPattern");
        }

        return r;
    }
}
