package gov.nist.csd.pm;

import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.serializer.PALDeserializer;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;

public class SamplePolicy {

    public static void loadSamplePolicyFromPAL(PolicyAuthor policyAuthor) throws IOException, PMException {
        String s = IOUtils.resourceToString("sample/sample.pal", StandardCharsets.UTF_8, SamplePolicy.class.getClassLoader());
        policyAuthor.fromString(s, new PALDeserializer(new UserContext(SUPER_USER)));
    }
}