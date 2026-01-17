package gov.nist.csd.pm.core.pdp.modification;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.VOID_TYPE;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OperationsModificationAdjudicatorTest {

    PAP pap;
    PDP pdp;
    EPP epp;

    TestEventSubscriber testEventProcessor;
    OperationsModificationAdjudicator ok;
    OperationsModificationAdjudicator fail;


    @BeforeEach
    void setup() throws PMException {
        pap = new TestPAP();

        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"

                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]

                create oa "oa1" in ["pc1"]
                
                associate "ua1" and "oa1" with ["*a"]
                associate "ua1" and PM_ADMIN_BASE_OA with ["*a"]
                
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                create o "o1" in ["oa1"]
                """);

        pdp = new PDP(pap);
        epp = new EPP(pdp, pap);
        epp.subscribeTo(pdp);

        testEventProcessor = new TestEventSubscriber();
        pdp.addEventSubscriber(testEventProcessor);

        ok = new OperationsModificationAdjudicator(new TestUserContext("u1"), pap);
        fail = new OperationsModificationAdjudicator(new UserContext(id("u2")), pap);
    }


    @Test
    void setResourceAccessRights() throws PMException {
        assertDoesNotThrow(() -> ok.setResourceAccessRights(new AccessRightSet("read")));
        assertEquals(new AccessRightSet("read"), pap.query().operations().getResourceAccessRights());
        assertThrows(UnauthorizedException.class, () -> fail.setResourceAccessRights(new AccessRightSet("read")));
    }

    @Test
    void createAdminOperation() throws PMException {
        AdminOperation<Void> op1 = new AdminOperation<>("op1", VOID_TYPE, List.of()) {
            @Override
            public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {

            }

            @Override
            public Void execute(PAP pap, Args actualArgs) throws PMException {
                return null;
            }

        };

        assertDoesNotThrow(() -> ok.createAdminOperation(op1));
        assertTrue(pap.query().operations().getAdminOperationNames().contains("op1"));
        assertThrows(UnauthorizedException.class, () -> fail.createAdminOperation(op1));
    }

    @Test
    void deleteAdminOperation() throws PMException {
        AdminOperation<Void> op1 = new AdminOperation<>("op1", VOID_TYPE, List.of()) {
            @Override
            public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {

            }

            @Override
            public Void execute(PAP pap, Args actualArgs) throws PMException {
                return null;
            }

        };
        ok.createAdminOperation(op1);

        assertDoesNotThrow(() -> ok.deleteAdminOperation("op1"));
        assertThrows(UnauthorizedException.class, () -> fail.deleteAdminOperation("op1"));
    }
}