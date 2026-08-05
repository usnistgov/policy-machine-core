package gov.nist.ngac.pm.core.pap.operation.arg.type;

import gov.nist.ngac.pm.core.pap.obligation.response.ObligationResponse;

/**
 * The PML type of an obligation's {@link ObligationResponse}.
 */
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
