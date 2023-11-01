package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.AdminPolicy;
import gov.nist.csd.pm.pap.AdminPolicyNode;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.BootstrapExistingPolicyException;
import gov.nist.csd.pm.policy.exceptions.NodeNameExistsException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.UnauthorizedException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.pml.model.exception.PMLExecutionException;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.VoidValue;
import gov.nist.csd.pm.util.SamplePolicy;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static gov.nist.csd.pm.pap.PAPTest.testAdminPolicy;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.CREATE_OBJECT_ATTRIBUTE;
import static org.junit.jupiter.api.Assertions.*;

class PDPTest {

    @Test
    void testRunTx() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        PDP pdp = new PDP(pap);

        pap.runTx((policy) -> {
            policy.graph().createPolicyClass("pc1");
            policy.graph().createUserAttribute("ua1", "pc1");
            policy.graph().createObjectAttribute("oa1", "pc1");
            policy.graph().createObjectAttribute("oa2", "pc1");
            policy.graph().createUser("u1", "ua1");
            policy.graph().createObject("o1", "oa1");
        });

        assertThrows(PMException.class, () -> pdp.runTx(new UserContext("u1"), ((policy) ->
                policy.graph().associate("ua1", "oa1", new AccessRightSet(CREATE_OBJECT_ATTRIBUTE)))));

        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("oa1"));
    }


    @Test
    void testBootstrapWithAdminPolicyOnly() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        PDP pdp = new PDP(pap);

        pdp.bootstrap(p -> {
            p.graph().createPolicyClass("pc1");
        });

        testAdminPolicy(pap, 2);
        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists(AdminPolicy.policyClassTargetName("pc1")));
    }

    @Test
    void testBootstrapWithExistingPolicyThrowsException() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        PDP pdp = new PDP(pap);
        pap.graph().createPolicyClass("pc1");
        assertThrows(BootstrapExistingPolicyException.class, () -> {
            pdp.bootstrap((policy) -> {});
        });

        pap.reset();

        pap.graph().setResourceAccessRights(new AccessRightSet("read"));
        pap.graph().createPolicyClass("pc1");
        pap.graph().createUserAttribute("ua1", "pc1");
        pap.graph().createUser("u1", "ua1");
        pap.graph().createObjectAttribute("oa1", "pc1");
        pap.graph().createObject("o1", "oa1");

        pap.prohibitions().create("pro1", new ProhibitionSubject("u1", ProhibitionSubject.Type.USER),
                                  new AccessRightSet("read"), true, new ContainerCondition("oa1", false));

        assertThrows(BootstrapExistingPolicyException.class, () -> {
            pdp.bootstrap((policy) -> {});
        });

        pap.obligations().create(new UserContext("u1"), "obl1");

        assertThrows(BootstrapExistingPolicyException.class, () -> {
            pdp.bootstrap((policy) -> {});
        });
    }

    @Test
    void testRollback() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.graph().createPolicyClass("pc1");
        pap.graph().createObjectAttribute("oa1", "pc1");
        pap.graph().createUserAttribute("ua1", "pc1");
        pap.graph().createUser("u1", "ua1");
        pap.graph().associate("ua1", AdminPolicyNode.POLICY_CLASS_TARGETS.nodeName(), new AccessRightSet("*"));

        PDP pdp = new PDP(pap);
        assertThrows(NodeNameExistsException.class, () -> {
            pdp.runTx(new UserContext("u1"), policy -> {
                policy.graph().createPolicyClass("pc2");
                // expect error and rollback
                policy.graph().createObjectAttribute("oa1", "pc2");
            });
        });

        assertTrue(pap.graph().nodeExists("pc1"));
        assertTrue(pap.graph().nodeExists("ua1"));
        assertTrue(pap.graph().nodeExists("oa1"));
        assertFalse(pap.graph().nodeExists("pc2"));
    }

    @Test
    void testExecutePML() throws PMException {
        try {
            PAP pap = new PAP(new MemoryPolicyStore());
            SamplePolicy.loadSamplePolicyFromPML(pap);

            FunctionDefinitionStatement functionDefinitionStatement = new FunctionDefinitionStatement.Builder("testfunc")
                    .returns(Type.voidType())
                    .args()
                    .executor((ctx, policy) -> {
                        policy.graph().createPolicyClass("pc3");
                        return new VoidValue();
                    })
                    .build();

            PDP pdp = new PDP(pap);
            pdp.runTx(new UserContext("u1"), policy -> {
                policy.userDefinedPML().createFunction(functionDefinitionStatement);
                policy.executePML(new UserContext("u1"), "create ua \"ua3\" assign to [\"pc2\"]");
            });

            assertTrue(pap.graph().nodeExists("ua3"));

            PMLExecutionException e = assertThrows(PMLExecutionException.class, () -> {
                pdp.runTx(new UserContext("u1"), policy -> {
                    policy.executePML(new UserContext("u1"), "testfunc()");
                });
            });
            assertEquals(UnauthorizedException.class, e.getCause().getClass());

            assertFalse(pap.graph().nodeExists("pc3"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}