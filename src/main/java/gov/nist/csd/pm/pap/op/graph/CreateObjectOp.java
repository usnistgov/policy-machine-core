package gov.nist.csd.pm.pap.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;

import java.util.Collection;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.CREATE_OBJECT;

public class CreateObjectOp extends CreateNodeOp{
    public CreateObjectOp() {
        super("create_object", CREATE_OBJECT);

    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().graph().createObject(
                (String) operands.get(NAME_OPERAND),
                (Collection<String>) operands.get(DESCENDANTS_OPERAND)
        );

        return null;
    }
}
