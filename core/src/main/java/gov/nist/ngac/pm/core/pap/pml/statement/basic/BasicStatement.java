package gov.nist.ngac.pm.core.pap.pml.statement.basic;

import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;

/**
 * Marker base class for PML control-flow and variable statements (if/foreach/return/break/continue,
 * variable declarations and assignments), as distinct from
 * {@link gov.nist.ngac.pm.core.pap.pml.statement.operation.OperationStatement}s.
 */
public abstract class BasicStatement<T> extends PMLStatement<T> {

} 