package gov.nist.csd.pm.core.pap.query.model.context;

/**
 * Represents a target context backed by a single node (by id or name).
 */
public sealed interface TargetNodeContext extends TargetContext
        permits TargetIdContext, TargetNameContext {
}
