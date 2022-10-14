package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.pdp.memory.MemoryPDP;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.UnknownAccessRightException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.Graph.checkAccessRightsValid;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Nested
    class CheckAccessRightsValid {

        MemoryPAP pap;
        public CheckAccessRightsValid() throws PMException {
            pap = new MemoryPAP();
            pap.graph.setResourceAccessRights(new AccessRightSet("r", "w"));
        }

        @Test
        void testInvalidWithSpecialAAR() throws PMException {
            assertThrows(UnknownAccessRightException.class,
                    () -> checkAccessRightsValid(pap.graph, new AccessRightSet(ALL_ACCESS_RIGHTS, "e")));
        }

        @Test
        void testValidWithSpecialAAR() {
            assertDoesNotThrow(() -> checkAccessRightsValid(pap.graph, new AccessRightSet(ALL_ACCESS_RIGHTS, "w")));
        }

        @Test
        void testInvalidWithSpecialAAAR() throws PMException {
            assertThrows(UnknownAccessRightException.class,
                    () -> checkAccessRightsValid(pap.graph, new AccessRightSet(ALL_ADMIN_ACCESS_RIGHTS, "e")));
        }

        @Test
        void testValidWithSpecialAAAR() {
            assertDoesNotThrow(() -> checkAccessRightsValid(pap.graph, new AccessRightSet(ALL_ADMIN_ACCESS_RIGHTS, "w")));
        }

        @Test
        void testInvalidWithSpecialARAR() throws PMException {
            assertThrows(UnknownAccessRightException.class,
                    () -> checkAccessRightsValid(pap.graph, new AccessRightSet(ALL_RESOURCE_ACCESS_RIGHTS, "e")));
        }

        @Test
        void testValidWithSpecialARAR() {
            assertDoesNotThrow(() -> checkAccessRightsValid(pap.graph, new AccessRightSet(ALL_RESOURCE_ACCESS_RIGHTS, "w")));
        }

        @Test
        void testInvalidWithAdminAccessRight() throws PMException {
            assertThrows(UnknownAccessRightException.class,
                    () -> checkAccessRightsValid(pap.graph, new AccessRightSet(CREATE_POLICY_CLASS, "e")));
        }

        @Test
        void testValidWithAdminAccessRight() {
            assertDoesNotThrow(() -> checkAccessRightsValid(pap.graph, new AccessRightSet(CREATE_POLICY_CLASS, "w")));
        }

    }
}