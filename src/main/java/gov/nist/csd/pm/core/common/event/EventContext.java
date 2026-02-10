package gov.nist.csd.pm.core.common.event;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record EventContext(EventContextUser user, String opName, Map<String, Object> args) {

    public EventContext(PAP pap, UserContext userCtx, String opName, Map<String, Object> args) throws PMException {
        this(buildEventContextUser(userCtx, pap), opName, args);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventContext(EventContextUser user1, String name, Map<String, Object> args1))) {
            return false;
        }
        return Objects.equals(opName, name) && Objects.equals(user, user1)
            && Objects.equals(args, args1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, opName, args);
    }

    private static EventContextUser buildEventContextUser(UserContext userCtx, PAP pap) throws PMException {
        if (userCtx.isUserDefined()) {
            Node node = pap.query().graph().getNodeById(userCtx.getUser());
            return new EventContextUser(node.getName(), userCtx.getProcess());
        } else {
            Collection<Long> attributeIds = userCtx.getAttributeIds();
            List<String> attributeNames = new ArrayList<>();
            for (Long attributeId : attributeIds) {
                Node node = pap.query().graph().getNodeById(attributeId);
                attributeNames.add(node.getName());
            }

            return new EventContextUser(attributeNames, userCtx.getProcess());
        }
    }
}
