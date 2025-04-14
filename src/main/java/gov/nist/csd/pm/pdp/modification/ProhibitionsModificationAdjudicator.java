package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.pap.function.op.prohibition.CreateProhibitionOp;
import gov.nist.csd.pm.pap.function.op.prohibition.DeleteProhibitionOp;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.op.prohibition.ProhibitionOp.ProhibitionOpArgs;
import gov.nist.csd.pm.pap.modification.ProhibitionsModification;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

import java.util.ArrayList;
import java.util.Collection;

public class ProhibitionsModificationAdjudicator extends Adjudicator implements ProhibitionsModification {

    public ProhibitionsModificationAdjudicator(UserContext userCtx, PAP pap) {
        super(pap, userCtx);
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public void createProhibition(String name, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, Collection<ContainerCondition> containerConditions) throws PMException {
        CreateProhibitionOp op = new CreateProhibitionOp();
        ProhibitionOpArgs args = new ProhibitionOpArgs(name, subject, accessRightSet, intersection, new ArrayList<>(containerConditions));

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void deleteProhibition(String name) throws PMException {
        Prohibition prohibition = pap.query().prohibitions().getProhibition(name);

        DeleteProhibitionOp op = new DeleteProhibitionOp();
        ProhibitionOpArgs args = new ProhibitionOpArgs(name, prohibition.getSubject(), prohibition.getAccessRightSet(),
            prohibition.isIntersection(), new ArrayList<>(prohibition.getContainers()));

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }
}
