package gov.nist.ngac.pm.core.pap.operation.graph;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import java.util.List;

/**
 * Admin operation "create_user_attribute": creates a new user attribute node.
 */
public class CreateUserAttributeOp extends CreateNodeOp {

    public CreateUserAttributeOp() {
        super("create_user_attribute");
    }

    @Override
    protected long createNode(PAP pap, String name, List<Long> descs) throws PMException {
        return pap.modify().graph().createUserAttribute(name, descs);
    }
}