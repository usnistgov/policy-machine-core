package gov.nist.ngac.pm.core.pap.operation.graph;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import java.util.List;

/**
 * Admin operation "create_object_attribute". Creates a new object attribute node.
 */
public class CreateObjectAttributeOp extends CreateNodeOp {

    public CreateObjectAttributeOp() {
        super("create_object_attribute");
    }

    @Override
    protected long createNode(PAP pap, String name, List<Long> descs) throws PMException {
        return pap.modify().graph().createObjectAttribute(name, descs);
    }
}
