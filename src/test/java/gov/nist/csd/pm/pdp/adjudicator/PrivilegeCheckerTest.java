package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.pap.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.query.UserContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrivilegeCheckerTest {

    static PAP pap;

    @BeforeAll
    static void setup() throws PMException {
        pap = new MemoryPAP();
        pap.deserialize(
                new UserContext("u1"),
                """
                        set resource operations ["read", "write"]
                        
                        create policy class "pc1"
                            create ua "ua1" in ["pc1"]
                            create ua "ua2" in ["pc1"]

                            create oa "oa1" in ["pc1"]
                            create oa "oa2" in ["pc1"]

                            associate "ua1" and "oa1" with ["read", "write"]
                            associate "ua1" and ADMIN_POLICY_OBJECT with ["read"]
                      
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
                     () -> PrivilegeChecker.check(pap, new UserContext("u3"), "o1", "read"));
        assertThrows(NodeDoesNotExistException.class,
                     () -> PrivilegeChecker.check(pap, new UserContext("u1"), "o2", "read"));
    }

    @Test
    void testCheckNodeIsPC() {
        assertDoesNotThrow(() -> PrivilegeChecker.check(pap, new UserContext("u1"), "pc1", "read"));
    }

    @Test
    void testAuthorize() {
        assertDoesNotThrow(() -> PrivilegeChecker.check(pap, new UserContext("u1"), "o1", "read"));
    }

    @Test
    void testUnauthorized() {
        assertThrows(PMException.class,
                     () -> PrivilegeChecker.check(pap, new UserContext("u2"), "o1", "read"));
    }

    @Test
    void testEmptyAccessRights() {
        assertDoesNotThrow(() -> PrivilegeChecker.check(pap, new UserContext("u1"), "o1"));
    }

}