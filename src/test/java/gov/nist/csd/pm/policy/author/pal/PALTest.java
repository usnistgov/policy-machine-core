package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.policy.author.pal.model.exception.PALCompilationException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.pap.memory.MemoryPAP;
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
        MemoryPAP pap = new MemoryPAP();
        pap.fromPAL(new UserContext(SUPER_USER), input);
        assertTrue(pap.graph().nodeExists("helloworld"));
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
        MemoryPAP pap = new MemoryPAP();
        pap.fromPAL(new UserContext(SUPER_USER), input);
        // 5 accounts for super policy class
        assertEquals(5, pap.graph().getPolicyClasses().size());
    }

    @Test
    void testCreatePolicy() throws PMException, PALCompilationException {
        String input = """
                create policy class 'pc1';
                """;
        MemoryPAP pap = new MemoryPAP();
        pap.fromPAL(new UserContext(SUPER_USER), input);
        assertTrue(pap.graph().nodeExists("pc1"));
    }

    @Test
    void testCreateAttr() throws PMException, PALCompilationException {
        String input = """
                create policy class 'pc1';
                create user attribute 'ua1' in 'pc1';
                create object attribute 'oa1' in 'pc1';
                """;
        MemoryPAP pap = new MemoryPAP();
        pap.fromPAL(new UserContext(SUPER_USER), input);
        assertTrue(pap.graph().nodeExists("ua1"));
        assertTrue(pap.graph().getParents("ua1").contains("pc1"));
        assertTrue(pap.graph().nodeExists("oa1"));
        assertTrue(pap.graph().getParents("oa1").contains("pc1"));
        assertTrue(pap.graph().getChildren("pc1").contains("ua1"));
        assertTrue(pap.graph().getChildren("pc1").contains("oa1"));
    }

    @Test
    void testCreateUserObject() throws PMException, PALCompilationException {
        String input = """
                create policy class 'pc1';
                create user attribute 'ua1' in 'pc1';
                create object attribute 'oa1' in 'pc1';
                create user 'u1' in 'ua1';
                create object 'o1' in 'oa1';
                """;
        MemoryPAP pap = new MemoryPAP();
        pap.fromPAL(new UserContext(SUPER_USER), input);
        assertTrue(pap.graph().nodeExists("u1"));
        assertTrue(pap.graph().getParents("u1").contains("ua1"));
        assertTrue(pap.graph().nodeExists("o1"));
        assertTrue(pap.graph().getParents("o1").contains("oa1"));
        assertTrue(pap.graph().getChildren("oa1").contains("o1"));
        assertTrue(pap.graph().getChildren("ua1").contains("u1"));
    }

    @Test
    void testSetNodeProperties() throws PMException, PALCompilationException {
        String input = """
                create policy class 'pc1';
                create user attribute 'ua1' in 'pc1';
                set properties of 'ua1' to {'key': 'value'};
                """;
        MemoryPAP pap = new MemoryPAP();
        pap.fromPAL(new UserContext(SUPER_USER), input);
        assertEquals("value", pap.graph().getNode("ua1").getProperties().get("key"));
    }

    @Test
    void testAssign() throws PMException, PALCompilationException {
        String input = """
                create policy class 'pc1';
                create user attribute 'ua1' in 'pc1';
                create user attribute 'ua2' in 'pc1';
                create user attribute 'ua3' in 'pc1';
                assign 'ua1' to 'ua2';
                assign 'ua1' to 'ua3';
                """;
        MemoryPAP pap = new MemoryPAP();
        pap.fromPAL(new UserContext(SUPER_USER), input);
        assertTrue(pap.graph().getParents("ua1").contains("ua2"));
        assertTrue(pap.graph().getParents("ua1").contains("ua3"));
    }

}