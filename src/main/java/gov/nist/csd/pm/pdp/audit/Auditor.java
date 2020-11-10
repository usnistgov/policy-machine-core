package gov.nist.csd.pm.pdp.audit;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.audit.model.Explain;

/**
 * Auditor provides methods to audit an NGAC graph.
 */
public interface Auditor {

    /**
     * Explain why a user has access to a target node. The Explain object contains the permissions granted for the user
     * on the target and a list of the policy classes that the given target node is contained in, and the permissions and
     * paths valid under each.
     *
     * An example result would be:
     * operations: [read]
     * policyClasses:
     *   pc2
     * 	   operations: [read]
     * 	   paths:
     * 	     u1-ua2-oa2-o1 ops=[read]
     *   pc1
     * 	   operations: [read, write]
     * 	   paths:
     * 	     u1-ua1-oa1-o1 ops=[read, write]
     *
     * @param user the user
     * @param target the target
     * @return an Explain object containing the details of the user's access on the target.
     * @throws PMException if there is an error traversing the graph to determine the paths.
     */
    Explain explain(String user, String target) throws PMException;
}
