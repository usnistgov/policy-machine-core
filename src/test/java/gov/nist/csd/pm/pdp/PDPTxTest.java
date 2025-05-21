package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.serialization.json.JSONSerializer;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PDPTxTest {

    @Test
    void testReset() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                associate "ua1" and PM_ADMIN_OBJECT with ["*a"]
                """);
        PDPTx u2 = new PDPTx(new UserContext(id("u2")), pap, List.of());
        assertThrows(UnauthorizedException.class, u2::reset);

        PDPTx u1 = new PDPTx(new TestUserContext("u1"), pap, List.of());
        assertDoesNotThrow(u1::reset);
    }

    @Test
    void testSerialize() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                associate "ua1" and PM_ADMIN_OBJECT with ["*a"]
                """);
        PDPTx u2 = new PDPTx(new UserContext(id("u2")), pap, List.of());
        assertThrows(UnauthorizedException.class, () -> u2.serialize(new JSONSerializer()));

        PDPTx u1 = new PDPTx(new TestUserContext("u1"), pap, List.of());
        assertDoesNotThrow(() -> u1.serialize(new JSONSerializer()));
    }

    @Test
    void testDeserialize() throws PMException {
        PAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                associate "ua1" and PM_ADMIN_OBJECT with ["*a"]
                """);

        String serialize = "create pc \"test\"";

        PDPTx u2 = new PDPTx(new UserContext(id("u2")), pap, List.of());
        assertThrows(UnauthorizedException.class, () -> u2.executePML(serialize));

        PDPTx u1 = new PDPTx(new TestUserContext("u1"), pap, List.of());
        assertDoesNotThrow(() -> u1.executePML(serialize));
    }
}