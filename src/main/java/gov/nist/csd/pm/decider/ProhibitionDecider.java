package gov.nist.csd.pm.decider;

import gov.nist.csd.pm.exceptions.PMDBException;
import gov.nist.csd.pm.exceptions.PMGraphException;

import java.util.HashSet;

public interface ProhibitionDecider {
    /**
     * List the permissions that are prohibited on the target node for the subject.  The subject can be the ID of a user
     * or a process.
     * @param subjectID the ID of the subject, either a user or a process.
     * @param targetID the ID of the target to get the prohibited permissions on.
     * @return the set of permissions that are denied for the subject on the target.
     * @throws PMDBException if there is an error listing the prohibited permissions on the target.
     * @throws PMGraphException if there is an error accessing the graph.
     */
    HashSet<String> listProhibitedPermissions(long subjectID, long targetID) throws PMDBException, PMGraphException;
}
