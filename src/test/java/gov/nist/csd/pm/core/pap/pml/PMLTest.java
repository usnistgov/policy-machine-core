package gov.nist.csd.pm.core.pap.pml;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.VOID_TYPE;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.Routine;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class PMLTest {

    private static final FormalParameter<String> ARGA = new FormalParameter<>("a", STRING_TYPE);
    private static final FormalParameter<List<String>> ARGB = new FormalParameter<>("b", ListType.of(STRING_TYPE));
    private static final FormalParameter<Map<String, String>> ARGC = new FormalParameter<>("c", MapType.of(STRING_TYPE, STRING_TYPE));

    @Test
    void testCallingNonPMLOperationAndRoutineFromPMLWithArgsAndReturnValue() throws PMException {
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua1"]
                associate "ua1" and PM_ADMIN_BASE_OA with ["assign"]
                
                create prohibition "pro1"
                deny U "u2"
                access rights ["assign"]
                on union of {PM_ADMIN_BASE_OA: false}
                """);

        AdminOperation<?> op1 = new AdminOperation<>("op1", VOID_TYPE, List.of(ARGA, ARGB, ARGC)) {
            @Override
            public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
                pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), "assign");
            }

            @Override
            public Void execute(PAP pap, Args actualArgs) throws PMException {
                String a = actualArgs.get(ARGA);
                List<String> b = actualArgs.get(ARGB);
                Map<String, String> c = actualArgs.get(ARGC);

                pap.modify().graph().createPolicyClass("1" + a);

                for (String b1 : b) {
                    pap.modify().graph().createPolicyClass("1" + b1);
                }

                for (Map.Entry<String, String> c1 : c.entrySet()) {
                    pap.modify().graph().createPolicyClass("1" + c1.getKey());
                    pap.modify().graph().createPolicyClass("1" + c1.getValue());
                }

                return null;
            }

        };
        pap.modify().operations().createAdminOperation(op1);

        pap.modify().operations().createAdminRoutine(new Routine<>("routine1", VOID_TYPE, List.of(ARGA, ARGB, ARGC)) {
            @Override
            public Void execute(PAP pap, Args args) throws PMException {
                pap.executeFunction(op1, args);

                return null;
            }

        });

        PDP pdp = new PDP(pap);
        pdp.executePML(new TestUserContext("u1"), """
                op1("a", ["b", "c"], {"d": "e", "f": "g"})
                """);
        assertTrue(pap.query().graph().nodeExists("1a"));
        assertTrue(pap.query().graph().nodeExists("1b"));
        assertTrue(pap.query().graph().nodeExists("1c"));
        assertTrue(pap.query().graph().nodeExists("1d"));
        assertTrue(pap.query().graph().nodeExists("1e"));
        assertTrue(pap.query().graph().nodeExists("1f"));
        assertTrue(pap.query().graph().nodeExists("1g"));

        assertThrows(UnauthorizedException.class, () -> pdp.executePML(new UserContext(id("u2")), """
                op1("a", ["b", "c"], {"d": "e", "f": "g"})
                """));

        pdp.executePML(new TestUserContext("u1"), """
                routine1("1", ["2", "3"], {"4": "5", "6": "7"})
                """);
        assertTrue(pap.query().graph().nodeExists("11"));
        assertTrue(pap.query().graph().nodeExists("12"));
        assertTrue(pap.query().graph().nodeExists("13"));
        assertTrue(pap.query().graph().nodeExists("14"));
        assertTrue(pap.query().graph().nodeExists("15"));
        assertTrue(pap.query().graph().nodeExists("16"));
        assertTrue(pap.query().graph().nodeExists("17"));

        assertThrows(UnauthorizedException.class, () -> pdp.executePML(new UserContext(id("u2")), """
                routine1("1", ["2", "3"], {"4": "5", "6": "7"})
                """));
    }

    @Test
    void testCallPMLOperationAndRoutineFromNonPML() throws PMException {
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua1"]
                associate "ua1" and PM_ADMIN_BASE_OA with ["assign"]
                
                create prohibition "pro1"
                deny U "u2"
                access rights ["assign"]
                on union of {PM_ADMIN_BASE_OA: false}
                
                adminop op1(string a, []string b, map[string]string c) {
                    check ["assign"] on [PM_ADMIN_BASE_OA]
                    create pc "1" + a
                
                    foreach x in b {
                        create pc "1" + x
                    }
                
                    foreach x, y in c {
                        create pc "1" + x
                        create pc "1" + y
                    }
                }
                
                routine routine1(string a, []string b, map[string]string c) {
                    op1(a, b, c)
                }
                """);

        PDP pdp = new PDP(pap);
        assertDoesNotThrow(() -> pdp.adjudicateAdminOperation(
            new TestUserContext("u1"),
            "op1",
            Map.of(
                ARGA.getName(), "a",
                ARGB.getName(), List.of("b", "c"),
                ARGC.getName(), Map.of("d", "e", "f", "g")
            )
        ));
        assertTrue(pap.query().graph().nodeExists("1a"));
        assertTrue(pap.query().graph().nodeExists("1b"));
        assertTrue(pap.query().graph().nodeExists("1c"));
        assertTrue(pap.query().graph().nodeExists("1d"));
        assertTrue(pap.query().graph().nodeExists("1e"));
        assertTrue(pap.query().graph().nodeExists("1f"));
        assertTrue(pap.query().graph().nodeExists("1g"));

        assertThrows(UnauthorizedException.class, () -> pdp.adjudicateAdminOperation(new UserContext(id("u2")),
            "op1",
            Map.of(
                ARGA.getName(), "a",
                ARGB.getName(), List.of("b", "c"),
                ARGC.getName(), Map.of("d", "e", "f", "g")
            )
        ));

        assertDoesNotThrow(() -> pdp.adjudicateAdminOperation(new TestUserContext("u1"),
            "op1",
            Map.of(
                ARGA.getName(), "1",
                ARGB.getName(), List.of("2", "3"),
                ARGC.getName(), Map.of("4", "5", "6", "7")
            )
        ));
        assertTrue(pap.query().graph().nodeExists("11"));
        assertTrue(pap.query().graph().nodeExists("12"));
        assertTrue(pap.query().graph().nodeExists("13"));
        assertTrue(pap.query().graph().nodeExists("14"));
        assertTrue(pap.query().graph().nodeExists("15"));
        assertTrue(pap.query().graph().nodeExists("16"));
        assertTrue(pap.query().graph().nodeExists("17"));

        assertThrows(UnauthorizedException.class, () -> pdp.adjudicateAdminOperation(new UserContext(id("u2")), "op1",
            Map.of(
                ARGA.getName(), "1",
                ARGB.getName(), List.of("2", "3"),
                ARGC.getName(), Map.of("4", "5", "6", "7")
            )
        ));
    }
}
