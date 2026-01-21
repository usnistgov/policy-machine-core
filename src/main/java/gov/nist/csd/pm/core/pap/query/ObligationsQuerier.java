package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.ObligationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObligationsQuerier extends Querier implements ObligationsQuery {

    public ObligationsQuerier(PolicyStore store) {
        super(store);
    }

    @Override
    public Obligation getObligation(String name) throws PMException {
        if (!obligationExists(name)) {
            throw new ObligationDoesNotExistException(name);
        }

        return store.obligations().getObligation(name);
    }

    @Override
    public Collection<Obligation> getObligations() throws PMException {
        return store.obligations().getObligations();
    }

    @Override
    public boolean obligationExists(String name) throws PMException {
        return store.obligations().obligationExists(name);
    }

    @Override
    public Collection<Obligation> getObligationsWithAuthor(long author) throws PMException {
        Collection<Obligation> obligations = store.obligations().getObligations();
        List<Obligation> withAuthor = new ArrayList<>();
        for (Obligation obligation : obligations) {
            if(obligation.getAuthorId() == author) {
                withAuthor.add(obligation);
            }
        }

        return withAuthor;
    }
}
