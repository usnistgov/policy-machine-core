package gov.nist.csd.pm.utils;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.Graph;

import java.util.Arrays;
import java.util.HashSet;

public class TestUtil {
    public static class TestCase {
        String          name;
        Graph           graph;
        HashSet<String> expectedOps;

        public TestCase(String name, Graph graph, String ... expectedOps) {
            this.name = name;
            this.graph = graph;
            this.expectedOps = new HashSet<>(Arrays.asList(expectedOps));
        }

        public String getName() {
            return name;
        }

        public Graph getGraph() throws PMException {
            return graph;
        }

        public HashSet<String> getExpectedOps() throws PMException {
            return expectedOps;
        }
    }
}
