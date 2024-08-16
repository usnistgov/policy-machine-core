package gov.nist.csd.pm.pap.op.prohibition;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.prohibition.ContainerCondition;
import gov.nist.csd.pm.pap.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.PAP;

import java.util.*;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.*;

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
