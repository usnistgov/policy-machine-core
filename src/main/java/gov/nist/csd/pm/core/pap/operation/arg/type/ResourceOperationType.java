package gov.nist.csd.pm.core.pap.operation.arg.type;

import gov.nist.csd.pm.core.pap.operation.ResourceOperation;

public final class ResourceOperationType extends Type<ResourceOperation> {

    @Override
    public ResourceOperation cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof ResourceOperation o)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to ResourceOperation");
        }

        return o;
    }
}