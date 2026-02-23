package gov.nist.csd.pm.core.pap.pml;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.epp.EventContext;
import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.core.pap.pml.operation.resource.PMLResourceOperation;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.pdp.bootstrap.PMLBootstrapperWithSuper;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class OperationTest {

    @Test
    void testElseIfNotAllPathsReturn() {
        String pml = """
                adminop fun(string a) string {
                    if a == "a" {
                        return "a"
                    } else if a == "b" {
                        return "b"
                    }
                }
                """;

        PMLCompilationException e = assertThrows(PMLCompilationException.class, () -> {
            PAP pap = new TestPAP();
            pap.executePML(new TestUserContext("u1"), pml);
        });
        assertEquals("not all conditional paths return", e.getErrors().get(0).errorMessage());
    }

    @Test
    void testElseAllPathsReturn() {
        String pml2 = """
                adminop fun(string a) string {
                    if a == "a" {
                        return "a"
                    } else if a == "b" {
                        return "b"
                    } else {
                        return "c"
                    }
                }
                """;

        assertDoesNotThrow(() -> {
            PAP pap = new TestPAP();
            pap.executePML(new TestUserContext("u1"), pml2);
        });
    }

    @Test
    void testElseWithNoElseIfAllPathsReturn() {
        String pml2 = """
                adminop fun(string a) string {
                    if a == "a" {
                        return "a"
                    } else {
                        return "b"
                    }
                }
                """;

        assertDoesNotThrow(() -> {
            PAP pap = new TestPAP();
            pap.executePML(new TestUserContext("u1"), pml2);
        });
    }

    @Test
    void testResourceOperationCheckBlock() throws PMException {
        String pml = """
            set resource access rights ["read"]
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create u "u1" in ["ua1"]
            
            @reqcap({
                require ["read"] on [PM_ADMIN_BASE_OA]
            })
            resourceop res1() {
            }
            """;

        MemoryPAP memoryPAP = new TestPAP();
        memoryPAP.executePML(new UserContext(-1), pml);

        PMLResourceOperation<?> res1 = (PMLResourceOperation<?>) memoryPAP.query().operations().getOperation("res1");
        ExecutionContext u1 = memoryPAP.buildExecutionContext(new UserContext(id("u1")));
        res1.setCtx(u1);
        PDP pdp = new PDP(memoryPAP);
        assertThrows(UnauthorizedException.class, () -> pdp.runTx(new UserContext(id("u1")), pdpTx -> {
            pdpTx.executeOperation(res1, new Args());
            return null;
        }));
    }

    @Test
    void test_whenArgAddedToEventCtx_argIsAvailableInObligationResponse() throws PMException {
        String pml = """
            @eventctx(a, b, string c)
            resourceop op1(string a, string b)
           
            create obligation "o1"
            when any user
            performs "op1" on (a, b, c) {
                return c == "test"
            }
            do(ctx) {
                create pc "pc1"
            }
            """;
        MemoryPAP pap = new TestPAP();
        pap.bootstrap(new PMLBootstrapperWithSuper(pml));
        PDP pdp = new PDP(pap);
        pdp.adjudicateOperation(new UserContext(id("super")), "op1", Map.of("a", "a", "b", "b"));
        EPP epp = new EPP(pdp, pap);
        epp.processEvent(new EventContext(
            new EventContextUser("super"),
            "op1",
            Map.of("a", "a", "b", "b", "c", "test")
        ));
        assertTrue(pap.query().graph().nodeExists("pc1"));
    }

}
