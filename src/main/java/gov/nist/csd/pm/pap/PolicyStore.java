package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.*;
import gov.nist.csd.pm.policy.tx.Transactional;

public abstract class PolicyStore implements Policy, Transactional {

    @Override
    public abstract GraphStore graph();

    @Override
    public abstract ProhibitionsStore prohibitions();

    @Override
    public abstract ObligationsStore obligations();

    @Override
    public abstract UserDefinedPMLStore userDefinedPML();
}
