package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
import static org.junit.jupiter.api.Assertions.*;

public class ExecutionTest {

    static UserContext superUser = new UserContext(SUPER_USER);

    @Test
    void testGraphPML() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());

        String input =
                """
                set resource access rights ['read', 'write']
                
                create policy class 'pc1'
                
                set properties of 'pc1' to {'k': 'v'}
                
                create object attribute 'oa1' in ['pc1']
                create object attribute 'oa2' in ['pc1']
                create object attribute 'oa3' in ['pc1']
                
                let parents = ['oa1', 'oa2', 'oa3']
                create object 'o1' in parents
                
                create user attribute 'ua1' in ['pc1']
                create user attribute 'ua2' in ['pc1']
                create user attribute 'ua3' in ['pc1']
                
                let username = 'u1'
                create user username in ['ua1']
                assign username to ['ua2', 'ua3']
                
                associate 'ua1' and 'oa1' with ['read', 'write']
                associate 'ua2' and 'oa2' with ['read', 'write']
                associate 'ua3' and 'oa3' with ['read', 'write']
                """;
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("oa1"));
        assertTrue(pap.graph().nodeExists("oa2"));
        assertTrue(pap.graph().nodeExists("oa3"));
        assertTrue(pap.graph().nodeExists("ua1"));
        assertTrue(pap.graph().nodeExists("ua2"));
        assertTrue(pap.graph().nodeExists("ua3"));
        assertTrue(pap.graph().nodeExists("o1"));
        assertTrue(pap.graph().nodeExists("u1"));

        assertEquals("v", pap.graph().getNode("pc1").getProperties().get("k"));

        List<String> children = pap.graph().getChildren("pc1");
        assertTrue(children.containsAll(Arrays.asList("ua1", "ua2", "ua3")));
        children = pap.graph().getChildren("pc1");
        assertTrue(children.containsAll(Arrays.asList("oa1", "oa2", "oa3")));

        assertTrue(pap.graph().getParents("ua1").contains("pc1"));
        assertTrue(pap.graph().getParents("ua2").contains("pc1"));
        assertTrue(pap.graph().getParents("ua3").contains("pc1"));
        assertTrue(pap.graph().getParents("oa1").contains("pc1"));
        assertTrue(pap.graph().getParents("oa2").contains("pc1"));
        assertTrue(pap.graph().getParents("oa3").contains("pc1"));
        assertTrue(pap.graph().getParents("u1").containsAll(Arrays.asList("ua1", "ua2", "ua3")));
        assertTrue(pap.graph().getParents("o1").containsAll(Arrays.asList("oa1", "oa2", "oa3")));

        assertEquals(new Association("ua1", "oa1", new AccessRightSet("read", "write")),
                pap.graph().getAssociationsWithSource("ua1").get(0));
        assertEquals(new Association("ua2", "oa2", new AccessRightSet("read", "write")),
                pap.graph().getAssociationsWithSource("ua2").get(0));
        assertEquals(new Association("ua3", "oa3", new AccessRightSet("read", "write")),
                pap.graph().getAssociationsWithSource("ua3").get(0));

        input =
                """
                deassign 'u1' from ['ua1', 'ua2']
                """;
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertFalse(pap.graph().getParents("u1").containsAll(Arrays.asList("ua1", "ua2")));
        assertFalse(pap.graph().getChildren("ua1").contains("u1"));
        assertFalse(pap.graph().getChildren("ua2").contains("u1"));

        input =
                """
                delete user 'u1'
                """;
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertFalse(pap.graph().nodeExists("u1"));

        input =
                """
                deassign 'o1' from ['oa1']
                """;
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertFalse(pap.graph().getParents("oa1").contains("oa1"));
        assertFalse(pap.graph().getChildren("oa1").contains("o1"));

        input =
                """
                delete object 'o1'
                """;
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertFalse(pap.graph().nodeExists("o1"));

        input =
                """
                delete user attribute 'ua1'
                delete user attribute 'ua2'
                delete user attribute 'ua3'
                """;
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertFalse(pap.graph().nodeExists("ua1"));
        assertFalse(pap.graph().nodeExists("ua2"));
        assertFalse(pap.graph().nodeExists("ua3"));


        input =
                """
                delete object attribute 'oa1'
                delete object attribute 'oa2'
                delete object attribute 'oa3'
                """;
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertFalse(pap.graph().nodeExists("oa1"));
        assertFalse(pap.graph().nodeExists("oa2"));
        assertFalse(pap.graph().nodeExists("oa3"));

        input =
                """
                delete policy class 'pc1'
                """;
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertFalse(pap.graph().nodeExists("pc1"));
    }

    @Test
    void testIf() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        String input = """
                let x = 'test'
                let y = 'test'
                if equals(x, y) {
                    create policy class 'pc1'
                }
                """;
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertTrue(pap.graph().nodeExists("pc1"));

        input = """
                let x = 'test'
                let y = 'test'
                let z = 'test1'
                if equals(x, z) {
                    create policy class 'pc1'
                } else if equals(x, y) {
                    create policy class 'pc2'
                }
                """;
        pap = new PAP(new MemoryPolicyStore());
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertFalse(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("pc2"));

        input = """
                let x = 'test'
                let y = 'test1'
                let z = 'test2'
                if equals(x, z) {
                    create policy class 'pc1'
                } else if equals(x, y) {
                    create policy class 'pc2'
                } else {
                    create policy class 'pc3'
                }
                """;
        pap = new PAP(new MemoryPolicyStore());
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertFalse(pap.graph().nodeExists("pc1"));
        assertFalse(pap.graph().nodeExists("pc2"));
        assertTrue(pap.graph().nodeExists("pc3"));

        input = """
                let x = 'test'
                let y = 'test1'
                let z = 'test2'
                if equals(x, y) {
                    create policy class 'pc1'
                } else {
                    create policy class 'pc2'
                }
                """;
        pap = new PAP(new MemoryPolicyStore());
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertFalse(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("pc2"));

        input = """
                let x = 'test'
                let y = 'test1'
                let z = 'test2'
                if !equals(x, y) {
                    create policy class 'pc1'
                } else {
                    create policy class 'pc2'
                }
                """;
        pap = new PAP(new MemoryPolicyStore());
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertTrue(pap.graph().nodeExists("pc1"));
        assertFalse(pap.graph().nodeExists("pc2"));
    }

    @Test
    void testForeach() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        String input = """
                foreach x in ['pc1', 'pc2', 'pc3'] {
                    create policy class x
                }
                """;
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("pc2"));
        assertTrue(pap.graph().nodeExists("pc3"));

        input = """
                let m = {'k1': 'pc1', 'k2': 'pc2', 'k3': 'pc3'}
                foreach x in m {
                    create policy class m[x]
                }
                """;
        pap = new PAP(new MemoryPolicyStore());
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("pc2"));
        assertTrue(pap.graph().nodeExists("pc3"));

        input = """
                foreach x, y in {'k1': ['pc1', 'pc2'], 'k2': ['pc3']} {
                    foreach z in y {
                        create policy class z
                    }
                }
                """;
        pap = new PAP(new MemoryPolicyStore());
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("pc2"));
        assertTrue(pap.graph().nodeExists("pc3"));

        input = """
                foreach x, y in {'k1': ['pc1', 'pc2'], 'k2': ['pc3']} {
                    foreach z in y {
                        create policy class z
                        break
                    }
                }
                """;
        pap = new PAP(new MemoryPolicyStore());
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertTrue(pap.graph().nodeExists("pc1"));
        assertFalse(pap.graph().nodeExists("pc2"));
        assertTrue(pap.graph().nodeExists("pc3"));

        input = """
                foreach x, y in {'k1': ['pc1', 'pc2'], 'k2': ['pc3']} {
                    foreach z in y {
                        continue
                        create policy class z
                    }
                }
                """;
        pap = new PAP(new MemoryPolicyStore());
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertFalse(pap.graph().nodeExists("pc1"));
        assertFalse(pap.graph().nodeExists("pc2"));
        assertFalse(pap.graph().nodeExists("pc3"));

        input = """
                let a = 'test'
                let b = 'test'
                foreach x in ['pc1', 'pc2', 'pc3'] {
                    if equals(a, b) {
                        a = 'test2'
                        continue
                    }
                    
                    create policy class x
                }
                """;
        pap = new PAP(new MemoryPolicyStore());
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertFalse(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("pc2"));
        assertTrue(pap.graph().nodeExists("pc3"));
    }

    @Test
    void testForRange() throws PMException {
        String input = """
                for i in range [1, 5] {
                    create policy class numToStr(i)
                }
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertEquals(6, pap.graph().getPolicyClasses().size());
        assertTrue(pap.graph().getPolicyClasses().containsAll(List.of("1", "2", "3", "4", "5")));
    }

    @Test
    void testFunction() throws PMException {
        String input = """
                function testFunc(any x) {
                    create policy class x
                }
                
                testFunc('pc1')
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertTrue(pap.graph().nodeExists("pc1"));

        String input1 = """
                function testFunc(any x) {
                    create policy class x
                }
                
                testFunc(['pc1'])
                """;
        PAP pap1 = new PAP(new MemoryPolicyStore());
        assertThrows(IllegalStateException.class, () -> PMLExecutor.compileAndExecutePML(pap1, superUser, input1));

        input = """
                let x = 'hello'
                function testFunc() {
                    x = concat([x, ' world'])
                    create policy class x
                }
                
                testFunc()
                """;
        PMLExecutor.compileAndExecutePML(pap1, superUser, input);
        assertTrue(pap1.graph().nodeExists("hello world"));
    }

    @Test
    void testChangeVariableValue() throws PMException {
        String input = """
                let a = 'hello world'
                const b = a
                create policy class b
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertTrue(pap.graph().nodeExists("hello world"));

        PAP pap1 = new PAP(new MemoryPolicyStore());
        String input1 = """
                let a = 'hello world'
                const b = a
                a = 'test'
                create policy class b
                """;
        PMLExecutor.compileAndExecutePML(pap1, superUser, input1);
        assertFalse(pap.graph().nodeExists("test"));
    }

    @Test
    void testMaps() throws PMException {
        String input = """
                let m = {'k1': {'k1-1': {'k1-1-1': 'v1'}}}
                let x = m['k1']['k1-1']['k1-1-1']
                create policy class x
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertTrue(pap.graph().getPolicyClasses().contains("v1"));
    }

    @Test
    void testArrayWithLiteral() throws PMException {
        String input = """
                set resource access rights ["read", "write"]
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertTrue(pap.graph().getResourceAccessRights().contains("read"));

        String input1 = """
                set resource access rights [["read", "write"], ["exec"]]
                """;
        assertThrows(PMException.class, () -> PMLExecutor.compileAndExecutePML(pap, superUser, input1));
    }

    @Test
    void testDeleteNonExistentNode() throws PMException {
        String input = """
                delete pc 'pc1'
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        assertDoesNotThrow(() -> PMLExecutor.compileAndExecutePML(pap, superUser, input));
    }

    @Test
    void testDeleteProhibition() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.graph().setResourceAccessRights(new AccessRightSet("read"));
        pap.graph().createPolicyClass("pc1");
        pap.graph().createUserAttribute("ua1", "pc1");
        pap.graph().createObjectAttribute("oa1", "pc1");

        String input = """
                create prohibition 'p1'
                deny user attribute "ua1"
                access rights ["read"]
                on union of ["oa1"]
                """;
        pap.executePML(superUser, input);

        input = """
                delete prohibition 'p1'
                """;
        pap.executePML(superUser, input);
        assertFalse(pap.prohibitions().getProhibitions().containsKey("p1"));
    }

    @Test
    void testArrayGetByIndex() throws PMException {
        String s = """
                let x = ["1", "2", "3"]
                create pc x[1]
                
                let y = [["1", "2", "3"]]
                create pc y[0][0]
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.executePML(new UserContext(SUPER_USER), s);
        assertTrue(pap.graph().nodeExists("2"));
        assertTrue(pap.graph().nodeExists("1"));
    }
}
