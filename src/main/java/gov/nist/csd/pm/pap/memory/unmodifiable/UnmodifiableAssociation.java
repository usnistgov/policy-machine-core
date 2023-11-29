package gov.nist.csd.pm.pap.memory.unmodifiable;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

public class UnmodifiableAssociation extends Association {

    public UnmodifiableAssociation(String source, String target, AccessRightSet ars) {
        super(source, target, new UnmodifiableAccessRightSet(ars));
    }

    @Override
    public void setSource(String source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTarget(String target) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAccessRightSet(AccessRightSet accessRightSet) {
        throw new UnsupportedOperationException();
    }
}
