package gov.nist.csd.pm.pdp.audit;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.MemGraph;

import java.util.*;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;

public class TestCases {
    public static class TestCase {
        String          name;
        Graph           graph;
        Map<String, List<String>> expectedPaths;
        Set<String> expectedOps;

        public TestCase(String name, Graph graph, Map<String, List<String>> expectedPaths, Set<String> expectedOps) {
            this.name = name;
            this.graph = graph;
            this.expectedPaths = expectedPaths;
            this.expectedOps = expectedOps;
        }

        public String getName() {
            return name;
        }

        public Graph getGraph() {
            return graph;
        }

        public Map<String, List<String>> getExpectedPaths() {
            return expectedPaths;
        }

        public Set<String> getExpectedOps() {
            return expectedOps;
        }
    }

    protected static TestCase[] getTests() throws PMException {
        return new TestCase[]{
                graph1(),
                graph2(),
                graph3(),
                graph4(),
                graph5(),
                graph7(),
                graph8(),
                graph9(),
                graph10(),
                graph11(),
                graph12(),
                graph13(),
                graph14(),
                graph15(),
                graph16(),
                graph17(),
                graph18(),
                graph19(),
                graph20(),
                graph21()
        };
    }

    public static long u1ID = 1;
    public static long o1ID = 2;
    private static long ua1ID = 3;
    private static long oa1ID = 4;
    private static long pc1ID = 5;
    private static long pc2ID = 6;
    private static long ua2ID = 7;
    private static long oa2ID = 8;

    private static final OperationSet RW = new OperationSet("read", "write");
    private static final OperationSet R = new OperationSet("read");
    private static final OperationSet W = new OperationSet("write");
    private static final OperationSet NOOPS = new OperationSet();

    public static TestCase graph1() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);
        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID);

        graph.associate(ua1ID, oa1ID, RW);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-oa1-o1 ops=[read, write]"));
        return new TestCase("graph1", graph, expectedPaths, RW);
    }

    public static TestCase graph2() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);
        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(ua2ID, "ua2", UA, null, pc1ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID, ua2ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID);

        graph.associate(ua1ID, oa1ID, R);
        graph.associate(ua2ID, oa1ID, W);
        
        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-oa1-o1 ops=[read]", "u1-ua2-oa1-o1 ops=[write]"));
        return new TestCase("graph2", graph, expectedPaths, RW);
    }

    public static TestCase graph3() throws PMException {
        Graph graph = new MemGraph();

        graph.createPolicyClass(pc1ID, "pc1", null);
        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(ua2ID, "ua2", UA, null, pc1ID);
        graph.createNode(ua1ID, "ua1", UA, null, ua2ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID);

        graph.associate(ua1ID, oa1ID, R);
        graph.associate(ua2ID, oa1ID, W);

        
        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-oa1-o1 ops=[read]", "u1-ua1-ua2-oa1-o1 ops=[write]"));
        return new TestCase("graph3", graph, expectedPaths, RW);
    }

    public static TestCase graph4() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);
        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList());
        return new TestCase("graph4", graph, expectedPaths, NOOPS);
    }

    public static TestCase graph5() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);
        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(ua2ID, "ua1", UA, null, pc1ID);
        graph.createNode(u1ID, "u1", U, null, ua2ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID);

        graph.associate(ua1ID, oa1ID, RW);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList());
        return new TestCase("graph5", graph, expectedPaths, NOOPS);
    }

    // removed graph 6 because of change to Graph interface -- requiring parent nodes on creation prevents floating nodes

    public static TestCase graph7() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);
        graph.createPolicyClass(pc2ID, "pc2", null);

        graph.createNode(ua2ID, "ua2", UA, null, pc2ID);
        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID, ua2ID);

        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(oa2ID, "oa2", OA, null, pc2ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID, oa2ID);

        graph.associate(ua1ID, oa1ID, R);
        graph.associate(ua2ID, oa2ID, RW);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-oa1-o1 ops=[read]"));
        expectedPaths.put("pc2", Arrays.asList("u1-ua2-oa2-o1 ops=[read, write]"));
        return new TestCase("graph7", graph, expectedPaths, R);
    }

    public static TestCase graph8() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);
        graph.createPolicyClass(pc2ID, "pc2", null);

        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(ua2ID, "ua2", UA, null, pc2ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID, ua2ID);

        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(oa2ID, "oa2", OA, null, pc2ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID, oa2ID);

        graph.associate(ua1ID, oa1ID, R);
        graph.associate(ua2ID, oa2ID, W);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-oa1-o1 ops=[read]"));
        expectedPaths.put("pc2", Arrays.asList("u1-ua2-oa2-o1 ops=[write]"));
        return new TestCase("graph8", graph, expectedPaths, NOOPS);
    }

    public static TestCase graph9() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);
        graph.createPolicyClass(pc2ID, "pc2", null);   
        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID);
        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID);

        graph.associate(ua1ID, oa1ID, RW);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-oa1-o1 ops=[read, write]"));
        expectedPaths.put("pc2", Arrays.asList());
        return new TestCase("graph9", graph, expectedPaths, NOOPS);
    }

    public static TestCase graph10() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);
        graph.createPolicyClass(pc2ID, "pc2", null);

        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(ua2ID, "ua2", UA, null, pc2ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID, ua2ID);

        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(oa2ID, "oa2", OA, null, pc2ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID, oa2ID);

        graph.associate(ua1ID, oa1ID, R);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-oa1-o1 ops=[read]"));
        expectedPaths.put("pc2", Arrays.asList());
        return new TestCase("graph10", graph, expectedPaths, NOOPS);
    }

    public static TestCase graph11() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);

        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID);

        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID);

        graph.associate(ua1ID, oa1ID, RW);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-oa1-o1 ops=[read, write]"));
        return new TestCase("graph11", graph, expectedPaths, RW);
    }

    public static TestCase graph12() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);
        graph.createPolicyClass(pc2ID, "pc2", null);

        graph.createNode(ua1ID, "ua1", UA, null, pc2ID);
        graph.createNode(ua2ID, "ua2", UA, null, pc1ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID, ua2ID);

        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID);

        graph.associate(ua1ID, oa1ID, R);
        graph.associate(ua2ID, oa1ID, W);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-oa1-o1 ops=[read]", "u1-ua2-oa1-o1 ops=[write]"));
        return new TestCase("graph12", graph, expectedPaths, RW);
    }

    public static TestCase graph13() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);
        graph.createPolicyClass(pc2ID, "pc2", null);

        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(oa2ID, "oa2", OA, null, pc2ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID, oa2ID);

        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(ua2ID, "ua2", UA, null, pc2ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID, ua2ID);

        graph.associate(ua1ID, oa1ID, R);
        graph.associate(ua2ID, oa2ID, RW);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-oa1-o1 ops=[read]"));
        expectedPaths.put("pc2", Arrays.asList("u1-ua2-oa2-o1 ops=[read, write]"));
        return new TestCase("graph13", graph, expectedPaths, R);
    }

    public static TestCase graph14() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);
        graph.createPolicyClass(pc2ID, "pc2", null);

        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(ua2ID, "ua2", UA, null, pc2ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID, ua2ID);

        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(oa2ID, "oa2", OA, null, pc2ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID, oa2ID);

        graph.associate(ua1ID, oa1ID, RW);
        graph.associate(ua2ID, oa2ID, R);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-oa1-o1 ops=[read, write]"));
        expectedPaths.put("pc2", Arrays.asList("u1-ua2-oa2-o1 ops=[read]"));
        return new TestCase("graph14", graph, expectedPaths, R);
    }

    public static TestCase graph15() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);
        graph.createPolicyClass(pc2ID, "pc2", null);

        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID);

        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(oa2ID, "oa2", OA, null, pc2ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID, oa2ID);

        graph.associate(ua1ID, oa1ID, RW);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-oa1-o1 ops=[read, write]"));
        expectedPaths.put("pc2", Arrays.asList());
        return new TestCase("graph15", graph, expectedPaths, NOOPS);
    }

    public static TestCase graph16() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);

        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID);

        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID);

        graph.associate(ua1ID, oa1ID, R);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-oa1-o1 ops=[read]"));
        return new TestCase("graph16", graph, expectedPaths, R);
    }

    public static TestCase graph17() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);

        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID);

        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID);

        graph.associate(ua1ID, oa1ID, R);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-oa1-o1 ops=[read]"));
        return new TestCase("graph17", graph, expectedPaths, R);
    }

    public static TestCase graph18() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);
        graph.createPolicyClass(pc2ID, "pc2", null);

        graph.createNode(ua2ID, "ua2", UA, null, pc1ID);
        graph.createNode(ua1ID, "ua1", UA, null, ua2ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID);

        graph.createNode(oa2ID, "oa2", OA, null, pc2ID);
        graph.createNode(oa1ID, "oa1", OA, null, oa2ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID);

        graph.associate(ua1ID, oa1ID, RW);
        graph.associate(ua2ID, oa2ID, R);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList());
        return new TestCase("graph18", graph, expectedPaths, NOOPS);
    }

    public static TestCase graph19() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);

        graph.createNode(ua2ID, "ua2", UA, null, pc1ID);
        graph.createNode(ua1ID, "ua1", UA, null, ua2ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID);

        graph.createNode(oa2ID, "oa2", OA, null, pc1ID);
        graph.createNode(oa1ID, "oa1", OA, null, oa2ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID);

        graph.associate(ua1ID, oa1ID, RW);
        graph.associate(ua2ID, oa2ID, R);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-oa1-o1 ops=[read, write]", "u1-ua1-ua2-oa2-oa1-o1 ops=[read]"));
        return new TestCase("graph19", graph, expectedPaths, RW);
    }

    public static TestCase graph20() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);

        graph.createNode(ua2ID, "ua2", UA, null, pc1ID);
        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(u1ID, "u1", U, null, ua2ID, ua1ID);

        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(oa2ID, "oa2", OA, null, pc1ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID, oa2ID);

        graph.associate(ua1ID, oa1ID, W);
        graph.associate(ua2ID, oa1ID, R);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua2-oa1-o1 ops=[read]", "u1-ua1-oa1-o1 ops=[write]"));
        return new TestCase("graph20", graph, expectedPaths, RW);
    }

    public static TestCase graph21() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass(pc1ID, "pc1", null);

        graph.createNode(ua1ID, "ua1", UA, null, pc1ID);
        graph.createNode(ua2ID, "ua2", UA, null, pc1ID);
        graph.createNode(u1ID, "u1", U, null, ua1ID, ua2ID);

        graph.createNode(oa1ID, "oa1", OA, null, pc1ID);
        graph.createNode(o1ID, "o1", O, null, oa1ID);

        graph.associate(ua1ID, oa1ID, W);
        graph.associate(ua2ID, oa1ID, R);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua2-oa1-o1 ops=[read]", "u1-ua1-oa1-o1 ops=[write]"));
        return new TestCase("graph21", graph, expectedPaths, RW);
    }
}
