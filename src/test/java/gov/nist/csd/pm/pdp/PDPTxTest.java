package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.serialization.json.JSONSerializer;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.pdp.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PDPTxTest {

    @Test
    void testReset() throws PMException {
        PAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                associate "ua1" and ADMIN_POLICY_OBJECT with ["*a"]
                """);
        PDPTx u2 = new PDPTx(new UserContext("u2"), pap, List.of());
        assertThrows(UnauthorizedException.class, u2::reset);

        PDPTx u1 = new PDPTx(new UserContext("u1"), pap, List.of());
        assertDoesNotThrow(u1::reset);
    }

    @Test
    void testSerialize() throws PMException {
        PAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                associate "ua1" and ADMIN_POLICY_OBJECT with ["*a"]
                """);
        PDPTx u2 = new PDPTx(new UserContext("u2"), pap, List.of());
        assertThrows(UnauthorizedException.class, () -> u2.serialize(new JSONSerializer()));

        PDPTx u1 = new PDPTx(new UserContext("u1"), pap, List.of());
        assertDoesNotThrow(() -> u1.serialize(new JSONSerializer()));
    }

    @Test
    void testDeserialize() throws PMException {
        PAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                create ua "ua2" in ["pc1"]
                create u "u1" in ["ua1"]
                create u "u2" in ["ua2"]
                associate "ua1" and ADMIN_POLICY_OBJECT with ["*a"]
                """);

        String serialize = "create pc \"test\"";

        PDPTx u2 = new PDPTx(new UserContext("u2"), pap, List.of());
        assertThrows(UnauthorizedException.class, () -> u2.deserialize(new UserContext(), serialize, new PMLDeserializer()));

        PDPTx u1 = new PDPTx(new UserContext("u1"), pap, List.of());
        assertDoesNotThrow(() -> u1.deserialize(new UserContext(), serialize, new PMLDeserializer()));
    }
}