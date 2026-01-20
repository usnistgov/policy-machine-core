package gov.nist.csd.pm.core.pap.pml.operation.admin;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.operation.ResourceOperation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class PMLStmtsResourceOperationTest {

    @Test
    void testNonPMLResourceOpInvokedInPML() throws PMException {
        MemoryPAP memoryPAP = new MemoryPAP();
        memoryPAP.modify().operations().setResourceAccessRights(new AccessRightSet("read"));
        memoryPAP.modify().operations().createOperation(new ResourceOperation<>("op1", STRING_TYPE, List.of()) {
            @Override
            protected String execute(PolicyQuery query, Args args) throws PMException {
                return "test";
            }
        });

        memoryPAP.executePML(new TestUserContext("u1"), """
            t := op1()
            create pc t
            """);

        assertTrue(memoryPAP.query().graph().nodeExists("test"));
    }

    @Test
    void testPMLResourceOpInvokedInPML() throws PMException {
        MemoryPAP memoryPAP = new MemoryPAP();
        memoryPAP.executePML(new TestUserContext("u1"), """
            set resource access rights ["read"]
            
            resourceop op1() string {
                return "test"
            }
            
            t := op1()
            create pc t
            """);

        assertTrue(memoryPAP.query().graph().nodeExists("test"));
    }

    @Test
    void testPMLResourceOpCanOnlyCallQueryOrBasicFunctions() throws PMException {
        MemoryPAP memoryPAP = new MemoryPAP();
        Object actual = memoryPAP.executePML(new TestUserContext("u1"), """
            set resource access rights ["read"]
            
            create pc "a"
            create pc "b"
            
            resourceop op1([]string a) map[string]any {
                if contains(a, "a") {
                    return getNode("a")
                }
                
                return {}
            }
            
            return op1(["a", "b"])
            
            """);

        Map<String, Object> actualMap = (Map<String, Object>) actual;
        assertEquals("a", actualMap.get("name"));
        assertEquals("PC", actualMap.get("type"));
        assertEquals(new HashMap<>(), new HashMap<>((Map) actualMap.get("properties")));
    }

}