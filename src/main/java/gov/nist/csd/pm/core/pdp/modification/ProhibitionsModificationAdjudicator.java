package gov.nist.csd.pm.core.pdp.modification;

import static gov.nist.csd.pm.core.pap.operation.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.CreateProcessProhibitionOp.PROCESS_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.CreateProcessProhibitionOp.USER_ID_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp.ARSET_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp.EXCLUSION_SET_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp.INCLUSION_SET_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp.IS_CONJUNCTIVE_PARAM;
import static gov.nist.csd.pm.core.pap.operation.prohibition.ProhibitionOp.NODE_ID_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.prohibition.NodeProhibition;
import gov.nist.csd.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.modification.ProhibitionsModification;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.prohibition.CreateNodeProhibitionOp;
import gov.nist.csd.pm.core.pap.operation.prohibition.CreateProcessProhibitionOp;
import gov.nist.csd.pm.core.pap.operation.prohibition.DeleteProhibitionOp;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.adjudication.Adjudicator;
import java.util.ArrayList;
import java.util.Set;

public class ProhibitionsModificationAdjudicator extends Adjudicator implements ProhibitionsModification {

    public ProhibitionsModificationAdjudicator(UserContext userCtx, PAP pap) {
        super(pap, userCtx);
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public void createNodeProhibition(String name,
                                      long nodeId,
                                      AccessRightSet accessRightSet,
                                      Set<Long> inclusionSet,
                                      Set<Long> exclusionSet,
                                      boolean isConjunctive) throws PMException {
        CreateNodeProhibitionOp op = new CreateNodeProhibitionOp();
        Args args = new Args()
            .put(NAME_PARAM, name)
            .put(NODE_ID_PARAM, nodeId)
            .put(ARSET_PARAM, new ArrayList<>(accessRightSet))
            .put(INCLUSION_SET_PARAM, new ArrayList<>(inclusionSet))
            .put(EXCLUSION_SET_PARAM, new ArrayList<>(exclusionSet))
            .put(IS_CONJUNCTIVE_PARAM, isConjunctive);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void createProcessProhibition(String name,
                                         long userId,
                                         String process,
                                         AccessRightSet accessRightSet,
                                         Set<Long> inclusionSet,
                                         Set<Long> exclusionSet,
                                         boolean isConjunctive) throws PMException {
        CreateProcessProhibitionOp op = new CreateProcessProhibitionOp();
        Args args = new Args()
            .put(NAME_PARAM, name)
            .put(USER_ID_PARAM, userId)
            .put(PROCESS_PARAM, process)
            .put(ARSET_PARAM, new ArrayList<>(accessRightSet))
            .put(INCLUSION_SET_PARAM, new ArrayList<>(inclusionSet))
            .put(EXCLUSION_SET_PARAM, new ArrayList<>(exclusionSet))
            .put(IS_CONJUNCTIVE_PARAM, isConjunctive);

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }

    @Override
    public void deleteProhibition(String name) throws PMException {
        Prohibition prohibition = pap.query().prohibitions().getProhibition(name);

        DeleteProhibitionOp op = new DeleteProhibitionOp();

        long nodeId = switch (prohibition) {
            case NodeProhibition nodeProhibition -> nodeProhibition.getNodeId();
            case ProcessProhibition processProhibition -> processProhibition.getUserId();
        };

        Args args = new Args()
            .put(NAME_PARAM, name)
            .put(NODE_ID_PARAM, nodeId)
            .put(INCLUSION_SET_PARAM, new ArrayList<>(prohibition.getInclusionSet()))
            .put(EXCLUSION_SET_PARAM, new ArrayList<>(prohibition.getExclusionSet()));

        op.canExecute(pap, userCtx, args);
        op.execute(pap, args);
    }
}
