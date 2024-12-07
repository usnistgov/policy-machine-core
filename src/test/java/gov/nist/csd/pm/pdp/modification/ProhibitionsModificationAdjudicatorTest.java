package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.op.prohibition.CreateProhibitionOp;
import gov.nist.csd.pm.common.op.prohibition.DeleteProhibitionOp;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.common.op.Operation.NAME_OPERAND;
import static gov.nist.csd.pm.common.op.prohibition.ProhibitionOp.*;
import static org.junit.jupiter.api.Assertions.*;

class ProhibitionsModificationAdjudicatorTest {

    PAP pap;
    PDP pdp;
    EPP epp;

    TestEventSubscriber testEventProcessor;
    ProhibitionsModificationAdjudicator ok;
    ProhibitionsModificationAdjudicator fail;

    @BeforeEach
    void setup() throws PMException {
        pap = new MemoryPAP();

        pap.executePML(new UserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                
                associate "ua1" and "oa1" with ["*a"]
                associate "ua1" and "oa2" with ["*a"]
                associate "ua1" and PM_ADMIN_OBJECT with ["*a"]
                associate "ua1" and "ua2" with ["*a"]
                
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                create o "o1" in ["oa1"]
                """);

        pdp = new PDP(pap);
        epp = new EPP(pdp, pap);

        testEventProcessor = new TestEventSubscriber();
        pdp.addEventSubscriber(testEventProcessor);

        ok = new ProhibitionsModificationAdjudicator(new UserContext("u1"), pap, pdp, pdp.getPrivilegeChecker());
        fail = new ProhibitionsModificationAdjudicator(new UserContext("u2"), pap, pdp, pdp.getPrivilegeChecker());
    }

    @Test
    void createProhibition() throws PMException {
        assertDoesNotThrow(() -> ok.createProhibition(
                "pro1",
                new ProhibitionSubject("u2", ProhibitionSubject.Type.USER),
                new AccessRightSet("assign"),
                true,
                List.of(new ContainerCondition("oa1", false))
        ));
        assertEquals(
                new EventContext(
                        "u1",
                        new CreateProhibitionOp(),
                        Map.of(
                                NAME_OPERAND, "pro1",
                                SUBJECT_OPERAND, new ProhibitionSubject("u2", ProhibitionSubject.Type.USER),
                                ARSET_OPERAND, new AccessRightSet("assign"),
                                INTERSECTION_OPERAND, true,
                                CONTAINERS_OPERAND, List.of(new ContainerCondition("oa1", false))
                        )
                ),
                testEventProcessor.getEventContext()
        );

        assertTrue(pap.query().prohibitions().getProhibitions().containsKey("u2"));

        assertDoesNotThrow(() -> ok.createProhibition(
                "pro2",
                new ProhibitionSubject("123", ProhibitionSubject.Type.PROCESS),
                new AccessRightSet("assign"),
                true,
                List.of(new ContainerCondition("oa1", true))
        ));
        assertEquals(
                new EventContext(
                        "u1",
                        new CreateProhibitionOp(),
                        Map.of(
                                NAME_OPERAND, "pro2",
                                SUBJECT_OPERAND, new ProhibitionSubject("123", ProhibitionSubject.Type.PROCESS),
                                ARSET_OPERAND, new AccessRightSet("assign"),
                                INTERSECTION_OPERAND, true,
                                CONTAINERS_OPERAND, List.of(new ContainerCondition("oa1", true))
                        )
                ),
                testEventProcessor.getEventContext()
        );

        assertTrue(pap.query().prohibitions().getProhibitions().containsKey("123"));


        assertThrows(UnauthorizedException.class, () -> fail.createProhibition(
                "pro1",
                new ProhibitionSubject("u2", ProhibitionSubject.Type.USER),
                new AccessRightSet("assign"),
                true,
                List.of(new ContainerCondition("oa1", false))
        ));

        assertThrows(UnauthorizedException.class, () -> fail.createProhibition(
                "pro1",
                new ProhibitionSubject("123", ProhibitionSubject.Type.PROCESS),
                new AccessRightSet("assign"),
                true,
                List.of(new ContainerCondition("oa1", true))
        ));
    }

    @Test
    void deleteProhibition() throws PMException {
        ok.createProhibition(
                "pro1",
                new ProhibitionSubject("u2", ProhibitionSubject.Type.USER),
                new AccessRightSet("assign"),
                true,
                List.of(new ContainerCondition("oa1", false))
        );

        assertThrows(UnauthorizedException.class, () -> fail.deleteProhibition("pro1"));
        assertDoesNotThrow(() -> ok.deleteProhibition("pro1"));

        assertEquals(
                new EventContext(
                        "u1",
                        new DeleteProhibitionOp(),
                        Map.of(
                                NAME_OPERAND, "pro1",
                                SUBJECT_OPERAND, new ProhibitionSubject("u2", ProhibitionSubject.Type.USER),
                                ARSET_OPERAND, new AccessRightSet("assign"),
                                INTERSECTION_OPERAND, true,
                                CONTAINERS_OPERAND, List.of(new ContainerCondition("oa1", false))
                        )
                ),
                testEventProcessor.getEventContext()
        );

        assertFalse(pap.query().prohibitions().getProhibitions().containsKey("u2"));
    }
}