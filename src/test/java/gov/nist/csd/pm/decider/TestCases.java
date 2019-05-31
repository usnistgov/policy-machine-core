package gov.nist.csd.pm.decider;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import java.util.Arrays;
import java.util.HashSet;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;

public class TestCases {

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

        public Graph getGraph() {
            return graph;
        }

        public HashSet<String> getExpectedOps() {
            return expectedOps;
        }
    }
    
    protected static TestCase[] getTests() throws PMException {
        return new TestCase[] {
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
                graph21(),
                graph22()
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

    private static TestCase graph1() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(pc1ID, "pc1", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(ua1ID, pc1ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(oa1ID, pc1ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("read", "write")));

        return new TestCase("graph1", graph, "read", "write");
    }
    private static TestCase graph2() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(ua2ID, "ua2", UA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(oa2ID, "oa2", OA, null);
        graph.createNode(pc1ID, "pc1", PC, null);
        graph.createNode(pc2ID, "pc2", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(u1ID, ua2ID);
        graph.assign(ua1ID, pc1ID);
        graph.assign(ua1ID, pc2ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(o1ID, oa2ID);
        graph.assign(oa1ID, pc1ID);
        graph.assign(oa2ID, pc2ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("read")));

        return new TestCase("graph2", graph);
    }
    private static TestCase graph3() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(pc1ID, "pc1", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(oa1ID, pc1ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("read", "write")));

        return new TestCase("graph3", graph, "read", "write");
    }
    private static TestCase graph4() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(ua2ID, "ua2", UA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(pc1ID, "pc1", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(u1ID, ua2ID);
        graph.assign(ua2ID, pc1ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(oa1ID, pc1ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("read")));
        graph.associate(ua2ID, oa1ID, new HashSet<>(Arrays.asList("write")));

        return new TestCase("graph4", graph, "read", "write");
    }
    private static TestCase graph5() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(ua2ID, "ua2", UA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(oa2ID, "oa2", OA, null);
        graph.createNode(pc1ID, "pc1", PC, null);
        graph.createNode(pc2ID, "pc2", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(u1ID, ua2ID);
        graph.assign(ua1ID, pc2ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(o1ID, oa2ID);
        graph.assign(oa1ID, pc1ID);
        graph.assign(oa2ID, pc2ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("read")));
        graph.associate(ua2ID, oa2ID, new HashSet<>(Arrays.asList("read", "write")));

        return new TestCase("graph5", graph, "read");
    }
    private static TestCase graph6() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(ua2ID, "ua2", UA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(oa2ID, "oa2", OA, null);
        graph.createNode(pc1ID, "pc1", PC, null);
        graph.createNode(pc2ID, "pc2", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(u1ID, ua2ID);
        graph.assign(ua1ID, pc1ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(o1ID, oa2ID);
        graph.assign(oa1ID, pc1ID);
        graph.assign(oa2ID, pc2ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("read", "write")));
        graph.associate(ua2ID, oa2ID, new HashSet<>(Arrays.asList("read")));

        return new TestCase("graph6", graph, "read");
    }
    private static TestCase graph7() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(oa2ID, "oa2", OA, null);
        graph.createNode(pc1ID, "pc1", PC, null);
        graph.createNode(pc2ID, "pc2", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(ua1ID, pc1ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(o1ID, oa2ID);
        graph.assign(oa1ID, pc1ID);
        graph.assign(oa2ID, pc2ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("read", "write")));

        return new TestCase("graph7", graph);
    }
    private static TestCase graph8() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(pc1ID, "pc1", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(oa1ID, pc1ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("*")));

        return new TestCase("graph8", graph, "*");
    }
    private static TestCase graph9() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(ua2ID, "ua2", UA, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(pc1ID, "pc1", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(oa1ID, pc1ID);
        graph.assign(ua1ID, pc1ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("*")));
        graph.associate(ua2ID, oa1ID, new HashSet<>(Arrays.asList("read", "write")));

        return new TestCase("graph9", graph, "*");
    }
    private static TestCase graph10() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(ua2ID, "ua2", UA, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(oa2ID, "oa2", OA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(pc1ID, "pc1", PC, null);
        graph.createNode(pc2ID, "pc2", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(u1ID, ua2ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(o1ID, oa2ID);
        graph.assign(oa1ID, pc1ID);
        graph.assign(oa2ID, pc2ID);
        graph.assign(ua1ID, pc1ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("*")));
        graph.associate(ua2ID, oa2ID, new HashSet<>(Arrays.asList("read", "write")));

        return new TestCase("graph10", graph, "read", "write");
    }
    private static TestCase graph11() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(oa2ID, "oa2", OA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(pc1ID, "pc1", PC, null);
        graph.createNode(pc2ID, "pc2", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(o1ID, oa2ID);
        graph.assign(oa1ID, pc1ID);
        graph.assign(oa2ID, pc2ID);
        graph.assign(ua1ID, pc1ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("*")));

        return new TestCase("graph11", graph);
    }
    private static TestCase graph12() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(ua2ID, "ua2", UA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(pc1ID, "pc1", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(u1ID, ua2ID);
        graph.assign(ua1ID, pc1ID);
        graph.assign(ua2ID, pc1ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(oa1ID, pc1ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("read")));
        graph.associate(ua2ID, oa1ID, new HashSet<>(Arrays.asList("write")));


        return new TestCase("graph12", graph, "read", "write");
    }
    private static TestCase graph13() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(ua2ID, "ua2", UA, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(oa2ID, "oa2", OA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(pc1ID, "pc1", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(ua1ID, ua2ID);
        graph.assign(ua2ID, pc1ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(oa1ID, oa2ID);
        graph.assign(oa2ID, pc1ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("*")));
        graph.associate(ua2ID, oa2ID, new HashSet<>(Arrays.asList("read")));

        return new TestCase("graph13", graph, "*");
    }
    private static TestCase graph14() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(ua2ID, "ua2", UA, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(pc1ID, "pc1", PC, null);
        graph.createNode(pc2ID, "pc2", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(u1ID, ua2ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(oa1ID, pc1ID);
        graph.assign(oa1ID, pc2ID);
        graph.assign(ua1ID, pc1ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("*")));
        graph.associate(ua2ID, oa1ID, new HashSet<>(Arrays.asList("*")));

        return new TestCase("graph14", graph, "*");
    }
    private static TestCase graph15() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(ua2ID, "ua2", UA, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(oa2ID, "oa2", OA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(pc1ID, "pc1", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(ua1ID, ua2ID);
        graph.assign(ua1ID, pc1ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(oa1ID, oa2ID);
        graph.assign(oa2ID, pc1ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("*")));
        graph.associate(ua2ID, oa2ID, new HashSet<>(Arrays.asList("read")));

        return new TestCase("graph15", graph, "*");
    }
    private static TestCase graph16() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(ua2ID, "ua2", UA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(pc1ID, "pc1", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(ua1ID, ua2ID);
        graph.assign(ua2ID, pc1ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(oa1ID, pc1ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("read")));
        graph.associate(ua2ID, oa1ID, new HashSet<>(Arrays.asList("write")));

        return new TestCase("graph16", graph, "read", "write");
    }
    private static TestCase graph17() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(pc1ID, "pc1", PC, null);


        return new TestCase("graph17", graph);
    }
    private static TestCase graph18() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(pc1ID, "pc1", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(ua1ID, pc1ID);
        graph.assign(oa1ID, pc1ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("read", "write")));


        return new TestCase("graph18", graph);
    }
    private static TestCase graph19() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(pc1ID, "pc1", PC, null);

        graph.assign(ua1ID, pc1ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(oa1ID, pc1ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("read")));


        return new TestCase("graph19", graph);
    }
    private static TestCase graph20() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(ua2ID, "ua2", UA, null);
        graph.createNode(o1ID, "o1", O, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(oa2ID, "oa2", OA, null);
        graph.createNode(pc1ID, "pc1", PC, null);
        graph.createNode(pc2ID, "pc2", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(u1ID, ua2ID);
        graph.assign(ua1ID, pc1ID);
        graph.assign(ua1ID, pc2ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(o1ID, oa2ID);
        graph.assign(oa1ID, pc1ID);
        graph.assign(oa2ID, pc2ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("read")));
        graph.associate(ua2ID, oa2ID, new HashSet<>(Arrays.asList("read", "write")));

        return new TestCase("graph20", graph, "read");
    }
    private static TestCase graph21() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(ua2ID, "ua2", UA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(oa2ID, "oa2", OA, null);
        graph.createNode(pc1ID, "pc1", PC, null);
        graph.createNode(pc2ID, "pc2", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(u1ID, ua2ID);
        graph.assign(ua1ID, pc1ID);
        graph.assign(ua1ID, pc2ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(o1ID, oa2ID);
        graph.assign(oa1ID, pc1ID);
        graph.assign(oa2ID, pc2ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("read")));
        graph.associate(ua2ID, oa2ID, new HashSet<>(Arrays.asList("write")));

        return new TestCase("graph21", graph);
    }
    private static TestCase graph22() throws PMException {
        Graph graph = new MemGraph();
        graph.createNode(u1ID, "u1", U, null);
        graph.createNode(ua1ID, "ua1", UA, null);
        graph.createNode(o1ID, "o1", NodeType.O, null);
        graph.createNode(oa1ID, "oa1", OA, null);
        graph.createNode(pc1ID, "pc1", PC, null);
        graph.createNode(pc2ID, "pc2", PC, null);

        graph.assign(u1ID, ua1ID);
        graph.assign(ua1ID, pc1ID);
        graph.assign(o1ID, oa1ID);
        graph.assign(oa1ID, pc1ID);

        graph.associate(ua1ID, oa1ID, new HashSet<>(Arrays.asList("read", "write")));

        return new TestCase("graph22", graph, "read", "write");
    }
}
