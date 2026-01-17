package gov.nist.csd.pm.core.pdp.modification;

import static gov.nist.csd.pm.core.pap.function.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.prohibition.ProhibitionOp.ARSET_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.prohibition.ProhibitionOp.CONTAINERS_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.prohibition.ProhibitionOp.INTERSECTION_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.prohibition.ProhibitionOp.SUBJECT_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.prohibition.CreateProhibitionOp;
import gov.nist.csd.pm.core.pap.function.op.prohibition.DeleteProhibitionOp;
import gov.nist.csd.pm.core.pap.modification.ProhibitionsModification;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.adjudication.Adjudicator;
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
        Args args = new Args()
            .put(NAME_PARAM, name)
            .put(SUBJECT_PARAM, subject)
            .put(ARSET_PARAM, new ArrayList<>(accessRightSet))
            .put(INTERSECTION_PARAM, intersection)
            .put(CONTAINERS_PARAM, new ArrayList<>(containerConditions));

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void deleteProhibition(String name) throws PMException {
        Prohibition prohibition = pap.query().prohibitions().getProhibition(name);

        DeleteProhibitionOp op = new DeleteProhibitionOp();

        Args args = new Args()
            .put(NAME_PARAM, name)
            .put(SUBJECT_PARAM, prohibition.getSubject())
            .put(ARSET_PARAM, new ArrayList<>(prohibition.getAccessRightSet()))
            .put(INTERSECTION_PARAM, prohibition.isIntersection())
            .put(CONTAINERS_PARAM, new ArrayList<>(prohibition.getContainers()));

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }
}
