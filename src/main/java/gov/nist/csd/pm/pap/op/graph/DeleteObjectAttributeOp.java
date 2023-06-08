package gov.nist.csd.pm.pap.op.graph;

import gov.nist.csd.pm.common.graph.node.NodeType;

import java.util.Collection;
import java.util.List;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.*;

public class DeleteObjectAttributeOp extends DeleteNodeOp{
    public DeleteObjectAttributeOp() {
        super("delete_object_attribute", DELETE_OBJECT_ATTRIBUTE, DELETE_OBJECT_ATTRIBUTE_FROM);
    }
}
