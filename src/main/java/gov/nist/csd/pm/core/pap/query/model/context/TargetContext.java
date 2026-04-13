package gov.nist.csd.pm.core.pap.query.model.context;

import java.io.Serializable;

/**
 * Represents the target resource in an access decision.
 * Two types are supported:
 *   - NodeTargetContext: identifies a specific target node by id (IdTargetContext) or by name (NameTargetContext).
 *   - AnonymousTargetContext: identifies a target by a set of attribute IDs (AttributeIdsTargetContext) or names
 *                             (AttributeNamesTargetContext).
 */
public sealed interface TargetContext extends Serializable
        permits NodeTargetContext, AnonymousTargetContext {
}
