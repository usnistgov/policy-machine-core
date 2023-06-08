package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.io.Serializable;

public interface PolicyEvent extends Serializable {

    String getEventName();

    void apply(Policy policy) throws PMException;

}
