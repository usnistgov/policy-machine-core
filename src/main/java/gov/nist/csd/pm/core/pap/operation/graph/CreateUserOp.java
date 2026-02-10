package gov.nist.csd.pm.core.pap.operation.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import java.util.List;

public class CreateUserOp extends CreateNodeOp {

    public CreateUserOp() {
        super("create_user");
    }

    @Override
    protected long createNode(PAP pap, String name, List<Long> descs) throws PMException {
        return pap.modify().graph().createUser(name, descs);
    }
}
