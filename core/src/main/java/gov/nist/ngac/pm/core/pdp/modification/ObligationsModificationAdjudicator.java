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
import static gov.nist.ngac.pm.core.pap.operation.obligation.CreateObligationOp.AUTHOR_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.obligation.CreateObligationOp.EVENT_PATTERN_PARAM;
import static gov.nist.ngac.pm.core.pap.operation.obligation.CreateObligationOp.OBLIGATION_RESPONSE_PARAM;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.modification.ObligationsModification;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.obligation.CreateObligationOp;
import gov.nist.ngac.pm.core.pap.operation.obligation.DeleteObligationOp;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pdp.adjudication.Adjudicator;

/**
 * A {@link ObligationsModification} that checks the acting user's admin privileges before delegating to
 * the PAP.
 */
public class ObligationsModificationAdjudicator extends Adjudicator implements ObligationsModification {

    public ObligationsModificationAdjudicator(UserContext userCtx, PAP pap) {
        super(pap, userCtx);
        this.userCtx = userCtx;
        this.pap = pap;
    }

    @Override
    public void createObligation(Obligation obligation) throws PMException {
        long authorId = obligation.getAuthor().resolveNodeIds(pap.query().graph()).iterator().next();
        CreateObligationOp op = new CreateObligationOp();
        Args args = new Args()
            .put(AUTHOR_PARAM, authorId)
            .put(NAME_PARAM, obligation.getName())
            .put(EVENT_PATTERN_PARAM, obligation.getEventPattern())
            .put(OBLIGATION_RESPONSE_PARAM, obligation.getResponse());

        op.canExecute(pap, userCtx, args);
        op.execute(pap, userCtx, args);
    }

    @Override
    public void deleteObligation(String name) throws PMException {
        Obligation obligation = pap.query().obligations().getObligation(name);

        DeleteObligationOp op = new DeleteObligationOp();
        Args args = new Args()
            .put(NAME_PARAM, obligation.getName())
            .put(EVENT_PATTERN_PARAM, obligation.getEventPattern());

        op.canExecute(pap, userCtx, args);
        op.execute(pap, userCtx, args);
    }
}
