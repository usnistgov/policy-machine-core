package gov.nist.csd.pm.core.pap.query.model.context;

import java.io.Serializable;

public sealed interface UserContext extends Serializable
        permits UserNodeContext, AnonymousUserContext, CompositeUserContext {

    String getProcess();
}
