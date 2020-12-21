package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.common.FunctionalEntity;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.Obligation;

import java.util.List;

public class ObligationsAdmin implements Obligations {

    private Obligations obligations;

    public ObligationsAdmin(FunctionalEntity pip) throws PMException {
        this.obligations = pip.getObligations();
    }

    @Override
    public void add(Obligation obligation, boolean enable) throws PMException {
        obligations.add(obligation, enable);
    }

    @Override
    public Obligation get(String label) throws PMException {
        return obligations.get(label);
    }

    @Override
    public List<Obligation> getAll() throws PMException {
        return obligations.getAll();
    }

    @Override
    public void update(String label, Obligation obligation) throws PMException {
        obligations.update(label, obligation);
    }

    @Override
    public void delete(String label) throws PMException {
        obligations.delete(label);
    }

    @Override
    public void setEnable(String label, boolean enabled) throws PMException {
        obligations.setEnable(label, enabled);
    }

    @Override
    public List<Obligation> getEnabled() throws PMException {
        return obligations.getEnabled();
    }
}
