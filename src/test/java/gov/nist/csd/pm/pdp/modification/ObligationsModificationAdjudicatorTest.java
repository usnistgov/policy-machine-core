package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.obligation.EventPattern;
import gov.nist.csd.pm.pap.obligation.Response;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.UnauthorizedException;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ObligationsModificationAdjudicatorTest {

    PAP pap;
    PDP pdp;
    EPP epp;

    TestEventSubscriber testEventProcessor;
    ObligationsModificationAdjudicator ok;
    ObligationsModificationAdjudicator fail;


    @BeforeEach
    void setup() throws PMException {
        pap = new TestPAP();

        pap.executePML(new TestUserContext("u1"), """
               create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create oa "oa1" in ["pc1"]
                create oa "oa2" in ["pc1"]
                
                associate "ua1" and "oa1" with ["*a"]
                associate "ua1" and "oa2" with ["*a"]
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

        ok = new ObligationsModificationAdjudicator(new TestUserContext("u1"), pap);
        fail = new ObligationsModificationAdjudicator(new UserContext(id("u2")), pap);
    }


    @Test
    void createObligation() {
        assertDoesNotThrow(() -> ok.createObligation(id("u1"), "name", List.of(
                new Rule(
                        "rule1",
                        new EventPattern(new SubjectPattern(), new OperationPattern(), Map.of()),
                        new Response("e", List.of())
                )
        )));
        assertThrows(UnauthorizedException.class, () -> fail.createObligation(id("u1"), "name", List.of(
                new Rule(
                        "rule1",
                        new EventPattern(new SubjectPattern(), new OperationPattern(), Map.of()),
                        new Response("e", List.of())
                )
        )));
    }

    @Test
    void deleteObligation() throws PMException {
        ok.createObligation(id("u1"), "test", List.of(
                new Rule(
                        "rule1",
                        new EventPattern(new SubjectPattern(), new OperationPattern(), Map.of()),
                        new Response("e", List.of())
                )
        ));
        ok.createObligation(id("u1"), "test2", List.of(
                new Rule(
                        "rule1",
                        new EventPattern(new SubjectPattern(), new OperationPattern(), Map.of()),
                        new Response("e", List.of())
                )
        ));
        assertDoesNotThrow(() -> ok.deleteObligation("test"));
        assertThrows(UnauthorizedException.class, () -> fail.deleteObligation("test2"));
    }
}