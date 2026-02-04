package gov.nist.csd.pm.core.pap.operation.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import java.util.List;

public class CreateObjectOp extends CreateNodeOp {

    public CreateObjectOp() {
        super("create_object");
    }

    @Override
    protected long createNode(PAP pap, String name, List<Long> descs) throws PMException {
        return pap.modify().graph().createObject(name, descs);
    }

}
