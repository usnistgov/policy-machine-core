package gov.nist.csd.pm.core.pap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collection;
import java.util.List;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
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

    protected void assertIdOfNameInLongArray(Collection<Long> ids, String name) throws PMException {
        assertTrue(ids.contains(id(name)));
    }

    protected void assertIdOfNameNotInLongArray(Collection<Long> ids, String name) throws PMException {
        assertFalse(ids.contains(id(name)));
    }
}
