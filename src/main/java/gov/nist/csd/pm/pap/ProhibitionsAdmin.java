package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.common.FunctionalEntity;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.List;

public class ProhibitionsAdmin implements Prohibitions {

    private Prohibitions prohibitions;

    public ProhibitionsAdmin(FunctionalEntity pip) throws PMException {
        this.prohibitions = pip.getProhibitions();
    }

    @Override
    public void add(Prohibition prohibition) throws PMException {
        String name = prohibition.getName();

        //check that the prohibition name is not null or empty
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("a null name was provided when creating a prohibition");
        }

        //check the prohibitions doesn't already exist
        for(Prohibition p : getAll()) {
            if(p.getName().equals(name)) {
                throw new PMException(String.format("a prohibition with the name %s already exists", name));
            }
        }

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
    public List<Prohibition> getProhibitionsFor(String subject) throws PMException {
        return prohibitions.getProhibitionsFor(subject);
    }

    @Override
    public void update(String prohibitionName, Prohibition prohibition) throws PMException {
        prohibitions.update(prohibitionName, prohibition);
    }

    @Override
    public void delete(String prohibitionName) throws PMException {
        prohibitions.delete(prohibitionName);
    }
}
