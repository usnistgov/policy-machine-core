package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.obligation.EventPattern;
import gov.nist.csd.pm.pap.obligation.Response;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ObligationsModificationAdjudicatorTest {

    PAP pap;
    PDP pdp;
    EPP epp;

    TestEventProcessor testEventProcessor;
    ObligationsModificationAdjudicator ok;
    ObligationsModificationAdjudicator fail;


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
                associate "ua1" and ADMIN_POLICY_OBJECT with ["*a"]
                
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                create o "o1" in ["oa1"]
                """);

        pdp = new PDP(pap);
        epp = new EPP(pdp, pap);

        testEventProcessor = new TestEventProcessor();
        pdp.addEventListener(testEventProcessor);

        ok = new ObligationsModificationAdjudicator(new UserContext("u1"), pap, pdp);
        fail = new ObligationsModificationAdjudicator(new UserContext("u2"), pap, pdp);
    }


    @Test
    void createObligation() {
        assertDoesNotThrow(() -> ok.createObligation("u1", "name", List.of(
                new Rule(
                        "rule1",
                        new EventPattern(new SubjectPattern(), new OperationPattern(), Map.of()),
                        new Response("e", List.of())
                )
        )));
        assertThrows(UnauthorizedException.class, () -> fail.createObligation("u1", "name", List.of(
                new Rule(
                        "rule1",
                        new EventPattern(new SubjectPattern(), new OperationPattern(), Map.of()),
                        new Response("e", List.of())
                )
        )));
    }

    @Test
    void deleteObligation() throws PMException {
        ok.createObligation("u1", "test", List.of(
                new Rule(
                        "rule1",
                        new EventPattern(new SubjectPattern(), new OperationPattern(), Map.of()),
                        new Response("e", List.of())
                )
        ));
        ok.createObligation("u1", "test2", List.of(
                new Rule(
                        "rule1",
                        new EventPattern(new SubjectPattern(), new OperationPattern(), Map.of()),
                        new Response("e", List.of())
                )
        ));
        assertDoesNotThrow(() -> ok.deleteObligation("test"));
        assertFalse(pap.query().prohibitions().getProhibitions().containsKey("test"));
        assertThrows(UnauthorizedException.class, () -> fail.deleteObligation("test2"));
    }
}