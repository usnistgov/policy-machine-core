package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.author.pal.model.exception.PALCompilationException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.serializer.PALDeserializer;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PALTest {

    @Test
    void testBuiltinFunction() throws PMException, PALCompilationException {
        String input = """
                let x = concat(['hello', 'world']);
                create policy class x;
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.fromString(input, new PALDeserializer(new UserContext(SUPER_USER)));
        assertTrue(pap.nodeExists("helloworld"));
    }

    @Test
    void testExpression() throws PMException, PALCompilationException {
        String input = """
                let a1 = 'test';
                let a2 = ['1', '2', a1];
                let a3 = {'1': '2', '3': a1};
                
                function testFunc(string x) string {
                    return concat([x, '_test']);
                }
                
                let a4 = testFunc('test');
                let a5 = testFunc(a4);
                let a6 = a5;
                let a7 = '1';
                let a8 = a3[a7];
                let a9 = a3['1'];
                
                create policy class a4;
                create policy class a5;
                create policy class concat(['test', a6]);
                create policy class a8;
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.fromString(input, new PALDeserializer(new UserContext(SUPER_USER)));
        // 5 accounts for super policy class
        assertEquals(5, pap.getPolicyClasses().size());
    }

    @Test
    void testCreatePolicy() throws PMException, PALCompilationException {
        String input = """
                create policy class 'pc1';
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.fromString(input, new PALDeserializer(new UserContext(SUPER_USER)));
        assertTrue(pap.nodeExists("pc1"));
    }

    @Test
    void testCreateAttr() throws PMException, PALCompilationException {
        String input = """
                create policy class 'pc1';
                create user attribute 'ua1' in ['pc1'];
                create object attribute 'oa1' in ['pc1'];
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.fromString(input, new PALDeserializer(new UserContext(SUPER_USER)));
        assertTrue(pap.nodeExists("ua1"));
        assertTrue(pap.getParents("ua1").contains("pc1"));
        assertTrue(pap.nodeExists("oa1"));
        assertTrue(pap.getParents("oa1").contains("pc1"));
        assertTrue(pap.getChildren("pc1").contains("ua1"));
        assertTrue(pap.getChildren("pc1").contains("oa1"));
    }

    @Test
    void testCreateUserObject() throws PMException, PALCompilationException {
        String input = """
                create policy class 'pc1';
                create user attribute 'ua1' in ['pc1'];
                create object attribute 'oa1' in ['pc1'];
                create user 'u1' in ['ua1'];
                create object 'o1' in ['oa1'];
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.fromString(input, new PALDeserializer(new UserContext(SUPER_USER)));
        assertTrue(pap.nodeExists("u1"));
        assertTrue(pap.getParents("u1").contains("ua1"));
        assertTrue(pap.nodeExists("o1"));
        assertTrue(pap.getParents("o1").contains("oa1"));
        assertTrue(pap.getChildren("oa1").contains("o1"));
        assertTrue(pap.getChildren("ua1").contains("u1"));
    }

    @Test
    void testSetNodeProperties() throws PMException, PALCompilationException {
        String input = """
                create policy class 'pc1';
                create user attribute 'ua1' in ['pc1'];
                set properties of 'ua1' to {'key': 'value'};
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.fromString(input, new PALDeserializer(new UserContext(SUPER_USER)));
        assertEquals("value", pap.getNode("ua1").getProperties().get("key"));
    }

    @Test
    void testAssign() throws PMException, PALCompilationException {
        String input = """
                create policy class 'pc1';
                create user attribute 'ua1' in ['pc1'];
                create user attribute 'ua2' in ['pc1'];
                create user attribute 'ua3' in ['pc1'];
                assign 'ua1' to ['ua2', 'ua3'];
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.fromString(input, new PALDeserializer(new UserContext(SUPER_USER)));
        assertTrue(pap.getParents("ua1").contains("ua2"));
        assertTrue(pap.getParents("ua1").contains("ua3"));
    }

}