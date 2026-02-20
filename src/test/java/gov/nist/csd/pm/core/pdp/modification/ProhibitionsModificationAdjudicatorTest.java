package gov.nist.csd.pm.core.pdp.modification;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.prohibition.NodeProhibition;
import gov.nist.csd.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

                associate "ua1" to "oa1" with ["admin:*"]
                associate "ua1" to "oa2" with ["admin:*"]
                associate "ua1" to PM_ADMIN_BASE_OA with ["admin:*"]
                associate "ua1" to "ua2" with ["admin:*"]

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
    void createNodeProhibition() throws PMException {
        assertDoesNotThrow(() -> ok.createNodeProhibition(
                "pro1",
                id("u2"),
		        new AccessRightSet("admin:graph:assignment:ascendant:create"),
		        Set.of(id("oa1")),
		        Set.of(),
		        true));

        assertFalse(pap.query().prohibitions().getProhibitions().stream().filter(p -> {
	        return p instanceof NodeProhibition np && np.getNodeId() == id("u2");
        }).toList().isEmpty());

        assertDoesNotThrow(() -> ok.createProcessProhibition(
                "pro2",
                id("u2"),
                "123",
		        new AccessRightSet("admin:graph:assignment:ascendant:create"),
		        Set.of(id("oa1")),
		        Set.of(),
		        true));

	    assertFalse(pap.query().prohibitions().getProhibitions().stream()
			    .filter(p -> p instanceof ProcessProhibition pp && Objects.equals(pp.getProcess(), "123")).toList().isEmpty());

        assertThrows(UnauthorizedException.class, () -> fail.createNodeProhibition(
                "pro3",
                id("u2"),
		        new AccessRightSet("admin:graph:assignment:ascendant:create"),
		        Set.of(id("oa1")),
		        Set.of(),
		        true));

        assertThrows(UnauthorizedException.class, () -> fail.createProcessProhibition(
                "pro4",
                id("u2"),
                "123",
		        new AccessRightSet("admin:graph:assignment:ascendant:create"),
		        Set.of(id("oa1")),
		        Set.of(),
		        true));
    }

    @Test
    void deleteProhibition() throws PMException {
        ok.createNodeProhibition(
                "pro1",
                id("u2"),
		        new AccessRightSet("admin:graph:assignment:ascendant:create"),
		        Set.of(id("oa1")),
		        Set.of(),
		        true);

        assertThrows(UnauthorizedException.class, () -> fail.deleteProhibition("pro1"));
        assertDoesNotThrow(() -> ok.deleteProhibition("pro1"));

	    assertTrue(pap.query().prohibitions().getProhibitions().stream().filter(p -> {
		    return p instanceof NodeProhibition np && np.getNodeId() == id("u2");
	    }).toList().isEmpty());
    }
}
