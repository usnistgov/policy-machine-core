package gov.nist.csd.pm.pap.query.explain;

import java.util.List;

public record ExplainNode(String node, List<ExplainAssociation> associations) {

}
