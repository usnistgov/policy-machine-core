package gov.nist.csd.pm.pap.function.op.graph;

import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.mapType;

import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.arg.ListIdNodeFormalParameter;
import gov.nist.csd.pm.pap.function.op.arg.IdNodeFormalParameter;
import gov.nist.csd.pm.pap.function.op.Operation;

import java.util.List;
import java.util.Map;

public abstract class GraphOp<R, A extends Args> extends Operation<R, A> {

    public static final FormalParameter<NodeType> TYPE_PARAM = new FormalParameter<>("type", new NodeTypeType());
    public static final FormalParameter<Map<String, String>> PROPERTIES_PARAM = new FormalParameter<>("properties", mapType(STRING_TYPE, STRING_TYPE));
    public static final FormalParameter<AccessRightSet> ARSET_PARAM = new FormalParameter<>("arset", new AccessRightSetType());

    public static final ListIdNodeFormalParameter DESCENDANTS_PARAM = new ListIdNodeFormalParameter("descendants");

    public static final IdNodeFormalParameter ASCENDANT_PARAM = new IdNodeFormalParameter("ascendant");
    public static final IdNodeFormalParameter UA_PARAM = new IdNodeFormalParameter("ua");
    public static final IdNodeFormalParameter TARGET_PARAM = new IdNodeFormalParameter("target");

    public GraphOp(String name, List<FormalParameter<?>> formalParameters) {
        super(name, formalParameters);
    }
}
