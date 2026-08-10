package gov.nist.ngac.pm.core.pap.operation.arg.type;

import gov.nist.ngac.pm.core.pap.obligation.event.EventPattern;

/**
 * Supported type for EventPattern.
 */
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
