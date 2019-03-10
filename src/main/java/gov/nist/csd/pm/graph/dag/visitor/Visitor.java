package gov.nist.csd.pm.graph.dag.visitor;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.Node;

public interface Visitor {

    void visit(Node node) throws PMException;
}
