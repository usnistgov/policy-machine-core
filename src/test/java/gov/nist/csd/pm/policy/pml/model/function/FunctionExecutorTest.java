package gov.nist.csd.pm.policy.pml.model.function;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FunctionExecutorTest {

    @Test
    void testSerialization() throws PMException, PMLScopeException {
        FunctionExecutor fe = (ctx, policy) -> {
            policy.graph().createPolicyClass("pc1");
            policy.graph().createUserAttribute(ctx.scope().getValue("test").getStringValue(), "pc1");
            return new Value();
        };

        byte[] serialized = SerializationUtils.serialize(fe);
        FunctionExecutor deserialized = SerializationUtils.deserialize(serialized);

        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        ExecutionContext execCtx = new ExecutionContext(new UserContext("u1"));
        execCtx.scope().putValue("test", new Value("test"));
        deserialized.exec(execCtx, memoryPolicyStore);

        assertTrue(memoryPolicyStore.graph().nodeExists("pc1"));
        assertTrue(memoryPolicyStore.graph().nodeExists("test"));

        FunctionDefinitionStatement functionDefinitionStatement = new FunctionDefinitionStatement(
                "123",
                Type.string(),
                List.of(),
                (ctx, policy) -> {
                    policy.graph().createPolicyClass("pc1");
                    policy.graph().createUserAttribute(ctx.scope().getValue("test").getStringValue(), "pc1");
                    return new Value();
                }
        );
        serialized = SerializationUtils.serialize(functionDefinitionStatement);
        FunctionDefinitionStatement deserializedFunc = SerializationUtils.deserialize(serialized);
        memoryPolicyStore = new MemoryPolicyStore();

        deserializedFunc.getFunctionExecutor().exec(execCtx, memoryPolicyStore);

        assertTrue(memoryPolicyStore.graph().nodeExists("pc1"));
        assertTrue(memoryPolicyStore.graph().nodeExists("test"));
    }

}