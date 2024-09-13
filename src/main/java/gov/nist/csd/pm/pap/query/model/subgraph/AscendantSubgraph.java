package gov.nist.csd.pm.pap.query.model.subgraph;

import java.util.Collection;

public record AscendantSubgraph(String name, Collection<AscendantSubgraph> ascendants) {
}
