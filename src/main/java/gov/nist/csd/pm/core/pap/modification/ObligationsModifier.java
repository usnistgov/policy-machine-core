package gov.nist.csd.pm.core.pap.modification;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.ObligationNameExistsException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.csd.pm.core.pap.store.PolicyStore;

public class ObligationsModifier extends Modifier implements ObligationsModification {

    public ObligationsModifier(PolicyStore store) {
        super(store);
    }

    @Override
    public void createObligation(long authorId,
                                 String name,
                                 EventPattern eventPattern,
                                 ObligationResponse response) throws PMException {
        checkCreateInput(authorId, name);

        policyStore.obligations().createObligation(authorId, name, eventPattern, response);
    }

    @Override
    public void deleteObligation(String name) throws PMException {
        if(!checkDeleteInput(name)) {
            return;
        }

        policyStore.obligations().deleteObligation(name);
    }

    /**
     * Check the obligation being created.
     *
     * @param author The author of the obligation.
     * @param name   The name of the obligation.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected void checkCreateInput(long author, String name) throws PMException {
        if (policyStore.obligations().obligationExists(name)) {
            throw new ObligationNameExistsException(name);
        }

        checkAuthorExists(author);
    }

    /**
     * Check if the obligation exists. If it doesn't, return false to indicate to the caller that execution should not
     * proceed.
     *
     * @param name The name of the obligation.
     * @return True if the execution should proceed, false otherwise.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    protected boolean checkDeleteInput(String name) throws PMException {
	    return policyStore.obligations().obligationExists(name);
    }

    private void checkAuthorExists(long author) throws PMException {
        if (!policyStore.graph().nodeExists(author)) {
            throw new NodeDoesNotExistException(author);
        }
    }
}
