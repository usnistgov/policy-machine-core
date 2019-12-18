package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.obligations.MemObligations;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

class TestUtil {
    static TestContext getTestCtx() throws PMException {
        Graph graph = new MemGraph();
        Node o1 = graph.createNode(new Random().nextLong(), "o1", NodeType.O, null);
        Node oa1 = graph.createNode(new Random().nextLong(), "oa1", NodeType.OA, null);
        Node u1 = graph.createNode(new Random().nextLong(), "u1", NodeType.U, null);
        Node ua1 = graph.createNode(new Random().nextLong(), "ua1", NodeType.UA, null);
        Node pc1 = graph.createNode(new Random().nextLong(), "pc1", NodeType.PC, null);

        graph.assign(o1.getID(), oa1.getID());
        graph.assign(oa1.getID(), pc1.getID());
        graph.assign(u1.getID(), ua1.getID());
        graph.assign(ua1.getID(), pc1.getID());

        graph.associate(ua1.getID(), oa1.getID(), new HashSet<>(Arrays.asList("read", "write")));

        return new TestContext(new PDP(new PAP(graph, new MemProhibitions(), new MemObligations()), null),
                u1, ua1, o1, oa1, pc1);
    }

    static class TestContext {
        PDP pdp;
        Node u1;
        Node ua1;
        Node o1;
        Node oa1;
        Node pc1;

        public TestContext(PDP pdp, Node u1, Node ua1, Node o1, Node oa1, Node pc1) {
            this.pdp = pdp;
            this.u1 = u1;
            this.ua1 = ua1;
            this.o1 = o1;
            this.oa1 = oa1;
            this.pc1 = pc1;
        }

        public PDP getPdp() {
            return pdp;
        }

        public void setPdp(PDP pdp) {
            this.pdp = pdp;
        }

        public Node getU1() {
            return u1;
        }

        public void setU1(Node u1) {
            this.u1 = u1;
        }

        public Node getUa1() {
            return ua1;
        }

        public void setUa1(Node ua1) {
            this.ua1 = ua1;
        }

        public Node getO1() {
            return o1;
        }

        public void setO1(Node o1) {
            this.o1 = o1;
        }

        public Node getOa1() {
            return oa1;
        }

        public void setOa1(Node oa1) {
            this.oa1 = oa1;
        }

        public Node getPc1() {
            return pc1;
        }

        public void setPc1(Node pc1) {
            this.pc1 = pc1;
        }
    }
}
