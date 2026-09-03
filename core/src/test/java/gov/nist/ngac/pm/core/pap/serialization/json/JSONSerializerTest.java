/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.serialization.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.gson.Gson;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.obligation.event.EventPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.operation.AnyOperationPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.ngac.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.ngac.pm.core.util.PolicyEquals;
import gov.nist.ngac.pm.core.util.SamplePolicy;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

class JSONSerializerTest {

    @Test
    void testSerialize() throws PMException, IOException {
        MemoryPAP pap = new MemoryPAP();
        SamplePolicy.loadSamplePolicyFromJSON(pap);

        String serialize = pap.serialize(new JSONSerializer());
        MemoryPAP pap2 = new MemoryPAP();

        pap2.deserialize(serialize, new JSONDeserializer());
        pap2.serialize(new JSONSerializer());

        PolicyEquals.assertPolicyEquals(pap.query(), pap2.query());
    }

    @Test
    void testSerializeDoesNotIncludeNullFields() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        String serialize = pap.serialize(new JSONSerializer());
        Map map = new Gson().fromJson(serialize, Map.class);
        assertNull(map.get("prohibitions"));
        assertNull(map.get("obligations"));
        assertNull(map.get("operations"));
        assertNull(map.get("routines"));

        Map graph = (Map) map.get("graph");
        assertNull(graph.get("uas"));
        assertNull(graph.get("users"));
        assertNull(graph.get("objects"));
        assertNull(((Map)((List) graph.get("oas")).getFirst()).get("associations"));
    }

    @Test
    void testObligationAuthorAlwaysSerializesAsId() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUser("u1", List.of(ua1));

        EventPattern eventPattern = new EventPattern(new SubjectPattern(), new AnyOperationPattern());

        // obligation authored with UserIdContext
        pap.modify().obligations().createObligation(
            new Obligation(NodeUserContext.of(u1), "obl-by-id", eventPattern, new ObligationResponse("ctx", List.of())));
        // obligation authored with UsernameContext
        pap.modify().obligations().createObligation(
            new Obligation(NodeUserContext.of("u1"), "obl-by-name", eventPattern, new ObligationResponse("ctx", List.of())));

        JSONSerializer serializer = new JSONSerializer();
        JSONPolicy jsonPolicy = serializer.buildJSONPolicy(pap.query());

        for (JSONObligation jsonObligation : jsonPolicy.getObligations()) {
            assertEquals(u1, jsonObligation.getAuthor(),
                "obligation '" + jsonObligation.getName() + "' should serialize author as id " + u1);
        }
    }
}