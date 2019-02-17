package gov.nist.csd.pm.prohibitions.loader;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import gov.nist.csd.pm.prohibitions.Neo4jProhibitionsDAO;
import gov.nist.csd.pm.prohibitions.ProhibitionsDAO;
import gov.nist.csd.pm.prohibitions.model.Prohibition;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Neo4jProhibitionsLoaderIT {

    @Test
    void testLoadProhibitions() throws PMException, IOException {
        // create a prohibition
        ProhibitionsDAO dao = new Neo4jProhibitionsDAO(TestUtils.getDatabaseContext());
        Prohibition prohibition = new Prohibition();
        prohibition.setName("deny");
        prohibition.setIntersection(false);
        prohibition.setOperations(new HashSet<>(Arrays.asList("read", "write")));
        prohibition.setSubject(new Prohibition.Subject(123, Prohibition.SubjectType.USER));
        prohibition.addNode(new NodeContext().id(1234).complement(false));
        dao.createProhibition(prohibition);

        ProhibitionsLoader loader = new Neo4jProhibitionsLoader(TestUtils.getDatabaseContext());
        List<Prohibition> prohibitions = loader.loadProhibitions();
        assertTrue(prohibitions.contains(prohibition));

        for (Prohibition p : prohibitions) {
            if (p.equals(prohibition)) {
                prohibition = p;
            }
        }

        assertEquals("deny", prohibition.getName());
        assertFalse(prohibition.isIntersection());
        assertEquals(123, prohibition.getSubject().getSubjectID());
        assertEquals(prohibition.getSubject().getSubjectType(), Prohibition.SubjectType.USER);
        assertTrue(prohibition.getNodes().size() >= 1);
        assertEquals(1234, prohibition.getNodes().get(0).getID());
        assertFalse(prohibition.getNodes().get(0).isComplement());

        // delete prohibition
        dao.deleteProhibition("deny");
    }
}