package gov.nist.ngac.pm.core.pap.obligation.event.operation;

import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementSerializable;
import java.io.Serializable;

/**
 * The operation half of an {@link gov.nist.ngac.pm.core.pap.obligation.event.EventPattern}: either
 * matches any operation ({@link AnyOperationPattern}) or a specific named operation, optionally gated by
 * an "on (...)" condition ({@link MatchesOperationPattern}).
 */
public abstract sealed class OperationPattern implements Serializable, PMLStatementSerializable
    permits AnyOperationPattern, MatchesOperationPattern {


}
