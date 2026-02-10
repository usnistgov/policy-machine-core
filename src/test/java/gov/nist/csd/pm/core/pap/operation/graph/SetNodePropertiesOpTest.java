package gov.nist.csd.pm.core.pap.operation.graph;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SetNodePropertiesOpTest {

    @Test
    void testMetadata() {
        SetNodePropertiesOp op = new SetNodePropertiesOp();
        assertEquals("set_node_properties", op.getName());
        assertEquals(VOID_TYPE, op.getReturnType());
        assertEquals(2, op.getFormalParameters().size());
        assertEquals("id", op.getFormalParameters().get(0).getName());
        assertEquals("properties", op.getFormalParameters().get(1).getName());
        assertNotNull(op.getRequiredCapabilities());
        assertFalse(op.getRequiredCapabilities().isEmpty());
    }

    @Test
    void testCanExecuteWhenAuthorized() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["admin:graph:node:update"]
                create u "u1" in ["ua1"]
                create o "o1" in ["oa1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        SetNodePropertiesOp op = new SetNodePropertiesOp();
        Args args = op.validateAndPrepareArgs(Map.of(
                "id", id("o1"),
                "properties", Map.of("key", "value")
        ));
        op.canExecute(pap, new UserContext(id("u1")), args);
    }

    @Test
    void testCanExecuteWhenUnauthorized() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["read"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                create o "o1" in ["oa1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        SetNodePropertiesOp op = new SetNodePropertiesOp();
        Args args = op.validateAndPrepareArgs(Map.of(
                "id", id("o1"),
                "properties", Map.of("key", "value")
        ));
        assertThrows(UnauthorizedException.class, () -> op.canExecute(pap, new UserContext(id("u2")), args));
    }
}
