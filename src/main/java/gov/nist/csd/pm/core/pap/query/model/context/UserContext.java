package gov.nist.csd.pm.core.pap.query.model.context;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.store.GraphStore;
import java.io.Serializable;

public sealed interface UserContext extends Serializable
        permits SingleUserContext, CompositeUserContext {

    String getProcess();

    /**
     * Check that the user in the context exists.
     * @param graphStore the GraphStore used to check.
     * @throws PMException if any error occurs while checking.
     */
    void checkExists(GraphStore graphStore) throws PMException;
}
