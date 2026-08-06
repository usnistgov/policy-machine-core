package gov.nist.ngac.pm.core.pap.modification;

import gov.nist.ngac.pm.core.common.exception.ObligationNameExistsException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;

/**
 * {@link ObligationsModification} implementation, validating an obligation's author and name before
 * delegating to the backend {@link gov.nist.ngac.pm.core.pap.store.ObligationsStore}.
 */
public class ObligationsModifier extends Modifier implements ObligationsModification {

    public ObligationsModifier(PolicyStore store) {
        super(store);
    }

    @Override
    public void createObligation(Obligation obligation) throws PMException {
        checkCreateInput(obligation.getAuthor(), obligation.getName());

        policyStore.obligations().createObligation(obligation);
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
    protected void checkCreateInput(NodeUserContext author, String name) throws PMException {
        if (policyStore.obligations().obligationExists(name)) {
            throw new ObligationNameExistsException(name);
        }

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
}
