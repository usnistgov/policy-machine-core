package gov.nist.csd.pm.core.common.event;

import java.util.*;

public record EventContext(EventContextUser user, String opName, Map<String, Object> args) {

}
