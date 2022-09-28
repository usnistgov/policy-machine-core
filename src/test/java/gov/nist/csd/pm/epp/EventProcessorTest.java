/*
package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.epp.events.AssignEvent;
import gov.nist.csd.pm.epp.events.AssignToEvent;
import gov.nist.csd.pm.epp.events.DeassignEvent;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.model.operations.OperationSet;
import gov.nist.csd.pm.pap.author.Author;
import gov.nist.csd.pm.model.access.UserContext;
import gov.nist.csd.pm.pip.memory.MemoryPolicyStore;
import gov.nist.csd.pm.model.graph.nodes.Node;
import gov.nist.csd.pm.model.obligation.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
import static gov.nist.csd.pm.model.obligation.Response.Do;
import static gov.nist.csd.pm.model.obligation.event.EventPattern.*;
import static org.junit.jupiter.api.Assertions.*;

class EPPTest {

    private PDP pdp;
    private PolicyAdmin pap;
    private Node u1;
    private Node ua1;
    private Node o1;
    private Node oa1;
    private Node pc1;

    @BeforeEach
    void setup() throws PMException {
        OperationSet ops = new OperationSet("read", "write", "execute");
        pap = new PolicyAdmin(new MemoryPolicyStore());
        pdp = PDP.newPolicyDecider(pap, ops);
        pdp.runTx(new UserContext("super"), (g, p, o) -> {
            pc1 = g.createPolicyClass("pc1", null);
            oa1 = g.createObjectAttribute("oa1", pc1.getName());
            g.createObjectAttribute("oa2", pc1.getName());
            o1 = g.createObject("o1", oa1.getName());
            ua1 = g.createUserAttribute("ua1", pc1.getName());
            u1 = g.createUser("u1", ua1.getName());

            g.associate(ua1.getName(), oa1.getName(), new OperationSet("read", "write"));
        });

    }

    @Test
    void TestEvent() throws PMException, IOException {
        Author author = new Author(pap);
        author.addObligation(new UserContext(SUPER_USER), "event test",
                new Rule("u1 assign to",
                        When(Users("u1")),
                        Performs("assign to"),
                        On("oa1"),
                        Do((respAuthor, evtCtx) -> {
                            if (respAuthor.getGraph().exists("u1 assign to success")) {
                                return;
                            }

                            respAuthor.updatePolicyClass("pc1", (pc1) -> {
                                pc1.createObjectAttribute("u1 assign to success", (oa) -> {
                                    oa.addProperty("prop1", "value1");
                                });
                            });
                        })
                ),
                new Rule("anyUser assign",
                        When(AnyUser()),
                        Performs("assign"),
                        On("o1"),
                        Do((respAuthor, evtCtx) -> {
                            if (respAuthor.getGraph().exists("anyUser assign success")) {
                                return;
                            }

                            respAuthor.updatePolicyClass("pc1", (pc1) -> {
                                pc1.createObjectAttribute("anyUser assign success");
                            });
                        })
                ),
                new Rule("anyUser in list deassign",
                        When(Users("u1")),
                        Performs("deassign"),
                        On("oa1"),
                        Do((respAuthor, evtCtx) -> {
                            if (respAuthor.getGraph().exists("anyUser in list deassign success")) {
                                return;
                            }

                            respAuthor.updatePolicyClass("pc1", (pc1) -> {
                                pc1.createObjectAttribute("anyUser in list deassign success");
                            });
                        })
                )
        );

        // test u1 assign to
        pdp.getEPP().processEvent(new AssignToEvent(new UserContext(u1.getName(), "123"), oa1, o1));
        Node node = pap.getPolicyStore().getGraphStore().getNode("u1 assign to success");
        assertTrue(node.getProperties().containsKey("prop1"));
        assertTrue(node.getProperties().get("prop1").equalsIgnoreCase("value1"));

        // test anyUser assign
        pdp.getEPP().processEvent(new AssignEvent(new UserContext(u1.getName(), "123"), o1, oa1));
        assertDoesNotThrow(() -> pap.getPolicyStore().getGraphStore().getNode("anyUser assign success"));

        // test anyUser in list deassign
        pdp.getEPP().processEvent(new DeassignEvent(new UserContext(u1.getName(),"123"), o1, oa1));
        assertDoesNotThrow(() -> pap.getPolicyStore().getGraphStore().getNode("anyUser in list deassign success"));
    }
}*/
