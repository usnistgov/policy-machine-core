package gov.nist.csd.pm.pap.function.arg.type;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;

public final class AccessRightSetType extends ArgType<AccessRightSet> {

    @Override
    public AccessRightSet cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof AccessRightSet accessRightSet)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to AccessRightSet");
        }

        return accessRightSet;
    }
}
