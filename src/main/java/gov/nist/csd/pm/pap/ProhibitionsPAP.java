package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.List;

public class ProhibitionsPAP implements Prohibitions {

    private Prohibitions prohibitions;

    public ProhibitionsPAP(MemProhibitions prohibitionsDAO) throws PMException {
        this.prohibitions = prohibitionsDAO;
    }

    @Override
    public void add(Prohibition prohibition) throws PMException {
        prohibitions.add(prohibition);
    }

    @Override
    public List<Prohibition> getAll() throws PMException {
        return prohibitions.getAll();
    }

    @Override
    public Prohibition get(String prohibitionName) throws PMException {
        return prohibitions.get(prohibitionName);
    }

    @Override
    public void update(Prohibition prohibition) throws PMException {
        prohibitions.update(prohibition);
    }

    @Override
    public void delete(String prohibitionName) throws PMException {
        prohibitions.delete(prohibitionName);
    }
}
