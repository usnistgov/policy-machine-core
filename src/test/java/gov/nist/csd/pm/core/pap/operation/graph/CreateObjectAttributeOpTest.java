package gov.nist.csd.pm.core.pap.operation.graph;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.*;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CreateObjectAttributeOpTest {

    @Test
    void testMetadata() {
        CreateObjectAttributeOp op = new CreateObjectAttributeOp();
        assertEquals("create_object_attribute", op.getName());
        assertEquals(LONG_TYPE, op.getReturnType());
        assertEquals(2, op.getFormalParameters().size());
        assertEquals("name", op.getFormalParameters().get(0).getName());
        assertEquals("descendants", op.getFormalParameters().get(1).getName());
        assertNotNull(op.getRequiredCapabilities());
        assertFalse(op.getRequiredCapabilities().isEmpty());
    }

    @Test
    void testIsSatisfiedWhenAuthorized() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and "oa1" with ["admin:graph:node:create"]
                create u "u1" in ["ua1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        CreateObjectAttributeOp op = new CreateObjectAttributeOp();
        Args args = op.validateAndPrepareArgs(Map.of(
                "name", "oa2",
                "descendants", List.of(id("oa1"))
        ));
        assertTrue(op.getRequiredCapabilities().get(0).isSatisfied(pap, new UserContext(id("u1")), args));
    }

    @Test
    void testIsSatisfiedWhenUnauthorized() throws PMException {
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
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        CreateObjectAttributeOp op = new CreateObjectAttributeOp();
        Args args = op.validateAndPrepareArgs(Map.of(
                "name", "oa2",
                "descendants", List.of(id("oa1"))
        ));
        assertFalse(op.getRequiredCapabilities().get(0).isSatisfied(pap, new UserContext(id("u2")), args));
    }
}
