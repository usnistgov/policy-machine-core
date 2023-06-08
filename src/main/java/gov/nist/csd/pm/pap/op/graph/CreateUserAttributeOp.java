package gov.nist.csd.pm.pap.op.graph;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;

import java.util.Collection;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.CREATE_USER_ATTRIBUTE;

public class CreateUserAttributeOp extends CreateNodeOp{
    public CreateUserAttributeOp() {
        super("create_user_attribute", CREATE_USER_ATTRIBUTE);

    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().graph().createUserAttribute(
                (String) operands.get(NAME_OPERAND),
                (Collection<String>) operands.get(DESCENDANTS_OPERAND)
        );

        return null;
    }
}