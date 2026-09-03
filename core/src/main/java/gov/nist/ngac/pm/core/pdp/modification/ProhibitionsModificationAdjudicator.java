/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pdp.modification;

import static gov.nist.ngac.pm.core.pap.operation.Operation.NAME_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.prohibition.CreateProcessProhibitionOp.PROCESS_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.prohibition.CreateProcessProhibitionOp.USER_ID_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.prohibition.ProhibitionOp.ARSET_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.prohibition.ProhibitionOp.EXCLUSION_SET_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.prohibition.ProhibitionOp.INCLUSION_SET_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.prohibition.ProhibitionOp.IS_CONJUNCTIVE_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.prohibition.ProhibitionOp.NODE_ID_PARAM;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.prohibition.NodeProhibition;
import gov.nist.ngac.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.modification.ProhibitionsModification;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.prohibition.CreateNodeProhibitionOp;
import gov.nist.ngac.pm.core.pap.operation.prohibition.CreateProcessProhibitionOp;
import gov.nist.ngac.pm.core.pap.operation.prohibition.DeleteProhibitionOp;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pdp.adjudication.Adjudicator;
import java.util.ArrayList;
import java.util.Set;

/**
 * A {@link ProhibitionsModification} that checks the acting user's admin privileges before delegating to
 * the PAP.
 */
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
        op.execute(pap, userCtx, args);
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
        op.execute(pap, userCtx, args);
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
        op.execute(pap, userCtx, args);
    }
}
