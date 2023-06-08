package gov.nist.csd.pm.policy.events;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class SerializationTest {

    @Test
    void testSerialization() {
        CreatePolicyClassEvent createPolicyClassEvent = new CreatePolicyClassEvent("pc1", Map.of("a", "b"));
    }

}
