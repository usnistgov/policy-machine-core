package gov.nist.csd.pm.policy.model.graph.dag.visitor;

import gov.nist.csd.pm.policy.exceptions.PMException;

public interface Visitor {

    void visit(String node) throws PMException;
}
