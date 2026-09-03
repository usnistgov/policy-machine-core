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

package gov.nist.ngac.pm.core.pap.serialization;

import static gov.nist.ngac.pm.core.util.PolicyEquals.assertPolicyEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.serialization.json.JSONDeserializer;
import gov.nist.ngac.pm.core.pap.serialization.json.JSONSerializer;
import gov.nist.ngac.pm.core.util.SamplePolicy;
import gov.nist.ngac.pm.core.util.TestIdGenerator;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;


public class SerializationTest {

    @Test
    void testJSON() throws PMException, IOException {
        MemoryPAP pap = new TestPAP();
        SamplePolicy.loadSamplePolicyFromPML(pap);

        String json = pap.serialize(new JSONSerializer());

        MemoryPAP jsonPAP = new TestPAP();
        jsonPAP.deserialize(json, new JSONDeserializer());

        assertPolicyEquals(pap.query(), jsonPAP.query());
    }

    @Test
    void testSerializationWithAdminNodes() throws PMException {
        PAP pap = new TestPAP()
                .withIdGenerator(new TestIdGenerator());

        pap.executePML(NodeUserContext.of("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                associate "ua1" to PM_ADMIN_BASE_OA with ["admin:*"]                
                """);
        String json = pap.serialize(new JSONSerializer());

        MemoryPAP jsonPAP = new TestPAP();
        jsonPAP.deserialize(json, new JSONDeserializer());

        assertPolicyEquals(pap.query(), jsonPAP.query());
    }

    @Test
    void testSerializationNodeProperties() throws PMException {
        MemoryPAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                set properties of "ua1" to {"a": "b"}
                """);
        String json = pap.serialize(new JSONSerializer());

        MemoryPAP jsonPAP = new TestPAP();
        jsonPAP.deserialize(json, new JSONDeserializer());

        assertPolicyEquals(pap.query(), jsonPAP.query());
    }

    @Test
    void testDeserializeRollsBackOnRuntimeException() throws PMException, IOException {
        MemoryPAP pap = new TestPAP();
        SamplePolicy.loadSamplePolicyFromPML(pap);

        MemoryPAP expected = new TestPAP();
        SamplePolicy.loadSamplePolicyFromPML(expected);

        PMException e = assertThrows(PMException.class, () -> pap.deserialize("test", (target, input) -> {
            target.modify().graph().createPolicyClass("pc2");
            throw new IllegalStateException("test");
        }));

        assertInstanceOf(IllegalStateException.class, e.getCause());
        assertFalse(pap.query().graph().nodeExists("pc2"));
        assertPolicyEquals(pap.query(), expected.query());
    }
}