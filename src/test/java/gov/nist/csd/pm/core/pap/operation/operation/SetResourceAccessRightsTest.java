package gov.nist.csd.pm.core.pap.operation.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.*;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SetResourceAccessRightsTest {

    @Test
    void testMetadata() {
        SetResourceAccessRights op = new SetResourceAccessRights();
        assertEquals("set_resource_access_rights", op.getName());
        assertEquals(VOID_TYPE, op.getReturnType());
        assertEquals(1, op.getFormalParameters().size());
        assertEquals("arset", op.getFormalParameters().get(0).getName());
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
                associate "ua1" and PM_ADMIN_OBJECT with ["admin:policy:resource_access_rights:update"]
                create u "u1" in ["ua1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        SetResourceAccessRights op = new SetResourceAccessRights();
        Args args = new Args();
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

        SetResourceAccessRights op = new SetResourceAccessRights();
        Args args = new Args();
        assertFalse(op.getRequiredCapabilities().get(0).isSatisfied(pap, new UserContext(id("u2")), args));
    }
}
