package gov.nist.csd.pm.core.pap.query.model.context;

import java.io.Serializable;

public sealed interface TargetContext extends Serializable
        permits NodeTargetContext, AnonymousTargetContext {
}
