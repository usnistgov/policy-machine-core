package gov.nist.csd.pm.audit;

import gov.nist.csd.pm.audit.model.Path;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.Graph;
import gov.nist.csd.pm.graph.MemGraph;
import gov.nist.csd.pm.graph.model.nodes.Node;
import gov.nist.csd.pm.utils.TestUtil;

import java.util.*;

import static gov.nist.csd.pm.graph.model.nodes.NodeType.*;

public class TestCases {
    public static class TestCase {
        String          name;
        Graph           graph;
        Map<String, List<String>> expectedPaths;

        public TestCase(String name, Graph graph, Map<String, List<String>> expectedPaths) {
            this.name = name;
            this.graph = graph;
            this.expectedPaths = expectedPaths;
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
    }

    protected static TestCase[] getTests() throws PMException {
        return new TestCase[]{
                graph1(),
                graph2(),
                graph3(),
                graph4(),
                graph5(),
                graph6(),
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

    public static TestCase graph1() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(ua1ID, UA), new Node(pc1ID, PC));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read", "write")));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-[read,write]-oa1-o1"));
        return new TestCase("graph1", graph, expectedPaths);
    }

    public static TestCase graph2() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(ua2ID, "ua2", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(u1ID, U), new Node(ua2ID, UA));
        graph.assign(new Node(ua1ID, UA), new Node(pc1ID, PC));
        graph.assign(new Node(ua2ID, UA), new Node(pc1ID, PC));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read")));
        graph.associate(new Node(ua2ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("write")));
        
        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-[read]-oa1-o1", "u1-ua2-[write]-oa1-o1"));
        return new TestCase("graph2", graph, expectedPaths);
    }

    public static TestCase graph3() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(ua2ID, "ua2", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(ua1ID, UA), new Node(ua2ID, UA));
        graph.assign(new Node(ua2ID, UA), new Node(pc1ID, PC));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read")));
        graph.associate(new Node(ua2ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("write")));

        
        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-[read]-oa1-o1", "u1-ua1-ua2-[write]-oa1-o1"));
        return new TestCase("graph3", graph, expectedPaths);
    }

    public static TestCase graph4() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));


        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList());
        return new TestCase("graph4", graph, expectedPaths);
    }

    public static TestCase graph5() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(ua1ID, UA), new Node(pc1ID, PC));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read", "write")));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList());
        return new TestCase("graph5", graph, expectedPaths);
    }

    public static TestCase graph6() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));

        graph.assign(new Node(ua1ID, UA), new Node(pc1ID, PC));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read")));


        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList());
        return new TestCase("graph6", graph, expectedPaths);
    }

    public static TestCase graph7() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(ua2ID, "ua2", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(oa2ID, "oa2", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));
        graph.createNode(new Node(pc2ID, "pc2", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(u1ID, U), new Node(ua2ID, UA));
        graph.assign(new Node(ua1ID, UA), new Node(pc1ID, PC));
        graph.assign(new Node(ua2ID, UA), new Node(pc2ID, PC));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(o1ID, O), new Node(oa2ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));
        graph.assign(new Node(oa2ID, OA), new Node(pc2ID, PC));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read")));
        graph.associate(new Node(ua2ID, UA), new Node(oa2ID, OA), new HashSet<>(Arrays.asList("read", "write")));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-[read]-oa1-o1"));
        expectedPaths.put("pc2", Arrays.asList("u1-ua2-[read,write]-oa2-o1"));
        return new TestCase("graph7", graph, expectedPaths);
    }

    public static TestCase graph8() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(ua2ID, "ua2", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(oa2ID, "oa2", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));
        graph.createNode(new Node(pc2ID, "pc2", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(u1ID, U), new Node(ua2ID, UA));
        graph.assign(new Node(ua1ID, UA), new Node(pc1ID, PC));
        graph.assign(new Node(ua2ID, UA), new Node(pc2ID, PC));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(o1ID, O), new Node(oa2ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));
        graph.assign(new Node(oa2ID, OA), new Node(pc2ID, PC));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read")));
        graph.associate(new Node(ua2ID, UA), new Node(oa2ID, OA), new HashSet<>(Arrays.asList("write")));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-[read]-oa1-o1"));
        expectedPaths.put("pc2", Arrays.asList("u1-ua2-[write]-oa2-o1"));
        return new TestCase("graph8", graph, expectedPaths);
    }

    public static TestCase graph9() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));
        graph.createNode(new Node(pc2ID, "pc2", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(ua1ID, UA), new Node(pc1ID, PC));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read", "write")));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-[read,write]-oa1-o1"));
        expectedPaths.put("pc2", Arrays.asList());
        return new TestCase("graph9", graph, expectedPaths);
    }

    public static TestCase graph10() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(ua2ID, "ua2", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(oa2ID, "oa2", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));
        graph.createNode(new Node(pc2ID, "pc2", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(u1ID, U), new Node(ua2ID, UA));
        graph.assign(new Node(ua1ID, UA), new Node(pc1ID, PC));
        graph.assign(new Node(ua2ID, UA), new Node(pc2ID, PC));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(o1ID, O), new Node(oa2ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));
        graph.assign(new Node(oa2ID, OA), new Node(pc2ID, PC));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read")));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-[read]-oa1-o1"));
        expectedPaths.put("pc2", Arrays.asList());
        return new TestCase("graph10", graph, expectedPaths);
    }

    public static TestCase graph11() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read", "write")));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-[read,write]-oa1-o1"));
        return new TestCase("graph11", graph, expectedPaths);
    }

    public static TestCase graph12() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(ua2ID, "ua2", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(u1ID, U), new Node(ua2ID, UA));
        graph.assign(new Node(ua2ID, UA), new Node(pc1ID, PC));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read")));
        graph.associate(new Node(ua2ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("write")));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-[read]-oa1-o1", "u1-ua2-[write]-oa1-o1"));
        return new TestCase("graph12", graph, expectedPaths);
    }

    public static TestCase graph13() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(ua2ID, "ua2", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(oa2ID, "oa2", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));
        graph.createNode(new Node(pc2ID, "pc2", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(u1ID, U), new Node(ua2ID, UA));
        graph.assign(new Node(ua2ID, UA), new Node(pc2ID, PC));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(o1ID, O), new Node(oa2ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));
        graph.assign(new Node(oa2ID, OA), new Node(pc2ID, PC));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read")));
        graph.associate(new Node(ua2ID, UA), new Node(oa2ID, OA), new HashSet<>(Arrays.asList("read", "write")));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-[read]-oa1-o1"));
        expectedPaths.put("pc2", Arrays.asList("u1-ua2-[read,write]-oa2-o1"));
        return new TestCase("graph13", graph, expectedPaths);
    }

    public static TestCase graph14() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(ua2ID, "ua2", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(oa2ID, "oa2", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));
        graph.createNode(new Node(pc2ID, "pc2", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(u1ID, U), new Node(ua2ID, UA));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(o1ID, O), new Node(oa2ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));
        graph.assign(new Node(oa2ID, OA), new Node(pc2ID, PC));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read", "write")));
        graph.associate(new Node(ua2ID, UA), new Node(oa2ID, OA), new HashSet<>(Arrays.asList("read")));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-[read,write]-oa1-o1"));
        expectedPaths.put("pc2", Arrays.asList("u1-ua2-[read]-oa2-o1"));
        return new TestCase("graph14", graph, expectedPaths);
    }

    public static TestCase graph15() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(oa2ID, "oa2", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));
        graph.createNode(new Node(pc2ID, "pc2", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(ua1ID, UA), new Node(pc1ID, PC));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(o1ID, O), new Node(oa2ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));
        graph.assign(new Node(oa2ID, OA), new Node(pc2ID, PC));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read", "write")));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-[read,write]-oa1-o1"));
        expectedPaths.put("pc2", Arrays.asList());
        return new TestCase("graph15", graph, expectedPaths);
    }

    public static TestCase graph16() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(ua1ID, UA), new Node(pc1ID, PC));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read")));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-[read]-oa1-o1"));
        return new TestCase("graph16", graph, expectedPaths);
    }

    public static TestCase graph17() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read")));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-[read]-oa1-o1"));
        return new TestCase("graph17", graph, expectedPaths);
    }

    public static TestCase graph18() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(ua2ID, "ua2", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(oa2ID, "oa2", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(ua1ID, UA), new Node(ua2ID, UA));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(oa2ID, OA));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read", "write")));
        graph.associate(new Node(ua2ID, UA), new Node(oa2ID, OA), new HashSet<>(Arrays.asList("read")));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList());
        return new TestCase("graph18", graph, expectedPaths);
    }

    public static TestCase graph19() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(ua2ID, "ua2", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(oa2ID, "oa2", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(ua1ID, UA), new Node(ua2ID, UA));
        graph.assign(new Node(ua2ID, UA), new Node(pc1ID, PC));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(oa2ID, OA));
        graph.assign(new Node(oa2ID, OA), new Node(pc1ID, OA));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read", "write")));
        graph.associate(new Node(ua2ID, UA), new Node(oa2ID, OA), new HashSet<>(Arrays.asList("read")));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua1-[read,write]-oa1-o1", "u1-ua1-ua2-[read]-oa2-oa1-o1"));
        return new TestCase("graph19", graph, expectedPaths);
    }

    public static TestCase graph20() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(ua2ID, "ua2", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(oa2ID, "oa2", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(u1ID, U), new Node(ua2ID, UA));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(o1ID, O), new Node(oa2ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));
        graph.assign(new Node(oa2ID, OA), new Node(pc1ID, OA));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("write")));
        graph.associate(new Node(ua2ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read")));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua2-[read]-oa1-o1", "u1-ua1-[write]-oa1-o1"));
        return new TestCase("graph20", graph, expectedPaths);
    }

    public static TestCase graph21() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(new Node(u1ID, "u1", U, null));
        graph.createNode(new Node(ua1ID, "ua1", UA, null));
        graph.createNode(new Node(ua2ID, "ua2", UA, null));
        graph.createNode(new Node(o1ID, "o1", O, null));
        graph.createNode(new Node(oa1ID, "oa1", OA, null));
        graph.createNode(new Node(pc1ID, "pc1", PC, null));

        graph.assign(new Node(u1ID, U), new Node(ua1ID, UA));
        graph.assign(new Node(u1ID, U), new Node(ua2ID, UA));
        graph.assign(new Node(o1ID, O), new Node(oa1ID, OA));
        graph.assign(new Node(oa1ID, OA), new Node(pc1ID, PC));
        graph.assign(new Node(ua1ID, UA), new Node(pc1ID, PC));

        graph.associate(new Node(ua1ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("write")));
        graph.associate(new Node(ua2ID, UA), new Node(oa1ID, OA), new HashSet<>(Arrays.asList("read")));

        Map<String, List<String>> expectedPaths = new HashMap<>();
        expectedPaths.put("pc1", Arrays.asList("u1-ua2-[read]-oa1-o1", "u1-ua1-[write]-oa1-o1"));
        return new TestCase("graph21", graph, expectedPaths);
    }
}
