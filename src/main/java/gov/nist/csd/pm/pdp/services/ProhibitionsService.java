package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.operations.Operations;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.SuperGraph;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static gov.nist.csd.pm.operations.Operations.*;

public class ProhibitionsService extends Service {

    public ProhibitionsService(PAP pap, EPP epp) {
        super(pap, epp);
    }

    public void createProhibition(UserContext userCtx, Prohibition prohibition) throws PMException {
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

    public List<Prohibition> getProhibitions() throws PMException {
        return getProhibitionsPAP().getAll();
    }

    /**
     * Get the prohibition with the given node from the PAP. An exception will be thrown if one does not exist.
     *
     * @param prohibitionName The name of the Prohibition to retrieve.
     * @return the prohibition with the given node.
     * @throws PMAuthorizationException if the current user is not authorized to carry out the action.
     * @throws PMException if there is an error with the graph.
     * @throws PMException if a prohibition with the given name does not exist.
     */
    public Prohibition getProhibition(String prohibitionName) throws PMException {
        Prohibition prohibition = getProhibitionsPAP().get(prohibitionName);
        if(prohibition == null) {
            throw new PMException(String.format("prohibition with the name %s does not exist", prohibitionName));
        }

        return prohibition;
    }

    /**
     * Update the prohibition.  The prohibition is identified by the name.
     *
     * @param prohibition The prohibition to update.
     * @throws IllegalArgumentException if the given prohibition is null.
     * @throws IllegalArgumentException if the given prohibition's name is null.
     * @throws PMAuthorizationException if the current user is not authorized to carry out the action.
     * @throws PMException if there is an error with the graph.
     * @throws PMException if a prohibition with the given name does not exist.
     */
    public void updateProhibition(UserContext userCtx, Prohibition prohibition) throws PMException {
        if(prohibition == null) {
            throw new IllegalArgumentException("the prohibition to update was null");
        } else if(prohibition.getName() == null || prohibition.getName().isEmpty()) {
            throw new IllegalArgumentException("cannot update a prohibition with a null name");
        }

        // delete the prohibition
        deleteProhibition(prohibition.getName());

        //create prohibition in PAP
        createProhibition(userCtx, prohibition);
    }

    public void deleteProhibition(String prohibitionName) throws PMException {
        //check that the prohibition exists
        getProhibition(prohibitionName);

        //delete prohibition in PAP
        getProhibitionsPAP().delete(prohibitionName);
    }

    public void reset(UserContext userCtx) throws PMException {
        if(userCtx == null) {
            throw new PMException("no user context provided to the PDP");
        }

        // check that the user can reset the graph
        if (!hasPermissions(userCtx, SuperGraph.getSuperO().getID(), RESET)) {
            throw new PMAuthorizationException("unauthorized permissions to reset the graph");
        }

        List<Prohibition> prohibitions = getProhibitions();
        Set<String> names = new HashSet<>();
        for (Prohibition prohib : prohibitions) {
            names.add(prohib.getName());
        }
        for (String name : names) {
            deleteProhibition(name);
        }
    }
}