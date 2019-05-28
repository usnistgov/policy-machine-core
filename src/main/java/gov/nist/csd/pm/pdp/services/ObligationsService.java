package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pip.obligations.model.Obligation;

import java.util.List;

public class ObligationsService extends Service {

    public ObligationsService(PAP pap, EPP epp) {
        super(pap, epp);
    }

    public void add(UserContext userCtx, Obligation obligation) throws PMException {
        getPAP().getObligationsPAP().add(obligation);
    }

    public Obligation get(UserContext userCtx, String label) {
        return getPAP().getObligationsPAP().get(label);
    }

    public List<Obligation> getAll(UserContext userCtx) {
        return getPAP().getObligationsPAP().getAll();
    }

    public void update(UserContext userCtx, String label, Obligation obligation) {
        getPAP().getObligationsPAP().update(label, obligation);
    }

    public void delete(UserContext userCtx, String label) {
        getPAP().getObligationsPAP().delete(label);
    }

    public void enable(String label) {
        getPAP().getObligationsPAP().setEnable(label, true);
    }

    public List<Obligation> getEnabled(UserContext userCtx) {
        return getPAP().getObligationsPAP().getEnabled();
    }
}
