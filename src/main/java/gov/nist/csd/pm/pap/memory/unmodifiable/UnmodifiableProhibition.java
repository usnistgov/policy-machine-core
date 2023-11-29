package gov.nist.csd.pm.pap.memory.unmodifiable;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.util.List;

public class UnmodifiableProhibition extends Prohibition {

    public UnmodifiableProhibition() {
    }

    public UnmodifiableProhibition(String name, ProhibitionSubject subject,
                                   AccessRightSet accessRightSet, boolean intersection,
                                   List<ContainerCondition> containers) {
        super(name, subject, new UnmodifiableAccessRightSet(accessRightSet), intersection, containers);
    }

    public UnmodifiableProhibition(Prohibition prohibition) {
        super(prohibition);
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSubject(ProhibitionSubject subject) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setContainers(List<ContainerCondition> containers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAccessRightSet(AccessRightSet accessRightSet) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIntersection(boolean intersection) {
        throw new UnsupportedOperationException();
    }
}
