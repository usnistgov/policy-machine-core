package gov.nist.csd.pm.pdp.reviewer;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.serialization.pml.PMLDeserializer;
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

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ObligationsReviewerTest {

    private static ObligationsReviewer obligationReviewer;

    @BeforeAll
    static void setup() throws PMException {
        String pml =
                "                create pc \"pc1\" {\n" +
                "                    uas {\n" +
                "                        \"ua1\"\n" +
                "                    }\n" +
                "                    oas {\n" +
                "                        \"oa1\"\n" +
                "                            \"oa1-1\"\n" +
                "                            \"oa1-2\"\n" +
                "                        \"oa2\"\n" +
                "                    }\n" +
                "                }\n" +
                "                \n" +
                "                create u \"u1\" assign to [\"ua1\"]\n" +
                "                create u \"u2\" assign to [\"ua1\"]\n" +
                "                \n" +
                "                create obligation \"o1\" {\n" +
                "                    create rule \"r1\"\n" +
                "                    when users [\"u1\"]\n" +
                "                    performs [\"assign_to\"]\n" +
                "                    do(ctx){\n" +
                "                        create policy class \"test\"\n" +
                "                    }\n" +
                "                }\n" +
                "                \n" +
                "                create obligation \"o2\" {\n" +
                "                    create rule \"r2\"\n" +
                "                    when any user\n" +
                "                    performs [\"assign_to\"]\n" +
                "                    do(ctx){\n" +
                "                        create policy class \"test\"\n" +
                "                    }\n" +
                "                }";
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        pml =
                "              create obligation \"o3\" {\n" +
                "                    create rule \"r3\"\n" +
                "                    when any user\n" +
                "                    performs [\"assign_to\"]\n" +
                "                    on [\"oa2\"]\n" +
                "                    do(ctx) {\n" +
                "                        create policy class \"test\"\n" +
                "                    }\n" +
                "              }\n" +
                "              \n" +
                "              create obligation \"o4\" {\n" +
                "                    create rule \"r4\"\n" +
                "                    when users [\"u2\"]\n" +
                "                    performs [\"assign\"]\n" +
                "                    on [\"oa1\"]\n" +
                "                    do(ctx) {}\n" +
                "              }";
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