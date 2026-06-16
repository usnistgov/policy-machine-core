package gov.nist.csd.pm.core.epp;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.obligation.event.EventContextUser;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record EventContext(EventContextUser user, String opName, Map<String, Object> args) {

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("user", user.getName());
        map.put("attrs", user.getAttrs());
        map.put("process", user.getProcess());
        map.put("opName", opName);
        map.put("args", args);

        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventContext that = (EventContext) o;
        return Objects.equals(opName, that.opName) && Objects.equals(user,
            that.user) && Objects.equals(args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, opName, args);
    }

    public static EventContext fromUserContext(PAP pap,
                                               UserContext userCtx,
                                               String opName,
                                               Map<String, Object> args) throws PMException {
        EventContextUser user = userCtx.toEventContextUser(pap.query().graph());
        return new EventContext(user, opName, args);
    }
}
