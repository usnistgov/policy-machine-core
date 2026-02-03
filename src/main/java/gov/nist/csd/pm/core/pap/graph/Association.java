package gov.nist.csd.pm.core.pap.graph;

import gov.nist.csd.pm.core.pap.operation.accessrights.AccessRightSet;
import java.io.Serializable;

/**
 * This object represents an Association in a NGAC graph. An association is a relationship between two nodes,
 * similar to an assignment, except an Association has a set of access rights.
 */
public record Association(long source, long target, AccessRightSet arset) implements Serializable {

}
