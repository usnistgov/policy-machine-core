package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.common.routine.Routine;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.UnauthorizedException;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;


import static gov.nist.csd.pm.util.TestIdGenerator.id;
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
                associate "ua1" and PM_ADMIN_OBJECT with ["*a"]
                
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                create o "o1" in ["oa1"]
                """);

        pdp = new PDP(pap);
        epp = new EPP(pdp, pap);

        testEventProcessor = new TestEventSubscriber();
        pdp.addEventSubscriber(testEventProcessor);

        ok = new RoutinesModificationAdjudicator(new TestUserContext("u1"), pap, pdp.getPrivilegeChecker());
        fail = new RoutinesModificationAdjudicator(new UserContext(id("u2")), pap, pdp.getPrivilegeChecker());
    }


    @Test
    void createAdminRoutine() throws PMException {
        Routine<Void> routine1 = new Routine<>("routine1", List.of()) {
            @Override
            public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
                return null;
            }
        };

        assertDoesNotThrow(() -> ok.createAdminRoutine(routine1));
        assertTrue(pap.query().routines().getAdminRoutineNames().contains("routine1"));
        assertThrows(UnauthorizedException.class, () -> fail.createAdminRoutine(routine1));
    }

    @Test
    void deleteAdminRoutine() throws PMException {
        Routine<Void> routine1 = new Routine<>("routine1", List.of()) {
            @Override
            public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
                return null;
            }
        };
        ok.createAdminRoutine(routine1);

        assertDoesNotThrow(() -> ok.deleteAdminRoutine("routine1"));
        assertThrows(UnauthorizedException.class, () -> fail.deleteAdminRoutine("routine1"));
    }
}