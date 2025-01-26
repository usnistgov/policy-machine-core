package gov.nist.csd.pm.common.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;

import java.util.Collection;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_OBJECT_ATTRIBUTE;

public class CreateObjectAttributeOp extends CreateNodeOp{

    public CreateObjectAttributeOp() {
        super("create_object_attribute", CREATE_OBJECT_ATTRIBUTE);
    }

    @Override
    public Long execute(PAP pap, Map<String, Object> operands) throws PMException {
        return pap.modify().graph().createObjectAttribute(
                (String) operands.get(NAME_OPERAND),
                (Collection<Long>) operands.get(DESCENDANTS_OPERAND)
        );
    }
}
