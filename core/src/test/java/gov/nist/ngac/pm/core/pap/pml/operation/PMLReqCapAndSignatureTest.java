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

package gov.nist.ngac.pm.core.pap.pml.operation;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import gov.nist.ngac.pm.core.pap.pml.operation.admin.PMLStmtsAdminOperation;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.RequireStatement;
import gov.nist.ngac.pm.core.pdp.UnauthorizedException;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

class PMLReqCapAndSignatureTest {

    private RequireStatement makeRequireStatement(List<String> accessRights, List<String> targets) {
        return new RequireStatement(
            ArrayLiteralExpression.of(
                accessRights.stream().map(s -> (gov.nist.ngac.pm.core.pap.pml.expression.Expression<String>) new StringLiteralExpression(s)).toList(),
                STRING_TYPE
            ),
            ArrayLiteralExpression.of(
                targets.stream().map(s -> (gov.nist.ngac.pm.core.pap.pml.expression.Expression<String>) new StringLiteralExpression(s)).toList(),
                STRING_TYPE
            )
        );
    }

    @Test
    void testPMLRequiredCapabilityFuncToFormattedString() {
        RequireStatement requireStmt = makeRequireStatement(
            List.of("read"), List.of("oa1")
        );
        PMLStatementBlock block = new PMLStatementBlock(requireStmt);
        PMLRequiredCapabilityFunc func = new PMLRequiredCapabilityFunc(block);

        String actual0 = func.toFormattedString(0);
        assertEquals("@ReqCap({\n    require [\"read\"] on [\"oa1\"]\n})", actual0);

        String actual1 = func.toFormattedString(1);
        assertEquals("@ReqCap({\n        require [\"read\"] on [\"oa1\"]\n    })", actual1);
    }

    @Test
    void testPMLRequiredCapabilityFuncIsSatisfied() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of("u1"), """
                set resource access rights ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" to "oa1" with ["read"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                """);

        RequireStatement stmt = makeRequireStatement(
            List.of("read"), List.of("oa1")
        );
        PMLStatementBlock block = new PMLStatementBlock(stmt);
        PMLRequiredCapabilityFunc func = new PMLRequiredCapabilityFunc(block);

        assertTrue(func.isSatisfied(pap, NodeUserContext.of("u1"), new Args()));
        assertFalse(func.isSatisfied(pap, NodeUserContext.of("u2"), new Args()));
    }

    @Test
    void testPMLOperationSignatureToFormattedStringWithReqCap() {
        RequireStatement stmt = makeRequireStatement(
            List.of("read"), List.of("oa1")
        );
        PMLStatementBlock block = new PMLStatementBlock(stmt);
        PMLRequiredCapabilityFunc func = new PMLRequiredCapabilityFunc(block);

        PMLOperationSignature sig = new PMLOperationSignature(
            OperationType.ADMINOP,
            "op1",
            VOID_TYPE,
            List.of(),
            List.of(func)
        );

        String actual = sig.toFormattedString(0);
        assertTrue(actual.startsWith("@ReqCap("));
        assertTrue(actual.contains("adminop op1()"));

        actual = sig.toFormattedString(1);
        assertTrue(actual.startsWith("    @ReqCap("));
        assertTrue(actual.contains("    adminop op1()"));
    }

    @Test
    void testPMLOperationSignatureToFormattedStringNoReqCap() {
        PMLOperationSignature sig = new PMLOperationSignature(
            OperationType.ADMINOP,
            "op1",
            VOID_TYPE,
            List.of(),
            List.of()
        );

        String actual = sig.toFormattedString(0);
        assertFalse(actual.contains("@ReqCap"));
        assertEquals("adminop op1() ", actual);
    }

    @Test
    void testReqCapListVisitorCompilesToPMLRequiredCapabilityFunc() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of("u1"), """
                set resource access rights ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" to "oa1" with ["read"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]

                @ReqCap({
                    require ["read"] on ["oa1"]
                })
                adminop test() {
                    create PC "pc2"
                }
                """);

        Operation<?> op = pap.query().operations().getOperation("test");

        assertDoesNotThrow(() -> op.canExecute(pap, NodeUserContext.of("u1"), new Args()));
        assertThrows(UnauthorizedException.class,
            () -> op.canExecute(pap, NodeUserContext.of("u2"), new Args()));
    }

    @Test
    void testReqCapWithMultipleRequireStatements() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of("u1"), """
                set resource access rights ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                associate "ua1" to "oa1" with ["read"]
                associate "ua1" to "oa2" with ["write"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]

                @ReqCap({
                    require ["read"] on ["oa1"]
                    require ["write"] on ["oa2"]
                })
                adminop test() {
                    create PC "pc2"
                }
                """);

        Operation<?> op = pap.query().operations().getOperation("test");

        assertDoesNotThrow(() -> op.canExecute(pap, NodeUserContext.of("u1"), new Args()));
        assertThrows(UnauthorizedException.class,
            () -> op.canExecute(pap, NodeUserContext.of("u2"), new Args()));
    }

    @Test
    void testAdminOpToFormattedStringWithReqCap() {
        RequireStatement requireStmt = makeRequireStatement(
            List.of("read"), List.of("oa1")
        );
        PMLStatementBlock reqCapBlock = new PMLStatementBlock(requireStmt);
        PMLRequiredCapabilityFunc func = new PMLRequiredCapabilityFunc(reqCapBlock);

        PMLStatementBlock body = new PMLStatementBlock(List.of());

        PMLStmtsAdminOperation<Void> op = new PMLStmtsAdminOperation<>(
            "myOp",
            VOID_TYPE,
            List.of(),
            List.of(func),
            body
        );

        String formatted = op.toFormattedString(0);
        assertTrue(formatted.contains("@ReqCap("));
        assertTrue(formatted.contains("adminop myOp()"));
        assertTrue(formatted.contains("require [\"read\"] on [\"oa1\"]"));
    }
}
