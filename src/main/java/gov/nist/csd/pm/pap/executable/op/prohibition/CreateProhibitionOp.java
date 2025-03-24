package gov.nist.csd.pm.pap.executable.op.prohibition;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.PAP;

import java.util.Collection;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_PROCESS_PROHIBITION;
import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_PROHIBITION;

public class CreateProhibitionOp extends ProhibitionOp {

    public CreateProhibitionOp() {
        super("create_prohibition", CREATE_PROCESS_PROHIBITION, CREATE_PROHIBITION);
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().prohibitions().createProhibition(
                (String) operands.get(NAME_OPERAND),
                (ProhibitionSubject) operands.get(SUBJECT_OPERAND),
                (AccessRightSet) operands.get(ARSET_OPERAND),
                (Boolean) operands.get(INTERSECTION_OPERAND),
                (Collection<ContainerCondition>) operands.get(CONTAINERS_OPERAND)
        );

        return null;
    }
}
