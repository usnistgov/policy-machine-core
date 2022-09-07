package gov.nist.csd.pm.policy.author;

public abstract class PolicyAuthor {

    public abstract GraphAuthor graph();
    public abstract ProhibitionsAuthor prohibitions();
    public abstract ObligationsAuthor obligations();
    public abstract PALAuthor pal();

}
