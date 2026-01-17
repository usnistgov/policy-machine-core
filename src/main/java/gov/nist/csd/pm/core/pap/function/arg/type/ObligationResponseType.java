package gov.nist.csd.pm.core.pap.function.arg.type;

import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;

public final class ObligationResponseType extends Type<ObligationResponse> {

    @Override
    public ObligationResponse cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof ObligationResponse r)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to ObligationResponse");
        }

        return r;
    }
}
