package gov.nist.csd.pm;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLSerializer;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;

public class SamplePolicy {

    public static void loadSamplePolicyFromPML(Policy policy) throws IOException, PMException {
        String s = IOUtils.resourceToString("sample/sample.pml", StandardCharsets.UTF_8, SamplePolicy.class.getClassLoader());
        policy.deserialize().fromPML(new UserContext(SUPER_USER), s);
    }
}
