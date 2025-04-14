package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.UnauthorizedException;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.*;

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
                associate "ua1" and PM_ADMIN_OBJECT with ["*a"]
                
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
    void setResourceOperations() throws PMException {
        assertDoesNotThrow(() -> ok.setResourceOperations(new AccessRightSet("read")));
        assertEquals(new AccessRightSet("read"), pap.query().operations().getResourceOperations());
        assertThrows(UnauthorizedException.class, () -> fail.setResourceOperations(new AccessRightSet("read")));
    }

    @Test
    void createAdminOperation() throws PMException {
        Operation<Void, ?> op1 = new Operation<>("op1", List.of()) {
            @Override
            public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {

            }

            @Override
            public Void execute(PAP pap, Args actualArgs) throws PMException {
                return null;
            }

            @Override
            protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
                return new Args(argsMap);
            }
        };

        assertDoesNotThrow(() -> ok.createAdminOperation(op1));
        assertTrue(pap.query().operations().getAdminOperationNames().contains("op1"));
        assertThrows(UnauthorizedException.class, () -> fail.createAdminOperation(op1));
    }

    @Test
    void deleteAdminOperation() throws PMException {
        Operation<Void, Args> op1 = new Operation<>("op1", List.of()) {
            @Override
            public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {

            }

            @Override
            public Void execute(PAP pap, Args actualArgs) throws PMException {
                return null;
            }

            @Override
            protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
                return new Args(argsMap);
            }
        };
        ok.createAdminOperation(op1);

        assertDoesNotThrow(() -> ok.deleteAdminOperation("op1"));
        assertThrows(UnauthorizedException.class, () -> fail.deleteAdminOperation("op1"));
    }
}