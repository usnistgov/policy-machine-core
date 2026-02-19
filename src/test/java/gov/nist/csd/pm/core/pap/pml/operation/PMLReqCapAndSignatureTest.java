package gov.nist.csd.pm.core.pap.pml.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature.OperationType;
import gov.nist.csd.pm.core.pap.pml.operation.admin.PMLStmtsAdminOperation;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.operation.RequireStatement;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PMLReqCapAndSignatureTest {

    private RequireStatement makeRequireStatement(List<String> accessRights, List<String> targets) {
        return new RequireStatement(
            ArrayLiteralExpression.of(
                accessRights.stream().map(s -> (gov.nist.csd.pm.core.pap.pml.expression.Expression<String>) new StringLiteralExpression(s)).toList(),
                STRING_TYPE
            ),
            ArrayLiteralExpression.of(
                targets.stream().map(s -> (gov.nist.csd.pm.core.pap.pml.expression.Expression<String>) new StringLiteralExpression(s)).toList(),
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
        assertEquals("@reqcap({\n    require [\"read\"] on [\"oa1\"]\n})", actual0);

        String actual1 = func.toFormattedString(1);
        assertEquals("@reqcap({\n        require [\"read\"] on [\"oa1\"]\n    })", actual1);
    }

    @Test
    void testPMLRequiredCapabilityFuncIsSatisfied() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                set resource access rights ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["read"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                """);

        RequireStatement stmt = makeRequireStatement(
            List.of("read"), List.of("oa1")
        );
        PMLStatementBlock block = new PMLStatementBlock(stmt);
        PMLRequiredCapabilityFunc func = new PMLRequiredCapabilityFunc(block);

        assertTrue(func.isSatisfied(pap, new TestUserContext("u1"), new Args()));
        assertFalse(func.isSatisfied(pap, new TestUserContext("u2"), new Args()));
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
        assertTrue(actual.startsWith("@reqcap("));
        assertTrue(actual.contains("adminop op1()"));

        actual = sig.toFormattedString(1);
        assertTrue(actual.startsWith("    @reqcap("));
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
        assertFalse(actual.contains("@reqcap"));
        assertEquals("adminop op1() ", actual);
    }

    @Test
    void testReqCapListVisitorCompilesToPMLRequiredCapabilityFunc() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                set resource access rights ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["read"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]

                @reqcap({
                    require ["read"] on ["oa1"]
                })
                adminop test() {
                    create PC "pc2"
                }
                """);

        Operation<?> op = pap.query().operations().getOperation("test");

        assertDoesNotThrow(() -> op.canExecute(pap, new TestUserContext("u1"), new Args()));
        assertThrows(UnauthorizedException.class,
            () -> op.canExecute(pap, new TestUserContext("u2"), new Args()));
    }

    @Test
    void testReqCapWithMultipleRequireStatements() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                set resource access rights ["read", "write"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                associate "ua1" and "oa1" with ["read"]
                associate "ua1" and "oa2" with ["write"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]

                @reqcap({
                    require ["read"] on ["oa1"]
                    require ["write"] on ["oa2"]
                })
                adminop test() {
                    create PC "pc2"
                }
                """);

        Operation<?> op = pap.query().operations().getOperation("test");

        assertDoesNotThrow(() -> op.canExecute(pap, new TestUserContext("u1"), new Args()));
        assertThrows(UnauthorizedException.class,
            () -> op.canExecute(pap, new TestUserContext("u2"), new Args()));
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
        assertTrue(formatted.contains("@reqcap("));
        assertTrue(formatted.contains("adminop myOp()"));
        assertTrue(formatted.contains("require [\"read\"] on [\"oa1\"]"));
    }
}
