package gov.nist.csd.pm.core.pap.operation.prohibition;

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
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CreateNodeProhibitionOpTest {

    @Test
    void testMetadata() {
        CreateNodeProhibitionOp op = new CreateNodeProhibitionOp();
        assertEquals("create_node_prohibition", op.getName());
        assertEquals(VOID_TYPE, op.getReturnType());
        assertEquals(6, op.getFormalParameters().size());
        assertEquals("name", op.getFormalParameters().get(0).getName());
        assertEquals("node_id", op.getFormalParameters().get(1).getName());
        assertEquals("arset", op.getFormalParameters().get(2).getName());
        assertEquals("inclusion_set", op.getFormalParameters().get(3).getName());
        assertEquals("exclusion_set", op.getFormalParameters().get(4).getName());
        assertEquals("is_conjunctive", op.getFormalParameters().get(5).getName());
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
                create ua "ua2" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" to "ua2" with ["admin:prohibition:node:create"]
                associate "ua1" to "oa1" with ["admin:prohibition:inclusion:create"]
                create u "u1" in ["ua1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        CreateNodeProhibitionOp op = new CreateNodeProhibitionOp();
        Args args = op.validateAndPrepareArgs(Map.of(
                "name", "pro1",
                "node_id", id("ua2"),
                "arset", List.of("read"),
                "inclusion_set", List.of(id("oa1")),
                "exclusion_set", List.of(),
                "is_conjunctive", false
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
                associate "ua1" to "oa1" with ["read"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        CreateNodeProhibitionOp op = new CreateNodeProhibitionOp();
        Args args = op.validateAndPrepareArgs(Map.of(
                "name", "pro1",
                "node_id", id("ua2"),
                "arset", List.of("read"),
                "inclusion_set", List.of(id("oa1")),
                "exclusion_set", List.of(),
                "is_conjunctive", false
        ));
        assertThrows(UnauthorizedException.class, () -> op.canExecute(pap, new UserContext(id("u2")), args));
    }
}
