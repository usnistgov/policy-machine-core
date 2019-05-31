package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.Obligation;

import java.util.List;

public class ObligationsPAP implements Obligations {

    private Obligations obligations;

    public ObligationsPAP(Obligations obligations) {
        this.obligations = obligations;
    }

    @Override
    public void add(Obligation obligation) throws PMException {
        obligations.add(obligation);
    }

    @Override
    public Obligation get(String label) {
        return obligations.get(label);
    }

    @Override
    public List<Obligation> getAll() {
        return obligations.getAll();
    }

    @Override
    public void update(String label, Obligation obligation) {
        obligations.update(label, obligation);
    }

    @Override
    public void delete(String label) {
        obligations.delete(label);
    }

    @Override
    public void setEnable(String label, boolean enabled) {
        obligations.setEnable(label, enabled);
    }

    @Override
    public List<Obligation> getEnabled() {
        return obligations.getEnabled();
    }

}
