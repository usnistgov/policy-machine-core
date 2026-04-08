package gov.nist.csd.pm.core.pap.query.model.context;

/**
 * Represents a target context defined by a set of target attribute ids or names.
 */
public sealed interface AnonymousTargetContext extends TargetContext
        permits AttributeIdsTargetContext, AttributeNamesTargetContext {
}
