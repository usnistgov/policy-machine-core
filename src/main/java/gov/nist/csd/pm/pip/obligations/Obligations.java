package gov.nist.csd.pm.pip.obligations;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.obligations.model.Obligation;

import java.util.List;

public interface Obligations {
    void add(Obligation obligation, boolean enable) throws PMException;

    Obligation get(String label);

    List<Obligation> getAll();

    void update(String label, Obligation obligation);

    void delete(String label);

    void setEnable(String label, boolean enabled);

    List<Obligation> getEnabled();

}
