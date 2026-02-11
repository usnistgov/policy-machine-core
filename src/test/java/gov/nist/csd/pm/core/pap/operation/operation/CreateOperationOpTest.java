package gov.nist.csd.pm.core.pap.operation.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnNode;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.operation.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;

class CreateOperationOpTest {

    private static AdminOperation<Void> testOpWithReqCapOnNode(String nodeName) {
        return new AdminOperation<>(
            "dummy_op",
            VOID_TYPE,
            List.of(),
            new RequiredCapability(new RequiredPrivilegeOnNode(nodeName, AdminAccessRight.ADMIN_GRAPH_NODE_CREATE))
        ) {
            @Override
            public Void execute(PAP pap, Args args) throws PMException {
                return null;
            }
        };
    }

    private static AdminOperation<Void> testOpWithNoReqCaps() {
        return new AdminOperation<>(
            "dummy_op_no_reqcaps",
            VOID_TYPE,
            List.of(),
            List.of()
        ) {
            @Override
            public Void execute(PAP pap, Args args) throws PMException {
                return null;
            }
        };
    }

    @Test
    void testMetadata() {
        CreateOperationOp op = new CreateOperationOp();
        assertEquals("create_operation", op.getName());
        assertEquals(VOID_TYPE, op.getReturnType());
        assertEquals(1, op.getFormalParameters().size());
        assertEquals("operation", op.getFormalParameters().get(0).getName());
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
                associate "ua1" and PM_ADMIN_BASE_OA with ["admin:operation:create", "admin:access:query"]
                create u "u1" in ["ua1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        AdminOperation<Void> testOp = testOpWithReqCapOnNode(AdminPolicyNode.PM_ADMIN_BASE_OA.nodeName());

        CreateOperationOp op = new CreateOperationOp();
        Args args = new Args();
        args.put(CreateOperationOp.OPERATION_PARAM, testOp);
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
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        AdminOperation<Void> testOp = testOpWithReqCapOnNode(AdminPolicyNode.PM_ADMIN_BASE_OA.nodeName());

        CreateOperationOp op = new CreateOperationOp();
        Args args = new Args();
        args.put(CreateOperationOp.OPERATION_PARAM, testOp);
        assertThrows(UnauthorizedException.class, () -> op.canExecute(pap, new UserContext(id("u2")), args));
    }

    @Test
    void testCanExecuteWhenUserLacksAccessQuery() throws PMException {
        MemoryPAP pap = new TestPAP();
        // User has admin:operation:create but NOT admin:access:query
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and PM_ADMIN_BASE_OA with ["admin:operation:create"]
                create u "u1" in ["ua1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        AdminOperation<Void> testOp = testOpWithReqCapOnNode(AdminPolicyNode.PM_ADMIN_BASE_OA.nodeName());

        CreateOperationOp op = new CreateOperationOp();
        Args args = new Args();
        args.put(CreateOperationOp.OPERATION_PARAM, testOp);
        assertThrows(UnauthorizedException.class, () -> op.canExecute(pap, new UserContext(id("u1")), args));
    }

    @Test
    void testCanExecuteWithOperationHavingNoReqCaps() throws PMException {
        MemoryPAP pap = new TestPAP();
        String pml = """
                set resource access rights ["read"]
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create oa "oa1" in ["pc1"]
                associate "ua1" and PM_ADMIN_BASE_OA with ["admin:operation:create"]
                create u "u1" in ["ua1"]
                """;
        pap.executePML(new UserContext(id("u1")), pml);

        AdminOperation<Void> testOp = testOpWithNoReqCaps();

        CreateOperationOp op = new CreateOperationOp();
        Args args = new Args();
        args.put(CreateOperationOp.OPERATION_PARAM, testOp);
        // No RequiredPrivilegeOnNode entries, so the loop is skipped; only super.canExecute is checked
        op.canExecute(pap, new UserContext(id("u1")), args);
    }
}
