package gov.nist.csd.pm.core.pap.pml.operation.basic.builtin;

import static gov.nist.csd.pm.core.pap.pml.operation.basic.PMLFunctionOperation.NODE_NAME_PARAM;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.accessrights.AccessRightSet;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.modification.GraphModification;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.pml.operation.builtin.GetAssociationsWithSource;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class GetAssociationsWithSourceTest {

    @Test
    void testOk() throws PMException {
        PAP pap = new MemoryPAP();
        GraphModification graph = pap.modify().graph();
        long pc1 = graph.createPolicyClass("pc1");
        long ua1 = graph.createUserAttribute("ua1", List.of(pc1));
        long oa1 = graph.createObjectAttribute("oa1", List.of(pc1));
        graph.associate(ua1, oa1, new AccessRightSet("*"));

        GetAssociationsWithSource getAssociationsWithSource = new GetAssociationsWithSource();
        List<Map<String, Object>> result = getAssociationsWithSource.execute(pap.query(), new Args(Map.of(NODE_NAME_PARAM, "ua1")));

        assertEquals(1, result.size());
        assertEquals(Map.of("ua", "ua1", "target", "oa1", "arset", List.of("*")), result.getFirst());
    }

    @Test
    void testWithPML() throws PMException {
        String pml = """
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create ua "ua2" in ["pc1"]
            create oa "oa1" in ["pc1"]
            associate "ua1" and "oa1" with ["*"]
            
            assocs := getAssociationsWithSource("ua1")
            
            foreach assoc in assocs {
                associate "ua2" and "oa1" with assoc.arset
            }
           
            """;
        MemoryPAP pap = new TestPAP();

        pap.executePML(new UserContext(0), pml);

        assertEquals(2, pap.query().graph().getAssociationsWithTarget(id("oa1")).size());
    }

}