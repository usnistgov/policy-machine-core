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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.serialization.json.JSONDeserializer;
import gov.nist.ngac.pm.core.pap.serialization.json.JSONGraph;
import gov.nist.ngac.pm.core.pap.serialization.json.JSONOperations;
import gov.nist.ngac.pm.core.pap.serialization.json.JSONPolicy;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class JSONSerializationTest {

    @Test
    void testJSONSerializationDoesNotThrowNPE() throws PMException, IOException {
        List<JSONPolicy> policies = List.of(
            new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), List.of(), new JSONOperations()),
            new JSONPolicy(null, new JSONGraph(), List.of(), List.of(), new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), null, List.of(), List.of(), new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(), null, List.of(),new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), null, new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), List.of(), null),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(List.of(), List.of(), List.of(), List.of(), List.of()), List.of(), List.of(), new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(null, List.of(), List.of(), List.of(), List.of()), List.of(), List.of(), new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(List.of(), null, List.of(), List.of(), List.of()), List.of(), List.of(), new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(List.of(), List.of(), null, List.of(), List.of()), List.of(), List.of(), new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(List.of(), List.of(), List.of(), null, List.of()), List.of(), List.of(), new JSONOperations()),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(List.of(), List.of(), List.of(), List.of(), null), List.of(), List.of(), new JSONOperations()),

            new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), List.of(), new JSONOperations(null, List.of(), List.of(), List.of(), List.of())),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), List.of(), new JSONOperations(List.of(), null, List.of(), List.of(), List.of())),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), List.of(), new JSONOperations(List.of(), List.of(), null, List.of(), List.of())),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), List.of(), new JSONOperations(List.of(), List.of(), List.of(), null, List.of())),
            new JSONPolicy(new AccessRightSet(), new JSONGraph(), List.of(), List.of(), new JSONOperations(List.of(), List.of(), List.of(), List.of(), null))
        );

        for (JSONPolicy policy : policies) {
            assertDoesNotThrow(() -> new MemoryPAP().deserialize(policy.toString(), new JSONDeserializer()));
        }
    }

}
