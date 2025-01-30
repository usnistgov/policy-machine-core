package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.literal.ArrayLiteral;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.UnauthorizedException;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.util.TestMemoryPAP.id;
import static org.junit.jupiter.api.Assertions.*;

class CheckStatementTest {

    @Test
    void test() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new TestUserContext("u1", pap), """
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

        ExecutionContext ctx = new ExecutionContext(new TestUserContext("u1", pap), pap);

        testCheck(ctx, pap, new CheckStatement(
                new StringLiteral("assign"),
                new StringLiteral("o1")
        ), false);

        testCheck(ctx, pap, new CheckStatement(
                new ArrayLiteral(List.of(new StringLiteral("assign"), new StringLiteral("assign_to")), Type.string()),
                new StringLiteral("o1")
        ), false);

        testCheck(ctx, pap, new CheckStatement(
                new ArrayLiteral(List.of(new StringLiteral("assign"), new StringLiteral("assign_to")), Type.string()),
                new ArrayLiteral(List.of(new StringLiteral("o1"), new StringLiteral("o2")), Type.string())
        ), false);

        testCheck(ctx, pap, new CheckStatement(
                new StringLiteral("assign"),
                new ArrayLiteral(List.of(new StringLiteral("o1"), new StringLiteral("o2")), Type.string())
        ), false);

        ctx = new ExecutionContext(new UserContext(id(pap, "u2")), pap);
        testCheck(ctx, pap, new CheckStatement(
                new StringLiteral("assign"),
                new StringLiteral("o1")
        ), true);

        testCheck(ctx, pap, new CheckStatement(
                new ArrayLiteral(List.of(new StringLiteral("assign"), new StringLiteral("assign_to")), Type.string()),
                new StringLiteral("o1")
        ), true);

        testCheck(ctx, pap, new CheckStatement(
                new ArrayLiteral(List.of(new StringLiteral("assign"), new StringLiteral("assign_to")), Type.string()),
                new ArrayLiteral(List.of(new StringLiteral("o1"), new StringLiteral("o2")), Type.string())
        ), true);

        testCheck(ctx, pap, new CheckStatement(
                new StringLiteral("assign"),
                new ArrayLiteral(List.of(new StringLiteral("o1"), new StringLiteral("o2")), Type.string())
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
                operation testOp() string {
                    return PM_ADMIN_OBJECT
                }
                
                operation op1() {
                    check "assign" on testOp()
                } {
                    create policy class "pc2"
                }
                
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                               
                associate "ua1" and PM_ADMIN_OBJECT with ["assign"]
                
                create u "u1" in ["ua1"]                
                """;
        PAP pap = new MemoryPAP();
        pap.executePML(new TestUserContext("u1", pap), pml);

        PDP pdp = new PDP(pap);
        pdp.adjudicateAdminOperation(new TestUserContext("u1", pap), "op1", Map.of());

        assertTrue(pap.query().graph().nodeExists("pc2"));
    }

}