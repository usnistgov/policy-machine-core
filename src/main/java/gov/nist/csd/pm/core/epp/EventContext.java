package gov.nist.csd.pm.core.epp;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.Collection;
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
        if (userCtx.isUserDefined()) {
            Node node = pap.query().graph().getNodeById(userCtx.getUser());
            user = new EventContextUser(node.getName(), userCtx.getProcess());
        } else {
            Collection<Long> attributeIds = userCtx.getAttributeIds();
            List<String> attributeNames = new ArrayList<>();
            for (Long attributeId : attributeIds) {
                Node node = pap.query().graph().getNodeById(attributeId);
                attributeNames.add(node.getName());
            }

            user = new EventContextUser(attributeNames, userCtx.getProcess());
        }

        return new EventContext(
            user,
            opName,
            args
        );
    }
}
