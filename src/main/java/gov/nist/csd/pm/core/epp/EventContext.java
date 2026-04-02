package gov.nist.csd.pm.core.epp;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.AttributeIdsContext;
import gov.nist.csd.pm.core.pap.query.model.context.AttributeNamesContext;
import gov.nist.csd.pm.core.pap.query.model.context.CompositeUserContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserIdContext;
import gov.nist.csd.pm.core.pap.query.model.context.UsernameContext;
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
        EventContextUser user = switch (userCtx) {
            case UserIdContext c -> {
                Node node = pap.query().graph().getNodeById(c.userId());
                yield new EventContextUser(node.getName(), userCtx.getProcess());
            }
            case UsernameContext c ->
                new EventContextUser(c.username(), userCtx.getProcess());
            case AttributeIdsContext c -> {
                List<String> names = new ArrayList<>();
                for (long id : c.attributeIds()) {
                    names.add(pap.query().graph().getNodeById(id).getName());
                }
                yield new EventContextUser(names, userCtx.getProcess());
            }
            case AttributeNamesContext c ->
                new EventContextUser(new ArrayList<>(c.attributeNames()), userCtx.getProcess());
            case CompositeUserContext c -> {
                List<String> names = new ArrayList<>();
                for (UserContext sub : c.contexts()) {
                    names.add(sub.toString());
                }
                yield new EventContextUser(names, userCtx.getProcess());
            }
        };

        return new EventContext(
            user,
            opName,
            args
        );
    }
}
