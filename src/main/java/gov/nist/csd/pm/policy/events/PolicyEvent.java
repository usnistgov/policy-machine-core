package gov.nist.csd.pm.policy.events;

import java.io.Serializable;

public interface PolicyEvent extends Serializable {

    String getEventName();

}
