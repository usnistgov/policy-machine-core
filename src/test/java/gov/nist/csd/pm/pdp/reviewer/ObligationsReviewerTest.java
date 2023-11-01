package gov.nist.csd.pm.pdp.reviewer;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.policy.events.graph.AssignEvent;
import gov.nist.csd.pm.policy.events.graph.AssignToEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.statement.CreatePolicyStatement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ObligationsReviewerTest {

    private static ObligationsReviewer obligationReviewer;

    @BeforeAll
    static void setup() throws PMException {
        String pml = """
                create pc "pc1" {
                    uas {
                        "ua1"
                    }
                    oas {
                        "oa1"
                            "oa1-1"
                            "oa1-2"
                        "oa2"
                    }
                }
                
                create u "u1" assign to ["ua1"]
                create u "u2" assign to ["ua1"]
                
                create obligation "o1" {
                    create rule "r1"
                    when users ["u1"]
                    performs ["assign_to"]
                    do(ctx){
                        create policy class "test"
                    }
                }
                
                create obligation "o2" {
                    create rule "r2"
                    when any user
                    performs ["assign_to"]
                    do(ctx){
                        create policy class "test"
                    }
                }
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        pml = """
              create obligation "o3" {
                    create rule "r3"
                    when any user
                    performs ["assign_to"]
                    on ["oa2"]
                    do(ctx) {
                        create policy class "test"
                    }
              }
              
              create obligation "o4" {
                    create rule "r4"
                    when users ["u2"]
                    performs ["assign"]
                    on ["oa1"]
                    do(ctx) {}
              }
              """;
        pap.executePML(new UserContext("u2"), pml);

        obligationReviewer = new ObligationsReviewer(pap, new GraphReviewer(pap));
    }

    @Test
    void TestGetObligationsWithAuthor() throws PMException {
        List<Obligation> u1 = obligationReviewer.getObligationsWithAuthor(new UserContext("u1"));
        assertEquals(2, u1.size());
        List<Obligation> u2 = obligationReviewer.getObligationsWithAuthor(new UserContext("u2"));
        assertEquals(2, u2.size());
    }

    @Test
    void testGetRulesWithEventSubject() throws PMException {
        Map<String, List<Rule>> obligations = obligationReviewer.getRulesWithEventSubject("u1");
        assertEquals(
                Set.of("o1", "o2", "o3"),
                obligations.keySet()
        );

        obligations = obligationReviewer.getRulesWithEventSubject("u2");
        assertEquals(
                Set.of("o2", "o3", "o4"),
                obligations.keySet()
        );
    }

    @Test
    void testGetRulesWithEventTarget() throws PMException {
        Map<String, List<Rule>> obligations = obligationReviewer.getRulesWithEventTarget("oa1");
        assertEquals(
                Set.of("o1", "o2", "o4"),
                obligations.keySet()
        );

        obligations = obligationReviewer.getRulesWithEventTarget("oa2");
        assertEquals(
                Set.of("o1", "o2", "o3"),
                obligations.keySet()
        );
    }

    @Test
    void testGetMatchingEventResponses() throws PMException {
        List<Response> matchingEventResponses = obligationReviewer.getMatchingEventResponses(
                new EventContext(new UserContext("u1"), "oa2", new AssignToEvent("oa1-2", "oa2")));
        assertEquals(3, matchingEventResponses.size());
        for (Response response : matchingEventResponses) {
            assertEquals(new CreatePolicyStatement(new StringLiteral("test")), response.getStatements().get(0));
        }
    }
}