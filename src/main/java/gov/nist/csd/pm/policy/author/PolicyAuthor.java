package gov.nist.csd.pm.policy.author;

import gov.nist.csd.pm.policy.PolicySerializable;

public abstract class PolicyAuthor implements PolicySerializable {

    public abstract GraphAuthor graph();
    public abstract ProhibitionsAuthor prohibitions();
    public abstract ObligationsAuthor obligations();
    public abstract PALAuthor pal();

}
