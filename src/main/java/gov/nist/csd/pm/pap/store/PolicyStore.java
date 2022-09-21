package gov.nist.csd.pm.pap.store;

import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.events.PolicySync;
import gov.nist.csd.pm.policy.tx.Transactional;

public abstract class PolicyStore extends PolicyAuthor implements PolicySync, Transactional {

    /*@Override
    public abstract GraphStore graph();

    @Override
    public abstract ProhibitionsStore prohibitions();

    @Override
    public abstract ObligationsStore obligations();

    @Override
    public abstract PALStore pal();*/

}
