package gov.nist.csd.pm.core.example;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.ResourceOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnNodeId;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnParameter;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class JavaExample {

    @Test
    void testJavaExample() throws PMException {
        PAP pap = new MemoryPAP();

        // set resource access rights
        pap.modify().operations().setResourceAccessRights(new AccessRightSet("read", "write"));

        // create initial graph config
        long pc1Id = pap.modify().graph().createPolicyClass("pc1");
        long usersId = pap.modify().graph().createUserAttribute("users", List.of(pc1Id));
        long adminId = pap.modify().graph().createUserAttribute("admin", List.of(pc1Id));
        pap.modify().graph().createUser("admin_user", List.of(adminId));
        pap.modify().graph().associate(adminId, usersId, new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_CREATE));

        long userHomes = pap.modify().graph().createObjectAttribute("user homes", List.of(pc1Id));
        long userInboxes = pap.modify().graph().createObjectAttribute("user inboxes", List.of(pc1Id));
        pap.modify().graph().associate(adminId, userHomes, AccessRightSet.wildcard());
        pap.modify().graph().associate(adminId, userInboxes, AccessRightSet.wildcard());

        // prohibit the admin user from reading inboxes
        pap.modify().prohibitions().createNodeProhibition(
            "deny admin on user inboxes",
            adminId,
            new AccessRightSet("read"),
            Set.of(userInboxes),
            Set.of(),
            false
        );

        // create resource operation to read a file
        NodeNameFormalParameter nameFormalParameter = new NodeNameFormalParameter("name");
        ResourceOperation<Void> resourceOp = new ResourceOperation<>("read_file", VOID_TYPE, List.of(nameFormalParameter),
            List.of(new RequiredCapability(new RequiredPrivilegeOnParameter(nameFormalParameter, new AccessRightSet("read"))))) {
            @Override
            public Void execute(PolicyQuery query, Args args) throws PMException {
                return null;
            }
        };
        pap.modify().operations().createOperation(resourceOp);

        /*
        (policyQuery, userCtx, args) -> policyQuery.access()
                .computePrivileges(userCtx, new TargetContext(usersId))
                .contains(AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_CREATE.toString())))
         */

        // create a custom administration operation
        FormalParameter<String> usernameParam = new FormalParameter<>("username", STRING_TYPE);
        AdminOperation<?> adminOp = new AdminOperation<>("create_new_user", VOID_TYPE, List.of(usernameParam),
            List.of(new RequiredCapability(new RequiredPrivilegeOnNodeId(usersId, AdminAccessRight.ADMIN_GRAPH_ASSIGNMENT_DESCENDANT_CREATE)))) {

            @Override
            public Void execute(PAP pap, Args args) throws PMException {
                String username = args.get(usernameParam);

                pap.modify().graph().createUser(username, List.of(usersId));
                pap.modify().graph().createObjectAttribute(username + " home", List.of(userHomes));
                pap.modify().graph().createObjectAttribute(username + " inbox", List.of(userInboxes));
                return null;
            }
        };
        pap.modify().operations().createOperation(adminOp);

        // - create an obligation on the custom admin operation that when ever a user is created, add an object to their
        // inbox titled "hello " + username
        // - obligations require the use of PML to define responses, so they may be serialized
        // - obligations require an author which we will set as the admin user since they are allowed to perform the
        // operations in the response
        String pml = """
            create obligation "o1"
            when any user
            performs "create_new_user"
            do(ctx) {
                objName := "welcome " + ctx.args.username
                inboxName := ctx.args.username + " inbox"
                create o objName in [inboxName]
            }
            """;
        pap.executePML(new UserContext(adminId), pml);

        // create a PDP to run transactions
        PDP pdp = new PDP(pap);

        // create an EPP to process events in the EPP and matching obligation responses
        EPP epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        // adjudicate the admin operation which will cause the EPP to execute the above obligation response
        pdp.adjudicateOperation(new UserContext(adminId), "create_new_user", Map.of("username", "testUser"));

        // check admin operation and obligation response was successful
        assertTrue(pap.query().graph().nodeExists("testUser home"));
        assertTrue(pap.query().graph().nodeExists("testUser inbox"));
        assertTrue(pap.query().graph().nodeExists("welcome testUser"));

        // try to execute the operation as the new testUser, expect unauthorized error
        long testUserId = pap.query().graph().getNodeId("testUser");
        assertThrows(
            UnauthorizedException.class,
            () -> pdp.adjudicateOperation(new UserContext(testUserId), "create_new_user", Map.of("username", "testUser2"))
        );
    }
}
