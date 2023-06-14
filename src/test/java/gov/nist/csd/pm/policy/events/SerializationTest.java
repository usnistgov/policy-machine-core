package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.events.graph.CreatePolicyClassEvent;
import gov.nist.csd.pm.policy.events.obligations.CreateObligationEvent;
import gov.nist.csd.pm.policy.events.prohibitions.CreateProhibitionEvent;
import gov.nist.csd.pm.policy.events.userdefinedpml.CreateFunctionEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.expression.VariableReference;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.CreatePolicyStatement;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;

import java.io.NotSerializableException;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
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
                "label",
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
                                        EventSubject.anyUser(),
                                        new Performs("test_event")
                                ),
                                new Response(
                                        new UserContext(SUPER_USER),
                                        new CreatePolicyStatement(new Expression(new VariableReference("test_pc", Type.string())))
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
        CreateFunctionEvent createFunctionEvent = new CreateFunctionEvent(new FunctionDefinitionStatement(
                "test_func",
                Type.string(),
                List.of(new FormalArgument("arg1", Type.string())),
                (ctx, policy) -> new Value("hello world")
        ));

        createFunctionEvent.apply(memoryPolicyStore);

        assertThrows(SerializationException.class, () -> memoryPolicyStore.serialize().toJSON());
    }

}
