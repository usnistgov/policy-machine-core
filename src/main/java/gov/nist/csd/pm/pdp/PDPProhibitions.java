package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.adjudicator.ProhibitionsAdjudicator;
import gov.nist.csd.pm.policy.Prohibitions;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.events.prohibitions.CreateProhibitionEvent;
import gov.nist.csd.pm.policy.events.prohibitions.DeleteProhibitionEvent;
import gov.nist.csd.pm.policy.events.prohibitions.UpdateProhibitionEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;

import java.util.List;
import java.util.Map;

class PDPProhibitions implements Prohibitions, PolicyEventEmitter {
    private UserContext userCtx;
    private ProhibitionsAdjudicator adjudicator;
    private PAP pap;
    private PolicyEventListener listener;

    public PDPProhibitions(UserContext userCtx, ProhibitionsAdjudicator adjudicator, PAP pap, PolicyEventListener listener) {
        this.userCtx = userCtx;
        this.adjudicator = adjudicator;
        this.pap = pap;
        this.listener = listener;
    }

    @Override
    public void create(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        adjudicator.create(label, subject, accessRightSet, intersection, containerConditions);

        pap.prohibitions().create(label, subject, accessRightSet, intersection, containerConditions);

        CreateProhibitionEvent createProhibitionEvent = new CreateProhibitionEvent(
                label, subject, accessRightSet, intersection, List.of(containerConditions)
        );

        // emit event for subject
        emitEvent(new EventContext(userCtx, subject.getName(), createProhibitionEvent));

        // emit event for each container specified
        for (ContainerCondition containerCondition : containerConditions) {
            emitEvent(new EventContext(userCtx, containerCondition.name(), createProhibitionEvent));
        }
    }

    @Override
    public void update(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        adjudicator.update(label, subject, accessRightSet, intersection, containerConditions);

        pap.prohibitions().update(label, subject, accessRightSet, intersection, containerConditions);

        UpdateProhibitionEvent updateProhibitionEvent = new UpdateProhibitionEvent(
                label, subject, accessRightSet, intersection, List.of(containerConditions)
        );

        // emit event for subject
        emitEvent(new EventContext(userCtx, subject.getName(), updateProhibitionEvent));

        // emit event for each container specified
        for (ContainerCondition containerCondition : containerConditions) {
            emitEvent(new EventContext(userCtx, containerCondition.name(), updateProhibitionEvent));
        }
    }

    @Override
    public void delete(String label) throws PMException {
        if (!exists(label)) {
            return;
        }

        adjudicator.delete(label);

        Prohibition prohibition = pap.prohibitions().get(label);

        pap.prohibitions().delete(label);

        emitDeleteProhibitionEvent(prohibition);
    }

    private void emitDeleteProhibitionEvent(Prohibition prohibition) throws PMException {
        ProhibitionSubject subject = prohibition.getSubject();
        List<ContainerCondition> containerConditions = prohibition.getContainers();

        DeleteProhibitionEvent deleteProhibitionEvent = new DeleteProhibitionEvent(prohibition.getLabel());

        // emit event for subject
        emitEvent(new EventContext(userCtx, subject.getName(), deleteProhibitionEvent));

        // emit event for each container specified
        for (ContainerCondition containerCondition : containerConditions) {
            emitEvent(new EventContext(userCtx, containerCondition.name(), deleteProhibitionEvent));
        }
    }

    @Override
    public Map<String, List<Prohibition>> getAll() throws PMException {
        return adjudicator.getAll();
    }

    @Override
    public boolean exists(String label) throws PMException {
        return adjudicator.exists(label);
    }

    @Override
    public List<Prohibition> getWithSubject(String subject) throws PMException {
        return adjudicator.getWithSubject(subject);
    }

    @Override
    public Prohibition get(String label) throws PMException {
        return adjudicator.get(label);
    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {

    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {

    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        this.listener.handlePolicyEvent(event);
    }
}
