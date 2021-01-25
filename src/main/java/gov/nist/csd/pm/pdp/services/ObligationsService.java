package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.audit.Auditor;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.services.guard.ObligationsGuard;
import gov.nist.csd.pm.common.FunctionalEntity;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.Obligation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObligationsService extends Service implements Obligations {

    private ObligationsGuard guard;

    public ObligationsService(UserContext userCtx, FunctionalEntity pap, EPP epp, Decider decider, Auditor auditor) {
        super(userCtx, pap, epp, decider, auditor);

        this.guard = new ObligationsGuard(pap, decider);
    }

    @Override
    public void add(Obligation obligation, boolean enable) throws PMException {
        guard.checkAdd(userCtx);

        getObligationsAdmin().add(obligation, enable);
    }

    @Override
    public Obligation get(String label) throws PMException {
        guard.checkGet(userCtx);

        return getObligationsAdmin().get(label);
    }

    @Override
    public List<Obligation> getAll() throws PMException {
        guard.checkGet(userCtx);

        return getObligationsAdmin().getAll();
    }

    @Override
    public void update(String label, Obligation obligation) throws PMException {
        guard.checkUpdate(userCtx);

        getObligationsAdmin().update(label, obligation);
    }

    @Override
    public void delete(String label) throws PMException {
        guard.checkDelete(userCtx);

        getObligationsAdmin().delete(label);
    }

    @Override
    public void setEnable(String label, boolean enabled) throws PMException {
        guard.checkEnable(userCtx);

        getObligationsAdmin().setEnable(label, enabled);
    }

    @Override
    public List<Obligation> getEnabled() throws PMException {
        guard.checkGet(userCtx);

        return getObligationsAdmin().getEnabled();
    }

    public void reset() throws PMException {
        guard.checkReset(userCtx);

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
