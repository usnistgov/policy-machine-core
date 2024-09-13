package gov.nist.csd.pm.pap.query.model.subgraph;

import java.util.Collection;

public record DescendantSubgraph(String name, Collection<DescendantSubgraph> descendants) {
}
