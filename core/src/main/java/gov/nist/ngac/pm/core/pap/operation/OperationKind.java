package gov.nist.ngac.pm.core.pap.operation;

/**
 * Discriminates a persisted {@link Operation} row's shape: a live implementation resolved through the
 * {@link gov.nist.ngac.pm.core.pap.NativeOperationRegistry}, or PML source text recompiled on read.
 */
public enum OperationKind {
    NATIVE,
    PML
}
