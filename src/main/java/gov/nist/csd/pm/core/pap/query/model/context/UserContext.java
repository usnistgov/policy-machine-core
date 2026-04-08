package gov.nist.csd.pm.core.pap.query.model.context;

import java.io.Serializable;

public sealed interface UserContext extends Serializable
        permits NodeUserContext, AnonymousUserContext, ConjunctiveUserContext {

    String getProcess();
}
