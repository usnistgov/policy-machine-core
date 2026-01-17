package gov.nist.csd.pm.core.pap.obligation.event.operation;

import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementSerializable;
import java.io.Serializable;

public abstract sealed class OperationPattern implements Serializable, PMLStatementSerializable
    permits AnyOperationPattern, MatchesOperationPattern {


}
