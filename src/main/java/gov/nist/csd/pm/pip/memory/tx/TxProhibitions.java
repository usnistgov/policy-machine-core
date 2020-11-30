package gov.nist.csd.pm.pip.memory.tx;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.List;

public class TxProhibitions implements Prohibitions {
    @Override
    public void add(Prohibition prohibition) throws PMException {

    }

    @Override
    public List<Prohibition> getAll() throws PMException {
        return null;
    }

    @Override
    public Prohibition get(String prohibitionName) throws PMException {
        return null;
    }

    @Override
    public List<Prohibition> getProhibitionsFor(String subject) throws PMException {
        return null;
    }

    @Override
    public void update(String prohibitionName, Prohibition prohibition) throws PMException {

    }

    @Override
    public void delete(String prohibitionName) throws PMException {

    }

    public void commit() {

    }
}
