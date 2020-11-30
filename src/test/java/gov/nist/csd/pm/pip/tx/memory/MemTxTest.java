package gov.nist.csd.pm.pip.tx.memory;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.MemPAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.audit.PReviewAuditor;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.memory.MemGraph;
import gov.nist.csd.pm.pip.memory.MemObligations;
import gov.nist.csd.pm.pip.memory.MemPIP;
import gov.nist.csd.pm.pip.memory.tx.MemTx;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.memory.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.tx.Tx;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.OA;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemTxTest {

    @Test
    void testConcurrency() throws InterruptedException {
        MemGraph graph = new MemGraph();
        MemProhibitions prohibitions = new MemProhibitions();
        MemObligations obligations = new MemObligations();
        MemTx tx = new MemTx(graph, prohibitions, obligations);
        MemTx tx2 = new MemTx(graph, prohibitions, obligations);
        new Thread(() -> {
            try {
                tx.runTx((g, p, o) -> {
                    System.out.println("in tx1");
                    g.createPolicyClass("txpc1", null);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                tx.commit();
            } catch (PMException e) {
                tx.rollback();
            }
        }).start();
        new Thread(() -> {
            try {
                tx2.runTx((g, p, o) -> {
                    System.out.println("in tx2");
                    g.createPolicyClass("txpc2", null);
                });
                tx2.commit();
            } catch (PMException e) {
                tx2.rollback();
            }
        }).start();
        Thread.sleep(4000);
        System.out.println(graph.getPolicyClasses());
    }

    @Test
    void testRollback() throws PMException {
        Graph graph = new MemGraph();
        Prohibitions prohibitions = new MemProhibitions();
        Obligations obligations = new MemObligations();
        Tx tx = new MemTx(graph, prohibitions, obligations);
        try {
            tx.runTx((g, p, o) -> {
                g.createPolicyClass("pc1", null);
                g.createNode("oa1", OA, null, "pc2");
            });
        } catch (PMException e) {
            // exception expected
        }
        assertFalse(graph.exists("pc1"));
    }

    @Test
    void testSuccess() throws PMException {
        Graph graph = new MemGraph();
        Prohibitions prohibitions = new MemProhibitions();
        Obligations obligations = new MemObligations();
        Tx tx = new MemTx(graph, prohibitions, obligations);
        tx.runTx((g, p , o) -> {
            g.createPolicyClass("pc1", null);
            g.createNode("oa1", OA, null, "pc1");
        });
        assertTrue(graph.exists("pc1"));
        assertTrue(graph.exists("oa1"));
    }

    @Test
    void testThreads() throws PMException, InterruptedException {
        Graph graph = new MemGraph();
        Prohibitions prohibitions = new MemProhibitions();
        Obligations obligations = new MemObligations();

        new Thread(() -> {
            Tx tx = new MemTx(graph, prohibitions, obligations);
            try {
                tx.runTx((g, p, o) -> {
                    g.createPolicyClass("pc1", null);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    g.createNode("oa1", OA, null, "pc1");
                });
            } catch (PMException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(1000);
        new Thread(() -> {
            Tx tx = new MemTx(graph, prohibitions, obligations);
            try {
                tx.runTx((g, p, o) -> {
                    g.deleteNode("oa1");
                    g.deleteNode("pc1");
                });
            } catch (PMException e) {
                e.printStackTrace();
            }
        }).start();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(graph.exists("oa1"));
        assertFalse(graph.exists("pc1"));
    }

    @Test
    void test3() throws InterruptedException, PMException {
        Graph graph = new MemGraph();
        Prohibitions prohibitions = new MemProhibitions();
        Obligations obligations = new MemObligations();
        MemPIP pip = new MemPIP(graph, prohibitions, obligations);
        Tx memTx = new MemTx(graph, prohibitions, obligations);
        new Thread(() -> {
            System.out.println("in thread 1");
            try {
                pip.runTx((g, p, o) -> {
                    System.out.println("in tx 1");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    g.createPolicyClass("pc1", null);
                    System.out.println("end tx 1");
                });
            } catch (PMException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(500);
        new Thread(() -> {
            System.out.println("in thread 2");
            try {
                pip.runTx((g, p, o) -> {
                    System.out.println("in tx 2");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    g.deleteNode("pc1");
                    System.out.println("end tx 2");
                });
            } catch (PMException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(3000);
        new Thread(() -> {
            System.out.println("in thread 3");
            try {
                memTx.runTx((g, p, o) -> {
                    System.out.println("in tx 3");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    g.createPolicyClass("pc1", null);
                    System.out.println("end tx 3");
                });
            } catch (PMException e) {
                e.printStackTrace();
            }
        }).start();
        assertFalse(graph.exists("pc1"));
    }

    @Test
    void test5() throws PMException, InterruptedException {
        Graph graph = new MemGraph();
        Prohibitions prohibitions = new MemProhibitions();
        Obligations obligations = new MemObligations();
        MemPIP pip = new MemPIP(graph, prohibitions, obligations);
        MemPAP pap = new MemPAP(pip);
        OperationSet ops = new OperationSet("read");
        PDP pdp = PDP.newPDP(pap, null, new PReviewDecider(graph, prohibitions, ops), new PReviewAuditor(graph, ops));

        new Thread(()-> {
            try {
                pap.runTx((g, p , o) -> {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    g.createPolicyClass("pc1", null);
                });
            } catch (PMException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(500);
        new Thread(()-> {
            try {
                pdp.runTx(new UserContext("super"), (g, p , o) -> {
                    g.deleteNode("pc1");
                });
            } catch (PMException e) {
                e.printStackTrace();
            }
        }).start();
        assertFalse(graph.exists("pc1"));
    }

    @Test
    void test6() throws InterruptedException, PMException {
        Graph graph = new MemGraph();
        new Thread(() -> {
            try {
                synchronized (graph) {
                    Thread.sleep(2000);
                    graph.createPolicyClass("pc1", null);
                }
            } catch (PMException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(500);
        new Thread(() -> {
            try {
                graph.createNode("oa1", OA, null, "pc1");
            } catch (PMException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(2500);
        assertTrue(graph.exists("oa1"));
        assertTrue(graph.exists("pc1"));
    }

    private boolean threw = false;
    @Test
    void test7() throws InterruptedException, PMException {
        Graph graph = new MemGraph();
        MemTx tx = new MemTx(graph, new MemProhibitions(), new MemObligations());
        graph.createPolicyClass("pc1", null);
        new Thread(() -> {
            try {
                tx.runTx((g, p, o) -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    graph.createNode("oa1", OA, null, "pc1");
                });
            } catch (PMException e) {
                threw = true;
            }
        }).start();
        Thread.sleep(500);
        new Thread(() -> {
            try {
                graph.createNode("oa1", OA, null, "pc1");
            } catch (PMException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(2500);
        assertTrue(threw);
    }

    @Test
    void test8() throws PMException, InterruptedException {
        Graph graph = new MemGraph();
        MemTx tx = new MemTx(graph, new MemProhibitions(), new MemObligations());
        graph.createPolicyClass("pc1", null);
        new Thread(() -> {
            try {
                tx.runTx((g, p, o) -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    graph.createNode("oa1", OA, null, "pc2");
                });
            } catch (PMException e) {
                threw = true;
            }
        }).start();
        Thread.sleep(500);
        new Thread(() -> {
            try {
                graph.createPolicyClass("pc2", null);
            } catch (PMException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(3000);
        assertTrue(graph.isAssigned("oa1", "pc2"));
    }
}