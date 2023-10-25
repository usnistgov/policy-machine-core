package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.serialization.pml.PMLSerializer;
import gov.nist.csd.pm.policy.events.graph.CreatePolicyClassEvent;
import gov.nist.csd.pm.policy.events.obligations.CreateObligationEvent;
import gov.nist.csd.pm.policy.events.prohibitions.CreateProhibitionEvent;
import gov.nist.csd.pm.policy.events.userdefinedpml.CreateFunctionEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.subject.AnyUserSubject;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.CreatePolicyStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SerializationTest {

    @Test
    void testGraphEvent() {
        CreatePolicyClassEvent expected = new CreatePolicyClassEvent("pc1", Map.of("a", "b"));
        byte[] serialize = SerializationUtils.serialize(expected);
        CreatePolicyClassEvent actual = SerializationUtils.deserialize(serialize);
        assertEquals(expected, actual);
    }

    @Test
    void testProhibitionEvent() {
        CreateProhibitionEvent expected = new CreateProhibitionEvent(
                "pro1",
                new ProhibitionSubject("u1", ProhibitionSubject.Type.USER),
                new AccessRightSet("read"),
                false,
                List.of(new ContainerCondition("oa1", true), new ContainerCondition("oa2", false))
        );
        byte[] serialize = SerializationUtils.serialize(expected);
        CreateProhibitionEvent actual = SerializationUtils.deserialize(serialize);
        assertEquals(expected, actual);
    }

    @Test
    void testObligationEvent() {
        CreateObligationEvent expected = new CreateObligationEvent(
                new UserContext("u1"),
                "obl",
                List.of(
                        new Rule(
                                "rule1",
                                new EventPattern(
                                        new AnyUserSubject(),
                                        new Performs("test_event")
                                ),
                                new Response(
                                        new UserContext("u1"),
                                        new CreatePolicyStatement(new StringLiteral("test_pc"))
                                )
                        )
                )
        );
        byte[] serialize = SerializationUtils.serialize(expected);
        CreateObligationEvent actual = SerializationUtils.deserialize(serialize);
        assertEquals(expected, actual);
    }

    @Test
    void testFuncExecDoestNotSerialize() throws PMException {
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        CreateFunctionEvent createFunctionEvent = new CreateFunctionEvent(new FunctionDefinitionStatement.Builder("test_func")
                                                                                  .returns(Type.string())
                                                                                  .args(
                                                                                          new FormalArgument("arg1", Type.string())
                                                                                  )
                                                                                  .executor((ctx, policy) -> new StringValue("hello world"))
                                                                                  .build());

        createFunctionEvent.apply(memoryPolicyStore);

        assertThrows(RuntimeException.class, () -> memoryPolicyStore.serialize(new PMLSerializer()));
    }

}
