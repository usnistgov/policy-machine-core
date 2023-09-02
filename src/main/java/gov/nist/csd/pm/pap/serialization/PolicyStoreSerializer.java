package gov.nist.csd.pm.pap.serialization;

import gov.nist.csd.pm.pap.PolicyStore;
import gov.nist.csd.pm.pap.serialization.json.JSONSerializer;
import gov.nist.csd.pm.pap.serialization.pml.PMLSerializer;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;

public class PolicyStoreSerializer implements PolicySerializer {

    private final PolicyStore policy;

    public PolicyStoreSerializer(PolicyStore policy) {
        this.policy = policy;
    }

    @Override
    public String toJSON() throws PMException {
        return JSONSerializer.toJSON(policy);
    }

    @Override
    public String toPML() throws PMException {
        return PMLSerializer.toPML(policy, false);
    }
}
