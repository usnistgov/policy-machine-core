package gov.nist.csd.pm.core.pap.operation;

/**
 * Discriminates a persisted {@link Operation} row's shape: a live implementation resolved through the
 * {@code NativeOperationRegistry}, or PML source text recompiled on read.
 */
public enum OperationKind {
    NATIVE,
    PML
}
