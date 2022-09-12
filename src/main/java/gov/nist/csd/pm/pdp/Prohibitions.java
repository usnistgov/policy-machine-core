package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.adjudicator.Adjudicator;
import gov.nist.csd.pm.pdp.reviewer.PolicyReviewer;
import gov.nist.csd.pm.policy.events.PolicyEventEmitter;
import gov.nist.csd.pm.policy.events.PolicyEventListener;
import gov.nist.csd.pm.policy.author.ProhibitionsAuthor;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.util.List;
import java.util.Map;

class Prohibitions implements ProhibitionsAuthor, PolicyEventEmitter {

    private final UserContext userCtx;
    private final PAP pap;
    private final Adjudicator adjudicator;
    private final List<PolicyEventListener> epps;

    public Prohibitions(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer, List<PolicyEventListener> epps) {
        this.userCtx = userCtx;
        this.pap = pap;
        this.adjudicator = new Adjudicator(userCtx, pap, policyReviewer);
        this.epps = epps;
    }

    @Override
    public void create(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        adjudicator.prohibitions().create(label, subject, accessRightSet, intersection, containerConditions);

        pap.prohibitions().create(label, subject, accessRightSet, intersection, containerConditions);

        CreateProhibitionEvent createProhibitionEvent = new CreateProhibitionEvent(
                label, subject, accessRightSet, intersection, List.of(containerConditions)
        );

        // emit event for subject
        emitEvent(new EventContext(userCtx, subject.name(), createProhibitionEvent));

        // emit event for each container specified
        for (ContainerCondition containerCondition : containerConditions) {
            emitEvent(new EventContext(userCtx, containerCondition.name(), createProhibitionEvent));
        }
    }

    @Override
    public void update(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        adjudicator.prohibitions().update(label, subject, accessRightSet, intersection, containerConditions);

        pap.prohibitions().update(label, subject, accessRightSet, intersection, containerConditions);

        UpdateProhibitionEvent updateProhibitionEvent = new UpdateProhibitionEvent(
                label, subject, accessRightSet, intersection, List.of(containerConditions)
        );

        // emit event for subject
        emitEvent(new EventContext(userCtx, subject.name(), updateProhibitionEvent));

        // emit event for each container specified
        for (ContainerCondition containerCondition : containerConditions) {
            emitEvent(new EventContext(userCtx, containerCondition.name(), updateProhibitionEvent));
        }
    }

    @Override
    public void delete(String label) throws PMException {
        adjudicator.prohibitions().delete(label);

        pap.prohibitions().delete(label);

        emitDeleteProhibitionEvent(label);
    }

    @Override
    public Map<String, List<Prohibition>> getAll() throws PMException {
        return adjudicator.prohibitions().getAll();
    }

    @Override
    public List<Prohibition> getWithSubject(String subject) throws PMException {
        return adjudicator.prohibitions().getWithSubject(subject);
    }

    @Override
    public Prohibition get(String label) throws PMException {
        return adjudicator.prohibitions().get(label);
    }

    private void emitDeleteProhibitionEvent(String label) throws PMException {
        Prohibition prohibition;
        try {
            prohibition = pap.prohibitions().get(label);
        } catch (PMException e) {
            throw new PMException("error getting prohibition " + label + " from PolicyReviewer: " + e.getMessage());
        }

        ProhibitionSubject subject = prohibition.getSubject();
        List<ContainerCondition> containerConditions = prohibition.getContainers();

        DeleteProhibitionEvent deleteProhibitionEvent = new DeleteProhibitionEvent(label);

        // emit event for subject
        emitEvent(new EventContext(userCtx, subject.name(), deleteProhibitionEvent));

        // emit event for each container specified
        for (ContainerCondition containerCondition : containerConditions) {
            emitEvent(new EventContext(userCtx, containerCondition.name(), deleteProhibitionEvent));
        }
    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {
        // adding event listeners is done by the PDP class
    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {
        // removing event listeners is done by the PDP class
    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        for (PolicyEventListener epp : epps) {
            epp.handlePolicyEvent(event);
        }
    }
}
