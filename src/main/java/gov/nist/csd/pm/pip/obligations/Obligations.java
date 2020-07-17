package gov.nist.csd.pm.pip.obligations;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.obligations.model.Obligation;

import java.util.List;

public interface Obligations {
    void add(Obligation obligation, boolean enable) throws PMException;

    Obligation get(String label) throws PMException;

    List<Obligation> getAll() throws PMException;

    void update(String label, Obligation obligation) throws PMException;

    void delete(String label) throws PMException;

    void setEnable(String label, boolean enabled) throws PMException;

    List<Obligation> getEnabled() throws PMException;

}
