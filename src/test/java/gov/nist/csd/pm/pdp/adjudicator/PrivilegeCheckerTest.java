package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrivilegeCheckerTest {

    static PAP pap;

    @BeforeAll
    static void setup() throws PMException {
        pap = new TestPAP();
        pap.deserialize(
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
                        """,
                        new PMLDeserializer()
        );
    }

    @Test
    void testCheckUserAndTargetDoesNotExist() throws PMException {
        assertThrows(NodeDoesNotExistException.class,
                     () -> new PrivilegeChecker(pap).check(new UserContext(-3), "o1", List.of("read")));
        assertThrows(NodeDoesNotExistException.class,
                     () -> new PrivilegeChecker(pap).check(new TestUserContext( "u1"), "o2", List.of("read")));
    }

    @Test
    void testCheckNodeIsPC() {
        assertDoesNotThrow(() -> new PrivilegeChecker(pap).check(new TestUserContext("u1"), "pc1", List.of("read")));
    }

    @Test
    void testAuthorize() {
        assertDoesNotThrow(() -> new PrivilegeChecker(pap).check(new TestUserContext("u1"), "o1", List.of("read")));
    }

    @Test
    void testUnauthorized() {
        assertThrows(PMException.class,
                     () -> new PrivilegeChecker(pap).check(new UserContext(id("u2")), "o1", List.of("read")));
    }

    @Test
    void testEmptyAccessRights() {
        assertDoesNotThrow(() -> new PrivilegeChecker(pap).check(new TestUserContext("u1"), "o1", List.of()));
    }

}