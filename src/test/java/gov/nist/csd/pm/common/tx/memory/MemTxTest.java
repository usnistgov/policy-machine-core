package gov.nist.csd.pm.common.tx.memory;

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
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.common.tx.Tx;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.OA;
import static org.junit.jupiter.api.Assertions.*;

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
                    g.createPolicyClass("txpc2", null);
                });
                tx2.commit();
            } catch (PMException e) {
                tx2.rollback();
            }
        }).start();
        Thread.sleep(4000);
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

        tx.runTx((g, p, o) -> {
            Prohibition prohibition = new Prohibition.Builder("testp", "u1", new OperationSet("read"))
                    .setIntersection(true)
                    .addContainer("oa1", true)
                    .addContainer("oa2", false)
                    .build();
            p.add(prohibition);
        });
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

            Prohibition prohibition = new Prohibition.Builder("testp", "u1", new OperationSet("read"))
                    .setIntersection(true)
                    .addContainer("oa1", true)
                    .addContainer("oa2", false)
                    .build();
            p.add(prohibition);

            Obligation obligation = new Obligation("super");
            obligation.setLabel("label");
            o.add(obligation, true);
        });
        assertTrue(graph.exists("pc1"));
        assertTrue(graph.exists("oa1"));
        assertNotNull(prohibitions.get("testp"));
        assertNotNull(obligations.get("label"));
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
                pdp.withUser(new UserContext("super")).runTx((g, p , o) -> {
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
        Thread t1 = new Thread(() -> {
            try {
                synchronized (graph) {
                    Thread.sleep(2000);
                    graph.createPolicyClass("pc1", null);
                }
            } catch (PMException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                graph.createNode("oa1", OA, null, "pc1");
            } catch (PMException e) {
                e.printStackTrace();
            }
        });


        t1.start();
        t2.start();

        t1.join();
        t2.join();

        assertTrue(graph.exists("oa1"));
        assertTrue(graph.exists("pc1"));
    }

    private boolean threw = false;
    @Test
    void test7() throws InterruptedException, PMException {
        Graph graph = new MemGraph();
        MemTx tx = new MemTx(graph, new MemProhibitions(), new MemObligations());
        graph.createPolicyClass("pc1", null);
        Thread t1 = new Thread(() -> {
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
        });
        Thread t2 = new Thread(() -> {
            try {
                graph.createNode("oa1", OA, null, "pc1");
            } catch (PMException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        assertTrue(threw);
    }

    @Test
    void test8() throws PMException, InterruptedException {
        Graph graph = new MemGraph();
        MemTx tx = new MemTx(graph, new MemProhibitions(), new MemObligations());
        graph.createPolicyClass("pc1", null);
        Thread t1 = new Thread(() -> {
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
        });
        Thread t2 = new Thread(() -> {
            try {
                graph.createPolicyClass("pc2", null);
            } catch (PMException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        assertTrue(graph.isAssigned("oa1", "pc2"));
    }
}