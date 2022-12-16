package gov.nist.csd.pm.policy.events;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PolicyEventHandlerTest {

    @Test
    void testPolicyEventHandler() {
        PolicyEventHandler handler = new PolicyEventHandler(new CreatePolicyClassEvent("pc1", noprops()));
        AtomicBoolean ok = new AtomicBoolean(false);
        handler.onCreatePolicyClassEvent(e -> {
                    ok.set(true);
                })
                .onAssignEvent(e -> {
                    ok.set(false);
                });
        assertTrue(ok.get());
    }

}