package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class RequireStatementTest {

    @Test
    void test() throws PMException {
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]

                create oa "oa1" in ["pc1"]

                associate "ua1" to "oa1" with ["admin:graph:assignment:ascendant:create", "admin:graph:assignment:descendant:create"]

                create o "o1" in ["oa1"]
                create o "o2" in ["oa1"]
                create o "o3" in ["oa1"]

                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                """);

        ExecutionContext ctx = new ExecutionContext(new TestUserContext("u1"), pap);

        testRequire(ctx, pap, new RequireStatement(
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("admin:graph:assignment:ascendant:create")), STRING_TYPE),
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("o1")), STRING_TYPE)
        ), false);

        testRequire(ctx, pap, new RequireStatement(
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("admin:graph:assignment:ascendant:create")), STRING_TYPE),
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("o1"), new StringLiteralExpression("o2")), STRING_TYPE)
        ), false);

        // check empty checks for any
        testRequire(ctx, pap, new RequireStatement(
            ArrayLiteralExpression.of(new ArrayList<>(), STRING_TYPE),
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("o1")), STRING_TYPE)
        ), false);

        ctx = new ExecutionContext(new UserContext(id("u2")), pap);
        testRequire(ctx, pap, new RequireStatement(
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("admin:graph:assignment:ascendant:create")), STRING_TYPE),
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("o1"), new StringLiteralExpression("o2")), STRING_TYPE)
        ), true);

        testRequire(ctx, pap, new RequireStatement(
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("admin:graph:assignment:ascendant:create")), STRING_TYPE),
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("o1")), STRING_TYPE)
        ), true);

        testRequire(ctx, pap, new RequireStatement(
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("admin:graph:assignment:ascendant:create")), STRING_TYPE),
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("o1"), new StringLiteralExpression("o2")), STRING_TYPE)
        ), true);

        testRequire(ctx, pap, new RequireStatement(
            ArrayLiteralExpression.of(new ArrayList<>(), STRING_TYPE),
            ArrayLiteralExpression.of(List.of(new StringLiteralExpression("o1"), new StringLiteralExpression("o2")), STRING_TYPE)
        ), true);
    }

    private void testRequire(ExecutionContext ctx, PAP pap, RequireStatement requireStatement, boolean err) {
        if (err) {
            assertThrows(UnauthorizedException.class, () -> requireStatement.execute(ctx, pap));
        } else {
            assertDoesNotThrow(() -> requireStatement.execute(ctx, pap));
        }
    }

    @Test
    void testOperationInRequire() throws PMException {
        String pml = """
                adminop testOp() string {
                    return PM_ADMIN_BASE_OA
                }

                @reqcap({
                    require ["admin:graph:assignment:ascendant:create"] on [testOp()]
                })
                adminop op1() {
                    create PC "pc2"
                }

                create pc "pc1"
                create ua "ua1" in ["pc1"]

                associate "ua1" to PM_ADMIN_BASE_OA with ["admin:graph:assignment:ascendant:create"]

                create u "u1" in ["ua1"]
                """;
        PAP pap = new TestPAP();
        PMLCompilationRuntimeException e = assertThrows(
            PMLCompilationRuntimeException.class,
            () -> pap.executePML(new TestUserContext("u1"), pml));
        assertEquals("unknown operation 'testOp' in scope", e.getMessage());
    }

}
