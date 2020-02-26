package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.operations.Operations;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static gov.nist.csd.pm.operations.Operations.*;

public class ProhibitionsService extends Service implements Prohibitions {

    public ProhibitionsService(PAP pap, EPP epp) {
        super(pap, epp);
    }

    public List<Prohibition> getProhibitions() throws PMException {
        return getProhibitionsPAP().getAll();
    }

    @Override
    public void add(Prohibition prohibition) throws PMException {
        String name = prohibition.getName();
        Prohibition.Subject subject = prohibition.getSubject();
        List<Prohibition.Node> nodes = prohibition.getNodes();

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

        //check the user can create a prohibition on the subject and the nodes
        Decider decider = getDecider();
        if(subject.getSubjectType().equals(Prohibition.Subject.Type.USER) || subject.getSubjectType().equals(Prohibition.Subject.Type.USER_ATTRIBUTE)) {
            // first check that the subject exists
            if(!getGraphPAP().exists(subject.getSubjectID())) {
                throw new PMException(String.format("node with ID %d and type %s does not exist", subject.getSubjectID(), subject.getSubjectType()));
            }
            if(!decider.check(userCtx.getUserID(), userCtx.getProcessID(), subject.getSubjectID(), CREATE_PROHIBITION)) {
                throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", subject.getSubjectID(), PROHIBIT_SUBJECT));
            }
        }

        for(Prohibition.Node node : nodes) {
            if(!decider.check(userCtx.getUserID(), userCtx.getProcessID(), node.getID(), CREATE_PROHIBITION)) {
                throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", node.getID(), Operations.PROHIBIT_RESOURCE));
            }
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
    public List<Prohibition> getProhibitionsFor(long subjectID) throws PMException {
        return getPAP().getProhibitionsPAP().getProhibitionsFor(subjectID);
    }

    @Override
    public void update(Prohibition prohibition) throws PMException {
        if(prohibition == null) {
            throw new IllegalArgumentException("the prohibition to update was null");
        } else if(prohibition.getName() == null || prohibition.getName().isEmpty()) {
            throw new IllegalArgumentException("cannot update a prohibition with a null name");
        }

        // delete the prohibition
        delete(prohibition.getName());

        //create prohibition in PAP
        add(prohibition);
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
        if (!hasPermissions(userCtx, superPolicy.getSuperPolicyClassRep().getID(), RESET)) {
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