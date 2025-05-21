package gov.nist.csd.pm.pdp.modification;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.UnauthorizedException;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static gov.nist.csd.pm.util.TestIdGenerator.id;
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
                associate "ua1" and "ua2" with ["*a"]
                
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                create o "o1" in ["oa1"]
                """);

        pdp = new PDP(pap);
        epp = new EPP(pdp, pap);
	    epp.subscribeTo(pdp);

        testEventProcessor = new TestEventSubscriber();
        pdp.addEventSubscriber(testEventProcessor);

        ok = new ProhibitionsModificationAdjudicator(new TestUserContext("u1"), pap);
        fail = new ProhibitionsModificationAdjudicator(new UserContext(id("u2")), pap);
    }

    @Test
    void createProhibition() throws PMException {
        assertDoesNotThrow(() -> ok.createProhibition(
                "pro1",
                new ProhibitionSubject(id("u2")),
		        new AccessRightSet("assign"),
		        true,
		        List.of(new ContainerCondition(id("oa1"), false))));

        assertFalse(pap.query().prohibitions().getProhibitions().stream().filter(p -> {
	        return p.getSubject().getNodeId() == id("u2");
        }).toList().isEmpty());

        assertDoesNotThrow(() -> ok.createProhibition(
                "pro2",
                new ProhibitionSubject("123"),
		        new AccessRightSet("assign"),
		        true,
		        List.of(new ContainerCondition(id("oa1"), true))));

	    assertFalse(pap.query().prohibitions().getProhibitions().stream()
			    .filter(p -> Objects.equals(p.getSubject().getProcess(), "123")).toList().isEmpty());

        assertThrows(UnauthorizedException.class, () -> fail.createProhibition(
                "pro1",
                new ProhibitionSubject(id("u2")),
		        new AccessRightSet("assign"),
		        true,
		        List.of(new ContainerCondition(id("oa1"), false))));

        assertThrows(UnauthorizedException.class, () -> fail.createProhibition(
                "pro1",
                new ProhibitionSubject("123"),
		        new AccessRightSet("assign"),
		        true,
		        List.of(new ContainerCondition(id("oa1"), true))));
    }

    @Test
    void deleteProhibition() throws PMException {
        ok.createProhibition(
                "pro1",
                new ProhibitionSubject(id("u2")),
		        new AccessRightSet("assign"),
		        true,
		        List.of(new ContainerCondition(id("oa1"), false)));

        assertThrows(UnauthorizedException.class, () -> fail.deleteProhibition("pro1"));
        assertDoesNotThrow(() -> ok.deleteProhibition("pro1"));

	    assertTrue(pap.query().prohibitions().getProhibitions().stream().filter(p -> {
		    return p.getSubject().getNodeId() == id("u2");
	    }).toList().isEmpty());
    }
}