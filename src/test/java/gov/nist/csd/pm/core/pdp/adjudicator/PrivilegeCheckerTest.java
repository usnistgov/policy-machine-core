package gov.nist.csd.pm.core.pdp.adjudicator;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PrivilegeCheckerTest {

    static TestPAP pap;

    @BeforeAll
    static void setup() throws PMException {
        pap = new TestPAP();
        pap.executePML(
                new TestUserContext("u1"),
                """
                        set resource access rights ["read", "write"]

                        create PC "pc1"
                            create ua "ua1" in ["pc1"]
                            create ua "ua2" in ["pc1"]

                            create oa "oa1" in ["pc1"]
                            create oa "oa2" in ["pc1"]

                            associate "ua1" and "oa1" with ["read", "write"]
                            associate "ua1" and PM_ADMIN_BASE_OA with ["read"]

                        create U "u1" in ["ua1"]
                        create U "u2" in ["ua2"]

                        create O "o1" in ["oa1"]
                        """
        );
    }

    @Test
    void testCheckUserAndTargetDoesNotExist() throws PMException {
        assertThrows(NodeDoesNotExistException.class,
                     () -> pap.check(new UserContext(-99), id("o1"), List.of("read")));
        assertThrows(NodeDoesNotExistException.class,
                     () -> pap.check(new TestUserContext( "u1"), id("o2"), List.of("read")));
    }

    @Test
    void testCheckNodeIsPC() {
        assertDoesNotThrow(() -> pap.check(new TestUserContext("u1"), id("pc1"), List.of("read")));
    }

    @Test
    void testAuthorize() {
        assertDoesNotThrow(() -> pap.check(new TestUserContext("u1"), id("o1"), List.of("read")));
    }

    @Test
    void testUnauthorized() {
        assertThrows(PMException.class,
                     () -> pap.check(new UserContext(id("u2")), id("o1"), List.of("read")));
    }

    @Test
    void testEmptyAccessRights() {
        assertDoesNotThrow(() -> pap.check(new TestUserContext("u1"), id("o1"), List.of()));
    }

}
