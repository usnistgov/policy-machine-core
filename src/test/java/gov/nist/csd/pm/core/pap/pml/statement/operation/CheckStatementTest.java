package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CheckStatementTest {

    @Test
    void test() throws PMException {
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
               
                create oa "oa1" in ["pc1"]
                
                associate "ua1" and "oa1" with ["assign", "assign_to"]
                
                create o "o1" in ["oa1"]
                create o "o2" in ["oa1"]
                create o "o3" in ["oa1"]
                
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                """);

        ExecutionContext ctx = new ExecutionContext(new TestUserContext("u1"), pap);

        testCheck(ctx, pap, new CheckStatement(
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("assign")), STRING_TYPE),
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("o1")), STRING_TYPE)
        ), false);

        testCheck(ctx, pap, new CheckStatement(
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("assign")), STRING_TYPE),
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("o1"), new StringLiteralExpression("o2")), STRING_TYPE)
        ), false);

        // check empty checks for any
        testCheck(ctx, pap, new CheckStatement(
            ArrayLiteralExpression.of(new ArrayList<>(), STRING_TYPE),
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("o1")), STRING_TYPE)
        ), false);

        ctx = new ExecutionContext(new UserContext(id("u2")), pap);
        testCheck(ctx, pap, new CheckStatement(
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("assign")), STRING_TYPE),
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("o1"), new StringLiteralExpression("o2")), STRING_TYPE)
        ), true);

        testCheck(ctx, pap, new CheckStatement(
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("assign")), STRING_TYPE),
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("o1")), STRING_TYPE)
        ), true);

        testCheck(ctx, pap, new CheckStatement(
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("assign")), STRING_TYPE),
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("o1"), new StringLiteralExpression("o2")), STRING_TYPE)
        ), true);

        testCheck(ctx, pap, new CheckStatement(
            ArrayLiteralExpression.of(new ArrayList<>(), STRING_TYPE),
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("o1"), new StringLiteralExpression("o2")), STRING_TYPE)
        ), true);
    }

    private void testCheck(ExecutionContext ctx, PAP pap, CheckStatement checkStatement, boolean err) {
        if (err) {
            assertThrows(UnauthorizedException.class, () -> checkStatement.execute(ctx, pap));
        } else {
            assertDoesNotThrow(() -> checkStatement.execute(ctx, pap));
        }
    }

    @Test
    void testOperationInCheck() throws PMException {
        String pml = """
                adminop testOp() string {
                    return PM_ADMIN_BASE_OA
                }
                
                adminop op1() {
                    check ["assign"] on [testOp()]
                    create PC "pc2"
                }
                
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                               
                associate "ua1" and PM_ADMIN_BASE_OA with ["assign"]
                
                create u "u1" in ["ua1"]                
                """;
        PAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), pml);

        PDP pdp = new PDP(pap);
        pdp.adjudicateAdminOperation(new TestUserContext("u1"), "op1", Map.of());

        assertTrue(pap.query().graph().nodeExists("pc2"));
    }

}