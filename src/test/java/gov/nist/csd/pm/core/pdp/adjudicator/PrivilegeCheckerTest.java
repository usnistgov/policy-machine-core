package gov.nist.csd.pm.core.pdp.adjudicator;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrivilegeCheckerTest {

    static PAP pap;

    @BeforeAll
    static void setup() throws PMException {
        pap = new TestPAP();
        pap.executePML(
                new TestUserContext("u1"),
                """
                        set resource operations ["read", "write"]
                        
                        create policy class "pc1"
                            create ua "ua1" in ["pc1"]
                            create ua "ua2" in ["pc1"]

                            create oa "oa1" in ["pc1"]
                            create oa "oa2" in ["pc1"]

                            associate "ua1" and "oa1" with ["read", "write"]
                            associate "ua1" and PM_ADMIN_OBJECT with ["read"]
                      
                        create user "u1" in ["ua1"]
                        create user "u2" in ["ua2"]
                        
                        create object "o1" in ["oa1"]
                        """
        );
    }

    @Test
    void testCheckUserAndTargetDoesNotExist() throws PMException {
        assertThrows(NodeDoesNotExistException.class,
                     () -> pap.privilegeChecker().check(new UserContext(-3), id("o1"), List.of("read")));
        assertThrows(NodeDoesNotExistException.class,
                     () -> pap.privilegeChecker().check(new TestUserContext( "u1"), id("o2"), List.of("read")));
    }

    @Test
    void testCheckNodeIsPC() {
        assertDoesNotThrow(() -> pap.privilegeChecker().check(new TestUserContext("u1"), id("pc1"), List.of("read")));
    }

    @Test
    void testAuthorize() {
        assertDoesNotThrow(() -> pap.privilegeChecker().check(new TestUserContext("u1"), id("o1"), List.of("read")));
    }

    @Test
    void testUnauthorized() {
        assertThrows(PMException.class,
                     () -> pap.privilegeChecker().check(new UserContext(id("u2")), id("o1"), List.of("read")));
    }

    @Test
    void testEmptyAccessRights() {
        assertDoesNotThrow(() -> pap.privilegeChecker().check(new TestUserContext("u1"), id("o1"), List.of()));
    }

}