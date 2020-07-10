package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.Obligation;

import java.util.List;

public class ObligationsAdmin implements Obligations {

    private Obligations obligations;

    public ObligationsAdmin(Obligations obligations) {
        this.obligations = obligations;
    }

    @Override
    public void add(Obligation obligation, boolean enable) throws PMException {

    }

    @Override
    public Obligation get(String label) {
        return null;
    }

    @Override
    public List<Obligation> getAll() {
        return null;
    }

    @Override
    public void update(String label, Obligation obligation) {

    }

    @Override
    public void delete(String label) {

    }

    @Override
    public void setEnable(String label, boolean enabled) {

    }

    @Override
    public List<Obligation> getEnabled() {
        return null;
    }
}
