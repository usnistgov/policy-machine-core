package gov.nist.csd.pm.core.pap.obligation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.jupiter.api.Test;

public class JavaObligationResponseTest {

    static class TestResponse extends JavaObligationResponse {

        @Override
        public void execute(PAP pap, UserContext author, EventContext evtCtx) throws PMException {
            pap.modify().graph().createPolicyClass("test");
        }
    }

    @Test
    void testSerialization() throws IOException, ClassNotFoundException, PMException {
        TestResponse testResponse = new TestResponse();

        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(testResponse);
            oos.flush();
            bytes = bos.toByteArray();
        }

        JavaObligationResponse copy;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis)) {
            copy = (JavaObligationResponse) ois.readObject();
        }

        MemoryPAP memoryPAP = new MemoryPAP();
        copy.execute(memoryPAP, new UserContext(0), new EventContext(null, null, null));

        assertTrue(memoryPAP.query().graph().nodeExists("test"));
    }

}
