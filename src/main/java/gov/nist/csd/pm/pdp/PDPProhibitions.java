package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.epp.EventEmitter;
import gov.nist.csd.pm.epp.EventProcessor;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.adjudicator.AdjudicatorProhibitions;
import gov.nist.csd.pm.policy.Prohibitions;
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

class PDPProhibitions implements Prohibitions, EventEmitter {
    private UserContext userCtx;
    private AdjudicatorProhibitions adjudicator;
    private PAP pap;
    private EventProcessor listener;

    public PDPProhibitions(UserContext userCtx, AdjudicatorProhibitions adjudicator, PAP pap, EventProcessor listener) {
        this.userCtx = userCtx;
        this.adjudicator = adjudicator;
        this.pap = pap;
        this.listener = listener;
    }

    @Override
    public void create(String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        adjudicator.create(name, subject, accessRightSet, intersection, containerConditions);

        pap.prohibitions().create(name, subject, accessRightSet, intersection, containerConditions);

        CreateProhibitionEvent createProhibitionEvent = new CreateProhibitionEvent(
                name, subject, accessRightSet, intersection, List.of(containerConditions)
        );

        // emit event for subject
        emitEvent(new EventContext(userCtx, subject.getName(), createProhibitionEvent));

        // emit event for each container specified
        for (ContainerCondition containerCondition : containerConditions) {
            emitEvent(new EventContext(userCtx, containerCondition.getName(), createProhibitionEvent));
        }
    }

    @Override
    public void update(String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        adjudicator.update(name, subject, accessRightSet, intersection, containerConditions);

        pap.prohibitions().update(name, subject, accessRightSet, intersection, containerConditions);

        UpdateProhibitionEvent updateProhibitionEvent = new UpdateProhibitionEvent(
                name, subject, accessRightSet, intersection, List.of(containerConditions)
        );

        // emit event for subject
        emitEvent(new EventContext(userCtx, subject.getName(), updateProhibitionEvent));

        // emit event for each container specified
        for (ContainerCondition containerCondition : containerConditions) {
            emitEvent(new EventContext(userCtx, containerCondition.getName(), updateProhibitionEvent));
        }
    }

    @Override
    public void delete(String name) throws PMException {
        if (!exists(name)) {
            return;
        }

        adjudicator.delete(name);

        Prohibition prohibition = pap.prohibitions().get(name);

        pap.prohibitions().delete(name);

        emitDeleteProhibitionEvent(prohibition);
    }

    private void emitDeleteProhibitionEvent(Prohibition prohibition) throws PMException {
        ProhibitionSubject subject = prohibition.getSubject();
        List<ContainerCondition> containerConditions = prohibition.getContainers();

        DeleteProhibitionEvent deleteProhibitionEvent = new DeleteProhibitionEvent(prohibition.getName());

        // emit event for subject
        emitEvent(new EventContext(userCtx, subject.getName(), deleteProhibitionEvent));

        // emit event for each container specified
        for (ContainerCondition containerCondition : containerConditions) {
            emitEvent(new EventContext(userCtx, containerCondition.getName(), deleteProhibitionEvent));
        }
    }

    @Override
    public Map<String, List<Prohibition>> getAll() throws PMException {
        return adjudicator.getAll();
    }

    @Override
    public boolean exists(String name) throws PMException {
        return adjudicator.exists(name);
    }

    @Override
    public List<Prohibition> getWithSubject(String subject) throws PMException {
        return adjudicator.getWithSubject(subject);
    }

    @Override
    public Prohibition get(String name) throws PMException {
        return adjudicator.get(name);
    }

    @Override
    public void addEventListener(EventProcessor listener) {

    }

    @Override
    public void removeEventListener(EventProcessor listener) {

    }

    @Override
    public void emitEvent(EventContext event) throws PMException {
        this.listener.processEvent(event);
    }
}
