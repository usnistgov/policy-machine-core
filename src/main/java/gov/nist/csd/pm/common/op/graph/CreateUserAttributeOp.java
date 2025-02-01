package gov.nist.csd.pm.common.op.graph;


import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;

import java.util.Collection;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_USER_ATTRIBUTE;

public class CreateUserAttributeOp extends CreateNodeOp{
    public CreateUserAttributeOp() {
        super("create_user_attribute", CREATE_USER_ATTRIBUTE);

    }

    @Override
    public Long execute(PAP pap, Map<String, Object> operands) throws PMException {
        return pap.modify().graph().createUserAttribute(
                (String) operands.get(NAME_OPERAND),
                (Collection<Long>) operands.get(DESCENDANTS_OPERAND)
        );
    }

    public static class EventCtx extends EventContext {

        public EventCtx(String user, String process, String name, Collection<String> descendants) {
            super(user, process, "create_user_attribute", Map.of(
                    NAME_OPERAND, name,
                    DESCENDANTS_OPERAND, descendants
            ));
        }
    }
}