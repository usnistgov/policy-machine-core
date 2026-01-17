package gov.nist.csd.pm.core.pdp.modification;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.AnyOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.response.PMLObligationResponse;
import gov.nist.csd.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        ok = new ObligationsModificationAdjudicator(new TestUserContext("u1"), pap);
        fail = new ObligationsModificationAdjudicator(new UserContext(id("u2")), pap);
    }


    @Test
    void createObligation() {
        assertDoesNotThrow(() -> ok.createObligation(id("u1"), "name",
                        new EventPattern(new SubjectPattern(), new AnyOperationPattern()),
                        new PMLObligationResponse("e", List.of())

        ));
        assertThrows(UnauthorizedException.class, () -> fail.createObligation(id("u1"), "name1",
                        new EventPattern(new SubjectPattern(), new AnyOperationPattern()),
                        new PMLObligationResponse("e", List.of())
        ));
    }

    @Test
    void deleteObligation() throws PMException {
        ok.createObligation(id("u1"), "test",
                        new EventPattern(new SubjectPattern(), new AnyOperationPattern()),
                        new PMLObligationResponse("e", List.of())
        );
        ok.createObligation(id("u1"), "test2",
                        new EventPattern(new SubjectPattern(), new AnyOperationPattern()),
                        new PMLObligationResponse("e", List.of())
        );
        assertDoesNotThrow(() -> ok.deleteObligation("test"));
        assertThrows(UnauthorizedException.class, () -> fail.deleteObligation("test2"));
    }
}