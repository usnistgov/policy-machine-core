package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.SamplePolicy;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.mysql.MysqlPolicyStore;
import gov.nist.csd.pm.pap.mysql.MysqlTestEnv;
import gov.nist.csd.pm.policy.PolicyEquals;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static gov.nist.csd.pm.pap.SuperUserBootstrapper.SUPER_USER;
import static org.junit.jupiter.api.Assertions.*;

public abstract class PAPTest {

    PAP pap;

    public abstract PAP getPAP() throws PMException;

    @BeforeEach
    void setup() throws PMException {
        pap = getPAP();
    }

    @Test
    void testTx() throws PMException {
            pap.beginTx();
            pap.graph().createPolicyClass("pc1");
            pap.graph().createObjectAttribute("oa1", "pc1");
            pap.graph().createUserAttribute("ua1", "pc1");
            pap.graph().associate("ua1", "oa1", new AccessRightSet());
            pap.commit();

            assertTrue(pap.graph().nodeExists("pc1"));
            assertTrue(pap.graph().nodeExists("oa1"));
            assertTrue(pap.graph().nodeExists("ua1"));
            assertTrue(pap.graph().getAssociationsWithSource("ua1").contains(new Association("ua1", "oa1")));

            pap.beginTx();
            pap.graph().deleteNode("ua1");
            pap.rollback();
            assertTrue(pap.graph().nodeExists("ua1"));
    }

    private static final String input = """
            set resource access rights ['read', 'write', 'execute']
            create policy class 'pc1'
            set properties of 'pc1' to {'k':'v'}
            create oa 'oa1' in ['pc1']
            set properties of 'oa1' to {'k1':'v1'}
            create ua 'ua1' in ['pc1']
            associate 'ua1' and 'oa1' with ['read', 'write']
            create prohibition 'p1' deny user attribute 'ua1' access rights ['read'] on union of [!'oa1']
            create obligation 'obl1' {
                create rule 'rule1'
                when any user
                performs ['event1', 'event2']
                do(evtCtx) {
                    let event = evtCtx['event']
                    if equals(event, 'event1') {
                        create policy class 'e1'
                    } else if equals(event, 'event2') {
                        create policy class 'e2'
                    }
                }
            }
            const testConst = "hello world"
            function testFunc() void {
                create pc "pc1"
            }
            """;
    private static final String expected = """
            const testConst = 'hello world'
            function testFunc() void {create policy class 'pc1'}
            set resource access rights ['read', 'write', 'execute']
            create policy class 'super_policy'
            create user attribute 'super_ua' in ['super_policy']
            create user attribute 'super_ua1' in ['super_policy']
            associate 'super_ua1' and 'super_ua' with ['*']
            create object attribute 'super_oa' in ['super_policy']
            associate 'super_ua' and 'super_oa' with ['*']
            create user 'super' in ['super_ua']
            assign 'super' to ['super_ua1']
            create object attribute 'super_policy_pc_rep' in ['super_oa']
            create object attribute 'pc1_pc_rep' in ['super_oa']
            create policy class 'pc1'
            set properties of 'pc1' to {'k': 'v'}
            create object attribute 'oa1' in ['pc1']
            set properties of 'oa1' to {'k1': 'v1'}
            associate 'super_ua' and 'oa1' with ['*']
            create user attribute 'ua1' in ['pc1']
            associate 'super_ua' and 'ua1' with ['*']
            associate 'ua1' and 'oa1' with ['read', 'write']
            create prohibition 'p1' deny user attribute 'ua1' access rights ['read'] on union of [!'oa1']
            create obligation 'obl1' {create rule 'rule1' when any user performs ['event1', 'event2'] on any policy element do (evtCtx) {let event = evtCtx['event']if equals(event, 'event1') {create policy class 'e1'} else if equals(event, 'event2') {create policy class 'e2'} }}
            """.trim();
    @Test
    void testSerialize() throws PMException {
            pap.deserialize().fromPML(new UserContext(SUPER_USER), input);
            String actual = pap.serialize().toPML();
            assertEquals(new ArrayList<>(), pmlEqual(expected, actual));

            pap.deserialize().fromPML(new UserContext(SUPER_USER), actual);
            actual = pap.serialize().toPML();
            assertEquals(new ArrayList<>(), pmlEqual(expected, actual));

            String json = pap.serialize().toJSON();
            MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
            memoryPolicyStore.deserialize().fromJSON(json);
            pap.deserialize().fromJSON(json);
            PolicyEquals.check(pap, memoryPolicyStore);
    }

    private List<String> pmlEqual(String expected, String actual) {
        List<String> expectedLines = sortLines(expected);
        List<String> actualLines = sortLines(actual);
        expectedLines.removeIf(line -> actualLines.contains(line));
        return expectedLines;
    }

    private List<String> sortLines(String pml) {
        List<String> lines = new ArrayList<>();
        Scanner sc = new Scanner(pml);
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }

        Collections.sort(lines);
        return lines;
    }

    @Test
    void testExecutePML() throws PMException {
            try {
                SamplePolicy.loadSamplePolicyFromPML(pap);

                FunctionDefinitionStatement functionDefinitionStatement = new FunctionDefinitionStatement(
                        "testfunc",
                        Type.voidType(),
                        List.of(),
                        (ctx, policy) -> {
                            policy.graph().createPolicyClass("pc3");
                            return new Value();
                        }
                );

                pap.executePML(new UserContext(SUPER_USER), "create ua 'ua3' in ['pc2']\ntestfunc()", functionDefinitionStatement);
                assertTrue(pap.graph().nodeExists("ua3"));
                assertTrue(pap.graph().nodeExists("pc3"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

}