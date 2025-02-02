package gov.nist.csd.pm.pap.pml;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;


import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static gov.nist.csd.pm.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.*;

public class ExecutionTest {

    @Test
    void testGraphPML() throws PMException {
        PAP pap = new TestPAP();

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
        pap.executePML(new TestUserContext("u1"), input);

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

        Collection<Long> ascendants = pap.query().graph().getAdjacentAscendants(id("pc1"));
        assertTrue(ascendants.containsAll(ids("ua1", "ua2", "ua3")));
        ascendants = pap.query().graph().getAdjacentAscendants(id("pc1"));
        assertTrue(ascendants.containsAll(ids("oa1", "oa2", "oa3")));

        assertTrue(pap.query().graph().getAdjacentDescendants(id("ua1")).contains(id("pc1")));
        assertTrue(pap.query().graph().getAdjacentDescendants(id("ua2")).contains(id("pc1")));
        assertTrue(pap.query().graph().getAdjacentDescendants(id("ua3")).contains(id("pc1")));
        assertTrue(pap.query().graph().getAdjacentDescendants(id("oa1")).contains(id("pc1")));
        assertTrue(pap.query().graph().getAdjacentDescendants(id("oa2")).contains(id("pc1")));
        assertTrue(pap.query().graph().getAdjacentDescendants(id("oa3")).contains(id("pc1")));
        assertTrue(pap.query().graph().getAdjacentDescendants(id("u1")).containsAll(ids("ua1", "ua2", "ua3")));
        assertTrue(pap.query().graph().getAdjacentDescendants(id("o1")).containsAll(ids("oa1", "oa2", "oa3")));

        assertEquals(new Association(id("ua1"), id("oa1"), new AccessRightSet("read", "write")),
                pap.query().graph().getAssociationsWithSource(id("ua1")).iterator().next());
        assertEquals(new Association(id("ua2"), id("oa2"), new AccessRightSet("read", "write")),
                pap.query().graph().getAssociationsWithSource(id("ua2")).iterator().next());
        assertEquals(new Association(id("ua3"), id("oa3"), new AccessRightSet("read", "write")),
                pap.query().graph().getAssociationsWithSource(id("ua3")).iterator().next());

        input = """
                dissociate "ua1" and "oa1"
                """;
        pap.executePML(new TestUserContext("u1"), input);
        assertTrue(pap.query().graph().getAssociationsWithSource(id("ua1")).isEmpty());

        input =
                """
                deassign "u1" from ["ua1", "ua2"]
                """;
        pap.executePML(new TestUserContext("u1"), input);
        assertFalse(pap.query().graph().getAdjacentDescendants(id("u1")).containsAll(ids("ua1", "ua2")));
        assertFalse(pap.query().graph().getAdjacentAscendants(id("ua1")).contains(id("u1")));
        assertFalse(pap.query().graph().getAdjacentAscendants(id("ua2")).contains(id("u1")));

        input =
                """
                delete node "u1"
                """;
        pap.executePML(new TestUserContext("u1"), input);
        assertFalse(pap.query().graph().nodeExists("u1"));

        input =
                """
                deassign "o1" from ["oa1"]
                """;
        pap.executePML(new TestUserContext("u1"), input);
        assertFalse(pap.query().graph().getAdjacentDescendants(id("oa1")).contains(id("oa1")));
        assertFalse(pap.query().graph().getAdjacentAscendants(id("oa1")).contains(id("o1")));

        input =
                """
                delete node "o1"
                """;
        pap.executePML(new TestUserContext("u1"), input);
        assertFalse(pap.query().graph().nodeExists("o1"));

        input =
                """
                delete node "ua1"
                delete node "ua2"
                delete node "ua3"
                """;
        pap.executePML(new TestUserContext("u1"), input);
        assertFalse(pap.query().graph().nodeExists("ua1"));
        assertFalse(pap.query().graph().nodeExists("ua2"));
        assertFalse(pap.query().graph().nodeExists("ua3"));


        input =
                """
                delete node "oa1"
                delete node "oa2"
                delete node "oa3"
                """;
        pap.executePML(new TestUserContext("u1"), input);
        assertFalse(pap.query().graph().nodeExists("oa1"));
        assertFalse(pap.query().graph().nodeExists("oa2"));
        assertFalse(pap.query().graph().nodeExists("oa3"));

        input =
                """
                delete node "pc1"
                """;
        pap.executePML(new TestUserContext("u1"), input);
        assertFalse(pap.query().graph().nodeExists("pc1"));
    }

    @Test
    void testIf() throws PMException {
         PAP pap = new TestPAP();
        String input = """
                var x = "test"
                var y = "test"
                if equals(x, y) {
                    create policy class "pc1"
                }
                """;
        pap.executePML(new TestUserContext("u1"), input);
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
        pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), input);

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
        pap = new TestPAP();
        
        pap.executePML(new TestUserContext("u1"), input);

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
        pap = new TestPAP();
        
        pap.executePML(new TestUserContext("u1"), input);

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
        pap = new TestPAP();
        
        pap.executePML(new TestUserContext("u1"), input);

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertFalse(pap.query().graph().nodeExists("pc2"));
    }

    @Test
    void testForeach() throws PMException {
         PAP pap = new TestPAP();
        
        String input = """
                foreach x in ["pc1", "pc2", "pc3"] {
                    create policy class x
                }
                """;
        pap.executePML(new TestUserContext("u1"), input);

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("pc2"));
        assertTrue(pap.query().graph().nodeExists("pc3"));

        input = """
                var m = {"k1": "pc1", "k2": "pc2", "k3": "pc3"}
                foreach x, y in m {
                    create policy class y
                }
                """;
        pap = new TestPAP();
        
        pap.executePML(new TestUserContext("u1"), input);

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
        pap = new TestPAP();
        
        pap.executePML(new TestUserContext("u1"), input);

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
        pap = new TestPAP();
        
        pap.executePML(new TestUserContext("u1"), input);

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
        pap = new TestPAP();
        
        pap.executePML(new TestUserContext("u1"), input);

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
        pap = new TestPAP();
        
        pap.executePML(new TestUserContext("u1"), input);

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
        PAP pap = new TestPAP();
        
        pap.executePML(new TestUserContext("u1"), input);

        assertTrue(pap.query().graph().nodeExists("pc1"));

        String input1 = """
                operation testFunc(any x) {
                    create policy class x
                }
                
                testFunc(["pc1"])
                """;

        
        PAP pap1 = new TestPAP();
        assertThrows(ClassCastException.class, () -> pap1.executePML(new TestUserContext("u1"), input1));

        PAP pap2 = new TestPAP();
        String input2 = """
                x := "hello"
                operation testFunc() {
                    create policy class x + " world"
                }
                
                testFunc()
                """;

        PMLCompilationException e = assertThrows(
                PMLCompilationException.class,
                () -> pap2.executePML(new TestUserContext("u1"), input2)
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
         PAP pap = new TestPAP();
        
        pap.executePML(new TestUserContext("u1"), input);
        assertTrue(pap.query().graph().getPolicyClasses().contains(id("v1")));
    }

    @Test
    void testArrayWithLiteral() throws PMException {
        String input = """
                set resource operations ["read", "write"]
                """;
         PAP pap = new TestPAP();
        
        pap.executePML(new TestUserContext("u1"), input);
        assertTrue(pap.query().operations().getResourceOperations().contains("read"));

        String input1 = """
                set resource operations [["read", "write"], ["exec"]]
                """;
        assertThrows(PMException.class, () -> pap.executePML(new TestUserContext("u1"), input1));
    }

    @Test
    void testDeleteNonExistentNode() throws PMException {
        String input = """
                delete node "pc1"
                """;
         PAP pap = new TestPAP();
        
        assertDoesNotThrow(() -> pap.executePML(new TestUserContext("u1"), input));
    }

    @Test
    void testDeleteProhibition() throws PMException {
         PAP pap = new TestPAP();
        
        pap.modify().operations().setResourceOperations(new AccessRightSet("read"));
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));

        String input = """
                create prohibition "p1"
                deny user attribute "ua1"
                access rights ["read"]
                on union of ["oa1"]
                
                create prohibition "p2"
                deny user attribute "ua1"
                access rights ["read"]
                on union of ["oa1"]
                """;
        pap.executePML(new TestUserContext("u1"), input);

        input = """
                delete prohibition "p1"
                """;
        pap.executePML(new TestUserContext("u1"), input);
        assertTrue(pap.query().prohibitions().getProhibitions().stream().filter(p -> p.getName().equals("p1")).toList().isEmpty());
    }

    @Test
    void testReturnValue() throws PMException {
        String pml = """
                operation testFunc(string s) string {
                    return s
                }
                
                create policy class testFunc("test")
                """;

         PAP pap = new TestPAP();
        pap.deserialize(new TestUserContext("u1"), pml, new PMLDeserializer());

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

         PAP pap = new TestPAP();
        pap.deserialize(new TestUserContext("u1"), pml, new PMLDeserializer());

        assertFalse(pap.query().graph().nodeExists("test"));
        assertTrue(pap.query().graph().nodeExists("test2"));
    }
}
