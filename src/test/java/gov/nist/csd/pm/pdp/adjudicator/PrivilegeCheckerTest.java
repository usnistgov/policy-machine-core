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
                "set resource operations [\"read\", \"write\"]\n" +
                        "                        \n" +
                        "                        create policy class \"pc1\"\n" +
                        "                            create ua \"ua1\" in [\"pc1\"]\n" +
                        "                            create ua \"ua2\" in [\"pc1\"]\n" +
                        "\n" +
                        "                            create oa \"oa1\" in [\"pc1\"]\n" +
                        "                            create oa \"oa2\" in [\"pc1\"]\n" +
                        "\n" +
                        "                            associate \"ua1\" and \"oa1\" with [\"read\", \"write\"]\n" +
                        "                            associate \"ua1\" and PM_ADMIN_OBJECT with [\"read\"]\n" +
                        "                      \n" +
                        "                        create user \"u1\" in [\"ua1\"]\n" +
                        "                        create user \"u2\" in [\"ua2\"]\n" +
                        "                        \n" +
                        "                        create object \"o1\" in [\"oa1\"]",
                        new PMLDeserializer()
        );
    }

    @Test
    void testCheckUserAndTargetDoesNotExist() throws PMException {
        assertThrows(NodeDoesNotExistException.class,
                     () -> new PrivilegeChecker(pap).check(new UserContext("u3"), "o1", "read"));
        assertThrows(NodeDoesNotExistException.class,
                     () -> new PrivilegeChecker(pap).check(new UserContext("u1"), "o2", "read"));
    }

    @Test
    void testCheckNodeIsPC() {
        assertDoesNotThrow(() -> new PrivilegeChecker(pap).check(new UserContext("u1"), "pc1", "read"));
    }

    @Test
    void testAuthorize() {
        assertDoesNotThrow(() -> new PrivilegeChecker(pap).check(new UserContext("u1"), "o1", "read"));
    }

    @Test
    void testUnauthorized() {
        assertThrows(PMException.class,
                     () -> new PrivilegeChecker(pap).check(new UserContext("u2"), "o1", "read"));
    }

    @Test
    void testEmptyAccessRights() {
        assertDoesNotThrow(() -> new PrivilegeChecker(pap).check(new UserContext("u1"), "o1"));
    }

}