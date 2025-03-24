package gov.nist.csd.pm.pap.executable.op.graph;


import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;

import java.util.Collection;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_USER;

public class CreateUserOp extends CreateNodeOp{
    public CreateUserOp() {
        super("create_user", CREATE_USER);
    }

    @Override
    public Long execute(PAP pap, Map<String, Object> operands) throws PMException {
        return pap.modify().graph().createUser(
                (String) operands.get(NAME_OPERAND),
                (Collection<Long>) operands.get(DESCENDANTS_OPERAND)
        );
    }

    public static class EventCtx extends EventContext {

        public EventCtx(String user, String process, String name, Collection<String> descendants) {
            super(user, process, "create_user", Map.of(
                    NAME_OPERAND, name,
                    DESCENDANTS_OPERAND, descendants
            ));
        }
    }
}
