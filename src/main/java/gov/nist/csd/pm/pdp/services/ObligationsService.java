package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.SuperGraph;
import gov.nist.csd.pm.pip.obligations.model.Obligation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static gov.nist.csd.pm.operations.Operations.RESET;

public class ObligationsService extends Service {

    public ObligationsService(PAP pap, EPP epp) {
        super(pap, epp);
    }

    public void add(UserContext userCtx, Obligation obligation, boolean enable) throws PMException {
        getPAP().getObligationsPAP().add(obligation, enable);
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

    public void reset(UserContext userCtx) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        // check that the user can reset the graph
        if (!hasPermissions(userCtx, SuperGraph.getSuperO().getID(), RESET)) {
            throw new PMAuthorizationException("unauthorized permissions to reset the graph");
        }

        List<Obligation> obligations = getAll(userCtx);
        Set<String> labels = new HashSet<String>();
        for (Obligation obli : obligations) {
            labels.add(obli.getLabel());
        }
        for (String label : labels) {
            delete(userCtx, label);
        }
    }
}
