package gov.nist.csd.pm.util;

import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.serialization.json.JSONDeserializer;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class SamplePolicy {

    public static void loadSamplePolicyFromPML(PAP pap) throws IOException, PMException {
        String s = IOUtils.resourceToString("sample/sample.pml", StandardCharsets.UTF_8, SamplePolicy.class.getClassLoader());

        pap.modify().graph().createPolicyClass("test_pc");
        pap.modify().graph().createUserAttribute("ua1", Collections.singleton("test_pc"));
        pap.modify().graph().createUser("u1", Collections.singleton("ua1"));
        pap.modify().graph().associate("ua1", AdminPolicyNode.PM_ADMIN_OBJECT.nodeName(), new AccessRightSet("*"));

        pap.deserialize(new UserContext("u1"), s, new PMLDeserializer());
    }

    public static void loadSamplePolicyFromJSON(PAP pap) throws IOException, PMException {
        String s = IOUtils.resourceToString("sample/sample.json", StandardCharsets.UTF_8, SamplePolicy.class.getClassLoader());
        pap.modify().graph().createPolicyClass("test_pc");
        pap.modify().graph().createUserAttribute("ua1", Collections.singleton("test_pc"));
        pap.modify().graph().createUser("u1", Collections.singleton("ua1"));
        pap.modify().graph().associate("ua1", AdminPolicyNode.PM_ADMIN_OBJECT.nodeName(), new AccessRightSet("*"));

        pap.deserialize(new UserContext("u1"), s, new JSONDeserializer());
    }

    public static String loadSamplePolicyPML() throws IOException {
        return IOUtils.resourceToString("sample/sample.pml", StandardCharsets.UTF_8, SamplePolicy.class.getClassLoader());
    }
}
