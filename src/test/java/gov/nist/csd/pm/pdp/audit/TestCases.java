package gov.nist.csd.pm.pdp.audit;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.memory.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

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
                graph21(),
                graph22(),
                graph23(),
                graph24(),
                graph25(),
        };
    }

    private static final OperationSet RW = new OperationSet("read", "write");
    private static final OperationSet R = new OperationSet("read");
    private static final OperationSet W = new OperationSet("write");
    private static final OperationSet NOOPS = new OperationSet();

    public static TestCase graph1() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("u1", U, null, "ua1");
        graph.createNode("o1", O, null, "oa1");

        graph.associate("ua1", "oa1", RW);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read, write]"));
        return new TestCase("graph1", graph, expectedPaths, RW);
    }

    public static TestCase graph2() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("ua2", UA, null, "pc1");
        graph.createNode("u1", U, null, "ua1", "ua2");
        graph.createNode("o1", O, null, "oa1");

        graph.associate("ua1", "oa1", R);
        graph.associate("ua2", "oa1", W);
        
        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read]", "u1(U)-ua2(UA)-oa1(OA)-o1(O) ops=[write]"));
        return new TestCase("graph2", graph, expectedPaths, RW);
    }

    public static TestCase graph3() throws PMException {
        Graph graph = new MemGraph();

        graph.createPolicyClass("pc1", null);
        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("ua2", UA, null, "pc1");
        graph.createNode("ua1", UA, null, "ua2");
        graph.createNode("u1", U, null, "ua1");
        graph.createNode("o1", O, null, "oa1");

        graph.associate("ua1", "oa1", R);
        graph.associate("ua2", "oa1", W);

        
        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read]", "u1(U)-ua1(UA)-ua2(UA)-oa1(OA)-o1(O) ops=[write]"));
        return new TestCase("graph3", graph, expectedPaths, RW);
    }

    public static TestCase graph4() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("o1", O, null, "oa1");
        graph.createNode("u1", U, null, "ua1");

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList());
        return new TestCase("graph4", graph, expectedPaths, NOOPS);
    }

    public static TestCase graph5() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("ua2", UA, null, "pc1");
        graph.createNode("u1", U, null, "ua2");
        graph.createNode("o1", O, null, "oa1");

        graph.associate("ua1", "oa1", RW);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList());
        return new TestCase("graph5", graph, expectedPaths, NOOPS);
    }

    // removed graph 6 because of change to Graph interface -- requiring parent nodes on creation prevents floating nodes

    public static TestCase graph7() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createPolicyClass("pc2", null);

        graph.createNode("ua2", UA, null, "pc2");
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("u1", U, null, "ua1", "ua2");

        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("oa2", OA, null, "pc2");
        graph.createNode("o1", O, null, "oa1", "oa2");

        graph.associate("ua1", "oa1", R);
        graph.associate("ua2", "oa2", RW);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read]"));
        expectedPaths.put("pc2", Arrays.asList("u1(U)-ua2(UA)-oa2(OA)-o1(O) ops=[read, write]"));
        return new TestCase("graph7", graph, expectedPaths, R);
    }

    public static TestCase graph8() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createPolicyClass("pc2", null);

        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("ua2", UA, null, "pc2");
        graph.createNode("u1", U, null, "ua1", "ua2");

        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("oa2", OA, null, "pc2");
        graph.createNode("o1", O, null, "oa1", "oa2");

        graph.associate("ua1", "oa1", R);
        graph.associate("ua2", "oa2", W);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read]"));
        expectedPaths.put("pc2", Arrays.asList("u1(U)-ua2(UA)-oa2(OA)-o1(O) ops=[write]"));
        return new TestCase("graph8", graph, expectedPaths, NOOPS);
    }

    public static TestCase graph9() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createPolicyClass("pc2", null);   
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("u1", U, null, "ua1");
        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("o1", O, null, "oa1");

        graph.associate("ua1", "oa1", RW);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read, write]"));
        return new TestCase("graph9", graph, expectedPaths, RW);
    }

    public static TestCase graph10() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createPolicyClass("pc2", null);

        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("ua2", UA, null, "pc2");
        graph.createNode("u1", U, null, "ua1", "ua2");

        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("oa2", OA, null, "pc2");
        graph.createNode("o1", O, null, "oa1", "oa2");

        graph.associate("ua1", "oa1", R);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read]"));
        expectedPaths.put("pc2", Arrays.asList());
        return new TestCase("graph10", graph, expectedPaths, NOOPS);
    }

    public static TestCase graph11() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);

        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("u1", U, null, "ua1");

        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("o1", O, null, "oa1");

        graph.associate("ua1", "oa1", RW);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read, write]"));
        return new TestCase("graph11", graph, expectedPaths, RW);
    }

    public static TestCase graph12() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createPolicyClass("pc2", null);

        graph.createNode("ua1", UA, null, "pc2");
        graph.createNode("ua2", UA, null, "pc1");
        graph.createNode("u1", U, null, "ua1", "ua2");

        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("o1", O, null, "oa1");

        graph.associate("ua1", "oa1", R);
        graph.associate("ua2", "oa1", W);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read]", "u1(U)-ua2(UA)-oa1(OA)-o1(O) ops=[write]"));
        return new TestCase("graph12", graph, expectedPaths, RW);
    }

    public static TestCase graph13() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createPolicyClass("pc2", null);

        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("oa2", OA, null, "pc2");
        graph.createNode("o1", O, null, "oa1", "oa2");

        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("ua2", UA, null, "pc2");
        graph.createNode("u1", U, null, "ua1", "ua2");

        graph.associate("ua1", "oa1", R);
        graph.associate("ua2", "oa2", RW);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read]"));
        expectedPaths.put("pc2", Arrays.asList("u1(U)-ua2(UA)-oa2(OA)-o1(O) ops=[read, write]"));
        return new TestCase("graph13", graph, expectedPaths, R);
    }

    public static TestCase graph14() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createPolicyClass("pc2", null);

        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("ua2", UA, null, "pc2");
        graph.createNode("u1", U, null, "ua1", "ua2");

        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("oa2", OA, null, "pc2");
        graph.createNode("o1", O, null, "oa1", "oa2");

        graph.associate("ua1", "oa1", RW);
        graph.associate("ua2", "oa2", R);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read, write]"));
        expectedPaths.put("pc2", Arrays.asList("u1(U)-ua2(UA)-oa2(OA)-o1(O) ops=[read]"));
        return new TestCase("graph14", graph, expectedPaths, R);
    }

    public static TestCase graph15() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createPolicyClass("pc2", null);

        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("u1", U, null, "ua1");

        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("oa2", OA, null, "pc2");
        graph.createNode("o1", O, null, "oa1", "oa2");

        graph.associate("ua1", "oa1", RW);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read, write]"));
        expectedPaths.put("pc2", Arrays.asList());
        return new TestCase("graph15", graph, expectedPaths, NOOPS);
    }

    public static TestCase graph16() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);

        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("u1", U, null, "ua1");

        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("o1", O, null, "oa1");

        graph.associate("ua1", "oa1", R);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read]"));
        return new TestCase("graph16", graph, expectedPaths, R);
    }

    public static TestCase graph17() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);

        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("u1", U, null, "ua1");

        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("o1", O, null, "oa1");

        graph.associate("ua1", "oa1", R);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read]"));
        return new TestCase("graph17", graph, expectedPaths, R);
    }

    public static TestCase graph18() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createPolicyClass("pc2", null);

        graph.createNode("ua2", UA, null, "pc1");
        graph.createNode("ua1", UA, null, "ua2");
        graph.createNode("u1", U, null, "ua1");

        graph.createNode("oa2", OA, null, "pc2");
        graph.createNode("oa1", OA, null, "oa2");
        graph.createNode("o1", O, null, "oa1");

        graph.associate("ua1", "oa1", RW);
        graph.associate("ua2", "oa2", R);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc2", Arrays.asList("u1(U)-ua1(UA)-ua2(UA)-oa2(OA)-oa1(OA)-o1(O) ops=[read]", "u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read, write]"));
        return new TestCase("graph18", graph, expectedPaths, NOOPS);
    }

    public static TestCase graph19() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);

        graph.createNode("ua2", UA, null, "pc1");
        graph.createNode("ua1", UA, null, "ua2");
        graph.createNode("u1", U, null, "ua1");

        graph.createNode("oa2", OA, null, "pc1");
        graph.createNode("oa1", OA, null, "oa2");
        graph.createNode("o1", O, null, "oa1");

        graph.associate("ua1", "oa1", RW);
        graph.associate("ua2", "oa2", R);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read, write]", "u1(U)-ua1(UA)-ua2(UA)-oa2(OA)-oa1(OA)-o1(O) ops=[read]"));
        return new TestCase("graph19", graph, expectedPaths, RW);
    }

    public static TestCase graph20() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);

        graph.createNode("ua2", UA, null, "pc1");
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("u1", U, null, "ua1", "ua2");

        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("oa2", OA, null, "pc1");
        graph.createNode("o1", O, null, "oa1", "oa2");

        graph.associate("ua1", "oa1", W);
        graph.associate("ua2", "oa1", R);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua2(UA)-oa1(OA)-o1(O) ops=[read]", "u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[write]"));
        return new TestCase("graph20", graph, expectedPaths, RW);
    }

    public static TestCase graph21() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);

        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("ua2", UA, null, "pc1");
        graph.createNode("u1", U, null, "ua1", "ua2");

        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("o1", O, null, "oa1");

        graph.associate("ua1", "oa1", W);
        graph.associate("ua2", "oa1", R);

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua2(UA)-oa1(OA)-o1(O) ops=[read]", "u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[write]"));
        return new TestCase("graph21", graph, expectedPaths, RW);
    }

    public static TestCase graph22() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("oa2", OA, null, "pc1");
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("o1", O, null, "oa1", "oa2");
        graph.createNode("u1", U, null, "ua1");

        graph.associate("ua1", "oa1", new OperationSet("read"));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read]"));
        return new TestCase("graph22", graph, expectedPaths, R);
    }

    public static TestCase graph23() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("oa2", OA, null, "pc1");
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("o1", O, null, "oa1");
        graph.createNode("u1", U, null, "ua1");

        graph.associate("ua1", "oa1", new OperationSet("read"));
        graph.associate("ua1", "oa2", new OperationSet("write"));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read]"));
        return new TestCase("graph23", graph, expectedPaths, R);
    }

    public static TestCase graph24() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createNode("oa2", OA, null, "pc1");
        graph.createNode("oa1", OA, null, "oa2");
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("o1", O, null, "oa1");
        graph.createNode("u1", U, null, "ua1");

        graph.associate("ua1", "oa1", new OperationSet("read"));
        graph.associate("ua1", "oa2", new OperationSet("write"));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read]", "u1(U)-ua1(UA)-oa2(OA)-oa1(OA)-o1(O) ops=[write]"));
        return new TestCase("graph24", graph, expectedPaths, RW);
    }

    public static TestCase graph25() throws PMException {
        Graph graph = new MemGraph();
        Node pc1 = graph.createPolicyClass("pc1", null);
        Node ua1 = graph.createNode("ua1", UA, null, pc1.getName());
        Node u1 = graph.createNode("u1", U, null, ua1.getName());
        Node oa2 = graph.createNode("oa2", OA, null, pc1.getName());
        Node oa1 = graph.createNode("oa1", OA, null, oa2.getName());
        Node o1 = graph.createNode("o1", O, null, oa1.getName());

        graph.associate(ua1.getName(), oa1.getName(), new OperationSet("*r"));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1(U)-ua1(UA)-oa1(OA)-o1(O) ops=[read, write, execute]"));
        return new TestCase("graph25", graph, expectedPaths, RW);
    }
}
