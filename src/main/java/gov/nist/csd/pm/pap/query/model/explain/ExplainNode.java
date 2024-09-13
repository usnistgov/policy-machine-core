package gov.nist.csd.pm.pap.query.model.explain;

import java.util.List;

public record ExplainNode(String node, List<ExplainAssociation> associations) {

}
