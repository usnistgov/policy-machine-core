package gov.nist.csd.pm.pap.op.graph;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;

import java.util.Collection;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.CREATE_USER;

public class CreateUserOp extends CreateNodeOp{
    public CreateUserOp() {
        super("create_user", CREATE_USER);
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().graph().createUser(
                (String) operands.get(NAME_OPERAND),
                (Collection<String>) operands.get(DESCENDANTS_OPERAND)
        );

        return null;
    }
}
