package gov.nist.csd.pm.core.pap.operation.prohibition;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.*;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DeleteProhibitionOpTest {

    @Test
    void testMetadata() {
        DeleteProhibitionOp op = new DeleteProhibitionOp();
        assertEquals("delete_prohibition", op.getName());
        assertEquals(VOID_TYPE, op.getReturnType());
        assertEquals(1, op.getFormalParameters().size());
        assertEquals("name", op.getFormalParameters().get(0).getName());
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
                create ua "ua2" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and "ua2" with ["admin:prohibition:subject:delete"]
                associate "ua1" and "oa1" with ["admin:prohibition:inclusion:delete"]
                create u "u1" in ["ua1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        // Create a prohibition first so delete can look it up
        pap.modify().prohibitions().createProhibition(
                "pro1",
                new ProhibitionSubject(id("ua2")),
                new AccessRightSet("read"),
                false,
                List.of(new ContainerCondition(id("oa1"), false))
        );

        DeleteProhibitionOp op = new DeleteProhibitionOp();
        Args args = op.validateAndPrepareArgs(Map.of("name", "pro1"));
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

        // Create a prohibition first so delete can look it up
        pap.modify().prohibitions().createProhibition(
                "pro1",
                new ProhibitionSubject(id("ua2")),
                new AccessRightSet("read"),
                false,
                List.of(new ContainerCondition(id("oa1"), false))
        );

        DeleteProhibitionOp op = new DeleteProhibitionOp();
        Args args = op.validateAndPrepareArgs(Map.of("name", "pro1"));
        assertFalse(op.getRequiredCapabilities().get(0).isSatisfied(pap, new UserContext(id("u2")), args));
    }
}
