package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class PAPTestInitializer {

    protected PAP pap;

    public abstract PAP initializePAP() throws PMException;

    @BeforeEach
    void setup() throws PMException {
        pap = initializePAP();
    }

    protected Node node(String name) throws PMException {
        return pap.query()
                .graph()
                .getNodeByName(name);
    }

    protected long id(String name) throws PMException {
        return pap.query().graph().getNodeByName(name).getId();
    }

    protected List<Long> ids(String ... names) throws PMException {
        long[] ids = new long[names.length];
        for (int i = 0; i < names.length; i++) {
            ids[i] = id(names[i]);
        }

        return LongStream.of(ids)
                .boxed()
                .toList();
    }

    protected void assertIdOfNameInLongArray(long[] ids, String name) throws PMException {
        assertTrue(Arrays.stream(ids).boxed().toList().contains(id(name)));
    }
}
