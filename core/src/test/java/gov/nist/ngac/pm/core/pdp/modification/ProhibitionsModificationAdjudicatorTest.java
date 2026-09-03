/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pdp.modification;

import static gov.nist.ngac.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.prohibition.NodeProhibition;
import gov.nist.ngac.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.ngac.pm.core.epp.EPP;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pdp.PDP;
import gov.nist.ngac.pm.core.pdp.UnauthorizedException;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

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

        pap.executePML(NodeUserContext.of("u1"), """
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

        ok = new ProhibitionsModificationAdjudicator(NodeUserContext.of("u1"), pap);
        fail = new ProhibitionsModificationAdjudicator(NodeUserContext.of(id("u2")), pap);
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
