package gov.nist.ngac.pm.core.pap.operation.graph;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import java.util.List;

/**
 * Admin operation "create_user". Creates a new user node.
 */
public class CreateUserOp extends CreateNodeOp {

    public CreateUserOp() {
        super("create_user");
    }

    @Override
    protected long createNode(PAP pap, String name, List<Long> descs) throws PMException {
        return pap.modify().graph().createUser(name, descs);
    }
}
