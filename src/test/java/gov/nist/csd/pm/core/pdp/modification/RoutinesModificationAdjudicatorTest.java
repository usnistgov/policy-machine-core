package gov.nist.csd.pm.core.pdp.modification;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.*;

class RoutinesModificationAdjudicatorTest {

    PAP pap;
    PDP pdp;
    EPP epp;

    TestEventSubscriber testEventProcessor;
    RoutinesModificationAdjudicator ok;
    RoutinesModificationAdjudicator fail;


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
        epp.subscribeTo(pdp);

        testEventProcessor = new TestEventSubscriber();
        pdp.addEventSubscriber(testEventProcessor);

        ok = new RoutinesModificationAdjudicator(new TestUserContext("u1"), pap);
        fail = new RoutinesModificationAdjudicator(new UserContext(id("u2")), pap);
    }


    @Test
    void createAdminRoutine() throws PMException {
        Routine<?, ?> routine1 = new Routine<>("routine1", List.of()) {
            @Override
            public Void execute(PAP pap, Args actualArgs) throws PMException {
                return null;
            }

            @Override
            protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
                return new Args(argsMap);
            }
        };

        assertDoesNotThrow(() -> ok.createAdminRoutine(routine1));
        assertTrue(pap.query().routines().getAdminRoutineNames().contains("routine1"));
        assertThrows(UnauthorizedException.class, () -> fail.createAdminRoutine(routine1));
    }

    @Test
    void deleteAdminRoutine() throws PMException {
        Routine<? ,?> routine1 = new Routine<>("routine1", List.of()) {
            @Override
            public Void execute(PAP pap, Args actualArgs) throws PMException {
                return null;
            }

            @Override
            protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
                return new Args(argsMap);
            }
        };
        ok.createAdminRoutine(routine1);

        assertDoesNotThrow(() -> ok.deleteAdminRoutine("routine1"));
        assertThrows(UnauthorizedException.class, () -> fail.deleteAdminRoutine("routine1"));
    }
}