package gov.nist.csd.pm;

import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.pdp.memory.MemoryPDP;
import gov.nist.csd.pm.policy.author.GraphAuthor;
import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.author.pal.statement.AssociateStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.serializer.PALDeserializer;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;

public class SamplePolicy {

    public static void loadSamplePolicyFromPAL(PolicyAuthor policyAuthor) throws IOException, PMException {
        String s = IOUtils.resourceToString("sample/sample.pal", StandardCharsets.UTF_8, SamplePolicy.class.getClassLoader());
        policyAuthor.fromString(s, new PALDeserializer(new UserContext(SUPER_USER)));
    }
}
