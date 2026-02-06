package gov.nist.csd.pm.core.pap.operation.graph;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.*;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DeleteNodeOpTest {

    @Test
    void testMetadata() {
        DeleteNodeOp op = new DeleteNodeOp();
        assertEquals("delete_node", op.getName());
        assertEquals(VOID_TYPE, op.getReturnType());
        assertEquals(3, op.getFormalParameters().size());
        assertEquals("id", op.getFormalParameters().get(0).getName());
        assertEquals("type", op.getFormalParameters().get(1).getName());
        assertEquals("descendants", op.getFormalParameters().get(2).getName());
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
                associate "ua1" and "oa1" with ["admin:graph:node:delete", "admin:graph:assignment:descendant:delete"]
                create u "u1" in ["ua1"]
                create o "o1" in ["oa1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        DeleteNodeOp op = new DeleteNodeOp();
        Args args = op.validateAndPrepareArgs(Map.of(
                "id", id("o1"),
                "type", "O",
                "descendants", List.of(id("oa1"))
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

        DeleteNodeOp op = new DeleteNodeOp();
        Args args = op.validateAndPrepareArgs(Map.of(
                "id", id("o1"),
                "type", "O",
                "descendants", List.of(id("oa1"))
        ));
        assertThrows(UnauthorizedException.class, () -> op.canExecute(pap, new UserContext(id("u2")), args));
    }
}
