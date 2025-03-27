package gov.nist.csd.pm.pap.pml;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.Operation;
import gov.nist.csd.pm.pap.executable.routine.Routine;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.pml.executable.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.MapValue;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.UnauthorizedException;
import gov.nist.csd.pm.pdp.adjudication.AdjudicationResponse;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pdp.adjudication.Decision.DENY;
import static gov.nist.csd.pm.pdp.adjudication.Decision.GRANT;
import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.*;

public class PMLTest {

    private static final PMLFormalArg ARGA = new PMLFormalArg("a", Type.string());
    private static final PMLFormalArg ARGB = new PMLFormalArg("b", Type.array(Type.string()));
    private static final PMLFormalArg ARGC = new PMLFormalArg("c", Type.map(Type.string(), Type.string()));

    @Test
    void testCallingNonPMLOperationAndRoutineFromPMLWithOperandsAndReturnValue() throws PMException {
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua1"]
                associate "ua1" and PM_ADMIN_OBJECT with ["assign"]
                
                create prohibition "pro1"
                deny user "u2"
                access rights ["assign"]
                on union of [PM_ADMIN_OBJECT]
                """);

        Operation<?> op1 = new Operation<>("op1", List.of(ARGA, ARGB, ARGC)) {
            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs operands) throws PMException {
                privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), "assign");
            }

            @Override
            public Object execute(PAP pap, ActualArgs actualArgs) throws PMException {
                String a = actualArgs.get(ARGA).getStringValue();
                List<Value> b = actualArgs.get(ARGB).getArrayValue();
                Map<Value, Value> c = actualArgs.get(ARGC).getMapValue();

                pap.modify().graph().createPolicyClass("1" + a);

                for (Value b1 : b) {
                    pap.modify().graph().createPolicyClass("1" + b1.getStringValue());
                }

                for (Map.Entry<Value, Value> c1 : c.entrySet()) {
                    pap.modify().graph().createPolicyClass("1" + c1.getKey().getStringValue());
                    pap.modify().graph().createPolicyClass("1" + c1.getValue().getStringValue());
                }

                return null;
            }
        };
        pap.modify().operations().createAdminOperation(op1);

        pap.modify().routines().createAdminRoutine(new Routine<>("routine1", List.of(ARGA, ARGB, ARGC)) {
            @Override
            public Object execute(PAP pap, ActualArgs actualArgs) throws PMException {
                pap.executeAdminExecutable(op1, actualArgs);

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
                associate "ua1" and PM_ADMIN_OBJECT with ["assign"]
                
                create prohibition "pro1"
                deny user "u2"
                access rights ["assign"]
                on union of [PM_ADMIN_OBJECT]
                
                operation op1(string a, []string b, map[string]string c) {
                    check "assign" on PM_ADMIN_OBJECT
                } {
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
        AdjudicationResponse<?> response = pdp.adjudicateAdminOperation(
            new TestUserContext("u1"),
                pap.query().operations().getAdminOperation("op1"),
                new ActualArgs()
                    .put(ARGA, new StringValue("a"))
                    .put(ARGB, new ArrayValue(Type.string(), new StringValue("b"), new StringValue("c")))
                    .put(ARGC, new MapValue(Map.of(new StringValue("d"), new StringValue("e"),
                        new StringValue("f"), new StringValue("g")), Type.string(), Type.string()))
        );
        assertEquals(GRANT, response.getDecision());
        assertTrue(pap.query().graph().nodeExists("1a"));
        assertTrue(pap.query().graph().nodeExists("1b"));
        assertTrue(pap.query().graph().nodeExists("1c"));
        assertTrue(pap.query().graph().nodeExists("1d"));
        assertTrue(pap.query().graph().nodeExists("1e"));
        assertTrue(pap.query().graph().nodeExists("1f"));
        assertTrue(pap.query().graph().nodeExists("1g"));


        response = pdp.adjudicateAdminOperation(new UserContext(id("u2")),
            pap.query().operations().getAdminOperation("op1"),
            new ActualArgs()
                .put(ARGA, new StringValue("a"))
                .put(ARGB, new ArrayValue(Type.string(), new StringValue("b"), new StringValue("c")))
                .put(ARGC, new MapValue(Map.of(new StringValue("d"), new StringValue("e"),
                    new StringValue("f"), new StringValue("g")), Type.string(), Type.string()))
        );
        assertEquals(DENY, response.getDecision());

        response = pdp.adjudicateAdminOperation(new TestUserContext("u1"),
            pap.query().operations().getAdminOperation("op1"),
            new ActualArgs()
                .put(ARGA, new StringValue("1"))
                .put(ARGB, new ArrayValue(Type.string(), new StringValue("2"), new StringValue("3")))
                .put(ARGC, new MapValue(Map.of(new StringValue("4"), new StringValue("5"),
                    new StringValue("6"), new StringValue("7")), Type.string(), Type.string()))
        );
        assertEquals(GRANT, response.getDecision());
        assertTrue(pap.query().graph().nodeExists("11"));
        assertTrue(pap.query().graph().nodeExists("12"));
        assertTrue(pap.query().graph().nodeExists("13"));
        assertTrue(pap.query().graph().nodeExists("14"));
        assertTrue(pap.query().graph().nodeExists("15"));
        assertTrue(pap.query().graph().nodeExists("16"));
        assertTrue(pap.query().graph().nodeExists("17"));

        response = pdp.adjudicateAdminOperation(new UserContext(id("u2")), pap.query().operations().getAdminOperation("op1"),
            new ActualArgs()
                .put(ARGA, new StringValue("1"))
                .put(ARGB, new ArrayValue(Type.string(), new StringValue("2"), new StringValue("3")))
                .put(ARGC, new MapValue(Map.of(new StringValue("4"), new StringValue("5"),
                    new StringValue("6"), new StringValue("7")), Type.string(), Type.string()))
        );
        assertEquals(DENY, response.getDecision());
    }


}
