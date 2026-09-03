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

package gov.nist.ngac.pm.core.pap.pml.statement.operation;


import static gov.nist.ngac.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.ngac.pm.core.util.TestIdGenerator.id;
import static gov.nist.ngac.pm.core.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.util.TestPAP;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

class AssignStatementTest {

    @Test
    void testSuccess() throws PMException {
        AssignStatement stmt = new AssignStatement(new StringLiteralExpression("u1"), buildArrayLiteral("ua2", "ua3"));

        PAP pap = new TestPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids(("pc1")));
        pap.modify().graph().createUserAttribute("ua2", ids(("pc1")));
        pap.modify().graph().createUserAttribute("ua3", ids(("pc1")));
        pap.modify().graph().createUser("u1", ids(("ua1")));
        ExecutionContext execCtx = new ExecutionContext(NodeUserContext.of("u1"), pap);
        stmt.execute(execCtx, pap);

        assertTrue(pap.query().graph().getAdjacentDescendants(id("u1"))
                .containsAll(ids("ua1", "ua2", "ua3")));
    }

    @Test
    void testToFormattedString() {
        AssignStatement stmt = new AssignStatement(new StringLiteralExpression("u1"), buildArrayLiteral("ua2", "ua3"));
        assertEquals(
                "assign \"u1\" to [\"ua2\", \"ua3\"]",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "    assign \"u1\" to [\"ua2\", \"ua3\"]",
                stmt.toFormattedString(1)
        );
    }

}