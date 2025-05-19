package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.function.arg.type.Type;

public final class AccessRightSetType extends Type<AccessRightSet> {

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

    @Override
    public Class<AccessRightSet> getExpectedClass() {
        return AccessRightSet.class;
    }
}
