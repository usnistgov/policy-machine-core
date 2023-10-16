package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.policy.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExecutionTest {

    static UserContext superUser = new UserContext("u1");

    @Test
    void testGraphPML() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());

        String input =
                "                set resource access rights [\"read\", \"write\"]\n" +
                "                \n" +
                "                create policy class \"pc1\"\n" +
                "                \n" +
                "                set properties of \"pc1\" to {\"k\": \"v\"}\n" +
                "                \n" +
                "                create object attribute \"oa1\" assign to [\"pc1\"]\n" +
                "                create object attribute \"oa2\" assign to [\"pc1\"]\n" +
                "                create object attribute \"oa3\" assign to [\"pc1\"]\n" +
                "                \n" +
                "                var parents = [\"oa1\", \"oa2\", \"oa3\"]\n" +
                "                create object \"o1\" assign to parents\n" +
                "                \n" +
                "                create user attribute \"ua1\" assign to [\"pc1\"]\n" +
                "                create user attribute \"ua2\" assign to [\"pc1\"]\n" +
                "                create user attribute \"ua3\" assign to [\"pc1\"]\n" +
                "                \n" +
                "                var username = \"u1\"\n" +
                "                create user username assign to [\"ua1\"]\n" +
                "                assign username to [\"ua2\", \"ua3\"]\n" +
                "                \n" +
                "                associate \"ua1\" and \"oa1\" with [\"read\", \"write\"]\n" +
                "                associate \"ua2\" and \"oa2\" with [\"read\", \"write\"]\n" +
                "                associate \"ua3\" and \"oa3\" with [\"read\", \"write\"]";
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

        input = "dissociate \"ua1\" and [\"oa1\"]";
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertTrue(pap.graph().getAssociationsWithSource("ua1").isEmpty());

        input =
                "deassign \"u1\" from [\"ua1\", \"ua2\"]";
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertFalse(pap.graph().getParents("u1").containsAll(Arrays.asList("ua1", "ua2")));
        assertFalse(pap.graph().getChildren("ua1").contains("u1"));
        assertFalse(pap.graph().getChildren("ua2").contains("u1"));

        input =
                "delete user \"u1\"";
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertFalse(pap.graph().nodeExists("u1"));

        input =
                "deassign \"o1\" from [\"oa1\"]";
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertFalse(pap.graph().getParents("oa1").contains("oa1"));
        assertFalse(pap.graph().getChildren("oa1").contains("o1"));

        input =
                "delete object \"o1\"";
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertFalse(pap.graph().nodeExists("o1"));

        input =
                "delete user attribute \"ua1\"\n" +
                        "                delete user attribute \"ua2\"\n" +
                        "                delete user attribute \"ua3\"";
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertFalse(pap.graph().nodeExists("ua1"));
        assertFalse(pap.graph().nodeExists("ua2"));
        assertFalse(pap.graph().nodeExists("ua3"));


        input =
                "                delete object attribute \"oa1\"\n" +
                "                delete object attribute \"oa2\"\n" +
                "                delete object attribute \"oa3\"";
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertFalse(pap.graph().nodeExists("oa1"));
        assertFalse(pap.graph().nodeExists("oa2"));
        assertFalse(pap.graph().nodeExists("oa3"));

        input =
                "delete policy class \"pc1\"";
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertFalse(pap.graph().nodeExists("pc1"));
    }

    @Test
    void testIf() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        String input =
                "var x = \"test\"\n" +
                "                var y = \"test\"\n" +
                "                if equals(x, y) {\n" +
                "                    create policy class \"pc1\"\n" +
                "                }";
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertTrue(pap.graph().nodeExists("pc1"));

        input =
                "                var x = \"test\"\n" +
                "                var y = \"test\"\n" +
                "                var z = \"test1\"\n" +
                "                if equals(x, z) {\n" +
                "                    create policy class \"pc1\"\n" +
                "                } else if equals(x, y) {\n" +
                "                    create policy class \"pc2\"\n" +
                "                }";
        pap = new PAP(new MemoryPolicyStore());
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertFalse(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("pc2"));

        input =
                "                var x = \"test\"\n" +
                "                var y = \"test1\"\n" +
                "                var z = \"test2\"\n" +
                "                if equals(x, z) {\n" +
                "                    create policy class \"pc1\"\n" +
                "                } else if equals(x, y) {\n" +
                "                    create policy class \"pc2\"\n" +
                "                } else {\n" +
                "                    create policy class \"pc3\"\n" +
                "                }";
        pap = new PAP(new MemoryPolicyStore());
        
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertFalse(pap.graph().nodeExists("pc1"));
        assertFalse(pap.graph().nodeExists("pc2"));
        assertTrue(pap.graph().nodeExists("pc3"));

        input =
                "                var x = \"test\"\n" +
                "                var y = \"test1\"\n" +
                "                var z = \"test2\"\n" +
                "                if equals(x, y) {\n" +
                "                    create policy class \"pc1\"\n" +
                "                } else {\n" +
                "                    create policy class \"pc2\"\n" +
                "                }";
        pap = new PAP(new MemoryPolicyStore());
        
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertFalse(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("pc2"));

        input =
                "                var x = \"test\"\n" +
                "                var y = \"test1\"\n" +
                "                var z = \"test2\"\n" +
                "                if !equals(x, y) {\n" +
                "                    create policy class \"pc1\"\n" +
                "                } else {\n" +
                "                    create policy class \"pc2\"\n" +
                "                }";
        pap = new PAP(new MemoryPolicyStore());
        
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertTrue(pap.graph().nodeExists("pc1"));
        assertFalse(pap.graph().nodeExists("pc2"));
    }

    @Test
    void testForeach() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        
        String input =
                "                foreach x in [\"pc1\", \"pc2\", \"pc3\"] {\n" +
                "                    create policy class x\n" +
                "                }";
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("pc2"));
        assertTrue(pap.graph().nodeExists("pc3"));

        input =
                "                var m = {\"k1\": \"pc1\", \"k2\": \"pc2\", \"k3\": \"pc3\"}\n" +
                "                foreach x, y in m {\n" +
                "                    create policy class y\n" +
                "                }";
        pap = new PAP(new MemoryPolicyStore());
        
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("pc2"));
        assertTrue(pap.graph().nodeExists("pc3"));

        input =
                "                foreach x, y in {\"k1\": [\"pc1\", \"pc2\"], \"k2\": [\"pc3\"]} {\n" +
                "                    foreach z in y {\n" +
                "                        create policy class z\n" +
                "                    }\n" +
                "                }";
        pap = new PAP(new MemoryPolicyStore());
        
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("pc2"));
        assertTrue(pap.graph().nodeExists("pc3"));

        input =
                "                foreach x, y in {\"k1\": [\"pc1\", \"pc2\"], \"k2\": [\"pc3\"]} {\n" +
                "                    foreach z in y {\n" +
                "                        create policy class z\n" +
                "                        break\n" +
                "                    }\n" +
                "                }";
        pap = new PAP(new MemoryPolicyStore());
        
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertTrue(pap.graph().nodeExists("pc1"));
        assertFalse(pap.graph().nodeExists("pc2"));
        assertTrue(pap.graph().nodeExists("pc3"));

        input =
                "                foreach x, y in {\"k1\": [\"pc1\", \"pc2\"], \"k2\": [\"pc3\"]} {\n" +
                "                    foreach z in y {\n" +
                "                        continue\n" +
                "                        create policy class z\n" +
                "                    }\n" +
                "                }";
        pap = new PAP(new MemoryPolicyStore());
        
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertFalse(pap.graph().nodeExists("pc1"));
        assertFalse(pap.graph().nodeExists("pc2"));
        assertFalse(pap.graph().nodeExists("pc3"));

        input =
                "                var a = \"test\"\n" +
                "                var b = \"test\"\n" +
                "                foreach x in [\"pc1\", \"pc2\", \"pc3\"] {\n" +
                "                    if equals(a, b) {\n" +
                "                        a = \"test2\"\n" +
                "                        continue\n" +
                "                    }\n" +
                "                    \n" +
                "                    create policy class x\n" +
                "                }";
        pap = new PAP(new MemoryPolicyStore());
        
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertFalse(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("pc2"));
        assertTrue(pap.graph().nodeExists("pc3"));
    }

    @Test
    void testFunction() throws PMException {
        String input =
                "                function testFunc(any x) {\n" +
                "                    create policy class x\n" +
                "                }\n" +
                "                \n" +
                "                testFunc(\"pc1\")";
        PAP pap = new PAP(new MemoryPolicyStore());
        
        PMLExecutor.compileAndExecutePML(pap, superUser, input);

        assertTrue(pap.graph().nodeExists("pc1"));

        String input1 =
                "                function testFunc(any x) {\n" +
                "                    create policy class x\n" +
                "                }\n" +
                "                \n" +
                "                testFunc([\"pc1\"])";
        PAP pap1 = new PAP(new MemoryPolicyStore());
        assertThrows(ClassCastException.class, () -> PMLExecutor.compileAndExecutePML(pap1, superUser, input1));

        PAP pap2 = new PAP(new MemoryPolicyStore());
        input = "const x = \"hello\"\n" +
                "                function testFunc() {\n" +
                "                    create policy class x + \" world\"\n" +
                "                }\n" +
                "                \n" +
                "                testFunc()";
        PMLExecutor.compileAndExecutePML(pap2, superUser, input);
        assertTrue(pap2.graph().nodeExists("hello world"));
    }

    @Test
    void testMaps() throws PMException {
        String input =
                "                var m = {\"k1\": {\"k1-1\": {\"k1-1-1\": \"v1\"}}}\n" +
                "                var x = m[\"k1\"][\"k1-1\"][\"k1-1-1\"]\n" +
                "                create policy class x";
        PAP pap = new PAP(new MemoryPolicyStore());
        
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertTrue(pap.graph().getPolicyClasses().contains("v1"));
    }

    @Test
    void testArrayWithLiteral() throws PMException {
        String input = "set resource access rights [\"read\", \"write\"]";
        PAP pap = new PAP(new MemoryPolicyStore());
        
        PMLExecutor.compileAndExecutePML(pap, superUser, input);
        assertTrue(pap.graph().getResourceAccessRights().contains("read"));

        String input1 = "set resource access rights [[\"read\", \"write\"], [\"exec\"]]";
        assertThrows(PMException.class, () -> PMLExecutor.compileAndExecutePML(pap, superUser, input1));
    }

    @Test
    void testDeleteNonExistentNode() throws PMException {
        String input = "delete pc \"pc1\"";
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

        String input =
                "                create prohibition \"p1\"\n" +
                "                deny user attribute \"ua1\"\n" +
                "                access rights [\"read\"]\n" +
                "                on union of [\"oa1\"]";
        pap.executePML(superUser, input);

        input = "delete prohibition \"p1\"";
        pap.executePML(superUser, input);
        assertFalse(pap.prohibitions().getAll().containsKey("p1"));
    }

    @Test
    void testReturnValue() throws PMException {
        String pml = "function testFunc(string s) string {\n" +
                "                    return s\n" +
                "                }\n" +
                "                \n" +
                "                create policy class testFunc(\"test\")";

        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        assertTrue(pap.graph().nodeExists("test"));
    }

    @Test
    void testOverwriteFunctionArg() throws PMException {
        String pml = "function testFunc(string s) string {\n" +
                "                    s = \"test2\"\n" +
                "                    return s\n" +
                "                }\n" +
                "                \n" +
                "                create policy class testFunc(\"test\")";

        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        assertFalse(pap.graph().nodeExists("test"));
        assertTrue(pap.graph().nodeExists("test2"));
    }

    @Test
    void testLocalConst() throws PMException {
        String pml =
                "function testFunc() {\n" +
                "    const x = \"x\"\n" +
                "    create policy class x\n" +
                "}\n" +
                "\n" +
                "testFunc()";
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(memoryPolicyStore, superUser, pml);

        assertTrue(memoryPolicyStore.graph().nodeExists("x"));
    }

    @Test
    void testLocalConstOverwritesGlobal() throws PMException {
        String pml =
                "const x = \"x\"\n" +
                "function testFunc() {\n" +
                "    const x = \"not x\"\n" +
                "    var x = \"not x\"\n" +
                "    create policy class x\n" +
                "}\n" +
                "\n" +
                "testFunc()";
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        PMLCompilationException e =
                assertThrows(PMLCompilationException.class,
                             () -> PMLExecutor.compileAndExecutePML(memoryPolicyStore, superUser, pml)
                );
        assertEquals(2, e.getErrors().size());
        assertEquals("const 'x' already defined in scope", e.getErrors().get(0).errorMessage());
        assertEquals("variable 'x' already defined in scope", e.getErrors().get(1).errorMessage());
    }
}
