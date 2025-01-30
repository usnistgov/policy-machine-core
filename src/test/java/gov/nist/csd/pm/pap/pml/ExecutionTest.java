package gov.nist.csd.pm.pap.pml;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static gov.nist.csd.pm.util.TestMemoryPAP.id;
import static gov.nist.csd.pm.util.TestMemoryPAP.ids;
import static org.junit.jupiter.api.Assertions.*;

public class ExecutionTest {
    
    @Test
    void testGraphPML() throws PMException {
        PAP pap = new MemoryPAP();

        String input =
                """
                set resource operations ["read", "write"]
                
                create policy class "pc1"
                
                set properties of "pc1" to {"k": "v"}
                
                create object attribute "oa1" in ["pc1"]
                create object attribute "oa2" in ["pc1"]
                create object attribute "oa3" in ["pc1"]
                
                var descendants = ["oa1", "oa2", "oa3"]
                create object "o1" in descendants
                
                create user attribute "ua1" in ["pc1"]
                create user attribute "ua2" in ["pc1"]
                create user attribute "ua3" in ["pc1"]
                
                var username = "u1"
                create user username in ["ua1"]
                assign username to ["ua2", "ua3"]
                
                associate "ua1" and "oa1" with ["read", "write"]
                associate "ua2" and "oa2" with ["read", "write"]
                associate "ua3" and "oa3" with ["read", "write"]
                """;
        pap.executePML(new TestUserContext("u1", pap), input);

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("oa1"));
        assertTrue(pap.query().graph().nodeExists("oa2"));
        assertTrue(pap.query().graph().nodeExists("oa3"));
        assertTrue(pap.query().graph().nodeExists("ua1"));
        assertTrue(pap.query().graph().nodeExists("ua2"));
        assertTrue(pap.query().graph().nodeExists("ua3"));
        assertTrue(pap.query().graph().nodeExists("o1"));
        assertTrue(pap.query().graph().nodeExists("u1"));

        assertEquals("v", pap.query().graph().getNodeByName("pc1").getProperties().get("k"));

        long[] ascendants = pap.query().graph().getAdjacentAscendants(id(pap, "pc1"));
        assertTrue(Arrays.stream(ascendants).boxed().toList().containsAll(ids(pap, "ua1", "ua2", "ua3")));
        ascendants = pap.query().graph().getAdjacentAscendants(id(pap, "pc1"));
        assertTrue(Arrays.stream(ascendants).boxed().toList().containsAll(ids(pap, "oa1", "oa2", "oa3")));

        assertTrue(Arrays.stream(pap.query().graph().getAdjacentDescendants(id(pap, "ua1"))).boxed().toList().contains(id(pap, "pc1")));
        assertTrue(Arrays.stream(pap.query().graph().getAdjacentDescendants(id(pap, "ua2"))).boxed().toList().contains(id(pap, "pc1")));
        assertTrue(Arrays.stream(pap.query().graph().getAdjacentDescendants(id(pap, "ua3"))).boxed().toList().contains(id(pap, "pc1")));
        assertTrue(Arrays.stream(pap.query().graph().getAdjacentDescendants(id(pap, "oa1"))).boxed().toList().contains(id(pap, "pc1")));
        assertTrue(Arrays.stream(pap.query().graph().getAdjacentDescendants(id(pap, "oa2"))).boxed().toList().contains(id(pap, "pc1")));
        assertTrue(Arrays.stream(pap.query().graph().getAdjacentDescendants(id(pap, "oa3"))).boxed().toList().contains(id(pap, "pc1")));
        assertTrue(Arrays.stream(pap.query().graph().getAdjacentDescendants(id(pap, "u1"))).boxed().toList().containsAll(ids(pap, "ua1", "ua2", "ua3")));
        assertTrue(Arrays.stream(pap.query().graph().getAdjacentDescendants(id(pap, "o1"))).boxed().toList().containsAll(ids(pap, "oa1", "oa2", "oa3")));

        assertEquals(new Association(id(pap, "ua1"), id(pap, "oa1"), new AccessRightSet("read", "write")),
                pap.query().graph().getAssociationsWithSource(id(pap, "ua1")).iterator().next());
        assertEquals(new Association(id(pap, "ua2"), id(pap, "oa2"), new AccessRightSet("read", "write")),
                pap.query().graph().getAssociationsWithSource(id(pap, "ua2")).iterator().next());
        assertEquals(new Association(id(pap, "ua3"), id(pap, "oa3"), new AccessRightSet("read", "write")),
                pap.query().graph().getAssociationsWithSource(id(pap, "ua3")).iterator().next());

        input = """
                dissociate "ua1" and "oa1"
                """;
        pap.executePML(new TestUserContext("u1", pap), input);
        assertTrue(pap.query().graph().getAssociationsWithSource(id(pap, "ua1")).isEmpty());

        input =
                """
                deassign "u1" from ["ua1", "ua2"]
                """;
        pap.executePML(new TestUserContext("u1", pap), input);
        assertFalse(Arrays.stream(pap.query().graph().getAdjacentDescendants(id(pap, "u1"))).boxed().toList().containsAll(ids(pap, "ua1", "ua2")));
        assertFalse(Arrays.stream(pap.query().graph().getAdjacentAscendants(id(pap, "ua1"))).boxed().toList().contains(id(pap, "u1")));
        assertFalse(Arrays.stream(pap.query().graph().getAdjacentAscendants(id(pap, "ua2"))).boxed().toList().contains(id(pap, "u1")));

        input =
                """
                delete user "u1"
                """;
        pap.executePML(new TestUserContext("u1", pap), input);
        assertFalse(pap.query().graph().nodeExists("u1"));

        input =
                """
                deassign "o1" from ["oa1"]
                """;
        pap.executePML(new TestUserContext("u1", pap), input);
        assertFalse(Arrays.stream(pap.query().graph().getAdjacentDescendants(id(pap, "oa1"))).boxed().toList().contains(id(pap, "oa1")));
        assertFalse(Arrays.stream(pap.query().graph().getAdjacentAscendants(id(pap, "oa1"))).boxed().toList().contains(id(pap, "o1")));

        input =
                """
                delete object "o1"
                """;
        pap.executePML(new TestUserContext("u1", pap), input);
        assertFalse(pap.query().graph().nodeExists("o1"));

        input =
                """
                delete user attribute "ua1"
                delete user attribute "ua2"
                delete user attribute "ua3"
                """;
        pap.executePML(new TestUserContext("u1", pap), input);
        assertFalse(pap.query().graph().nodeExists("ua1"));
        assertFalse(pap.query().graph().nodeExists("ua2"));
        assertFalse(pap.query().graph().nodeExists("ua3"));


        input =
                """
                delete object attribute "oa1"
                delete object attribute "oa2"
                delete object attribute "oa3"
                """;
        pap.executePML(new TestUserContext("u1", pap), input);
        assertFalse(pap.query().graph().nodeExists("oa1"));
        assertFalse(pap.query().graph().nodeExists("oa2"));
        assertFalse(pap.query().graph().nodeExists("oa3"));

        input =
                """
                delete policy class "pc1"
                """;
        pap.executePML(new TestUserContext("u1", pap), input);
        assertFalse(pap.query().graph().nodeExists("pc1"));
    }

    @Test
    void testIf() throws PMException {
         PAP pap = new MemoryPAP();
        String input = """
                var x = "test"
                var y = "test"
                if equals(x, y) {
                    create policy class "pc1"
                }
                """;
        pap.executePML(new TestUserContext("u1", pap), input);
        assertTrue(pap.query().graph().nodeExists("pc1"));

        input = """
                var x = "test"
                var y = "test"
                var z = "test1"
                if equals(x, z) {
                    create policy class "pc1"
                } else if equals(x, y) {
                    create policy class "pc2"
                }
                """;
        pap = new MemoryPAP();
        pap.executePML(new TestUserContext("u1", pap), input);

        assertFalse(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("pc2"));

        input = """
                var x = "test"
                var y = "test1"
                var z = "test2"
                if equals(x, z) {
                    create policy class "pc1"
                } else if equals(x, y) {
                    create policy class "pc2"
                } else {
                    create policy class "pc3"
                }
                """;
        pap = new MemoryPAP();
        
        pap.executePML(new TestUserContext("u1", pap), input);

        assertFalse(pap.query().graph().nodeExists("pc1"));
        assertFalse(pap.query().graph().nodeExists("pc2"));
        assertTrue(pap.query().graph().nodeExists("pc3"));

        input = """
                var x = "test"
                var y = "test1"
                var z = "test2"
                if equals(x, y) {
                    create policy class "pc1"
                } else {
                    create policy class "pc2"
                }
                """;
        pap = new MemoryPAP();
        
        pap.executePML(new TestUserContext("u1", pap), input);

        assertFalse(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("pc2"));

        input = """
                var x = "test"
                var y = "test1"
                var z = "test2"
                if !equals(x, y) {
                    create policy class "pc1"
                } else {
                    create policy class "pc2"
                }
                """;
        pap = new MemoryPAP();
        
        pap.executePML(new TestUserContext("u1", pap), input);

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertFalse(pap.query().graph().nodeExists("pc2"));
    }

    @Test
    void testForeach() throws PMException {
         PAP pap = new MemoryPAP();
        
        String input = """
                foreach x in ["pc1", "pc2", "pc3"] {
                    create policy class x
                }
                """;
        pap.executePML(new TestUserContext("u1", pap), input);

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("pc2"));
        assertTrue(pap.query().graph().nodeExists("pc3"));

        input = """
                var m = {"k1": "pc1", "k2": "pc2", "k3": "pc3"}
                foreach x, y in m {
                    create policy class y
                }
                """;
        pap = new MemoryPAP();
        
        pap.executePML(new TestUserContext("u1", pap), input);

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("pc2"));
        assertTrue(pap.query().graph().nodeExists("pc3"));

        input = """
                foreach x, y in {"k1": ["pc1", "pc2"], "k2": ["pc3"]} {
                    foreach z in y {
                        create policy class z
                    }
                }
                """;
        pap = new MemoryPAP();
        
        pap.executePML(new TestUserContext("u1", pap), input);

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("pc2"));
        assertTrue(pap.query().graph().nodeExists("pc3"));

        input = """
                foreach x, y in {"k1": ["pc1", "pc2"], "k2": ["pc3"]} {
                    foreach z in y {
                        create policy class z
                        break
                    }
                }
                """;
        pap = new MemoryPAP();
        
        pap.executePML(new TestUserContext("u1", pap), input);

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertFalse(pap.query().graph().nodeExists("pc2"));
        assertTrue(pap.query().graph().nodeExists("pc3"));

        input = """
                foreach x, y in {"k1": ["pc1", "pc2"], "k2": ["pc3"]} {
                    foreach z in y {
                        continue
                        create policy class z
                    }
                }
                """;
        pap = new MemoryPAP();
        
        pap.executePML(new TestUserContext("u1", pap), input);

        assertFalse(pap.query().graph().nodeExists("pc1"));
        assertFalse(pap.query().graph().nodeExists("pc2"));
        assertFalse(pap.query().graph().nodeExists("pc3"));

        input = """
                var a = "test"
                var b = "test"
                foreach x in ["pc1", "pc2", "pc3"] {
                    if equals(a, b) {
                        a = "test2"
                        continue
                    }
                    
                    create policy class x
                }
                """;
        pap = new MemoryPAP();
        
        pap.executePML(new TestUserContext("u1", pap), input);

        assertFalse(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("pc2"));
        assertTrue(pap.query().graph().nodeExists("pc3"));
    }

    @Test
    void testFunction() throws PMException {
        String input = """
                operation testFunc(any x) {
                    create policy class x
                }
                
                testFunc("pc1")
                """;
        PAP pap = new MemoryPAP();
        
        pap.executePML(new TestUserContext("u1", pap), input);

        assertTrue(pap.query().graph().nodeExists("pc1"));

        String input1 = """
                operation testFunc(any x) {
                    create policy class x
                }
                
                testFunc(["pc1"])
                """;

        
        PAP pap1 = new MemoryPAP();
        assertThrows(ClassCastException.class, () -> pap1.executePML(new TestUserContext("u1", pap), input1));

        PAP pap2 = new MemoryPAP();
        String input2 = """
                x := "hello"
                operation testFunc() {
                    create policy class x + " world"
                }
                
                testFunc()
                """;

        PMLCompilationException e = assertThrows(
                PMLCompilationException.class,
                () -> pap2.executePML(new TestUserContext("u1", pap), input2)
        );
        assertEquals(1, e.getErrors().size());
        assertEquals("unknown variable 'x' in scope", e.getErrors().getFirst().errorMessage());
    }

    @Test
    void testMaps() throws PMException {
        String input = """
                var m = {"k1": {"k1-1": {"k1-1-1": "v1"}}}
                var x = m["k1"]["k1-1"]["k1-1-1"]
                create policy class x
                """;
         PAP pap = new MemoryPAP();
        
        pap.executePML(new TestUserContext("u1", pap), input);
        assertTrue(Arrays.stream(pap.query().graph().getPolicyClasses()).boxed().toList().contains(id(pap, "v1")));
    }

    @Test
    void testArrayWithLiteral() throws PMException {
        String input = """
                set resource operations ["read", "write"]
                """;
         PAP pap = new MemoryPAP();
        
        pap.executePML(new TestUserContext("u1", pap), input);
        assertTrue(pap.query().operations().getResourceOperations().contains("read"));

        String input1 = """
                set resource operations [["read", "write"], ["exec"]]
                """;
        assertThrows(PMException.class, () -> pap.executePML(new TestUserContext("u1", pap), input1));
    }

    @Test
    void testDeleteNonExistentNode() throws PMException {
        String input = """
                delete pc "pc1"
                """;
         PAP pap = new MemoryPAP();
        
        assertDoesNotThrow(() -> pap.executePML(new TestUserContext("u1", pap), input));
    }

    @Test
    void testDeleteProhibition() throws PMException {
         PAP pap = new MemoryPAP();
        
        pap.modify().operations().setResourceOperations(new AccessRightSet("read"));
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids(pap, "pc1"));
        pap.modify().graph().createObjectAttribute("oa1", ids(pap, "pc1"));

        String input = """
                create prohibition "p1"
                deny user attribute "ua1"
                access rights ["read"]
                on union of ["oa1"]
                """;
        pap.executePML(new TestUserContext("u1", pap), input);

        input = """
                delete prohibition "p1"
                """;
        pap.executePML(new TestUserContext("u1", pap), input);
        assertFalse(pap.query().prohibitions().getProhibitions().stream().filter(p -> !p.getName().equals("p1")).toList().isEmpty());
    }

    @Test
    void testReturnValue() throws PMException {
        String pml = """
                operation testFunc(string s) string {
                    return s
                }
                
                create policy class testFunc("test")
                """;

         PAP pap = new MemoryPAP();
        pap.deserialize(new TestUserContext("u1", pap), pml, new PMLDeserializer());

        assertTrue(pap.query().graph().nodeExists("test"));
    }

    @Test
    void testOverwriteFunctionArg() throws PMException {
        String pml = """
                operation testFunc(string s) string {
                    s = "test2"
                    return s
                }
                
                create policy class testFunc("test")
                """;

         PAP pap = new MemoryPAP();
        pap.deserialize(new TestUserContext("u1", pap), pml, new PMLDeserializer());

        assertFalse(pap.query().graph().nodeExists("test"));
        assertTrue(pap.query().graph().nodeExists("test2"));
    }
}
