package gov.nist.csd.pm.core.pap.query.model.context;

/**
 * Represents a target context for a single node (by id or name).
 */
public abstract sealed class NodeTargetContext extends TargetContext
        permits IdTargetContext, NameTargetContext {
}
