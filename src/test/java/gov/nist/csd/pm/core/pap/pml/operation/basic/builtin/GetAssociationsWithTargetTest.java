package gov.nist.csd.pm.core.pap.pml.operation.basic.builtin;

import static gov.nist.csd.pm.core.pap.pml.operation.basic.PMLFunctionOperation.NODE_NAME_PARAM;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.modification.GraphModification;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.pml.operation.builtin.GetAssociationsWithTarget;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class GetAssociationsWithTargetTest {

    @Test
    void testOk() throws PMException {
        PAP pap = new MemoryPAP();
        GraphModification graph = pap.modify().graph();
        long pc1 = graph.createPolicyClass("pc1");
        long ua1 = graph.createUserAttribute("ua1", List.of(pc1));
        long oa1 = graph.createObjectAttribute("oa1", List.of(pc1));
        graph.associate(ua1, oa1, new AccessRightSet("*"));

        GetAssociationsWithTarget getAssociationsWithTarget = new GetAssociationsWithTarget();
        List<Map<String, Object>> result = getAssociationsWithTarget.execute(pap.query(), new Args(Map.of(NODE_NAME_PARAM, "oa1")));

        assertEquals(1, result.size());
        assertEquals(Map.of("ua", "ua1", "target", "oa1", "arset", List.of("*")), result.getFirst());
    }

}