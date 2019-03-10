package gov.nist.csd.pm.audit;

import gov.nist.csd.pm.audit.model.Path;
import gov.nist.csd.pm.exceptions.PMException;

import java.util.List;
import java.util.Map;

/**
 * Auditor provides methods to audit an NGAC graph.
 */
public interface Auditor {

    /**
     * Explain why a user has access to a target node. The returned data structure is a map of the policy class name
     * to the paths under that policy class.  A path consists of a set of edges that allow the user to reach the target
     * node. Every path will include an association which is the bridge from the user side of the graph to the target
     * side of the graph.
     *
     * @param userID The ID of the user.
     * @param targetID The ID of the target node.
     * @return The paths under each policy class from the given user to the given target node.
     * @throws PMException If there is an error traversing the graph to determine the paths.
     */
    Map<String, List<Path>> explain(long userID, long targetID) throws PMException;
}
