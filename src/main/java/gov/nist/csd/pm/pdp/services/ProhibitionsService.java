package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.operations.Operations;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.policy.SuperPolicy;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.ContainerCondition;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.*;

import static gov.nist.csd.pm.operations.Operations.*;

public class ProhibitionsService extends Service implements Prohibitions {

    public ProhibitionsService(PAP pap, EPP epp, SuperPolicy superPolicy) {
        super(pap, epp);

        this.superPolicy = superPolicy;
    }

    public List<Prohibition> getProhibitions() throws PMException {
        return getProhibitionsPAP().getAll();
    }

    @Override
    public void add(Prohibition prohibition) throws PMException {
        String name = prohibition.getName();

        //check that the prohibition name is not null or empty
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("a null name was provided when creating a prohibition");
        }

        //check the prohibitions doesn't already exist
        for(Prohibition p : getProhibitions()) {
            if(p.getName().equals(name)) {
                throw new PMException(String.format("a prohibition with the name %s already exists", name));
            }
        }

        // check that the user has permission to create a prohibition on the super policy object
        if(!getDecider().check(userCtx.getUser(), userCtx.getProcess(), superPolicy.getSuperObjectAttribute().getName(), CREATE_PROHIBITION)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", superPolicy.getSuperObjectAttribute().getName(), CREATE_PROHIBITION));
        }

        //create prohibition in PAP
        getProhibitionsPAP().add(prohibition);
    }

    @Override
    public List<Prohibition> getAll() throws PMException {
        return getProhibitionsPAP().getAll();
    }

    @Override
    public Prohibition get(String prohibitionName) throws PMException {
        return getPAP().getProhibitionsPAP().get(prohibitionName);
    }

    @Override
    public List<Prohibition> getProhibitionsFor(String subject) throws PMException {
        return getPAP().getProhibitionsPAP().getProhibitionsFor(subject);
    }

    @Override
    public void update(String prohibitionName, Prohibition prohibition) throws PMException {
        getPAP().getProhibitionsPAP().update(prohibitionName, prohibition);
    }

    @Override
    public void delete(String prohibitionName) throws PMException {
        getPAP().getProhibitionsPAP().delete(prohibitionName);
    }

    public void reset(UserContext userCtx) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        // check that the user can reset the graph
        if (!hasPermissions(userCtx, superPolicy.getSuperPolicyClassRep().getName(), RESET)) {
            throw new PMAuthorizationException("unauthorized permissions to reset the graph");
        }

        List<Prohibition> prohibitions = getProhibitions();
        Set<String> names = new HashSet<>();
        for (Prohibition prohib : prohibitions) {
            names.add(prohib.getName());
        }
        for (String name : names) {
            delete(name);
        }
    }
}