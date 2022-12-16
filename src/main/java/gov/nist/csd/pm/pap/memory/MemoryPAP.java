package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.serializer.PolicyDeserializer;
import gov.nist.csd.pm.policy.serializer.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;

public class MemoryPAP extends PAP {
    public MemoryPAP() throws PMException {
        super(new MemoryConnection(new MemoryPolicyStore()), true);
    }

    private MemoryPAP(boolean verifySuperPolicy) throws PMException {
        super(new MemoryConnection(new MemoryPolicyStore()), verifySuperPolicy);
    }

    @Override
    public String toString(PolicySerializer policySerializer) throws PMException {
        return this.policyStore.toString(policySerializer);
    }

    @Override
    public void fromString(String s, PolicyDeserializer policyDeserializer) throws PMException {
        this.policyStore.fromString(s, policyDeserializer);
        init(policyStore, true);
    }
}
