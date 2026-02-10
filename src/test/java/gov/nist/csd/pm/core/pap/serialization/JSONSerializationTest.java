package gov.nist.csd.pm.core.pap.serialization;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.serialization.json.JSONDeserializer;
import gov.nist.csd.pm.core.pap.serialization.json.JSONGraph;
import gov.nist.csd.pm.core.pap.serialization.json.JSONOperations;
import gov.nist.csd.pm.core.pap.serialization.json.JSONPolicy;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class JSONSerializationTest {

    @Test
    void testJSONSerializationDoesNotThrowNPE() throws PMException, IOException {
        List<JSONPolicy> policies = List.of(
            new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), List.of(), new JSONOperations()),
            new JSONPolicy(null, new JSONGraph(), List.of(), List.of(), new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), null, List.of(), List.of(), new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(), null, List.of(),new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), null, new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), List.of(), null),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(List.of(), List.of(), List.of(), List.of(), List.of()), List.of(), List.of(), new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(null, List.of(), List.of(), List.of(), List.of()), List.of(), List.of(), new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(List.of(), null, List.of(), List.of(), List.of()), List.of(), List.of(), new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(List.of(), List.of(), null, List.of(), List.of()), List.of(), List.of(), new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(List.of(), List.of(), List.of(), null, List.of()), List.of(), List.of(), new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(List.of(), List.of(), List.of(), List.of(), null), List.of(), List.of(), new JSONOperations()),

            new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), List.of(), new JSONOperations(null, List.of(), List.of(), List.of(), List.of())),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), List.of(), new JSONOperations(List.of(), null, List.of(), List.of(), List.of())),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), List.of(), new JSONOperations(List.of(), List.of(), null, List.of(), List.of())),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), List.of(), new JSONOperations(List.of(), List.of(), List.of(), null, List.of())),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), List.of(), new JSONOperations(List.of(), List.of(), List.of(), List.of(), null))
        );

        for (JSONPolicy policy : policies) {
            assertDoesNotThrow(() -> new MemoryPAP().deserialize(policy.toString(), new JSONDeserializer()));
        }
    }

}
