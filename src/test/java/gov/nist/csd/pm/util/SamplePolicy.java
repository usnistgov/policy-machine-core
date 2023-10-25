package gov.nist.csd.pm.util;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SamplePolicy {

    public static void loadSamplePolicyFromPML(PAP pap) throws IOException, PMException {
        String s = IOUtils.resourceToString("sample/sample.pml", StandardCharsets.UTF_8, SamplePolicy.class.getClassLoader());
        pap.deserialize(new UserContext("u1"), s, new PMLDeserializer());
    }

    public static String loadSamplePolicyPML() throws IOException {
        return IOUtils.resourceToString("sample/sample.pml", StandardCharsets.UTF_8, SamplePolicy.class.getClassLoader());
    }
}
