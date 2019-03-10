package gov.nist.csd.pm.audit.model;

import gov.nist.csd.pm.graph.Graph;
import gov.nist.csd.pm.graph.MemGraph;
import gov.nist.csd.pm.graph.model.nodes.Node;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class PathTest {

    @Test
    void TestToString() {
        Node u1 = new Node().name("u1");
        Node ua1 = new Node().name("ua1");
        Node ua2 = new Node().name("ua2");
        Node o1 = new Node().name("o1");
        Node oa1 = new Node().name("oa1");
        Node oa2 = new Node().name("oa2");
        Node pc1 = new Node().name("pc1");

        Path uToPc = new Path();
        uToPc.addEdge(new Path.Edge(u1, ua1));
        uToPc.addEdge(new Path.Edge(ua1, ua2));
        uToPc.addEdge(new Path.Edge(ua2, pc1));

        Path uToUa2 = new Path();
        uToUa2.addEdge(new Path.Edge(u1, ua1));
        uToUa2.addEdge(new Path.Edge(ua1, ua2));

        Path uToO = new Path();
        uToO.addEdge(new Path.Edge(u1, ua1));
        uToO.addEdge(new Path.Edge(ua1, oa1, new HashSet<>(Arrays.asList("read"))));
        uToO.addEdge(new Path.Edge(o1, oa1));

        Path uToO2 = new Path();
        uToO2.addEdge(new Path.Edge(u1, ua1));
        uToO2.addEdge(new Path.Edge(ua1, oa1, new HashSet<>(Arrays.asList("read"))));
        uToO2.addEdge(new Path.Edge(o1, oa1));

        Path oToPC = new Path();
        oToPC.addEdge(new Path.Edge(o1, oa1));
        oToPC.addEdge(new Path.Edge(oa1, oa2));
        oToPC.addEdge(new Path.Edge(oa2, pc1));

        Path u2UasToO = new Path();
        u2UasToO.addEdge(new Path.Edge(u1, ua1));
        u2UasToO.addEdge(new Path.Edge(ua1, ua2));
        u2UasToO.addEdge(new Path.Edge(ua2, oa1, new HashSet<>(Arrays.asList("read"))));
        u2UasToO.addEdge(new Path.Edge(o1, oa1));

        TestCase[] tests = new TestCase[] {
            new TestCase("u1 to pc1", uToPc, "u1-ua1-ua2-pc1"),
            new TestCase("u1 to ua1", uToUa2, "u1-ua1-ua2"),
            new TestCase("u1 to o1", uToO, "u1-ua1-[read]-oa1-o1"),
            new TestCase("u1 to o12", uToO2, "u1-ua1-[read]-oa1-o1"),
            new TestCase("o1 to pc1", oToPC, "o1-oa1-oa2-pc1"),
            new TestCase("u1 to o1, 2 uas", u2UasToO, "u1-ua1-ua2-[read]-oa1-o1"),
        };

        for(TestCase tc : tests) {
            assertEquals(tc.expected, tc.path.toString(), tc.getName() + ": expected " + tc.expected + " got " + tc.path.toString());
        }
    }

    static class TestCase {
        String name;
        Path path;
        String expected;

        public TestCase(String name, Path path, String expected) {
            this.name = name;
            this.path = path;
            this.expected = expected;
        }

        public String getName() {
            return name;
        }

        public Path getPath() {
            return path;
        }

        public String getExpected() {
            return expected;
        }
    }
}