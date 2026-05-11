package gov.nist.csd.pm.core.common.graph.dag;

import gov.nist.csd.pm.core.common.exception.PMException;
import java.util.Collection;

@FunctionalInterface
public interface AdjacencyRetriever {

    Collection<Long> getAdjacent(long nodeId) throws PMException;

}