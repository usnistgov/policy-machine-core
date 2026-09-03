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

package gov.nist.ngac.pm.core.util;

import static gov.nist.ngac.pm.core.util.TestIdGenerator.id;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.serialization.json.JSONDeserializer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import org.apache.commons.io.IOUtils;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;


public class SamplePolicy {

    public static void loadSamplePolicyFromPML(PAP pap) throws IOException, PMException {
        String s = IOUtils.resourceToString("sample/sample.pml", StandardCharsets.UTF_8, SamplePolicy.class.getClassLoader());

        long testPc = pap.modify().graph().createPolicyClass("test_pc");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", Collections.singleton(testPc));
        long u1 = pap.modify().graph().createUser("u1", Collections.singleton(ua1));
        pap.modify().graph().associate(id("ua1"), AdminPolicyNode.PM_ADMIN_BASE_OA.nodeId(), new AccessRightSet("*"));

        pap.executePML(NodeUserContext.of("u1"), s);
    }

    public static void loadSamplePolicyFromJSON(PAP pap) throws IOException, PMException {
        String s = IOUtils.resourceToString("sample/sample.json", StandardCharsets.UTF_8, SamplePolicy.class.getClassLoader());
        pap.deserialize(s, new JSONDeserializer());
    }

    public static String loadSamplePolicyPML() throws IOException {
        return IOUtils.resourceToString("sample/sample.pml", StandardCharsets.UTF_8, SamplePolicy.class.getClassLoader());
    }
}
