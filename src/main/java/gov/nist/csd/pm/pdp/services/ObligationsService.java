package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.policy.SuperPolicy;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.Obligation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static gov.nist.csd.pm.operations.Operations.RESET;

public class ObligationsService extends Service implements Obligations {

    public ObligationsService(PAP pap, EPP epp, SuperPolicy superPolicy) {
        super(pap, epp);

        this.superPolicy = superPolicy;
    }

    @Override
    public void add(Obligation obligation, boolean enable) throws PMException {
        getPAP().getObligationsPAP().add(obligation, enable);
    }

    @Override
    public Obligation get(String label) {
        return getPAP().getObligationsPAP().get(label);
    }

    @Override
    public List<Obligation> getAll() {
        return getPAP().getObligationsPAP().getAll();
    }

    @Override
    public void update(String label, Obligation obligation) {
        getPAP().getObligationsPAP().update(label, obligation);
    }

    @Override
    public void delete(String label) {
        getPAP().getObligationsPAP().delete(label);
    }

    @Override
    public void setEnable(String label, boolean enabled) {
        getPAP().getObligationsPAP().setEnable(label, enabled);
    }

    @Override
    public List<Obligation> getEnabled() {
        return getPAP().getObligationsPAP().getEnabled();
    }

    public void reset() throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        // check that the user can reset the graph
        if (!hasPermissions(userCtx, superPolicy.getSuperPolicyClassRep().getName(), RESET)) {
            throw new PMAuthorizationException("unauthorized permissions to reset obligations");
        }

        List<Obligation> obligations = getAll();
        Set<String> labels = new HashSet<>();
        for (Obligation obli : obligations) {
            labels.add(obli.getLabel());
        }
        for (String label : labels) {
            delete(label);
        }
    }
}
