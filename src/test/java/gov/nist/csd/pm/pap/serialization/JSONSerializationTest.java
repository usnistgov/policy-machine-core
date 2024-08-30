package gov.nist.csd.pm.pap.serialization;

import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.serialization.json.JSONDeserializer;
import gov.nist.csd.pm.pap.serialization.json.JSONGraph;
import gov.nist.csd.pm.pap.serialization.json.JSONPolicy;
import gov.nist.csd.pm.pap.serialization.json.JSONSerializer;
import gov.nist.csd.pm.util.SamplePolicy;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class JSONSerializationTest {

    @Test
    void testJSONSerializationDoesNotThrowNPE() throws PMException, IOException {
        List<JSONPolicy> policies = List.of(
                new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), List.of(), List.of(), List.of()),
                new JSONPolicy(null, new JSONGraph(), List.of(), List.of(), List.of(), List.of()),
                new JSONPolicy(new AccessRightSet(), null, List.of(), List.of(), List.of(), List.of()),
                new JSONPolicy(new AccessRightSet(), new JSONGraph(), null, List.of(), List.of(), List.of()),
                new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), null, List.of(), List.of()),
                new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), List.of(), null, List.of()),
                new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), List.of(), List.of(), null),
                new JSONPolicy(new AccessRightSet(), new JSONGraph(List.of(), List.of(), List.of(), List.of(), List.of()), List.of(), List.of(), List.of(), List.of()),
                new JSONPolicy(new AccessRightSet(), new JSONGraph(null, List.of(), List.of(), List.of(), List.of()), List.of(), List.of(), List.of(), List.of()),
                new JSONPolicy(new AccessRightSet(), new JSONGraph(List.of(), null, List.of(), List.of(), List.of()), List.of(), List.of(), List.of(), List.of()),
                new JSONPolicy(new AccessRightSet(), new JSONGraph(List.of(), List.of(), null, List.of(), List.of()), List.of(), List.of(), List.of(), List.of()),
                new JSONPolicy(new AccessRightSet(), new JSONGraph(List.of(), List.of(), List.of(), null, List.of()), List.of(), List.of(), List.of(), List.of()),
                new JSONPolicy(new AccessRightSet(), new JSONGraph(List.of(), List.of(), List.of(), List.of(), null), List.of(), List.of(), List.of(), List.of())
        );

        for (JSONPolicy policy : policies) {
            assertDoesNotThrow(() -> new MemoryPAP().deserialize(new UserContext(), policy.toString(), new JSONDeserializer()));
        }
    }

}
