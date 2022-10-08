package gov.nist.csd.pm.policy;

public interface PolicyReader {

    GraphReader graph();
    ProhibitionsReader prohibitions();
    ObligationsReader obligations();

}
