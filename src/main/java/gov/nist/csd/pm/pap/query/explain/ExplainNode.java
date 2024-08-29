package gov.nist.csd.pm.pap.query.explain;

import java.util.List;
import java.util.Objects;

public record ExplainNode(String node, List<ExplainAssociation> associations) {

}
