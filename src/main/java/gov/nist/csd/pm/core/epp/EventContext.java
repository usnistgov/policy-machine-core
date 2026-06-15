package gov.nist.csd.pm.core.epp;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.AnonymousUserContext;
import gov.nist.csd.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        EventContextUser user;
        if (userCtx instanceof NodeUserContext c) {
            long id = c.resolveNodeIds(pap.query().graph()::getNodeByName).iterator().next();
            user = new EventContextUser(pap.query().graph().getNodeById(id).getName(), userCtx.getProcess());
        } else if (userCtx instanceof AnonymousUserContext c) {
            List<String> names = new ArrayList<>();
            if (c.getAttributeNames() != null) {
                names.addAll(c.getAttributeNames());
            } else {
                for (long id : c.getAttributeIds()) {
                    names.add(pap.query().graph().getNodeById(id).getName());
                }
            }
            user = new EventContextUser(names, userCtx.getProcess());
        } else {
            throw new IllegalArgumentException("unsupported user context type: " + userCtx.getClass());
        }

        return new EventContext(
            user,
            opName,
            args
        );
    }
}
