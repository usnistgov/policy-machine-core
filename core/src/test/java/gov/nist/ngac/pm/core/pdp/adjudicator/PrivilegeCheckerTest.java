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

package gov.nist.ngac.pm.core.pdp.adjudicator;

import static gov.nist.ngac.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.ngac.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

class PrivilegeCheckerTest {

    static TestPAP pap;

    @BeforeAll
    static void setup() throws PMException {
        pap = new TestPAP();
        pap.executePML(
                NodeUserContext.of("u1"),
                """
                        set resource access rights ["read", "write"]

                        create PC "pc1"
                            create ua "ua1" in ["pc1"]
                            create ua "ua2" in ["pc1"]

                            create oa "oa1" in ["pc1"]
                            create oa "oa2" in ["pc1"]

                            associate "ua1" to "oa1" with ["read", "write"]
                            associate "ua1" to PM_ADMIN_BASE_OA with ["read"]

                        create U "u1" in ["ua1"]
                        create U "u2" in ["ua2"]

                        create O "o1" in ["oa1"]
                        """
        );
    }

    @Test
    void testCheckUserAndTargetDoesNotExist() throws PMException {
        assertThrows(NodeDoesNotExistException.class,
                     () -> pap.check(NodeUserContext.of(-99), id("o1"), List.of("read")));
        assertThrows(NodeDoesNotExistException.class,
                     () -> pap.check(NodeUserContext.of( "u1"), id("o2"), List.of("read")));
    }

    @Test
    void testCheckNodeIsPC() {
        assertDoesNotThrow(() -> pap.check(NodeUserContext.of("u1"), id("pc1"), List.of("read")));
    }

    @Test
    void testAuthorize() {
        assertDoesNotThrow(() -> pap.check(NodeUserContext.of("u1"), id("o1"), List.of("read")));
    }

    @Test
    void testUnauthorized() {
        assertThrows(PMException.class,
                     () -> pap.check(NodeUserContext.of(id("u2")), id("o1"), List.of("read")));
    }

    @Test
    void testEmptyAccessRights() {
        assertDoesNotThrow(() -> pap.check(NodeUserContext.of("u1"), id("o1"), List.of()));
    }

}
