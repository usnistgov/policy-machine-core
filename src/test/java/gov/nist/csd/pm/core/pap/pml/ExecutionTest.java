package gov.nist.csd.pm.core.pap.pml;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static gov.nist.csd.pm.core.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.graph.Association;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.Collection;
import org.junit.jupiter.api.Test;

public class ExecutionTest {

    @Test
    void testGraphPML() throws PMException {
        PAP pap = new TestPAP();

        String input =
                """
                set resource access rights ["read", "write"]
                
                create PC "pc1"
                
                set properties of "pc1" to {"k": "v"}
                
                create OA "oa1" in ["pc1"]
                create OA "oa2" in ["pc1"]
                create OA "oa3" in ["pc1"]
                
                var descendants = ["oa1", "oa2", "oa3"]
                create O "o1" in descendants
                
                create UA "ua1" in ["pc1"]
                create UA "ua2" in ["pc1"]
                create UA "ua3" in ["pc1"]
                
                var username = "u1"
                create U username in ["ua1"]
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
                if x == y {
                    create PC "pc1"
                }
                """;
        pap.executePML(new TestUserContext("u1"), input);
        assertTrue(pap.query().graph().nodeExists("pc1"));

        input = """
                var x = "test"
                var y = "test"
                var z = "test1"
                if x == z {
                    create PC "pc1"
                } else if x == y {
                    create PC "pc2"
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
                if x == z {
                    create PC "pc1"
                } else if x == y {
                    create PC "pc2"
                } else {
                    create PC "pc3"
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
                if x == y {
                    create PC "pc1"
                } else {
                    create PC "pc2"
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
                if x != y {
                    create PC "pc1"
                } else {
                    create PC "pc2"
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
                    create PC x
                }
                """;
        pap.executePML(new TestUserContext("u1"), input);

        assertTrue(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("pc2"));
        assertTrue(pap.query().graph().nodeExists("pc3"));

        input = """
                var m = {"k1": "pc1", "k2": "pc2", "k3": "pc3"}
                foreach x, y in m {
                    create PC y
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
                        create PC z
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
                        create PC z
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
                        create PC z
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
                    if a == b {
                        a = "test2"
                        continue
                    }
                    
                    create PC x
                }
                """;
        pap = new TestPAP();
        
        pap.executePML(new TestUserContext("u1"), input);

        assertFalse(pap.query().graph().nodeExists("pc1"));
        assertTrue(pap.query().graph().nodeExists("pc2"));
        assertTrue(pap.query().graph().nodeExists("pc3"));
    }

    @Test
    void testOperation() throws PMException {
        String input = """
                adminop testFunc(any x) {
                    create PC x
                }
                
                testFunc("pc1")
                """;
        PAP pap = new TestPAP();
        
        pap.executePML(new TestUserContext("u1"), input);

        assertTrue(pap.query().graph().nodeExists("pc1"));

        String input1 = """
                adminop testFunc(any x) {
                    create ua "ua1" in x
                }
                
                create pc "pc1"
                testFunc("pc1")
                """;

        
        PAP pap1 = new TestPAP();
        assertThrows(IllegalArgumentException.class, () -> pap1.executePML(new TestUserContext("u1"), input1));

        PAP pap2 = new TestPAP();
        String input2 = """
                x := "hello"
                adminop testFunc() {
                    create PC x + " world"
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
                create PC x
                """;
         PAP pap = new TestPAP();
        
        pap.executePML(new TestUserContext("u1"), input);
        assertTrue(pap.query().graph().getPolicyClasses().contains(id("v1")));
    }

    @Test
    void testArrayWithLiteral() throws PMException {
        String input = """
                set resource access rights ["read", "write"]
                """;
         PAP pap = new TestPAP();
        
        pap.executePML(new TestUserContext("u1"), input);
        assertTrue(pap.query().operations().getResourceAccessRights().contains("read"));

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
        
        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read"));
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));

        String input = """
                create conj node prohibition "p1"
                deny "ua1"
                arset ["read"]
                include ["oa1"]
                
                create conj node prohibition "p2"
                deny "ua1"
                arset ["read"]
                include ["oa1"]
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
                adminop testFunc(string s) string {
                    return s
                }
                
                create PC testFunc("test")
                """;

         PAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), pml);

        assertTrue(pap.query().graph().nodeExists("test"));
    }

    @Test
    void testOverwriteOperationArg() throws PMException {
        String pml = """
                adminop testFunc(string s) string {
                    s = "test2"
                    return s
                }
                
                create PC testFunc("test")
                """;

         PAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), pml);

        assertFalse(pap.query().graph().nodeExists("test"));
        assertTrue(pap.query().graph().nodeExists("test2"));
    }
}
